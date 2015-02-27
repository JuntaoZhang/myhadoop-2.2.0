package org.example.hadoop.state;

import org.apache.hadoop.mapreduce.v2.api.records.impl.pb.JobIdPBImpl;
import org.apache.hadoop.mapreduce.v2.app.job.JobStateInternal;
import org.apache.hadoop.mapreduce.v2.app.job.event.JobEvent;
import org.apache.hadoop.mapreduce.v2.app.job.event.JobEventType;
import org.apache.hadoop.mapreduce.v2.proto.MRProtos.JobIdProto;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.state.InvalidStateTransitonException;
import org.apache.hadoop.yarn.state.SingleArcTransition;
import org.apache.hadoop.yarn.state.StateMachine;
import org.apache.hadoop.yarn.state.StateMachineFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 通过事件触发内部状态转变
 */
public class JobStateMachine implements EventHandler<JobEvent> {
  private final String jobId;
  private EventHandler<JobEvent> eventHandler;
  private final Lock writeLock;
  private final Lock readLock;

  // 定义状态机
  protected static final StateMachineFactory<JobStateMachine, JobStateInternal, JobEventType, JobEvent>
      stateMachineFactory = new StateMachineFactory<JobStateMachine, JobStateInternal, JobEventType, JobEvent>(JobStateInternal.NEW)
      .addTransition(JobStateInternal.NEW, JobStateInternal.INITED, JobEventType.JOB_INIT, new InitTransition())
      .addTransition(JobStateInternal.INITED, JobStateInternal.SETUP, JobEventType.JOB_START, new StartTransition())
      .addTransition(JobStateInternal.SETUP, JobStateInternal.RUNNING, JobEventType.JOB_SETUP_COMPLETED, new SetupCompletedTransition())
      .addTransition(JobStateInternal.RUNNING, JobStateInternal.SUCCEEDED, JobEventType.JOB_COMPLETED, new JobTasksCompletedTransition())
      .installTopology();

  private final StateMachine<JobStateInternal, JobEventType, JobEvent> stateMachine;

  public JobStateMachine(String jobId) {
    this.jobId = jobId;
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    this.readLock = readWriteLock.readLock();
    this.writeLock = readWriteLock.writeLock();
    this.eventHandler = this;
    stateMachine = stateMachineFactory.make(this);
  }

  protected StateMachine<JobStateInternal, JobEventType, JobEvent> getStateMachine() {
    return stateMachine;
  }

  public JobIdPBImpl getJobId() {
    return new JobIdPBImpl(JobIdProto.newBuilder().setId(Integer.valueOf(jobId)).build());
  }

  public static class InitTransition implements SingleArcTransition<JobStateMachine, JobEvent> {
    @Override
    public void transition(JobStateMachine job, JobEvent event) {
      System.out.println("Receiving event " + event);
//      job.eventHandler.handle(new JobEvent(job.getJobId(), JobEventType.JOB_START));
    }
  }

  public static class StartTransition implements SingleArcTransition<JobStateMachine, JobEvent> {
    @Override
    public void transition(JobStateMachine job, JobEvent event) {
      System.out.println("Receiving event " + event);
//      job.eventHandler.handle(new JobEvent(job.getJobId(), JobEventType.JOB_SETUP_COMPLETED));
    }
  }

  public static class SetupCompletedTransition implements SingleArcTransition<JobStateMachine, JobEvent> {
    @Override
    public void transition(JobStateMachine job, JobEvent event) {
      System.out.println("Receiving event " + event);
//      job.eventHandler.handle(new JobEvent(job.getJobId(), JobEventType.JOB_COMPLETED));
    }
  }

  public static class JobTasksCompletedTransition implements SingleArcTransition<JobStateMachine, JobEvent> {
    @Override
    public void transition(JobStateMachine job, JobEvent event) {
      System.out.println("Receiving event " + event);
//      job.eventHandler.handle(new JobEvent(job.getJobId(), JobEventType.JOB_KILL));
    }
  }

  @Override
  public void handle(JobEvent event) {
    try {
      writeLock.lock();
      JobStateInternal oldState = getInternalState();
      try {
        getStateMachine().doTransition(event.getType(), event);
      } catch (InvalidStateTransitonException e) {
        e.printStackTrace();
        System.out.println("Can't handle this event at current state");
      }
      if (oldState != getInternalState()) {
        System.out.println("Job Transitioned from " + oldState + " to "
            + getInternalState());
      }
    } finally {
      writeLock.unlock();
    }
  }

  public JobStateInternal getInternalState() {
    readLock.lock();
    try {
      return getStateMachine().getCurrentState();
    } finally {
      readLock.unlock();
    }
  }

  public static void main(String[] args) {
    JobStateMachine machine = new JobStateMachine("1");
    machine.handle(new JobEvent(machine.getJobId(), JobEventType.JOB_INIT));
    System.out.printf("current machine state is `%s`.%n", machine.getInternalState());

    machine.handle(new JobEvent(machine.getJobId(), JobEventType.JOB_START));
    System.out.printf("current machine state is `%s`.%n", machine.getInternalState());

    machine.handle(new JobEvent(machine.getJobId(), JobEventType.JOB_SETUP_COMPLETED));
    System.out.printf("current machine state is `%s`.%n", machine.getInternalState());

    machine.handle(new JobEvent(machine.getJobId(), JobEventType.JOB_COMPLETED));
    System.out.printf("current machine state is `%s`.%n", machine.getInternalState());
  }
}