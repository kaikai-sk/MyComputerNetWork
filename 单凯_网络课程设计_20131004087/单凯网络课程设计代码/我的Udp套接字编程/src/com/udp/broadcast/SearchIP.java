package com.udp.broadcast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据接收端
 * @author 单凯
 */
public class SearchIP
{

	public static void main(String args[]) throws Exception
	{

		new SearchIP().lanchApp();
	}

	private void lanchApp()
	{
		receiveThread th = new receiveThread();
		th.start();
	}

	private class receiveThread extends Thread
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
					receiveIP();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		private void receiveIP() throws Exception
		{
			DatagramSocket dgSocket = new DatagramSocket(8989);
			byte[] by = new byte[1024];
			DatagramPacket packet = new DatagramPacket(by, by.length);
			dgSocket.receive(packet);

			String str = new String(packet.getData(), 0, packet.getLength());

			System.out.println("接收到数据大小:" + str.length());
			System.out.println("接收到的数据为：" + str);
			dgSocket.close();
			System.out.println("recevied message is ok.");
		}
	}

}