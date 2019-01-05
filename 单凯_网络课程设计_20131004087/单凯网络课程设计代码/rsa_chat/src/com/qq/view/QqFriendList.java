package com.qq.view;

/**
 * 我的好友列表,(也包括陌生人，黑名单)
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
 * 总体来说是卡片布局
 * 卡1：总体上是border布局，北边是按钮；中间是列表，南面是两个按钮
 * @author 单凯
 *
 */
//													监听按键消息
public class QqFriendList extends JFrame implements ActionListener,MouseListener{

	//处理第一张卡片(我的好友).
	JPanel jphy1,jphy2,jphy3;
	JButton jphy_jb1,jphy_jb2,jphy_jb3;
	JScrollPane jsp1;
	String owner;
	
	//处理第二张卡片(陌生人).
	JPanel jpmsr1,jpmsr2,jpmsr3;
	JButton jpmsr_jb1,jpmsr_jb2,jpmsr_jb3;
	JScrollPane jsp2;
	
	//好友列表label数组
	JLabel []jb1s;
	//好友列表
	ArrayList<User> friendList;
	User meUser;
	
	//把整个JFrame设置成CardLayout
	CardLayout cl;
	public static void main(String[] args) 
	{
		QqFriendList qqFriendList=new QqFriendList("");
	}
	
	public QqFriendList(String ownerId)
	{
		this.owner=ownerId;

		//对第一张卡片进行布局
		try
		{
			card1Layout(ownerId);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//对第二张卡片（陌生人）进行布局
		card2Layout();
		
		//将主窗体设置成卡片布局
		cl=new CardLayout();
		this.setLayout(cl);
		this.add(jphy1,"1");
		this.add(jpmsr1,"2");
		//在窗口显示自己的编号.
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
			{ // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
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
				System.out.println("找不到指定的文件");
			}
		} 
		catch (Exception e)
		{
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 对第二张卡片（陌生人）进行布局
	 */
	public void card2Layout()
	{
		// 处理第二张卡片
		jpmsr_jb1 = new JButton("我的好友");
		//注册监听
		jpmsr_jb1.addActionListener(this);
		jpmsr_jb2 = new JButton("陌生人");
		jpmsr_jb3 = new JButton("黑名单");
		jpmsr1 = new JPanel(new BorderLayout());
		// 假定有20个陌生人
		jpmsr2 = new JPanel(new GridLayout(20, 1, 4, 4));

		// 给jphy2，初始化20陌生人.
		JLabel[] jb1s2 = new JLabel[20];

		for (int i = 0; i < jb1s2.length; i++)
		{
			jb1s2[i] = new JLabel(i + 1 + "", new ImageIcon("image/mm.jpg"), JLabel.LEFT);
			jpmsr2.add(jb1s2[i]);
		}

		jpmsr3 = new JPanel(new GridLayout(2, 1));
		// 把两个按钮加入到jphy3
		jpmsr3.add(jpmsr_jb1);
		jpmsr3.add(jpmsr_jb2);

		jsp2 = new JScrollPane(jpmsr2);

		// 对jphy1,初始化
		jpmsr1.add(jpmsr3, "North");
		jpmsr1.add(jsp2, "Center");
		jpmsr1.add(jpmsr_jb3, "South");
	}
	
	/**
	 * 对第一张卡片进行布局
	 * @throws UnknownHostException 
	 */
	public void card1Layout(String ownerId) throws UnknownHostException
	{
		//处理第一张卡片(显示好友列表)
		jphy_jb1=new JButton("我的好友");
		jphy_jb2=new JButton("陌生人");
		//注册监听
		jphy_jb2.addActionListener(this);
		jphy_jb3=new JButton("黑名单");
		//总的panel
		jphy1=new JPanel(new BorderLayout());
		//假定有50个好友，中间的网格布局
		//50行，1列，行列间距都是4
		jphy2=new JPanel(new GridLayout(50,1,4,4));
		
		//给jphy2，初始化50好友.
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
			//创建每一行的label
			jb1s[i]=new JLabel(friendList.get(i).getNickName(),new ImageIcon("image/mm.jpg"),JLabel.LEFT);
			if(jb1s[i].getText().equals(ownerId))
			{
				jb1s[i].setEnabled(true);
			}
			//对每个label添加鼠标监听
			jb1s[i].addMouseListener(this);
			//加到面板当中
			jphy2.add(jb1s[i]);		
		}
		
		//南面，网格布局，两个按钮
		jphy3=new JPanel(new GridLayout(2,1));
		//把两个按钮加入到jphy3
		jphy3.add(jphy_jb2);
		jphy3.add(jphy_jb3);
		
		
		jsp1=new JScrollPane(jphy2);
		
		//对jphy1,初始化
		//将卡片1的总的panel添加内容
		jphy1.add(jphy_jb1,"North");
		jphy1.add(jsp1,"Center");
		jphy1.add(jphy3,"South");
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		//如果点击了陌生人按钮，就显示第二张卡片
		if(arg0.getSource()==jphy_jb2)
		{
			cl.show(this.getContentPane(), "2");
		}
		//点击好友按钮，显示第一张卡片
		else if(arg0.getSource()==jpmsr_jb1)
		{
			cl.show(this.getContentPane(), "1");
		}
	}

	public void mouseClicked(MouseEvent arg0) 
	{
		//响应用户双击的事件，并得到好友的编号.
		if(arg0.getClickCount()==2)
		{
			//得到该好友的编号
			String friendName=((JLabel)arg0.getSource()).getText();
			ClientChatThread chatThread=new ClientChatThread(friendList.get(getIndexOfFriend(friendName)), 
					meUser);
			chatThread.start();
		}
	}

//	返回好友下标
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
		// 得到鼠标进入的label
		JLabel jl = (JLabel) arg0.getSource();
		jl.setForeground(Color.red);
	}

	public void mouseExited(MouseEvent arg0) {
		//离开时字体变黑
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
