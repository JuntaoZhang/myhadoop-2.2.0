package org.example.hadoop.pd.simple2.api;

import org.apache.hadoop.ipc.ProtocolInfo;
import org.example.hadoop.pd.simple2.proto.CalculatorProtos.CalculatorService.BlockingInterface;

/**
 * Created by Juntao.Zhang on 12/30/14.
 */
@ProtocolInfo(protocolName = "org.example.hadoop.pd.simple2.api.CalculatorPB", protocolVersion = 1)
public interface CalculatorPB extends BlockingInterface {
}
