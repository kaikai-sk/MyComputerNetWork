package com.qq.view;

import java.awt.*;
import java.awt.event.*;

import javafx.stage.FileChooser;

import javax.swing.*;

import com.qq.utils.DES;
import com.qq.utils.FindByteArray;
import com.qq.utils.MyUdpCommunicationTool;
import com.qq.utils.NormalizeToEight;

import java.net.*;
import java.util.Date;
import java.io.*;
import java.math.BigInteger;

public class UDPCommunicationClient extends JFrame implements Runnable
{
	//与DH算法相关变量
	private int g = 5;
	private int p = 97;
	private String K = "";// 共享的密钥
	private int random;  //随机生成的数x
	private String publicKeyB = "";// B的公钥(R2)
	private byte[] ESCEOT; // 帧定界符
	
	//与通信相关的变量
	private DatagramSocket sendSocket;
	private DatagramPacket receivePacket;
	Thread c;
	private String oppositeIPAddress = "127.0.0.1";
	private int oppositePort = 8002;
	private int myLIstenningPort=8001;
	
	//与界面相关的变量
	JPanel contentPane;
	JLabel jLabel1 = new JLabel();
	TextArea jTextArea1 = new TextArea(100, 100);
	JLabel jLabel2 = new JLabel();
	JTextField jTextField1 = new JTextField();
	JButton jb = new JButton("产生发送R1");
	JButton jb2 = new JButton("产生共享密钥");
//	JButton sendFileButton=new JButton("发送文件");
	
	public UDPCommunicationClient(String title)
	{
		try
		{
			initGui(title);
			//初始化发送套接字和接收线程
			initSocketAndRecvthread();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initGui(String title) throws Exception
	{
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(null);
		this.setSize(new Dimension(400, 500));
		this.setLocation(300, 100);
		this.setTitle("UDPCLient");
		jLabel1.setText("通信记录:");
		jLabel1.setBounds(new Rectangle(16, 5, 68, 27));
		contentPane.setLayout(null);
		jTextArea1.setBounds(new Rectangle(15, 33, 349, 340));
		jTextArea1.setEditable(false);
		jLabel2.setText("输入通信内容:");
		jLabel2.setBounds(new Rectangle(17, 383, 92, 37)); // 创建输入内容区域
		jTextField1.setText("client");
		jTextField1.setBounds(new Rectangle(112, 385, 250, 31));
		jTextField1.setEditable(false);
		jTextField1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jTextField1_actionPerformed(e);
			}
		});
		//jb和jb2互相遮盖，位置相同
		jb.setBounds(new Rectangle(120, 425, 120, 30));
		jb.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jb_actionPerformed(e);
			}
		});
		jb2.setBounds(new Rectangle(120, 425, 120, 30));
		jb2.setVisible(false);
		jb2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jb2_actionPerformed(e);
			}
		});
//		sendFileButton.setBounds(150, 425, 120, 30);
//		sendFileButton.setVisible(false);
//		sendFileButton.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//				//发送文件的函数
//				//sendFileFunc();
//			}
//
//			
//		});
		
		contentPane.add(jLabel1, null);
		contentPane.add(jTextArea1, null);
		contentPane.add(jTextField1, null);
		contentPane.add(jLabel2, null);
		contentPane.add(jb);
		contentPane.add(jb2);
