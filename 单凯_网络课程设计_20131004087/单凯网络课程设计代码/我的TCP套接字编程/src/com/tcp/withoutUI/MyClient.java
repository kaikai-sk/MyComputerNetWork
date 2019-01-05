package com.tcp.withoutUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient
{

	public static void main(String[] args)
	{
		new MyClient();
	}

	public MyClient()
	{
		
		try
		{
			//链接某个服务器，		ip			端口号
			Socket s=new Socket("127.0.0.1", 9999);
			//给服务器发送数据
			//通过pw向s写数据，true表示即时刷新
			PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
			BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			//读取控制台输入的流对象
			BufferedReader brConsole=new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				System.out.println("请输入您相对服务器说的话：");
				pw.println(brConsole.readLine());
				String infoFromSrv = br.readLine();
				System.out.println("客户端接收到：" + infoFromSrv);
			}
		
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
