package com.tcp.transobject;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.common.User;

public class MyServer implements Runnable
{
	Thread thread;
	
	public static void main(String[] args)
	{
		MyServer ms=new MyServer();
	}
	
	public MyServer()
	{
		thread=new Thread(this);
		thread.start();
	}

	public void run()
	{
		Socket s;
		try
		{
			//��������6666�˿ڽ��м���
			ServerSocket ss=new ServerSocket(6666);
			System.out.println("��������6666�˿ڽ��м�������������������");
			while (true)
			{
				s=ss.accept();
				//һ����������ʽ�Ƕ�ȡ�����͵���user����
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User u=(User)ois.readObject();
				//������յ�����Ϣ
				System.out.println("�ӿͻ��˽��յ���"+"		"+u.getName()+"		"+u.getPass());
			}
		} 
		catch (Exception e)
		{
		}
	}
}
