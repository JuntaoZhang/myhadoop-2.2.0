package org.apache.hadoop.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.File;

/**
 * Created by juntaozhang on 3/2/15.
 */
public class TestDebugUtil {
  private static Log LOG = LogFactory.getLog(TestDebugUtil.class);
  @Test
  public void testPrintFile() throws Exception {
    org.apache.hadoop.util.DebugUtil
        .printFile(LOG, new File("README.txt"), "[name:%s] file is [\n%s\n]%n");
    LOG.info("end");
  }
}
