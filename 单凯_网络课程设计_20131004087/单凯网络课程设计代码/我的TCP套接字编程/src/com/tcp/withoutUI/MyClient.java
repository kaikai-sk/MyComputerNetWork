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
			//����ĳ����������		ip			�˿ں�
			Socket s=new Socket("127.0.0.1", 9999);
			//����������������
			//ͨ��pw��sд���ݣ�true��ʾ��ʱˢ��
			PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
			BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			//��ȡ����̨�����������
			BufferedReader brConsole=new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				System.out.println("����������Է�����˵�Ļ���");
				pw.println(brConsole.readLine());
				String infoFromSrv = br.readLine();
				System.out.println("�ͻ��˽��յ���" + infoFromSrv);
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
