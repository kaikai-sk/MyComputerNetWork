package com.udp.broadcast2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * ���ݽ��ն�
 * 
 * @author ����
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
			socket.joinGroup(group); // ����㲥��,����group��,socket���͵����ݱ�,���Ա����뵽group�еĳ�Ա���յ���
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
			System.out.println("������Ϣ����: " + message);

		}
	}

	public static void main(String[] args)
	{
		new MulReceiverIP().start();
	}

}