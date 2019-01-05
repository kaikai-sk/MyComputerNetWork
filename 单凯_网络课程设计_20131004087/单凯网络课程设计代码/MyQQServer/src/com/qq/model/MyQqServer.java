/**
 * 这是qq服务器，它在监听，等待某个qq客户端，来连接
 */
package com.qq.model;

import java.net.*;
import java.io.*;
import java.util.*;

import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;
import com.qq.common.UserAction;
import com.qq.db.ConnToDbTool;
import com.qq.utils.MD5;

public class MyQqServer  implements Runnable
{
	Thread s;
	DatagramPacket recvPacket;
	DatagramSocket recvSocket,sendSocket; 
	private int myListeningPort=9999;
	private int clientRcvPort=9997;
	private int trueCientRcvPort=8001;
	
	public MyQqServer() throws SocketException
	{
		System.out.println("我是服务器，在"+myListeningPort+"监听");
		recvSocket = new DatagramSocket(myListeningPort);
		s=new Thread(this);
		s.start();
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
			
			sendSocket = new DatagramSocket();
			byte[] by = new byte[1024 * 1024];
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream bo = new ObjectOutputStream(bs);
			bo.writeObject(o);
			by = bs.toByteArray();
			DatagramPacket data = new DatagramPacket(by, by.length,
					new InetSocketAddress("localhost", clientRcvPort));
			sendSocket.send(data);
			//从服务器端接收echo信息
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * UDP方式接受传过来的对象
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object udpRecvUserObject() throws SocketException, IOException,
			ClassNotFoundException
	{
		byte[] by = new byte[1024 * 1024];

		recvPacket = new DatagramPacket(by, by.length);
		recvSocket.receive(recvPacket);
		ByteArrayInputStream bs = new ByteArrayInputStream(recvPacket.getData());
		ObjectInputStream os = new ObjectInputStream(bs);
		Object m =  os.readObject();
		return m;
	}
	
	/**
	 * 检测验证码是否正确
	 * @return 
	 */
	private boolean checkVerifyCode(String sysVC,String vcInputed)
	{
		if(sysVC==null || vcInputed==null)
		{
			return false;
		}
		if(sysVC.toUpperCase().equals(vcInputed.toUpperCase()))
		{
			//正确
			return true;
		}
		else
		{
			return false;
		}
	}

	public void run()
	{
		while(true)
		{
			try
			{
				User user=(User)udpRecvUserObject();
				//创建一个消息对象，准备返回
				Message m=new Message();
				if(user.getUserAction()==UserAction.registe)
				{
					int qqId = ConnToDbTool.addUserToDB(user.getNickName(),
							user.getPasswd());
					if (qqId > 0)
					{
						
						// 返回一个成功登陆的信息报(连接成功的信息)
						m.setMesType(MessageType.message_registe_succeed);
						m.setInfo(new Integer(qqId).toString());
						udpSendObject(m);

						// 开启一个聊天线程（Udp）
						UdpCommunicationThread thread = new UdpCommunicationThread(
								recvPacket.getAddress().getHostAddress(), trueCientRcvPort);
					}
					else
					{
						m.setMesType(MessageType.message_registe_failed);
					
					}
				}
				if ( checkVerifyCode(user.getSysVerifyCode(),
								user.getVerifyCodeInputed())
						&& user.getUserAction() == UserAction.login)
				{
					if(ConnToDbTool.checkUser(user.getQqID(),
							new MD5().getMD5ofStr(user.getPasswd())))
					{
						// 返回一个成功登陆的信息报(连接成功的信息)
						m.setMesType(MessageType.message_succeed);
						udpSendObject(m);

						// 开启一个聊天线程（Udp）
						UdpCommunicationThread thread = new UdpCommunicationThread(
								recvPacket.getAddress().getHostAddress(),trueCientRcvPort);
					}
				}
				else 
				{
					m.setMesType(MessageType.message_login_fail);
					udpSendObject(m);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
