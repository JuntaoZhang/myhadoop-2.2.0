package org.example.hadoop.pd.simple2.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.example.hadoop.pd.simple2.business.CalculatorService;

/**
 * Created by Juntao.Zhang on 1/9/15.
 */
public class CalculatorServer {
  CalculatorService cs = new CalculatorService();

  protected void init(Configuration conf) throws Exception {
    conf.setInt(YarnConfiguration.RM_RESOURCE_TRACKER_CLIENT_THREAD_COUNT, 3);
    cs.setLocalConf(conf);
    cs.serviceStart();
  }

  public static void main(String[] args) throws Exception {
    new CalculatorServer().init(new Configuration());
  }
}
