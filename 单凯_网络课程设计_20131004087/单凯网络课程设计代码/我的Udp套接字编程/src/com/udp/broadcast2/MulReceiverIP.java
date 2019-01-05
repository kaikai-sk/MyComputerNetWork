package com.udp.broadcast2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 数据接收端
 * 
 * @author 单凯
 */
public class MulReceiverIP extends Thread
{

	int port = 9898;
	InetAddress group;
	MulticastSocket socket; // socket sends and receives the packet.
	DatagramPacket packet;
	byte[] buf = new byte[30];// If the message is longer than the packet's
								// length, the message is truncated.

	public MulReceiverIP()
	{
		try
		{
			socket = new MulticastSocket(port);

			group = InetAddress.getByName("233.0.0.0");
			socket.joinGroup(group); // 加入广播组,加入group后,socket发送的数据报,可以被加入到group中的成员接收到。
			packet = new DatagramPacket(buf, buf.length);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				socket.receive(packet);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// String message=new String(buf);
			String message = new String(packet.getData(), 0, packet.getLength());// very
																					// important
																					// !!
			System.out.println("接受消息内容: " + message);

		}
	}

	public static void main(String[] args)
	{
		new MulReceiverIP().start();
	}

}