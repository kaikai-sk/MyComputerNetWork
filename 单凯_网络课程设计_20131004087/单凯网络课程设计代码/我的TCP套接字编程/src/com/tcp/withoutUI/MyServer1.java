/**
 * �������˳���
 * ��9999�˿ڴ�����
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
			//	��9999�˿ڼ���
			ServerSocket ss=new ServerSocket(9999);
			//�ȴ�ĳ���ͻ��������ӣ��ú����᷵��һ��socket����
			Socket s=ss.accept();
			
			//socket�����������
			BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
			//�ӿ���̨��������
			BufferedReader brConsole=new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				String infoFromClient=br.readLine();
				System.out.println("���������յ���"+infoFromClient);
				System.out.println("��������ϣ���Կͻ���˵�Ļ�");
				//��ͻ��˷�����Ϣ
				pw.println(brConsole.readLine());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
