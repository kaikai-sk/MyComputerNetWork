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
			//服务器在6666端口进行监听
			ServerSocket ss=new ServerSocket(6666);
			System.out.println("服务器在6666端口进行监听。。。。。。。。");
			while (true)
			{
				s=ss.accept();
				//一对想留的形式是读取（传送的是user对像）
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User u=(User)ois.readObject();
				//输出接收到的信息
				System.out.println("从客户端接收到："+"		"+u.getName()+"		"+u.getPass());
			}
		} 
		catch (Exception e)
		{
		}
	}
}