//		contentPane.add(sendFileButton);
		this.setTitle(title);
	
	}

	/**
	 * 发送文件的函数
	 */
	private void sendFileFunc()
	{
		File readyToSendFile=openFile();
//		new UdpSendFileThread(
//				oppositePort, myLIstenningPort, readyToSendFile.getPath(),
//				oppositeIPAddress).start();
	}
	
	/**
	 * 通过文件打开对话框得到File对象
	 * @return
	 */
	private File openFile()  //打开文件   
    {   
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setDialogTitle("Open File");   
		int    flag=-100;
        //这里显示打开文件的对话框   
		try
		{
			flag = fileChooser.showOpenDialog(this);
		} 
		catch (HeadlessException head)
		{

			System.out.println("Open File Dialog ERROR!");
		}
          
		// 如果按下确定按钮，则获得该文件。
		if (flag == JFileChooser.APPROVE_OPTION)
		{
			// 获得该文件
			File f = fileChooser.getSelectedFile();
			System.out.println("open file----" + f.getName());
			return f;
		}
		else 
		{
			return null;
		}
    }   
	
	/**
	 * 非界面的初始化
	 */
	private void initSocketAndRecvthread()
	{
		try
		{
			sendSocket = new DatagramSocket();
		} 
		catch (SocketException e)
		{
			jTextArea1.append("不能打开数据报Socket,或者数据报Socket无法与指定端口连接！");
		}
		
		do
		{
			random = (int) (Math.random() * p);
		}while (random <= 1);
		ESCEOT = new byte[4];
		ESCEOT[0] = 2;
		ESCEOT[1] = 7;
		ESCEOT[2] = 0;
		ESCEOT[3] = 3;

		c = new Thread(this); // 创建一个线程
		c.start();
	}

	//接收线程
	public void run()
	{
		while (true)
		{
			try
			{
				byte buf[] = new byte[1024];
				receivePacket = new DatagramPacket(buf, buf.length);
				sendSocket.receive(receivePacket);
				
				byte[] databyte = receivePacket.getData();
				String receiveString = new String(databyte);
				if (jTextField1.isEditable() == false)
				{
					publicKeyB = receiveString;
					addMsgToTextArea("服务器端的R2：", receiveString);
				}
				if (jTextField1.isEditable())
				{
					byte[] databyteEOT = new byte[databyte.length];
					copyByteArray(databyteEOT, databyte);

					// 找到在哪里填充了字节
					int indexEOT = FindByteArray.findByteArray(databyteEOT, ESCEOT); 

					if (indexEOT != -1) // 把帧定界符去掉
					{// 减去字节填充
						databyte = new byte[indexEOT];
						for (int i = 0; i < indexEOT; i++)
						{
							databyte[i] = databyteEOT[i];
						}
					} 
					else
					{
						throw new Exception("没有找到 EOT");
					}

					byte[] data1 = DES.decrypt(databyte, K);
					receiveString = new String(data1);

					addMsgToTextArea("\n服务端密文是:", new String(databyte));
					addMsgToTextArea("\n服务端明文是:", receiveString + "\n");
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	protected void processWindowEvent(WindowEvent e)
	{
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			System.exit(0);
		}
	}

	//发送文本框的事件处理函数
	public void jTextField1_actionPerformed(ActionEvent e)
	{
		//只有在发送文本框可编辑的时候才会处理事件
		if (jTextField1.isEditable())
		{
			try
			{
				String string1 = jTextField1.getText().trim();
				
				//将饭送的消息显示到TextArea中
				addMsgToTextArea("客户端",string1);
				
				jTextField1.setText("");
				byte[] databyte = string1.getBytes();

				databyte = DES.encrypt(databyte, K);

				for (int i = 0; i < databyte.length; i++)
					System.out.print(databyte[i]);
				System.out.println();

				// 增加字节填充: 两个字符ESC,EOT（4个字节长度）实际中发现，只能填充[0000]
				int length = databyte.length + 4;
				byte[] databyteEND = new byte[length];
				for (int i = 0; i < databyte.length; i++)
				{
					databyteEND[i] = databyte[i];
				}
				databyteEND[length - 4] = 2;
				databyteEND[length - 3] = 7; // ESC: 27
				databyteEND[length - 2] = 0;
				databyteEND[length - 1] = 3; // EOT: 03
				
				System.out.println("发送的流：");
				for (int i = 0; i < databyteEND.length; i++)
					System.out.print(databyteEND[i]);
				System.out.println();

				MyUdpCommunicationTool.sendUdpDatagram(sendSocket,databyteEND,oppositeIPAddress,
						oppositePort);
				
			}
			catch (Exception ioe)
			{
				jTextArea1.append("网络通信出现错误,问题在于" + e.toString());
			}
		}
	}

	/**
	 * 向文本框中添加聊天信息
	 * @param string1
	 */
	private void addMsgToTextArea(String who,String msg)
	{
		jTextArea1.append("\n"+who+":");
		jTextArea1.append(msg + "\n");
	}

	/**
	 * “发送R1” 按钮的事件处理函数
	 * @param e
	 */
	public void jb_actionPerformed(ActionEvent e)
	{
		try
		{
			sendR1();
		} 
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	/**
	 * dh算法中的第三步，客户端先发送R1
	 */
	private void sendR1() throws Exception
	{
		//计算R1
		String publicKeyA = modularExponentiation(String.valueOf(g));
		//向TextArea中添加显示信息
		addDHInfoToTextArea(publicKeyA);
		//发送UDP数据报
		MyUdpCommunicationTool.sendUdpDatagram(sendSocket,publicKeyA,oppositeIPAddress,oppositePort);
	    
		jb2.setVisible(true);
		jb.setVisible(false);
	}

	/**
	 * 在进行密钥协商的时候向TextArea中添加显示信息
	 * @param publicKeyA
	 */
	private void addDHInfoToTextArea(String publicKeyA)
	{
		jTextArea1.append("发送给:" + oppositeIPAddress + " 端口号:" + oppositeIPAddress
					+ "\n");
		jTextArea1.append("\n客户端的R1:");
		jTextArea1.append(publicKeyA);
	}
	
	/**
	 * 计算共享密钥的按钮的事件响应函数
	 * @param e
	 */
	public void jb2_actionPerformed(ActionEvent e)
	{
		K = modularExponentiation(publicKeyB.trim());
		K = NormalizeToEight.normalize(K);
		
		//将共享密钥的信息添加到TextArea中
		addSharedKeyInfoToTextArea(K);
		
		jb2.setVisible(false);
		jTextField1.setEditable(true);
//		sendFileButton.setVisible(true);
	}

	/**
	 * 将共享密钥的信息添加到TextArea中
	 * @param K   共享密钥
	 */
	private void addSharedKeyInfoToTextArea(String K)
	{
		jTextArea1.append("\n共享密钥是：" + K + "\n");
		jTextArea1.append("\n产生共享密钥使用了 Diffie-Hellman算法");
		jTextArea1.append("\n加密算法使用了 DES算法");
		jTextArea1.append("\n-----------请放心，以下通信是经过加密的！------------\n");
	}

	/**
	 * 模幂运算
	 * @param base    被模数
	 * @return        求模的结果
	 */
	public String modularExponentiation(String base) 
	{
		BigInteger tmp = new BigInteger(base); // tmp = base
		tmp = tmp.pow(random); // tmp = base^random
		tmp = tmp.mod(BigInteger.valueOf(p)); // tmp = tmp%p
		return tmp.toString();
	}

	/*
	 * 把array2 复制给array1 复制成功返回true 复制失败返回false
	 */
	public boolean copyByteArray(byte[] array1, byte[] array2)
	{
		if (array1.length != array2.length)
			return false;

		for (int i = 0; i < array1.length; i++)
		{
			array1[i] = array2[i];
		}
		return true;
	}
	
	public static void main(String[] args)
	{
		new UDPCommunicationClient("").setVisible(true);
	}
}