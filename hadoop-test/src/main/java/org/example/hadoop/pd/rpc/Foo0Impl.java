package org.example.hadoop.pd.rpc;

import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Juntao.Zhang on 1/6/15.
 */
public class Foo0Impl implements Foo0 {

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
  public byte[] doPing() {
//    return ("foo0 doPing " + new Date().toString()).getBytes();
    byte[] tmp = new byte[1024*512];
    for (int i = 0; i < tmp.length; i++) {
      tmp[i]=(byte)i;
    }
    return tmp;
  }


  @Override
  public String doPong() {
    return "foo0 doPong " + new Date().toString();
  }

}
