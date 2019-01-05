package com.banko.rsachat;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.jws.soap.SOAPBinding.Use;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.qq.util.*;

public class ChatClient extends JFrame implements WindowListener,KeyListener
{
	private User commToUser;
	private User meUser;
	private String srvIP="59.71.138.138";
	
	private JTextArea jta;
	private JTextField jtf;
	private JButton jb;
	private JLabel name;
	private Socket s;
	private PrintWriter pw;
	private BufferedReader br;
	
	public ChatClient(final User commToUser,final User meUser) 
	{
		this.meUser=meUser;
		this.commToUser=commToUser;
		
		this.setSize(300, 350);
		this.setLocation(350,200);
		this.setTitle("聊天室");
		
		jta = new JTextArea(15, 20);
		jtf = new JTextField(15);
		jtf.addKeyListener(this);
		jb = new JButton("发送");
		name = new JLabel(meUser.getNickName());
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		jp.add(name);
		jp.add(jtf);
		jp.add(jb);
		jb.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sendMsg(commToUser, meUser);
			}

		});
		
		this.setTitle(meUser.getNickName()+"正在和"+this.commToUser.getNickName()+"聊天");
		this.setLayout(new BorderLayout());
		this.add(jta, BorderLayout.CENTER);
		this.add(jp, BorderLayout.SOUTH);
		getMessage();
		this.setVisible(true);
	}

	/**
	 * 将聊天面板中的消息发送出去
	 * @param commToUser
	 * @param meUser
	 */
	private void sendMsg(final User commToUser, final User meUser)
	{
		String message = commToUser.getIP() + " " + meUser.getIP()+" "
				+  meUser.getNickName()+"说:" + jtf.getText();
		pw.println(message);
		pw.flush(); // 清空缓冲区
		jtf.setText("");
	}
	
	private void getMessage()
	{
		try
		{
			s = new Socket(srvIP, 8888);
			pw = new PrintWriter(s.getOutputStream());
			new ClientThread().start();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	class ClientThread extends Thread{
		public void run() 
		{
			while(true) 
			{
				try
				{
					br = new BufferedReader(new InputStreamReader(s.getInputStream()));
					String message = br.readLine();
//					message = RSAChinese.decrypt(message, RSAChinese.key[2], RSAChinese.key[1]);
					jta.append(message + "\n");
				} 
				catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	}
	
	public static void main(String[] args) 
	{
//		new ChatClient("");
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		// TODO Auto-generated method stub
		this.dispose();
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyChar()==KeyEvent.VK_ENTER)
		{
			sendMsg(commToUser, meUser);
		}
	}
}
