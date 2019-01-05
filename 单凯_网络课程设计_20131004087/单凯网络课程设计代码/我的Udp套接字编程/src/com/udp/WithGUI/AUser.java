package com.udp.WithGUI;

//�û��˳���
import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//����
public class AUser extends JFrame
{
	JTextArea mainArea;
	JTextArea sendArea;
	JButton sendBtn;
	AUserChat userchat;

	public void setAUserChat(AUserChat userchat)
	{
		this.userchat = userchat;
	}

	// ���췽��
	public AUser()
	{
		super("�ͻ�A");
		Container contain = getContentPane();
		contain.setLayout(new BorderLayout());
		mainArea = new JTextArea();
		mainArea.setEditable(false);
		JScrollPane mainAreaP = new JScrollPane(mainArea);// Ϊ�ı����ӹ�����
		mainAreaP.setBorder(BorderFactory.createTitledBorder("�����¼"));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		sendArea = new JTextArea(3, 8);
		JScrollPane sendAreaP = new JScrollPane(sendArea);
		userchat = new AUserChat(this);
		userchat.start();
		sendBtn = new JButton("����");
		// �¼�����
		sendBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				userchat.sendMsg(sendArea.getText().trim());
				mainArea.append("[�ͻ�A]" + sendArea.getText().trim() + "\n");
				sendArea.setText("");
			}
		});
		panel.add(sendBtn, BorderLayout.EAST);
		panel.add(sendAreaP, BorderLayout.CENTER);
		contain.add(mainAreaP, BorderLayout.CENTER);
		contain.add(panel, BorderLayout.SOUTH);
		setSize(500, 300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// ������
	public static void main(String[] args)
	{
		AUser ui = new AUser();
	}

	// �߳�
	class AUserChat extends Thread
	{
		AUser ui;

		AUserChat(AUser ui)
		{
			this.ui = ui;
			ui.setAUserChat(this);
		}

		// ��дrun()
		public void run()
		{
			// �������ݰ�
			String s = null;
			DatagramSocket mail_data = null;
			DatagramPacket pack = null;
			byte data[] = new byte[8192];
			pack = new DatagramPacket(data, data.length);
			try
			{
				mail_data = new DatagramSocket(4321);
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
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String msg = new String(pack.getData(), 0, pack.getLength());
					ui.mainArea.append("�ͻ�B]:" + msg + "\n");
				}
			}
		}

		public void sendMsg(String s)
		{// �������ݰ�
			byte buffer[] = s.getBytes();
			try
			{
				InetAddress add = InetAddress.getByName("localhost");
				DatagramPacket data_pack = new DatagramPacket(buffer,
						buffer.length, add, 1234);
				DatagramSocket mail_data = new DatagramSocket();
				mail_data.send(data_pack);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
