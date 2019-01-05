package com.udp.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * ���ݷ��Ͷ�
 * @author ����
 */
public class SendIP
{
	public static void main(String args[])
	{
		new SendIP().lanchApp();
	}

	private void lanchApp()
	{
		SendThread th = new SendThread();
		th.start();
	}

	private class SendThread extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				try
				{
					BroadcastIP();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		private void BroadcastIP() throws Exception
		{
			DatagramSocket dgSocket = new DatagramSocket();
			byte b[] = "��ã������ҷ��������Ϣ".getBytes();
			DatagramPacket dgPacket = new DatagramPacket(b, b.length,
					InetAddress.getByName("255.255.255.255"), 8989);
			dgSocket.send(dgPacket);
			dgSocket.close();
			System.out.println("send message is ok.");
		}
	}

}