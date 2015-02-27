package org.example.hadoop.event;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.event.EventHandler;

@SuppressWarnings("unchecked")
public class SimpleMRAppMaster extends CompositeService {

  private Dispatcher dispatcher;   //中央异步调度器
  private String jobID;
  private int taskNumber;  //作业中包含的任务数
  private String[] taskIDS; //该作业中包含的所有任务

  public SimpleMRAppMaster(String name, String jobID, int taskNumber) {
    super(name);
    this.jobID = jobID;
    this.taskNumber = taskNumber;
    this.taskIDS = new String[taskNumber];
    for (int i = 0; i < taskNumber; i++) {
      this.taskIDS[i] = jobID + "_task_" + i;
    }
  }

  @Override
  protected void serviceInit(Configuration conf) throws Exception {
    dispatcher = new AsyncDispatcher();
    dispatcher.register(JobEventType.class, new JobEventHandler());
    dispatcher.register(TaskEventType.class, new TaskEventHandler());
    addService((Service) dispatcher);
    super.serviceInit(conf);
  }


  public Dispatcher getDispatcher() {
    return dispatcher;
  }

  private class JobEventHandler implements EventHandler<JobEvent> {

    @Override
    public void handle(JobEvent event) {
      //若收到杀死作业事件
      if (event.getType() == JobEventType.JOB_KILL) {
        System.out.println("收到杀死作业事件，要杀掉作业" + event.getJobID() + "下的所有任务");
        for (int i = 0; i < taskNumber; i++) {
          dispatcher.getEventHandler().handle(
              new TaskEvent(TaskEventType.T_KILL, taskIDS[i]));
        }

      } else if (event.getType() == JobEventType.JOB_INIT) {
        System.out.println("收到启动作业事件，要启动作业" + event.getJobID() + "下的所有任务");
        for (int i = 0; i < taskNumber; i++) {
          dispatcher.getEventHandler().handle(new TaskEvent(TaskEventType.T_SCHEDULE, taskIDS[i]));
        }
      }
    }
  }

  private class TaskEventHandler implements EventHandler<TaskEvent> {
    @Override
    public void handle(TaskEvent event) {
      if (event.getType() == TaskEventType.T_KILL) {
        System.out.println("收到杀死任务命令，开始杀死任务" + event.getTaskID());
      } else if (event.getType() == TaskEventType.T_SCHEDULE) {
        System.out.println("收到启动任务命令，开始启动任务" + event.getTaskID());
      }
    }
  }


  public static void main(String[] args) throws Exception {
    String jobID = "job_20150125_01";
    SimpleMRAppMaster appMaster = new SimpleMRAppMaster("Simple MRAppMaster", jobID, 5);
    YarnConfiguration conf = new YarnConfiguration(new Configuration());
    appMaster.init(conf);
    appMaster.start();
    appMaster.getDispatcher().getEventHandler().handle(new JobEvent(JobEventType.JOB_INIT, jobID));
    appMaster.getDispatcher().getEventHandler().handle(new JobEvent(JobEventType.JOB_KILL, jobID));
    appMaster.stop();
  }
}