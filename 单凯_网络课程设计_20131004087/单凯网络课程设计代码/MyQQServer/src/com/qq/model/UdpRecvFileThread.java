package com.qq.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UdpRecvFileThread extends Thread
{
	private DatagramSocket receiveSocket;
	private String ip;
	private int serverPort;
	private int clientPort;
	private File file;
	private String path;
	private DatagramPacket pkg;
	private DatagramPacket ackPkt;

	public UdpRecvFileThread(String ip, int serverPort, int clientPort, String path)
	{
		super();
		this.ip = ip;
		this.serverPort = serverPort;
		this.clientPort = clientPort;
		this.path = path;
	}

	
	/**
	 * 文件接收函数
	 */
	public void receive()
	{
		try
		{
			// 接收文件监听端口
			receiveSocket = new DatagramSocket(serverPort);
			// 写文件路径
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(new File(path)));

			// 读取文件包
			byte[] buf = new byte[1024 * 63];
			pkg = new DatagramPacket(buf, buf.length);
			// 发送收到文件后 确认信息包
			byte[] messagebuf = new byte[1024];
			messagebuf = "ok".getBytes();
			ackPkt = new DatagramPacket(messagebuf, messagebuf.length,
					new InetSocketAddress(ip, clientPort));
			// 循环接收包，每接到一个包后回给对方一个确认信息，对方才发下一个包(避免丢包和乱序)，直到收到一个结束包后跳出循环，结束文件传输，关闭流
			while (true)
			{
				receiveSocket.receive(pkg);
				if (new String(pkg.getData(), 0, pkg.getLength()).equals("end"))
				{
					System.out.println("文件接收完毕");
					bos.close();
					receiveSocket.close();
					break;
				}
				receiveSocket.send(ackPkt);
				System.out.println(new String(ackPkt.getData()));
				bos.write(pkg.getData(), 0, pkg.getLength());
				bos.flush();
			}
			bos.close();
			receiveSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		receive();
	}
	
	public String getPath()
	{
		return path;
	}

	public String getip()
	{
		return ip;
	}

	public void setPath(String path)
	{
		this.ip = path;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

}
