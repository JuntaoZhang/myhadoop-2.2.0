package org.example.hadoop.pd.simple.server.business;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.protobuf.BlockingService;
import org.example.hadoop.pd.simple.ipc.Server;
import org.example.hadoop.pd.simple.api.Calculator;

/**
 * CalculatorServer.java,实现计算器服务的类，
 * 此类依赖ipc Server接受请求并处理计算请求，注意到其自身实现了Calculator接口，
 * 本质上的计算是由其来完成的。也就是，Server接受客户端请求要执行方法M，
 * Server对象里有实现了CalculatorPB接口的对象A，那么请求就交给A处理（A其实是CalculatorPBServiceImpl类的对象，此类后面介绍），
 * 此时A对应的M方法的参数是pb的形式，
 * 另外A对象里其实包含对CalculatorService的一个引用，所以在A的M方法里，先对参数反序列化，然后将参数交给CalculatorService处理。
 */
public class CalculatorService implements Calculator {

  private Server server = null;
  private final Class protocol = Calculator.class;
  private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  private final String protoPackage = "org.example.hadoop.pd.simple.proto";
  private final String host = "localhost";
  private final int port = 8038;

  public CalculatorService() {

  }

  @Override
  public int add(int a, int b) {
    return a + b;
  }

  public int minus(int a, int b) {
    return a - b;
  }


  public void init() {
    createServer();
  }

  /*
   * return org.tao.simple.server.api.CalculatorPBServiceImpl
   */
  public Class<?> getPbServiceImplClass() {
    String packageName = protocol.getPackage().getName();
    String className = protocol.getSimpleName();
    String pbServiceImplName = packageName + "." + className + "PBServiceImpl";
    Class<?> clazz = null;
    try {
      clazz = Class.forName(pbServiceImplName, true, classLoader);
    } catch (ClassNotFoundException e) {
      System.err.println(e.toString());
    }
    return clazz;
  }

  /*
   * return org.example.hadoop.pd.simple.proto.Calculator$CalculatorService
   */
  public Class<?> getProtoClass() {
    String className = protocol.getSimpleName();
    String protoClazzName = protoPackage + "." + className + "$" + className + "Service";
    Class<?> clazz = null;
    try {
      clazz = Class.forName(protoClazzName, true, classLoader);
    } catch (ClassNotFoundException e) {
      System.err.println(e.toString());
    }
    return clazz;
  }

  public void createServer() {
    Class<?> pbServiceImpl = getPbServiceImplClass();
    Constructor<?> constructor = null;
    try {
      constructor = pbServiceImpl.getConstructor(protocol);
      constructor.setAccessible(true);
    } catch (NoSuchMethodException e) {
      System.err.print(e.toString());
    }

    Object service = null;  // instance of CalculatorPBServiceImpl
    try {
      if (constructor != null) {
        service = constructor.newInstance(this);
      }
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    /**
     * interface: org.example.hadoop.simple.server
     */
    Class<?> pbProtocol = null;
    if (service != null) {
      pbProtocol = service.getClass().getInterfaces()[0];
    }

    /**
     * class: org.example.hadoop.simple.proto.Calculator$CalculatorService
     */
    Class<?> protoClazz = getProtoClass();

    Method method = null;
    try {

      // pbProtocol.getInterfaces()[] 即是接口 org.example.hadoop.simple.proto.Calculator$CalculatorService$BlockingInterface

      if (pbProtocol != null) {
        method = protoClazz.getMethod("newReflectiveBlockingService", pbProtocol.getInterfaces()[0]);
        method.setAccessible(true);
      }
    } catch (NoSuchMethodException e) {
      System.err.print(e.toString());
    }

    try {
      if (method != null) {
        createServer(pbProtocol, (BlockingService) method.invoke(null, service));
      }
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

  }

  public void createServer(Class pbProtocol, BlockingService service) {
    server = new Server(pbProtocol, service, port);
    server.start();
  }

  public static void main(String[] args) {
    CalculatorService cs = new CalculatorService();
    cs.init();
  }

}