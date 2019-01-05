package com.tcp.ManyToOneUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyServer 
{
	ServerSocket ss;
	private int myListeningPort=9999;
	
	public MyServer()
	{
		// �������ȴ��ͻ��˵�����
		MyListen();
	}

	//�������ȴ��ͻ��˵�����
	public void MyListen()
	{
		try
		{
			ss = new ServerSocket(myListeningPort);
			System.out.println("����9999�˿ڼ���");
			while (true)
			{
				Socket clientSocket=ss.accept();
				new CreateServerThread(clientSocket);
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		new MyServer();
	}
}
