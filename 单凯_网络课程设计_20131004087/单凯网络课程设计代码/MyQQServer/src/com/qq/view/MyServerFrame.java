/**
 * 这是服务器端的控制界面，可以完成启动服务器，关闭服务器
 * 可以管理和监控用户.
 */
package com.qq.view;

import javax.swing.*;

import com.qq.model.MyQqServer;

import java.awt.*;
import java.awt.event.*;
import java.net.SocketException;
public class MyServerFrame extends JFrame implements ActionListener 
{
	JPanel jp1;
	JButton jb1,jb2;
	
	public static void main(String[] args) 
	{
		MyServerFrame mysf=new MyServerFrame();
	}
	
	public MyServerFrame()
	{
		jp1=new JPanel();
		jb1=new JButton("启动服务器");
		jb1.addActionListener(this);
		jb2=new JButton("关闭服务器");
		jp1.add(jb1);
		jp1.add(jb2);
		
		
		this.add(jp1);
		this.setSize(500, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		//如果点击启动服务器按钮
		if(arg0.getSource()==jb1)
		{
			try
			{
				new MyQqServer();
			} 
			catch (SocketException e)
			{
				e.printStackTrace();
			}
		}
		if (arg0.getSource()==jb2)
		{
			System.exit(0);
		}
	}
}
