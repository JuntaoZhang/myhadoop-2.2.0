package test.org.example.hadoop.pd.rpc;

import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.*;
import org.apache.hadoop.ipc.protobuf.RpcHeaderProtos;
import org.example.hadoop.pd.rpc.Foo0;
import org.example.hadoop.pd.rpc.Foo1;
import org.example.hadoop.pd.rpc.Server;
import org.example.hadoop.pd.rpc.TestRpcService;
import org.example.hadoop.pd.rpc.protobuf.TestProtos.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by Juntao.Zhang on 1/6/15.
 */
public class Client {

  InetSocketAddress addr;

  @Before
  public void before() throws UnknownHostException {
    addr = new InetSocketAddress(InetAddress.getByName(Server.ADDRESS), Server.PORT);
  }

  @Test
  public void simpleRpcFoo0() throws IOException {
    Configuration conf = new Configuration();
    ProtocolProxy<?> proxy0 = RPC.getProtocolProxy(Foo0.class, Foo0.versionID, addr, conf);

    Foo0 foo0 = (Foo0) proxy0.getProxy();
    System.out.println(foo0.doPing().length);
    System.out.println(foo0.doPong());
//    System.out.println(foo0.ping());
//
//    ProtocolProxy<?> proxy1 = RPC.getProtocolProxy(Foo1.class, Foo1.versionID, addr, conf);
//
//    Foo1 foo1 = (Foo1) proxy1.getProxy();
//    System.out.println(foo1.ping());
//    System.out.println(foo1.pong());
//
//    RPC.stopProxy(foo1);
    RPC.stopProxy(foo0);

  }

  // Now test a PB service - a server  hosts both PB and Writable Rpcs.
  @Test
  public void testPBService() throws Exception {
    // Set RPC engine to protobuf RPC engine
    Configuration conf2 = new Configuration();
    RPC.setProtocolEngine(conf2, TestRpcService.class,
        ProtobufRpcEngine.class);
    TestRpcService client = RPC.getProxy(TestRpcService.class, 0, addr, conf2);
    testProtoBufRpc(client);
  }

  public static void testProtoBufRpc(TestRpcService client) throws Exception {
    // Test ping method
    EmptyRequestProto emptyRequest = EmptyRequestProto.newBuilder().build();
    client.ping(null, emptyRequest);

    // Test echo method
    EchoRequestProto echoRequest = EchoRequestProto.newBuilder()
        .setMessage("hello").build();
    EchoResponseProto echoResponse = client.echo(null, echoRequest);
    Assert.assertEquals(echoResponse.getMessage(), "hello");

    // Test error method - error should be thrown as RemoteException
    try {
      client.error(null, emptyRequest);
      Assert.fail("Expected exception is not thrown");
    } catch (ServiceException e) {
      RemoteException re = (RemoteException) e.getCause();
      RpcServerException rse = (RpcServerException) re
          .unwrapRemoteException(RpcServerException.class);
      Assert.assertNotNull(rse);
      Assert.assertTrue(re.getErrorCode().equals(
          RpcHeaderProtos.RpcResponseHeaderProto.RpcErrorCodeProto.ERROR_RPC_SERVER));
    }
  }

  public static void main(String[] args) {
    System.out.println(1 << 2);
    System.out.println(1 << 4);
    System.out.println((byte) (1024 * 1024 + 7) % 256);//byte小于256
  }

}
