package org.example.hadoop.yarn.applications.distributedshell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by juntaozhang on 3/2/15.
 */
public class MyShellTest {
  public static void main(String[] args) {
    String cmd = "bash -c \"pwd;ls -al\"";//todo 需要 理 清楚
    System.out.println("begin");
    Runtime run = Runtime.getRuntime();
    Process pr = null;
    try {
      pr = run.exec(cmd);
      pr.waitFor();

      BufferedReader buf = new BufferedReader(new InputStreamReader(
          pr.getInputStream()));
      String line = "";
      while ((line = buf.readLine()) != null) {
        System.out.println("System CWD content: " + line);
      }
      buf.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("end");
  }
}
