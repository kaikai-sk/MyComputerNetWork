package com.tcp.transobject;

import java.io.ObjectOutputStream;
import java.net.Socket;

import com.common.User;

public class MyClient
{
	public static void main(String[] args)
	{
		MyClient myClient=new MyClient();
	}
	
	public MyClient()
	{
		try
		{
			//�����׽��֣����ӵ���������ip�Ͷ˿ں�
			Socket s=new Socket("127.0.0.1", 6666);
			//ͨ��objectoutputstream��������������
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			//����user����
			User u=new User();
			u.setName("sk");
			u.setPass("sk");
			oos.writeObject(u);
		} 
		catch (Exception e)
		{
			System.out.println(e.getStackTrace()+e.getMessage());
		}
	}
}
