package org.example.hadoop.pd.rpc;

import org.apache.hadoop.ipc.ProtocolInfo;
import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

/**
 * Created by Juntao.Zhang on 1/6/15.
 */
@ProtocolInfo(protocolName = "Foo1")
public interface Foo1 extends VersionedProtocol {
  public static final long versionID = 0L;

  String ping() throws IOException;

  String pong() throws IOException;

}
