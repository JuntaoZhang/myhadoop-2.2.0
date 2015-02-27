package org.example.hadoop.pd.simple2.api.impl.pb.client;

import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;
import org.example.hadoop.pd.simple2.proto.CalculatorProtos.*;
import org.example.hadoop.pd.simple2.Calculator;
import org.example.hadoop.pd.simple2.api.CalculatorPB;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Juntao.Zhang on 1/9/15.
 */
public class CalculatorPBClientImpl implements Calculator,Closeable {
  private CalculatorPB proxy;

  public CalculatorPBClientImpl(long clientVersion,
                                               InetSocketAddress addr, Configuration conf) throws IOException {
    RPC.setProtocolEngine(conf, CalculatorPB.class,
        ProtobufRpcEngine.class);
    proxy = RPC.getProxy(CalculatorPB.class, clientVersion, addr, conf);
  }

  @Override
  public void close() {
    if (this.proxy != null) {
      RPC.stopProxy(this.proxy);
    }
  }

  @Override
  public int add(int a, int b) {
    RequestProto request = RequestProto.newBuilder()
        .setNum1(a)
        .setNum2(b)
        .build();
    try {
      ResponseProto response = proxy.add(null, request);
      return  response.getResult();
    } catch (ServiceException e) {
      return -1;
    }
  }

  @Override
  public int minus(int a, int b) {
    RequestProto request = RequestProto.newBuilder()
        .setNum1(a)
        .setNum2(b)
        .build();
    try {
      ResponseProto response = proxy.minus(null, request);
      return  response.getResult();
    } catch (ServiceException e) {
      return -1;
    }
  }
}
