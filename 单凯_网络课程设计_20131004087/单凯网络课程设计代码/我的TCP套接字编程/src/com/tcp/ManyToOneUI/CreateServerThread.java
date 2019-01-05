package com.tcp.ManyToOneUI;

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

// 线程类
public class CreateServerThread extends JFrame implements Runnable,ActionListener,KeyListener
{
	//Gui
	JTextArea jta = new JTextArea();
	JTextField jtf = new JTextField(20);
	JButton jb = new JButton("发送");
	JScrollPane jsp;
	JPanel jp1 = new JPanel();
		
	private Socket client;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	Thread thread;
	
	public CreateServerThread(Socket s) throws IOException
	{
		client = s;
		setInteface();
		thread=new Thread(this);
		thread.start();
		
		bufferedReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		printWriter = new PrintWriter(client.getOutputStream(), true);
		System.out.println("Client(" + client.getInetAddress().getHostAddress()+"    "+client.getPort() + ") come in...");
	}

		public void run()
		{
			try
			{
				String line= bufferedReader.readLine();
				while (!(line).equals("bye"))
				{
					if(!line.equals(""))
					{
						jta.append(client.getInetAddress().toString()+":    "+line+"\r\n");
					}
					line=bufferedReader.readLine();
				}
				printWriter.close();
				bufferedReader.close();
				client.close();
			}
			catch (IOException e)
			{
			}
		}
		/**
		 * 设置界面
		 */
		private void setInteface()
		{
			jsp = new JScrollPane(jta);
			this.add(jsp, "Center");
			jp1.add(jtf);
			jp1.add(jb);
			this.add(jp1, "South");

			this.setTitle("简单聊天服务器端:-->"+client.getInetAddress().toString()+"    "+client.getPort());
			this.setSize(400, 300);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			jb.addActionListener(this);
			jtf.addKeyListener(this);
		}
		
		private void SendMsg()
		{
			String info = jtf.getText();
			try
			{
				PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
				pw.println("服务器端说：" + info + "\r\n");
				jta.append("服务器端说：" + info + "\r\n");
				jtf.setText("");
			} 
			catch (IOException e1)
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
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource().equals(jb))
			{
				SendMsg();
			}

		}
}
