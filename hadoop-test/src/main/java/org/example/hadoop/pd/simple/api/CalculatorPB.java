package org.example.hadoop.pd.simple.api;

import org.apache.hadoop.ipc.ProtocolInfo;
import org.example.hadoop.pd.simple.proto.Calculator.CalculatorService.*;

/**
 * Created by Juntao.Zhang on 12/30/14.
 */
@ProtocolInfo(protocolName = "org.example.hadoop.pd.simple.api.CalculatorPB", protocolVersion = 1)
public interface CalculatorPB extends BlockingInterface {
}
