package com.udp.WithGUI;

//用户B端
import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class BUser extends JFrame
{
	JTextArea mainArea;
	JTextArea sendArea;
	JButton sendBtn;
	BUserChat userchat;

	public void setBUserChat(BUserChat userchat)
	{
		this.userchat = userchat;
	}

	// 构造方法
	public BUser()
	{
		super("客户B");
		Container contain = getContentPane();
		contain.setLayout(new BorderLayout(3, 3));
		mainArea = new JTextArea();
		mainArea.setEditable(false);
		JScrollPane mainAreaP = new JScrollPane(mainArea);// 为文本区加滚动条
		mainAreaP.setBorder(BorderFactory.createTitledBorder("聊天记录"));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		sendArea = new JTextArea(3, 8);
		JScrollPane sendAreaP = new JScrollPane(sendArea);
		userchat = new BUserChat(this);
		userchat.start();
		sendBtn = new JButton("发送");
		// 事件处理
		sendBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				userchat.sendMsg(sendArea.getText().trim());
				mainArea.append("[客户B]" + sendArea.getText().trim() + "\n");
				sendArea.setText("");
			}
		});

		JPanel tmpPanel = new JPanel();
		tmpPanel.add(sendBtn);
		panel.add(tmpPanel, BorderLayout.EAST);
		panel.add(sendAreaP, BorderLayout.CENTER);
		contain.add(mainAreaP, BorderLayout.CENTER);
		contain.add(panel, BorderLayout.SOUTH);
		setSize(500, 300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 主方法
	public static void main(String[] args)
	{
		BUser ui = new BUser();
	}
}

// 线程
class BUserChat extends Thread
{
	BUser ui;

	BUserChat(BUser ui)
	{
		this.ui = ui;
		ui.setBUserChat(this);
	}

	// 重写run()
	public void run()
	{
		// 接收数据包
		String s = null;
		DatagramSocket mail_data = null;
		DatagramPacket pack = null;
		byte data[] = new byte[8192];
		pack = new DatagramPacket(data, data.length);
		try
		{
			mail_data = new DatagramSocket(1234);
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true)
		{
			if (mail_data == null)
				break;
			else
			{
				try
				{
					mail_data.receive(pack);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String msg = new String(pack.getData(), 0, pack.getLength());
				ui.mainArea.append("[客户A]:" + msg + "\n");
			}
		}
	}

	public void sendMsg(String s)
	{// 发送数据包
		byte buffer[] = s.getBytes();
		try
		{
			InetAddress add = InetAddress.getByName("localhost");
			DatagramPacket data_pack = new DatagramPacket(buffer,
					buffer.length, add, 4321);
			DatagramSocket mail_data = new DatagramSocket();
			mail_data.send(data_pack);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
