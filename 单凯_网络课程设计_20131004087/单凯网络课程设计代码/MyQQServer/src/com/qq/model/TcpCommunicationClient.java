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
	// 聊天消息文本域
	JTextArea jta;
	JTextField jtf;
	JButton jb;
	JPanel jp;
	// 客户端的消息
	User client;
	// 通信用的socket
	Socket socket;

	public TcpCommunicationClient(User client)
	{
		initGui(client);
	}

	/**
	 * 初始化界面
	 */
	private void initGui(User client)
	{
		jta = new JTextArea();
		jtf = new JTextField(15);
		jb = new JButton("发送");
		jb.addActionListener(this);
		jp = new JPanel();
		jp.add(jtf);
		jp.add(jb);

		this.add(jta, "Center");
		this.add(jp, "South");
		this.setTitle("服务器  " + " 正在和 " + client.getNickName() + " 聊天");
		// 设置图标
		this.setIconImage((new ImageIcon("image/qq.gif").getImage()));
		this.setSize(300, 200);
		this.setVisible(true);
	}

	// 写一个方法，让它显示消息
	public void showMessage(Message m)
	{
		String info = m.getSender().getNickName() + " 对 " + m.getGetter().getNickName() + " 说:" + m.getInfo() + "\r\n";
		this.jta.append(info);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource() == jb)
		{
			// 如果用户点击了发送按钮，创建消息对象
			Message m = new Message();
			m.setMesType(MessageType.message_comm_mes);
			m.setSender(new User("服务器"));
			m.setGetter(client);
			m.setInfo(jtf.getText());
			m.setSendTime(new java.util.Date().toString());

//			// 发送给服务器.
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
