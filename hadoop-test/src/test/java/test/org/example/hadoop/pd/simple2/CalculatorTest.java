package test.org.example.hadoop.pd.simple2;

import org.apache.hadoop.conf.Configuration;
import org.example.hadoop.pd.simple2.business.CalculatorService;
import org.example.hadoop.pd.simple2.api.impl.pb.client.CalculatorPBClientImpl;
import org.junit.Assert;

import java.net.InetSocketAddress;

public class CalculatorTest {

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    InetSocketAddress addr = CalculatorService.getInetSocketAddress();
    CalculatorPBClientImpl impl = new CalculatorPBClientImpl(1,addr,conf);
    Assert.assertEquals(12, impl.add(1,11));
    System.out.println("success finished.");
    impl.close();
  }

}
