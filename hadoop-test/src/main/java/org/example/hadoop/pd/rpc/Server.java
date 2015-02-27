package org.example.hadoop.pd.rpc;

import com.google.protobuf.BlockingService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;
import org.example.hadoop.pd.rpc.protobuf.TestRpcServiceProtos.*;
import org.apache.hadoop.net.NetUtils;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Juntao.Zhang on 1/6/15.
 */
public class Server {
//  public static final String ADDRESS = "114.90.33.85";
  public static final String ADDRESS = "172.16.251.129";
  public static final int PORT = 8888;

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    RPC.Server server = new RPC.Builder(conf)
        .setProtocol(Foo0.class)
        .setInstance(new Foo0Impl())
        .setBindAddress(ADDRESS)
        .setPort(PORT)
        .setNumHandlers(2)
        .setVerbose(true)
        .build();

    server.addProtocol(RPC.RpcKind.RPC_WRITABLE,Foo1.class,new Foo1Impl());
//    RPC.setProtocolEngine(conf,TestRpcService.class,ProtobufRpcEngine.class);//默认RpcEngine
    //Todo 应该有其他配置
//    Class.forName("org.apache.hadoop.ipc.ProtobufRpcEngine");

    // Add Protobuf server
    // Create server side implementation
//    PBServerImpl pbServerImpl = new PBServerImpl();
//    BlockingService service = TestProtobufRpcProto.newReflectiveBlockingService(pbServerImpl);
//    server.addProtocol(RPC.RpcKind.RPC_PROTOCOL_BUFFER, TestRpcService.class, service);

    server.start();


  }
}
