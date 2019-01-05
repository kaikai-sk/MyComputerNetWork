package com.qq.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;

public class TcpCommunicationClient extends JFrame implements ActionListener
{
	// ������Ϣ�ı���
	JTextArea jta;
	JTextField jtf;
	JButton jb;
	JPanel jp;
	// �ͻ��˵���Ϣ
	User client;
	// ͨ���õ�socket
	Socket socket;

	public TcpCommunicationClient(User client)
	{
		initGui(client);
	}

	/**
	 * ��ʼ������
	 */
	private void initGui(User client)
	{
		jta = new JTextArea();
		jtf = new JTextField(15);
		jb = new JButton("����");
		jb.addActionListener(this);
		jp = new JPanel();
		jp.add(jtf);
		jp.add(jb);

		this.add(jta, "Center");
		this.add(jp, "South");
		this.setTitle("������  " + " ���ں� " + client.getNickName() + " ����");
		// ����ͼ��
		this.setIconImage((new ImageIcon("image/qq.gif").getImage()));
		this.setSize(300, 200);
		this.setVisible(true);
	}

	// дһ��������������ʾ��Ϣ
	public void showMessage(Message m)
	{
		String info = m.getSender().getNickName() + " �� " + m.getGetter().getNickName() + " ˵:" + m.getInfo() + "\r\n";
		this.jta.append(info);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource() == jb)
		{
			// ����û�����˷��Ͱ�ť��������Ϣ����
			Message m = new Message();
			m.setMesType(MessageType.message_comm_mes);
			m.setSender(new User("������"));
			m.setGetter(client);
			m.setInfo(jtf.getText());
			m.setSendTime(new java.util.Date().toString());

//			// ���͸�������.
//			try
//			{
//				ObjectOutputStream oos = new ObjectOutputStream(
//						ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
//				oos.writeObject(m);
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
		}
	}
	
	public static void main(String[] args)
	{
		new TcpCommunicationClient(new User("sk","sk"));
	}
}
