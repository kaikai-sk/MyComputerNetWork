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
	//��DH�㷨��ر���
	private int g = 5;
	private int p = 97;
	private String K = "";// �������Կ
	private int random;  //������ɵ���x
	private String publicKeyB = "";// B�Ĺ�Կ(R2)
	private byte[] ESCEOT; // ֡�����
	
	//��ͨ����صı���
	private DatagramSocket sendSocket;
	private DatagramPacket receivePacket;
	Thread c;
	private String oppositeIPAddress = "127.0.0.1";
	private int oppositePort = 8002;
	private int myLIstenningPort=8001;
	
	//�������صı���
	JPanel contentPane;
	JLabel jLabel1 = new JLabel();
	TextArea jTextArea1 = new TextArea(100, 100);
	JLabel jLabel2 = new JLabel();
	JTextField jTextField1 = new JTextField();
	JButton jb = new JButton("��������R1");
	JButton jb2 = new JButton("����������Կ");
//	JButton sendFileButton=new JButton("�����ļ�");
	
	public UDPCommunicationClient(String title)
	{
		try
		{
			initGui(title);
			//��ʼ�������׽��ֺͽ����߳�
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
		jLabel1.setText("ͨ�ż�¼:");
		jLabel1.setBounds(new Rectangle(16, 5, 68, 27));
		contentPane.setLayout(null);
		jTextArea1.setBounds(new Rectangle(15, 33, 349, 340));
		jTextArea1.setEditable(false);
		jLabel2.setText("����ͨ������:");
		jLabel2.setBounds(new Rectangle(17, 383, 92, 37)); // ����������������
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
		//jb��jb2�����ڸǣ�λ����ͬ
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
//				//�����ļ��ĺ���
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
	 * �����ļ��ĺ���
	 */
	private void sendFileFunc()
	{
		File readyToSendFile=openFile();
//		new UdpSendFileThread(
//				oppositePort, myLIstenningPort, readyToSendFile.getPath(),
//				oppositeIPAddress).start();
	}
	
	/**
	 * ͨ���ļ��򿪶Ի���õ�File����
	 * @return
	 */
	private File openFile()  //���ļ�   
    {   
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setDialogTitle("Open File");   
		int    flag=-100;
        //������ʾ���ļ��ĶԻ���   
		try
		{
			flag = fileChooser.showOpenDialog(this);
		} 
		catch (HeadlessException head)
		{

			System.out.println("Open File Dialog ERROR!");
		}
          
		// �������ȷ����ť�����ø��ļ���
		if (flag == JFileChooser.APPROVE_OPTION)
		{
			// ��ø��ļ�
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
	 * �ǽ���ĳ�ʼ��
	 */
	private void initSocketAndRecvthread()
	{
		try
		{
			sendSocket = new DatagramSocket();
		} 
		catch (SocketException e)
		{
			jTextArea1.append("���ܴ����ݱ�Socket,�������ݱ�Socket�޷���ָ���˿����ӣ�");
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

		c = new Thread(this); // ����һ���߳�
		c.start();
	}

	//�����߳�
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
					addMsgToTextArea("�������˵�R2��", receiveString);
				}
				if (jTextField1.isEditable())
				{
					byte[] databyteEOT = new byte[databyte.length];
					copyByteArray(databyteEOT, databyte);

					// �ҵ�������������ֽ�
					int indexEOT = FindByteArray.findByteArray(databyteEOT, ESCEOT); 

					if (indexEOT != -1) // ��֡�����ȥ��
					{// ��ȥ�ֽ����
						databyte = new byte[indexEOT];
						for (int i = 0; i < indexEOT; i++)
						{
							databyte[i] = databyteEOT[i];
						}
					} 
					else
					{
						throw new Exception("û���ҵ� EOT");
					}

					byte[] data1 = DES.decrypt(databyte, K);
					receiveString = new String(data1);

					addMsgToTextArea("\n�����������:", new String(databyte));
					addMsgToTextArea("\n�����������:", receiveString + "\n");
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

	//�����ı�����¼�������
	public void jTextField1_actionPerformed(ActionEvent e)
	{
		//ֻ���ڷ����ı���ɱ༭��ʱ��Żᴦ���¼�
		if (jTextField1.isEditable())
		{
			try
			{
				String string1 = jTextField1.getText().trim();
				
				//�����͵���Ϣ��ʾ��TextArea��
				addMsgToTextArea("�ͻ���",string1);
				
				jTextField1.setText("");
				byte[] databyte = string1.getBytes();

				databyte = DES.encrypt(databyte, K);

				for (int i = 0; i < databyte.length; i++)
					System.out.print(databyte[i]);
				System.out.println();

				// �����ֽ����: �����ַ�ESC,EOT��4���ֽڳ��ȣ�ʵ���з��֣�ֻ�����[0000]
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
				
				System.out.println("���͵�����");
				for (int i = 0; i < databyteEND.length; i++)
					System.out.print(databyteEND[i]);
				System.out.println();

				MyUdpCommunicationTool.sendUdpDatagram(sendSocket,databyteEND,oppositeIPAddress,
						oppositePort);
				
			}
			catch (Exception ioe)
			{
				jTextArea1.append("����ͨ�ų��ִ���,��������" + e.toString());
			}
		}
	}

	/**
	 * ���ı��������������Ϣ
	 * @param string1
	 */
	private void addMsgToTextArea(String who,String msg)
	{
		jTextArea1.append("\n"+who+":");
		jTextArea1.append(msg + "\n");
	}

	/**
	 * ������R1�� ��ť���¼�������
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
	 * dh�㷨�еĵ��������ͻ����ȷ���R1
	 */
	private void sendR1() throws Exception
	{
		//����R1
		String publicKeyA = modularExponentiation(String.valueOf(g));
		//��TextArea�������ʾ��Ϣ
		addDHInfoToTextArea(publicKeyA);
		//����UDP���ݱ�
		MyUdpCommunicationTool.sendUdpDatagram(sendSocket,publicKeyA,oppositeIPAddress,oppositePort);
	    
		jb2.setVisible(true);
		jb.setVisible(false);
	}

	/**
	 * �ڽ�����ԿЭ�̵�ʱ����TextArea�������ʾ��Ϣ
	 * @param publicKeyA
	 */
	private void addDHInfoToTextArea(String publicKeyA)
	{
		jTextArea1.append("���͸�:" + oppositeIPAddress + " �˿ں�:" + oppositeIPAddress
					+ "\n");
		jTextArea1.append("\n�ͻ��˵�R1:");
		jTextArea1.append(publicKeyA);
	}
	
	/**
	 * ���㹲����Կ�İ�ť���¼���Ӧ����
	 * @param e
	 */
	public void jb2_actionPerformed(ActionEvent e)
	{
		K = modularExponentiation(publicKeyB.trim());
		K = NormalizeToEight.normalize(K);
		
		//��������Կ����Ϣ��ӵ�TextArea��
		addSharedKeyInfoToTextArea(K);
		
		jb2.setVisible(false);
		jTextField1.setEditable(true);
//		sendFileButton.setVisible(true);
	}

	/**
	 * ��������Կ����Ϣ��ӵ�TextArea��
	 * @param K   ������Կ
	 */
	private void addSharedKeyInfoToTextArea(String K)
	{
		jTextArea1.append("\n������Կ�ǣ�" + K + "\n");
		jTextArea1.append("\n����������Կʹ���� Diffie-Hellman�㷨");
		jTextArea1.append("\n�����㷨ʹ���� DES�㷨");
		jTextArea1.append("\n-----------����ģ�����ͨ���Ǿ������ܵģ�------------\n");
	}

	/**
	 * ģ������
	 * @param base    ��ģ��
	 * @return        ��ģ�Ľ��
	 */
	public String modularExponentiation(String base) 
	{
		BigInteger tmp = new BigInteger(base); // tmp = base
		tmp = tmp.pow(random); // tmp = base^random
		tmp = tmp.mod(BigInteger.valueOf(p)); // tmp = tmp%p
		return tmp.toString();
	}

	/*
	 * ��array2 ���Ƹ�array1 ���Ƴɹ�����true ����ʧ�ܷ���false
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