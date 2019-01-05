/**
 * ���ǿͻ������ӷ������ĺ�̨
 */
package com.qq.model;
import java.util.*;
import java.net.*;
import java.io.*;

import com.qq.common.*;

/**
 * �ͻ������ӷ���������
 * @author ����
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
	 * ����ע������
	 * ���أѣѺţ������Ļ�ʧ����
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
	
	//���͵�һ������
	public boolean sendLoginInfoToServer(Object o)
	{
		//���û������͸�������
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
	 * ���շ��ص���Ϣ����
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
	 * ���û���Ϣ���͸�������
	 * @param o Ҫ���͵�User����
	 */
	private void udpSendObject(Object o)
	{
		//���û���Ϣ���͸�������
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
