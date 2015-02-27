package org.example.hadoop.webapp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.webapp.WebApp;
import org.apache.hadoop.yarn.webapp.WebApps;

/**
 * Created by juntaozhang on 15/2/4.
 */
public class HelloWeb {
  public static void main(String[] args) {
    WebApp wa = WebApps.$for("hello")
        .at("localhost")
        .at(8888)
        .with(new Configuration())
        .start(new WebApp() {
          @Override
          public void setup() {
            route("/foo/action", FooController.class);
            route("/foo/:id", FooController.class, "show");
          }
        });
  }
}
