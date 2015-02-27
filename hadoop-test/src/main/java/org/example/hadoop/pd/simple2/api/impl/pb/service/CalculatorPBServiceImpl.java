package org.example.hadoop.pd.simple2.api.impl.pb.service;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import org.example.hadoop.pd.simple2.Calculator;
import org.example.hadoop.pd.simple2.api.CalculatorPB;
import org.example.hadoop.pd.simple2.proto.CalculatorProtos.*;

/**
 * Created by Juntao.Zhang on 12/30/14.
 */
public class CalculatorPBServiceImpl implements CalculatorPB {

  public Calculator real;

  public CalculatorPBServiceImpl(Calculator impl){
    this.real = impl;
  }

  @Override
  public ResponseProto add(RpcController controller, RequestProto request) throws ServiceException {
    ResponseProto.Builder build = ResponseProto.newBuilder();
    int add1 = request.getNum1();
    int add2 = request.getNum2();
    int sum = real.add(add1, add2);
    ResponseProto result = null;
    build.setResult(sum);
    result = build.build();
    return result;
  }

  @Override
  public ResponseProto minus(RpcController controller, RequestProto request) throws ServiceException {
    ResponseProto.Builder build = ResponseProto.newBuilder();
    int add1 = request.getNum1();
    int add2 = request.getNum2();
    int sum = real.minus(add1, add2);
    ResponseProto result = null;
    build.setResult(sum);
    result = build.build();
    return result;
  }

}