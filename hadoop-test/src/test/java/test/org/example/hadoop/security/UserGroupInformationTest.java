package test.org.example.hadoop.security;

import org.apache.hadoop.security.UserGroupInformation;
import org.junit.Test;

/**
 * Todo with kerberos
 * Created by juntaozhang on 15/1/22.
 */
public class UserGroupInformationTest {

    @Test
    public void testGetCurrentUser() throws Exception {
        UserGroupInformation userGroupInformation = UserGroupInformation.getCurrentUser();
        System.out.println(userGroupInformation);

        System.out.println("hello");
    }
}
