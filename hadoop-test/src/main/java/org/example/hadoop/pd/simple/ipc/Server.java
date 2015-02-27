package org.example.hadoop.pd.simple.ipc;

/**
 * 还需要一个发送、接受信息的ipc server/client端。
 * 这里偷懒只实现一个最最简单的server端，什么并发啊，异常处理啊，nio啊统统不考虑，因为这不是重点。
 * Created by Juntao.Zhang on 12/30/14.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import com.google.protobuf.*;
import com.google.protobuf.Descriptors.MethodDescriptor;
import org.example.hadoop.pd.simple.proto.CalculatorMsg.RequestProto;

public class Server extends Thread {
  private Class<?> protocol;
  private BlockingService impl;
  private int port;
  private ServerSocket serverSocket;

  public Server(Class<?> protocol, BlockingService protocolImpl, int port) {
    this.protocol = protocol;
    this.impl = protocolImpl;
    this.port = port;
  }

  public void run() {
    Socket clientSocket = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      //ignore
    }
    int testCount = 10; //进行10次计算后就退出

    while (testCount-- > 0) {
      try {
        clientSocket = serverSocket.accept();
        dos = new DataOutputStream(clientSocket.getOutputStream());
        dis = new DataInputStream(clientSocket.getInputStream());
        int dataLen = dis.readInt();
        byte[] dataBuffer = new byte[dataLen];
        int readCount = dis.read(dataBuffer);
        byte[] result = processOneRpc(dataBuffer);

        dos.writeInt(result.length);
        dos.write(result);
        dos.flush();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      if (dos != null) {
        dos.close();
      }
      if (dis != null) {
        dis.close();
      }
      serverSocket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public byte[] processOneRpc(byte[] data) throws Exception {
    RequestProto request = RequestProto.parseFrom(data);
    String methodName = request.getMethodName();
    MethodDescriptor methodDescriptor = impl.getDescriptorForType().findMethodByName(methodName);
    Message response = impl.callBlockingMethod(methodDescriptor, null, request);
    return response.toByteArray();
  }
}