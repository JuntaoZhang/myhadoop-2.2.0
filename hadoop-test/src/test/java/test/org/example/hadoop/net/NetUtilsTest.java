package test.org.example.hadoop.net;

import org.apache.hadoop.net.NetUtils;
import org.junit.Test;
import com.example.pojo.PersonProtos.*;

import javax.net.SocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * Created by juntaozhang on 15/1/21.
 */
public class NetUtilsTest {
  private final static InetSocketAddress SERVER_ADDRESS = new InetSocketAddress("127.0.0.1", 6666);

  @Test
  public void oldSocket() throws Exception {
    SocketFactory socketFactory = SocketFactory.getDefault();
    Socket socket = socketFactory.createSocket();
    socket.bind(new InetSocketAddress("localhost", 0));

    // 完成套接字通道的连接过程。
    System.out.println("client connect");
    NetUtils.connect(socket, SERVER_ADDRESS, 1000);
    System.out.println("完成连接!");

    OutputStream outStream = NetUtils.getOutputStream(socket);
    outStream.write("Hello Server".getBytes());
    outStream.close();

    socket.close();
  }

  @Test
  public void newSocket() throws Exception {
    SocketChannel channel = SocketChannel.open();
    channel.bind(new InetSocketAddress("localhost", 0));
    // 完成套接字通道的连接过程。
    System.out.println("client connect");
    NetUtils.connect(channel.socket(), SERVER_ADDRESS, 1000);
    System.out.println("完成连接!");

    OutputStream outStream = NetUtils.getOutputStream(channel.socket());
    outStream.write(new Date().toString().getBytes());
    outStream.close();

    channel.close();
  }

  @Test
  public void testGetOutputStream() throws Exception {
    SocketChannel channel = SocketChannel.open();
    channel.bind(new InetSocketAddress("localhost", 0));
    // 完成套接字通道的连接过程。
    System.out.println("client connect");
    NetUtils.connect(channel.socket(), SERVER_ADDRESS, 1000);
    System.out.println("完成连接!");

    DataOutputStream dataOutputStream = new DataOutputStream(NetUtils.getOutputStream(channel.socket()));
    dataOutputStream.writeLong(System.currentTimeMillis());
    dataOutputStream.writeUTF("I'm a boy.");
    dataOutputStream.writeDouble(3.4D);
    dataOutputStream.close();

    channel.close();
  }

  @Test
  public void writeFile() throws Exception {
    String file = "data/persons.txt";
    File f = new File(file);
    if (f.exists()) {
      f.delete();
    }
    f.createNewFile();
    FileOutputStream outputStream = new FileOutputStream(f);

    System.out.println("BEGIN WRITE");

    Person.newBuilder()
        .setEmail("zjt_hans@126.com")
        .setId(1)
        .setName("张峻滔")
        .addPhone(Person.PhoneNumber.newBuilder()
            .setNumber("15851654173").setType(Person.PhoneType.MOBILE))
        .addPhone(Person.PhoneNumber.newBuilder()
            .setNumber("0519-85200388").setType(Person.PhoneType.HOME))
        .build()
        .writeDelimitedTo(outputStream);

    outputStream.close();

  }

  @Test
  public void writeDelimitedTo() throws Exception {
    writeFile();
    SocketChannel channel = SocketChannel.open();
    channel.bind(new InetSocketAddress("localhost", 0));
    // 完成套接字通道的连接过程。
    System.out.println("client connect");
    NetUtils.connect(channel.socket(), SERVER_ADDRESS, 1000);
    System.out.println("完成连接!");

    DataOutputStream dataOutputStream = new DataOutputStream(NetUtils.getOutputStream(channel.socket()));

    String file = "data/persons.txt";
    FileInputStream inputStream = new FileInputStream(new File(file));

    Person.Builder builder = Person.newBuilder();
    builder.mergeDelimitedFrom(inputStream);
    builder.build()
        .writeDelimitedTo(dataOutputStream);

    Person.newBuilder()
        .setEmail("zxzjt.cool@163.com")
        .setId(2)
        .setName("郑湘")
        .addPhone(Person.PhoneNumber.newBuilder()
            .setNumber("15851654823").setType(Person.PhoneType.MOBILE))
        .addPhone(Person.PhoneNumber.newBuilder()
            .setNumber("0511-823742323").setType(Person.PhoneType.HOME))
        .build()
        .writeDelimitedTo(dataOutputStream);


    channel.close();
  }

}
