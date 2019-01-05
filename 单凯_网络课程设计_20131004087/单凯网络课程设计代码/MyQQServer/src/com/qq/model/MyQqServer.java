/**
 * ����qq�����������ڼ������ȴ�ĳ��qq�ͻ��ˣ�������
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
		System.out.println("���Ƿ���������"+myListeningPort+"����");
		recvSocket = new DatagramSocket(myListeningPort);
		s=new Thread(this);
		s.start();
	}

	/**
	 * ���û���Ϣ���͸�������
	 * @param o Ҫ���͵�User����
	 */
	private void udpSendObject(Object o)
	{
		//���û���Ϣ���͸�������
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
			//�ӷ������˽���echo��Ϣ
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * UDP��ʽ���ܴ������Ķ���
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
	 * �����֤���Ƿ���ȷ
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
			//��ȷ
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
				//����һ����Ϣ����׼������
				Message m=new Message();
				if(user.getUserAction()==UserAction.registe)
				{
					int qqId = ConnToDbTool.addUserToDB(user.getNickName(),
							user.getPasswd());
					if (qqId > 0)
					{
						
						// ����һ���ɹ���½����Ϣ��(���ӳɹ�����Ϣ)
						m.setMesType(MessageType.message_registe_succeed);
						m.setInfo(new Integer(qqId).toString());
						udpSendObject(m);

						// ����һ�������̣߳�Udp��
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
						// ����һ���ɹ���½����Ϣ��(���ӳɹ�����Ϣ)
						m.setMesType(MessageType.message_succeed);
						udpSendObject(m);

						// ����һ�������̣߳�Udp��
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
