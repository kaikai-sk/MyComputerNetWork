package com.qq.view;

/**
 * �ҵĺ����б�,(Ҳ����İ���ˣ�������)
 */

import javax.swing.*;
import com.banko.rsachat.ChatClient;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import com.qq.util.*;

/**
 * ������˵�ǿ�Ƭ����
 * ��1����������border���֣������ǰ�ť���м����б�������������ť
 * @author ����
 *
 */
//													����������Ϣ
public class QqFriendList extends JFrame implements ActionListener,MouseListener{

	//�����һ�ſ�Ƭ(�ҵĺ���).
	JPanel jphy1,jphy2,jphy3;
	JButton jphy_jb1,jphy_jb2,jphy_jb3;
	JScrollPane jsp1;
	String owner;
	
	//����ڶ��ſ�Ƭ(İ����).
	JPanel jpmsr1,jpmsr2,jpmsr3;
	JButton jpmsr_jb1,jpmsr_jb2,jpmsr_jb3;
	JScrollPane jsp2;
	
	//�����б�label����
	JLabel []jb1s;
	//�����б�
	ArrayList<User> friendList;
	User meUser;
	
	//������JFrame���ó�CardLayout
	CardLayout cl;
	public static void main(String[] args) 
	{
		QqFriendList qqFriendList=new QqFriendList("");
	}
	
	public QqFriendList(String ownerId)
	{
		this.owner=ownerId;

		//�Ե�һ�ſ�Ƭ���в���
		try
		{
			card1Layout(ownerId);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//�Եڶ��ſ�Ƭ��İ���ˣ����в���
		card2Layout();
		
		//�����������óɿ�Ƭ����
		cl=new CardLayout();
		this.setLayout(cl);
		this.add(jphy1,"1");
		this.add(jpmsr1,"2");
		//�ڴ�����ʾ�Լ��ı��.
		this.setTitle(ownerId);
		this.setSize(280, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private ArrayList<User> readFriendList()
	{
		ArrayList<User> list=new ArrayList<User>();
		try
		{
			String encoding = "GBK";
			File file = new File("friend.txt");
			if (file.isFile() && file.exists())
			{ // �ж��ļ��Ƿ����
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null)
				{
					String[] strings=lineTxt.split(" ");
					User user=new User(strings[0], strings[1]);
					list.add(user);
				}
				read.close();
				return list;
			} 
			else
			{
				System.out.println("�Ҳ���ָ�����ļ�");
			}
		} 
		catch (Exception e)
		{
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �Եڶ��ſ�Ƭ��İ���ˣ����в���
	 */
	public void card2Layout()
	{
		// ����ڶ��ſ�Ƭ
		jpmsr_jb1 = new JButton("�ҵĺ���");
		//ע�����
		jpmsr_jb1.addActionListener(this);
		jpmsr_jb2 = new JButton("İ����");
		jpmsr_jb3 = new JButton("������");
		jpmsr1 = new JPanel(new BorderLayout());
		// �ٶ���20��İ����
		jpmsr2 = new JPanel(new GridLayout(20, 1, 4, 4));

		// ��jphy2����ʼ��20İ����.
		JLabel[] jb1s2 = new JLabel[20];

		for (int i = 0; i < jb1s2.length; i++)
		{
			jb1s2[i] = new JLabel(i + 1 + "", new ImageIcon("image/mm.jpg"), JLabel.LEFT);
			jpmsr2.add(jb1s2[i]);
		}

		jpmsr3 = new JPanel(new GridLayout(2, 1));
		// ��������ť���뵽jphy3
		jpmsr3.add(jpmsr_jb1);
		jpmsr3.add(jpmsr_jb2);

		jsp2 = new JScrollPane(jpmsr2);

		// ��jphy1,��ʼ��
		jpmsr1.add(jpmsr3, "North");
		jpmsr1.add(jsp2, "Center");
		jpmsr1.add(jpmsr_jb3, "South");
	}
	
	/**
	 * �Ե�һ�ſ�Ƭ���в���
	 * @throws UnknownHostException 
	 */
	public void card1Layout(String ownerId) throws UnknownHostException
	{
		//�����һ�ſ�Ƭ(��ʾ�����б�)
		jphy_jb1=new JButton("�ҵĺ���");
		jphy_jb2=new JButton("İ����");
		//ע�����
		jphy_jb2.addActionListener(this);
		jphy_jb3=new JButton("������");
		//�ܵ�panel
		jphy1=new JPanel(new BorderLayout());
		//�ٶ���50�����ѣ��м�����񲼾�
		//50�У�1�У����м�඼��4
		jphy2=new JPanel(new GridLayout(50,1,4,4));
		
		//��jphy2����ʼ��50����.
		friendList=readFriendList();
		jb1s=new JLabel[friendList.size()];
		for(int i=0;i<jb1s.length;i++)
		{
			if(friendList.get(i).getIP().equals(InetAddress.getLocalHost().getHostAddress()))
			{
				meUser=friendList.get(i);
				this.setTitle(friendList.get(i).getNickName());
				continue;
			}
			//����ÿһ�е�label
			jb1s[i]=new JLabel(friendList.get(i).getNickName(),new ImageIcon("image/mm.jpg"),JLabel.LEFT);
			if(jb1s[i].getText().equals(ownerId))
			{
				jb1s[i].setEnabled(true);
			}
			//��ÿ��label���������
			jb1s[i].addMouseListener(this);
			//�ӵ���嵱��
			jphy2.add(jb1s[i]);		
		}
		
		//���棬���񲼾֣�������ť
		jphy3=new JPanel(new GridLayout(2,1));
		//��������ť���뵽jphy3
		jphy3.add(jphy_jb2);
		jphy3.add(jphy_jb3);
		
		
		jsp1=new JScrollPane(jphy2);
		
		//��jphy1,��ʼ��
		//����Ƭ1���ܵ�panel�������
		jphy1.add(jphy_jb1,"North");
		jphy1.add(jsp1,"Center");
		jphy1.add(jphy3,"South");
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		//��������İ���˰�ť������ʾ�ڶ��ſ�Ƭ
		if(arg0.getSource()==jphy_jb2)
		{
			cl.show(this.getContentPane(), "2");
		}
		//������Ѱ�ť����ʾ��һ�ſ�Ƭ
		else if(arg0.getSource()==jpmsr_jb1)
		{
			cl.show(this.getContentPane(), "1");
		}
	}

	public void mouseClicked(MouseEvent arg0) 
	{
		//��Ӧ�û�˫�����¼������õ����ѵı��.
		if(arg0.getClickCount()==2)
		{
			//�õ��ú��ѵı��
			String friendName=((JLabel)arg0.getSource()).getText();
			ClientChatThread chatThread=new ClientChatThread(friendList.get(getIndexOfFriend(friendName)), 
					meUser);
			chatThread.start();
		}
	}

//	���غ����±�
	private int getIndexOfFriend(String nickName)
	{
		for (int i = 0; i < friendList.size(); i++)
		{
			if(friendList.get(i).getNickName().equals(nickName))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	
	public void mouseEntered(MouseEvent arg0)
	{
		// �õ��������label
		JLabel jl = (JLabel) arg0.getSource();
		jl.setForeground(Color.red);
	}

	public void mouseExited(MouseEvent arg0) {
		//�뿪ʱ������
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.black);
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
//	public static void main(String[] args)
//	{
////		new QqFriendList("12");
//	}
}
