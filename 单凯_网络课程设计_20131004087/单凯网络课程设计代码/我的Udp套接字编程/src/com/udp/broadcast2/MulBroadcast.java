package com.udp.broadcast2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 数据发送端
 * @author 单凯
 */
public class MulBroadcast extends Thread
{

	String info = "节目预告： 恭喜您中500w彩票了";
	int port = 9898;
	InetAddress address;
	MulticastSocket socket;

	public MulBroadcast()
	{
		try
		{
			address = InetAddress.getByName("233.0.0.0");
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(address);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	// 最简单的方法也就是建立一个线程来运行
	public void run()
	{
		while (true)
		{
			byte[] data = info.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length,
					address, port);

			try
			{
				socket.send(packet);
				Thread.sleep(3000);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("消息已发送：");
		}
	}

	public static void main(String[] args)
	{
		new MulBroadcast().start();
	}

}