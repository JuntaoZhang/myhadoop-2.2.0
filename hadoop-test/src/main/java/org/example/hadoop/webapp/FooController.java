package org.example.hadoop.webapp;

import com.google.inject.Inject;
import org.apache.hadoop.yarn.webapp.Controller;

/**
 * Created by juntaozhang on 15/2/4.
 */
public class FooController extends Controller {
  @Inject
  FooController(RequestContext ctx) {
    super(ctx);
  }

  @Override public void index() {
    setTitle("Applications");
  }
}
