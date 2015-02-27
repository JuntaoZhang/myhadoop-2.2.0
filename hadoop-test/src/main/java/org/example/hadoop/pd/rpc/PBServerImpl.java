package org.example.hadoop.pd.rpc;

import com.google.common.base.Preconditions;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import org.apache.hadoop.ipc.RpcServerException;
import org.apache.hadoop.ipc.Server;
import org.example.hadoop.pd.rpc.protobuf.TestProtos;

import java.net.URISyntaxException;

/**
* Created by Juntao.Zhang on 1/6/15.
*/
public class PBServerImpl implements TestRpcService {

  @Override
  public TestProtos.EmptyResponseProto ping(RpcController unused,
                                            TestProtos.EmptyRequestProto request) throws ServiceException {
    // Ensure clientId is received
    byte[] clientId = Server.getClientId();
    Preconditions.checkNotNull(Server.getClientId());
    Preconditions.checkState(true,16== clientId.length);
    return TestProtos.EmptyResponseProto.newBuilder().build();
  }

  @Override
  public TestProtos.EchoResponseProto echo(RpcController unused, TestProtos.EchoRequestProto request)
      throws ServiceException {
    return TestProtos.EchoResponseProto.newBuilder().setMessage(request.getMessage())
        .build();
  }

  @Override
  public TestProtos.EmptyResponseProto error(RpcController unused,
                                             TestProtos.EmptyRequestProto request) throws ServiceException {
    throw new ServiceException("error", new RpcServerException("error"));
  }

  @Override
  public TestProtos.EmptyResponseProto error2(RpcController unused,
                                              TestProtos.EmptyRequestProto request) throws ServiceException {
    throw new ServiceException("error", new URISyntaxException("",
        "testException"));
  }
}
