package com.udp.DH;

import com.udp.DH.utils.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigInteger;
import java.net.*;
import java.io.*;

class CommunicationServer extends JFrame implements Runnable
{
	//�������صı���
	private JLabel     myLabel;
	private TextArea   textArea;
	private JTextField jTextFieldInput;
	private JPanel     panelNorth;
	private JButton    rButton;      // ���� R
	private JButton    keyButton;    // �������� key
	
	//��DH�㷨��صı���
	private int p;            // ����
	private int g;            // p��ԭ��
	private int random;       // ���ܵ��������y��
	private String r1;        // �Է���R1����������Bob��
	private String sharedKey; // ˫���������Կ
	
	Thread s;
	// �����շ�UDP���ݱ����׽���
	private DatagramSocket datagramSocket;             
	// ���������Ҫ�������Ϣ
	private DatagramPacket sendPacket, receivePacket;  
	// Ϊ�˷������ݣ�Ҫ�����ݷ�װ��DatagramPacket�У�ʹ��DatagramSocket���͸ð�
	private SocketAddress sendAddress;
	private String  name;
	private boolean canSend;
	private byte[] ESCEOT;  // ֡�����

	public CommunicationServer()
	{
		try
		{
			initGui();
			//���зǽ����һЩ��ʼ��
			elseInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void initGui() throws Exception
	{
		myLabel = new JLabel("ͨ�ż�¼");
		panelNorth = new JPanel();
		panelNorth.add(myLabel);
		panelNorth.add(getRButton());
		panelNorth.add(getKeyButton());
		jTextFieldInput = new JTextField();
		jTextFieldInput.setEditable(false);
		textArea = new TextArea();
		textArea.setEditable(false);
	
		setBounds(400, 100, 400, 400);
		setTitle("UDPServer");
		add(panelNorth, BorderLayout.NORTH);
		add(textArea, BorderLayout.CENTER);
		add(jTextFieldInput, BorderLayout.SOUTH);
		jTextFieldInput.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jTextFieldInput_actionPerformed(e);
			}
		});
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * �ǽ����һЩ��ʼ��
	 */
	private void elseInit()
	{
		canSend = false;
		p = 97;
		g = 5;
		do
		{
			random = (int) (Math.random()*p);
		}while(random <= 1);
		try
		{
			datagramSocket = new DatagramSocket(8002);      // �������շ����׽��֣�IP(chosen by the kernal),�˿ں�8002
			//System.out.println(datagramSocket.getPort()); // Ϊʲô�� -1 ?
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		ESCEOT = new byte[4];
		ESCEOT[0] = 2;
		ESCEOT[1] = 7;
		ESCEOT[2] = 0;
		ESCEOT[3] = 3;
		
		s = new Thread(this);  //�����߳�
		s.start();
	}

	public void run()  // java ���棬swing��һЩ�ؼ�����ר��һ���߳�ô��
	{
		while (true)
		{
			try
			{
				byte buf[] = new byte[1024];
				receivePacket = new DatagramPacket(buf, buf.length);  // ���Բ���ÿһ�ζ�newô����ͬһ��
				datagramSocket.receive(receivePacket);  // ͨ���׽��֣��ȴ���������
				canSend = true;                         // �������ܵ��ͻ��˵���Ϣ���ҷ��������������ܹ�������Ϣ�����ͻ��ˣ�
				sendAddress = receivePacket.getSocketAddress();
				
				byte[] databyte = receivePacket.getData();
				
				if(jTextFieldInput.isEditable() == true)   // ��jTextFieldInput���Ա༭��ʱ�򣬿��Է�����Ϣ����ʱ�Ž��м���
				{
					textArea.append("\n�ͻ��������ǣ�" + new String(databyte) + '\n');
					
					byte[] databyteEOT = new byte[databyte.length];
					copyByteArray(databyteEOT, databyte);
					
					int indexEOT = FindByteArray.findByteArray(databyteEOT, ESCEOT);  // �ҵ�������������ֽ�
					
					if(indexEOT != -1)                     // ��֡�����ȥ��
					{// ��ȥ�ֽ����
						databyte = new byte[indexEOT];
						for(int i = 0; i < indexEOT; i++)
						{
							databyte[i] = databyteEOT[i];
						}
					}
					else
					{
						throw new Exception("û���ҵ� EOT");
					}
					
					databyte = DES.decrypt(databyte, sharedKey);   
					String receivedString = new String(databyte);
					textArea.append("\n�ͻ��������ǣ�" + receivedString + '\n');
				}
				if(jTextFieldInput.isEditable() == false)  // ��jTextFieldInput�޷��༭��ʱ��(��ʼ�׶�), ���ܵ��ǹ�����Կ
				{
					textArea.append("https://github.com/caitaozhan/UDPChatAppEncryt");
					name = receivePacket.getAddress().toString().trim();
					textArea.append("\n��������:" + name + " �˿�:" + receivePacket.getPort());
					
					r1 = new String(databyte);
					r1 = r1.trim();                        // �Ѷ���Ŀո�ɾ����
					textArea.append("\n�ͻ��˵�R1 = " + r1);
				}
			}
			catch (IOException ioe)
			{
				textArea.append("����ͨ�ų��ִ���,��������" + ioe.toString());
			}
			catch (Exception e)
			{
				textArea.append("���ܳ����쳣");
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
	void jTextFieldInput_actionPerformed(ActionEvent e)
	{
		try
		{
			if (canSend == true)  // �����ȵȴ��ͻ����ȷ�����Ϣ
			{
				textArea.append("\n�����:");
				String string = jTextFieldInput.getText().trim();
				textArea.append(string);
				byte[] databyte = string.getBytes();
				
				databyte = DES.encrypt(databyte, sharedKey);  
				
				//�����ֽ����: �����ַ�ESC,EOT��4���ֽڳ��ȣ�
				int length = databyte.length + 4;
				byte[] databyteEND = new byte[length];
				for(int i = 0; i < databyte.length; i++)
				{
					databyteEND[i] = databyte[i];
				}
				databyteEND[length - 4] = 2;
				databyteEND[length - 3] = 7;  // ESC: 27
				databyteEND[length - 2] = 0;
				databyteEND[length - 1] = 3;  // EOT: 03
				
				sendPacket = new DatagramPacket(databyteEND, databyteEND.length, sendAddress);
				datagramSocket.send(sendPacket);
				
				jTextFieldInput.setText("");
				canSend = false;  // �ָ�Ϊ�����ܷ��͡���״̬���ȴ��ͻ��˷�����һ����Ϣ
			}
			else
			{
				System.out.println("��֪�����͸�˭");
			}
		}
		catch (IOException ioe)
		{
			textArea.append("����ͨ�ų��ִ�����������" + e.toString());
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	/**
	 * �õ�����������R2����button����
	 * @return
	 */
	public JButton getRButton()
	{
		if(rButton == null)  // ����һ�ε������������ʱ��rButton == null�����г�ʼ������
		{
			rButton = new JButton("��������R2");
			rButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						if(canSend == true)
						{
							String G = String.valueOf(g);
							String R2 = modularExponentiation(G);  // ��������Bob������ R2
							textArea.append("\n��������R2 = " + R2);
							byte[] databyte = R2.getBytes();
							
							sendPacket = new DatagramPacket(databyte, databyte.length,
									sendAddress);
							  // ���� R2
							datagramSocket.send(sendPacket);     
							
							// �ָ�Ϊ�����ܷ��͡���״̬���ȴ��ͻ��˷�����һ����Ϣ
							canSend = false;  
							// ������R���İ�ť��ʧ
							rButton.setVisible(false);   
							// ����������key���İ�ť����
							keyButton.setVisible(true);  
						}
						else 
						{
							System.out.println("��֪�����͸�˭");
						}
					}
					catch (IOException ioe)
					{
						textArea.append("����ͨ�ų��ִ�����������" + e.toString());
					}
					catch (Exception exception)
					{
						exception.printStackTrace();
					}
				}
			});
		}
		return rButton;
	}
	
	public JButton getKeyButton()
	{
		// ����һ�ε������������ʱ��keyButton == null�����г�ʼ������
		if(keyButton == null)  
		{
			keyButton = new JButton("����������Կ");
			keyButton.setVisible(false);;
			keyButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						// ����sharedKey������8λ��
						String key = modularExponentiation(r1);   
						// ��񻯳�Ϊ8λ��sharedKey
						sharedKey = NormalizeToEight.normalize(key); 
						//��textArea����ӹ�����Կ��Ϣ
						addSharedKeyInfoToTextArea();
						 // �ָ�Ϊ�����ܷ��͡���״̬���ȴ��ͻ��˷�����һ����Ϣ
						canSend = false;                        
						// ����������key����ť��ʧ 
						keyButton.setVisible(false);   
						// ���������Ա༭
						jTextFieldInput.setEditable(true);       
					}
					catch (NumberFormatException nfe)
					{
						textArea.append("String ת�� int �쳣");
					}
					catch (Exception exception)
					{
						exception.printStackTrace();
					}
				}	
			});
		}
		return keyButton;
	}
	
	private void addSharedKeyInfoToTextArea()
	{
		textArea.append("\n������Կ��: " + sharedKey + "\n����������Կʹ���� Diffie-Hellman-Caitao�㷨\n");
		textArea.append("�����㷨ʹ���� DES�㷨\n-----------����ģ�����ͨ���Ǿ������ܵģ�------------\n");
	}
	
	public String modularExponentiation(String base)  // ģ������
	{
		BigInteger tmp = new BigInteger(base);   // tmp = base
		tmp = tmp.pow(random);                   // tmp = base^random
		tmp = tmp.mod(BigInteger.valueOf(p));    // tmp = tmp%p
		return tmp.toString();
	}
	
	/*
	 * ��array2 ���Ƹ�array1
	 * ���Ƴɹ�����true
	 * ����ʧ�ܷ���false
	 */
	public boolean copyByteArray(byte[] array1, byte[] array2)
	{
		if(array1.length != array2.length)
			return false;
		
		for(int i = 0; i < array1.length; i++)
		{
			array1[i] = array2[i];
		}
		return true;
	}
}

public class UDPCommunicationServer
{
	public static void main(String[] args)
	{
		CommunicationServer UDPserver = new CommunicationServer();
		UDPserver.setVisible(true);
	}
}
