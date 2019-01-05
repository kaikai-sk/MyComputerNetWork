/**
 * 这是客户端连接服务器的后台
 */
package com.qq.model;
import java.util.*;
import java.net.*;
import java.io.*;

import com.qq.common.*;

/**
 * 客户端连接服务器的类
 * @author 单凯
 *
 */
public class QqClientConServer 
{
	DatagramSocket datagramSocket;
	private String srvIP="127.0.0.1";
	private int srvPort=9999;
	private int myListieningPort=9997;
	
	public QqClientConServer()
	{
		try
		{
			datagramSocket= new DatagramSocket(myListieningPort);
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 发送注册请求
	 * 返回ＱＱ号，＜０的话失败了
	 */
	public int sendRegisteInfoToServer(Object o)
	{
		int qqId;
		udpSendObject(o);
		try
		{
			Message message=recvObject();
			if(message.getMesType().equals(MessageType.message_registe_succeed))
			{
				qqId=Integer.parseInt(message.getInfo());
				return qqId;
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	//发送第一次请求
	public boolean sendLoginInfoToServer(Object o)
	{
		//将用户对象发送给服务器
		udpSendObject(o);
		
		Message m;
		try
		{
			m = recvObject();
			if(m.getMesType().equals(MessageType.message_succeed))
			{
				return true;
			}
			else 
			{
				return false; 
			}
		} 
		catch(Exception exception)
		{
			exception.printStackTrace();
			return false;
		}
	}

	/**
	 * 接收返回的消息对象
	 * @return
	 */
	private Message recvObject() throws SocketException, IOException,
			ClassNotFoundException
	{
		
		byte[] by = new byte[1024 * 1024];
		DatagramPacket data = new DatagramPacket(by, by.length);
		datagramSocket.receive(data);
		ByteArrayInputStream bs = new ByteArrayInputStream(data.getData());
		ObjectInputStream os = new ObjectInputStream(bs);
		Message m = (Message) os.readObject();
		return m;
	}

	/**
	 * 将用户信息发送给服务器
	 * @param o 要发送的User对象
	 */
	private void udpSendObject(Object o)
	{
		//将用户信息发送给服务器
		try
		{
			
			DatagramSocket sendSocket = new DatagramSocket();
			byte[] by = new byte[1024 * 1024];
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream bo = new ObjectOutputStream(bs);
			bo.writeObject(o);
			by = bs.toByteArray();
			DatagramPacket data = new DatagramPacket(by, by.length,
					new InetSocketAddress("localhost", srvPort));
			sendSocket.send(data);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
