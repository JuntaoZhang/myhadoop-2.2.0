package org.example.hadoop.pd.rpc;

import org.apache.hadoop.ipc.ProtocolInfo;
import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

/**
 * Created by Juntao.Zhang on 1/6/15.
 */
@ProtocolInfo(protocolName = "Foo0")
public interface Foo0 extends VersionedProtocol {
  public static final long versionID = 0L;

  byte[] doPing() throws IOException;

  String doPong() throws IOException;

}
