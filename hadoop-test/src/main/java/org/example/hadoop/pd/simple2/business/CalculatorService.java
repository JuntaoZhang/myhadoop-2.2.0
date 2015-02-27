package org.example.hadoop.pd.simple2.business;

import com.google.protobuf.BlockingService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.security.token.SecretManager;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnRuntimeException;
import org.example.hadoop.pd.simple2.Calculator;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CalculatorService implements Calculator {
  private Configuration localConf;
  private ConcurrentMap<Class<?>, Constructor<?>> serviceCache = new ConcurrentHashMap<Class<?>, Constructor<?>>();
  private ConcurrentMap<Class<?>, Method> protoCache = new ConcurrentHashMap<Class<?>, Method>();
  private Server server = null;
  private final String protoPackage = "org.example.hadoop.pd.simple2.proto";

  public CalculatorService() {

  }

  public Configuration getLocalConf() {
    return localConf;
  }

  public void setLocalConf(Configuration localConf) {
    this.localConf = localConf;
  }

  @Override
  public int add(int a, int b) {
    return a + b;
  }

  public int minus(int a, int b) {
    return a - b;
  }


  public void serviceStart() throws UnknownHostException {
    InetSocketAddress addr = getInetSocketAddress();
    this.server =
        getServer(Calculator.class, this, addr,
            localConf, null,
            localConf.getInt(YarnConfiguration.RM_RESOURCE_TRACKER_CLIENT_THREAD_COUNT,
                YarnConfiguration.DEFAULT_RM_RESOURCE_TRACKER_CLIENT_THREAD_COUNT),
            null);

    server.start();
  }

  public static InetSocketAddress getInetSocketAddress() throws UnknownHostException {
    return new InetSocketAddress(InetAddress.getByName("localhost"), 8038);
  }

  public Server getServer(Class<?> protocol, Object instance,
                          InetSocketAddress addr, Configuration conf,
                          SecretManager<? extends TokenIdentifier> secretManager, int numHandlers,
                          String portRangeConfig) {

    Constructor<?> constructor = serviceCache.get(protocol);
    if (constructor == null) {
      Class<?> pbServiceImplClazz = null;
      try {
        pbServiceImplClazz = localConf
            .getClassByName(getPbServiceImplClassName(protocol));
      } catch (ClassNotFoundException e) {
        throw new YarnRuntimeException("Failed to load class: ["
            + getPbServiceImplClassName(protocol) + "]", e);
      }
      try {
        constructor = pbServiceImplClazz.getConstructor(protocol);
        constructor.setAccessible(true);
        serviceCache.putIfAbsent(protocol, constructor);
      } catch (NoSuchMethodException e) {
        throw new YarnRuntimeException("Could not find constructor with params: "
            + Long.TYPE + ", " + InetSocketAddress.class + ", "
            + Configuration.class, e);
      }
    }

    Object service = null;
    try {
      service = constructor.newInstance(instance);
    } catch (InvocationTargetException e) {
      throw new YarnRuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new YarnRuntimeException(e);
    } catch (InstantiationException e) {
      throw new YarnRuntimeException(e);
    }

    Class<?> pbProtocol = service.getClass().getInterfaces()[0];
    Method method = protoCache.get(protocol);
    if (method == null) {
      Class<?> protoClazz = null;
      try {
        protoClazz = localConf.getClassByName(getProtoClassName(protocol));
      } catch (ClassNotFoundException e) {
        throw new YarnRuntimeException("Failed to load class: ["
            + getProtoClassName(protocol) + "]", e);
      }
      try {
        method = protoClazz.getMethod("newReflectiveBlockingService",
            pbProtocol.getInterfaces()[0]);
        method.setAccessible(true);
        protoCache.putIfAbsent(protocol, method);
      } catch (NoSuchMethodException e) {
        throw new YarnRuntimeException(e);
      }
    }

    try {
      return createServer(pbProtocol, addr, conf, secretManager, numHandlers,
          (BlockingService) method.invoke(null, service), portRangeConfig);
    } catch (InvocationTargetException e) {
      throw new YarnRuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new YarnRuntimeException(e);
    } catch (IOException e) {
      throw new YarnRuntimeException(e);
    }
  }

  /*
   * return  org.example.hadoop.pd.simple2.api.impl.pb.service.CalculatorPBServiceImpl
   */
  public String getPbServiceImplClassName(Class<?> protocol) {
    String packageName = protocol.getPackage().getName();
    String className = protocol.getSimpleName();
    return packageName + ".api.impl.pb.service" +"." + className + "PBServiceImpl";
  }

  /*
   * return org.example.hadoop.pd.simple2.proto.Calculator$CalculatorService
   */
  public String getProtoClassName(Class protocol) {
    String className = protocol.getSimpleName();
    return protoPackage + ".CalculatorProtos" + "$" + className + "Service";
  }

  private Server createServer(Class<?> pbProtocol, InetSocketAddress addr, Configuration conf,
                              SecretManager<? extends TokenIdentifier> secretManager, int numHandlers,
                              BlockingService blockingService, String portRangeConfig) throws IOException {
    RPC.setProtocolEngine(conf, pbProtocol, ProtobufRpcEngine.class);
    RPC.Server server = new RPC.Builder(conf).setProtocol(pbProtocol)
        .setInstance(blockingService).setBindAddress(addr.getHostName())
        .setPort(addr.getPort()).setNumHandlers(numHandlers).setVerbose(true)
        .setSecretManager(secretManager).setPortRangeConfig(portRangeConfig)
        .build();
    System.out.println("Adding protocol " + pbProtocol.getCanonicalName() + " to the server");
    server.addProtocol(RPC.RpcKind.RPC_PROTOCOL_BUFFER, pbProtocol, blockingService);
    return server;
  }

}