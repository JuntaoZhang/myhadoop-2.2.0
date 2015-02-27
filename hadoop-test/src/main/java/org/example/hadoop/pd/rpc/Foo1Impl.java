package org.example.hadoop.pd.rpc;

import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

/**
 * Created by Juntao.Zhang on 1/6/15.
 */
public class Foo1Impl implements Foo1 {

  @Override
  public long getProtocolVersion(String protocol, long clientVersion)
      throws IOException {
    return Foo0.versionID;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ProtocolSignature getProtocolSignature(String protocol,
                                                long clientVersion, int clientMethodsHash) throws IOException {
    Class<? extends VersionedProtocol> inter;
    try {
      inter = (Class<? extends VersionedProtocol>) getClass().
          getGenericInterfaces()[0];
    } catch (Exception e) {
      throw new IOException(e);
    }
    return ProtocolSignature.getProtocolSignature(clientMethodsHash,
        getProtocolVersion(protocol, clientVersion), inter);
  }

  @Override
  public String ping() {
    return "foo1 hello ping";
  }


  @Override
  public String pong() {
    return "foo1 hello pong";
  }

}
