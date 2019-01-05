/**
 * 服务器端程序；
 * 在9999端口处监听
 */
package com.tcp.withoutUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer1
{
	public static void main(String[] args)
	{
		new MyServer1();
	}
	
	public MyServer1()
	{
		try
		{
			//	在9999端口监听
			ServerSocket ss=new ServerSocket(9999);
			//等待某个客户端来连接，该函数会返回一个socket链接
			Socket s=ss.accept();
			
			//socket输入输出对象
			BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
			//从控制台接收输入
			BufferedReader brConsole=new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				String infoFromClient=br.readLine();
				System.out.println("服务器接收到："+infoFromClient);
				System.out.println("请输入您希望对客户端说的话");
				//向客户端返回消息
				pw.println(brConsole.readLine());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
