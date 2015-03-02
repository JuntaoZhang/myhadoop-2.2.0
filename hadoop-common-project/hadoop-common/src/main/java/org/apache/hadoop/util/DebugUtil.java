package org.apache.hadoop.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

import java.io.File;
import java.io.IOException;

/**
 * for debug and test
 * Created by juntaozhang on 3/2/15.
 */
public class DebugUtil {
  /**
   * org.apache.hadoop.util.DebugUtil.printFile(LOG,file,"XXXXX[name:%s] file is [\n%s\n]%n")
   *
   * @param LOG Log
   * @param file print file
   * @param msg XXXXX[name:%s] file is [\n%s\n]%n
   */
  public static void printFile(Log LOG,File file,String msg){
    if (LOG.isDebugEnabled()) {
      try {
        String fileStr = IOUtils
            .toString(new java.io.FileInputStream(file));
        LOG.debug(String.format(msg,file.getAbsolutePath(),fileStr));
      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      }
    }
  }

  public static void main(String[] args) {
    Object[] sendData = new Object[4];
    sendData[0] = Integer.valueOf(1);
    sendData[1] = "172.12.1.2";
    sendData[2] = Integer.valueOf(123);
    sendData[3] = "testadfaerfa";
    String sendDataString = String.format("%d,%s,%d,%s",(Object[]) sendData);
    System.out.println(sendDataString);
  }
}
