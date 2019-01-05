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
			//创建套接字，链接到服务器的ip和端口号
			Socket s=new Socket("127.0.0.1", 6666);
			//通过objectoutputstream给服务器传送像
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			//穿件user对象
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
