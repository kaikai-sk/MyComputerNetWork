package com.tcp.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyClient extends JFrame implements ActionListener,KeyListener
{
	JTextArea jta=new JTextArea();
	JTextField jtf=new JTextField(20);
	JButton jb=new JButton("发送");
	JScrollPane jsp;
	JPanel jp1=new JPanel();
	
	Socket s;
	String oppositeIPString="127.0.0.1";
	int oppositePort=9999;
	
	public static void main(String[] args)
	{
		new MyClient();
		
	}
	
	public MyClient()
	{
		setInterface();
		//链接到服务器端
		LinkToSrv();
	}

	/**
	 * 设置界面
	 */
	private void setInterface()
	{
		jsp=new JScrollPane(jta);
		this.add(jsp,"Center");
		jp1.add(jtf);
		jp1.add(jb);
		this.add(jp1, "South");
		
		this.setTitle("简单聊天  	客户端");
		this.setSize(400,300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jb.addActionListener(this);
		jtf.addKeyListener(this);
	}
	
	public void LinkToSrv()
	{
		try
		{
			 s=new Socket(oppositeIPString,oppositePort);
			//读取socket的流对象
			BufferedReader bReader=new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (true)
			{
				String info=bReader.readLine();
				jta.append(info+"\r\n");
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(jb))
		{
			SendMsg();
		}
	}

	private void SendMsg()
	{
		PrintWriter pw;
		try
		{
			pw = new PrintWriter(s.getOutputStream(),true);
			pw.println("客户端说："+jtf.getText()+"\r\n");
			jta.append("客户端说："+jtf.getText()+"\r\n");
			jtf.setText("");
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent arg0)
	{		
	}

	public void keyReleased(KeyEvent arg0)
	{
		if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
		{
			SendMsg();
		}
	}

	public void keyTyped(KeyEvent arg0)
	{
		
	}
}
