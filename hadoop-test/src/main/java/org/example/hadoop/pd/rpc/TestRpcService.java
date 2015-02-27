package org.example.hadoop.pd.rpc;

import org.apache.hadoop.ipc.ProtocolInfo;
import org.example.hadoop.pd.rpc.protobuf.TestRpcServiceProtos;

/**
* Created by Juntao.Zhang on 1/6/15.
*/
@ProtocolInfo(protocolName = "testProto", protocolVersion = 1)
public interface TestRpcService
    extends TestRpcServiceProtos.TestProtobufRpcProto.BlockingInterface {
}