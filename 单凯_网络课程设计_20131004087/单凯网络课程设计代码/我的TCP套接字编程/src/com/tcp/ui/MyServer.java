package com.tcp.ui;

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

public class MyServer extends JFrame implements ActionListener, KeyListener
{
	JTextArea jta = new JTextArea();
	JTextField jtf = new JTextField(20);
	JButton jb = new JButton("����");
	JScrollPane jsp;
	JPanel jp1 = new JPanel();

	Socket s;

	public static void main(String[] args)
	{
		new MyServer();
	}

	public MyServer()
	{
		setInteface();
		// �������ȴ��ͻ��˵�����
		MyListen();
	}

	/**
	 * ���ý���
	 */
	private void setInteface()
	{
		jsp = new JScrollPane(jta);
		this.add(jsp, "Center");
		jp1.add(jtf);
		jp1.add(jb);
		this.add(jp1, "South");

		this.setTitle("������	��������");
		this.setSize(400, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jb.addActionListener(this);
		jtf.addKeyListener(this);
	}

	//�������ȴ��ͻ��˵�����
	public void MyListen()
	{
		ServerSocket ss;
		try
		{
			ss = new ServerSocket(9999);
			System.out.println("����9999�˿ڼ���");
			while (true)
			{
				s = ss.accept();
				// ��дsocket��������
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				System.out.println("���ӳɹ�");
				String info = br.readLine();
				jta.append(info + "\r\n");
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(jb))
		{
			SendMsg();
		}

	}

	private void SendMsg()
	{
		String info = jtf.getText();
		try
		{
			PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
			pw.println("��������˵��" + info + "\r\n");
			jta.append("��������˵��" + info + "\r\n");
			jtf.setText("");
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			SendMsg();
		}
	}

	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}
}
