package org.example.hadoop.net;

import com.example.pojo.PersonProtos;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * OIO：先创建socket，通过socket获得输入流或者输出流
 * NIO：先打开服务器套接字通道，检索与此通道关联的服务器套接字，进行服务的绑定IP，通过open()方法找到Selector
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(6666);
        System.out.println("=====服务器运行，等待客户连接=====");
        while (true) {
            Socket client = server.accept();
            System.out.println("a client is connect!");
            DataInputStream dis = new DataInputStream(client.getInputStream());

//            byte[] tmp = new byte[100];
//            int len = dis.read(tmp);
//            System.out.println(new String(tmp, 0, len));

            PersonProtos.Person person;
            while ((person = PersonProtos.Person.parseDelimitedFrom(client.getInputStream())) != null) {
                System.out.println(person.getName());
            }

//            System.out.println(dis.readLong());
//            System.out.println(dis.readUTF());
//            System.out.println(dis.readDouble());

            dis.close();
            client.close();
        }

    }

}
