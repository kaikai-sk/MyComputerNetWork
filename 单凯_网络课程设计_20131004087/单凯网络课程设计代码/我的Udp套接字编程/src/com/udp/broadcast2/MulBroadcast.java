package com.udp.broadcast2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * ���ݷ��Ͷ�
 * @author ����
 */
public class MulBroadcast extends Thread
{

	String info = "��ĿԤ�棺 ��ϲ����500w��Ʊ��";
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
	// ��򵥵ķ���Ҳ���ǽ���һ���߳�������
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
			System.out.println("��Ϣ�ѷ��ͣ�");
		}
	}

	public static void main(String[] args)
	{
		new MulBroadcast().start();
	}

}