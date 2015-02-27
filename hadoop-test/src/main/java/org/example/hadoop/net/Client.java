package org.example.hadoop.net;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws Exception {
		Socket client = new Socket("127.0.0.1", 6666);
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		Thread.sleep(3000);
		dos.writeUTF("hello!");
		dos.flush();
		dos.close();
		client.close();
	}
}
