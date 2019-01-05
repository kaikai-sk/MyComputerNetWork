/**
 * 功能:qq客户端登录界面
 */
package com.qq.view;
import java.io.*;

import javax.swing.*;

import com.qq.common.User;
import com.qq.common.UserAction;
import com.qq.model.*;

import java.awt.*;
import java.awt.event.*;

/**
 * 总体是一个边界（东西南北中）布局
 * 中间是网格布局
 * 北边是个jlabel
 * @author 单凯
 *
 */
public class QqClientLogin extends JFrame implements ActionListener,MouseListener
{

	//定义北部需要的组件
	JLabel jbl1;
	
	//定义中部需要的组件
	//.中部有三个JPanel,有一个叫选项卡窗口管理
	JTabbedPane jtp;
	JPanel jp2,jp3,jp4;
	//静态文本框
	JLabel jp2_jbl1,jp2_jbl2,jp2_jbl3,jp2_jbl4,jp2_jLabelVerifyCode,jp2_imageVerifyCode;
	//清空选项
	JButton jp2_jb1;
	//文本框和密码框
	JTextField jp2_jtf;
	JTextField jp2_jTxtFieldVerifyCode;
	JPasswordField jp2_jpf;
	
	//记住密码，隐身登录
	JCheckBox jp2_jcb1,jp2_jcb2;
	
	//定义南部需要的组件
	//南面是流式布局
	JPanel jp1;
	JButton jp1_jb1,jp1_jb2,jp1_jb3;
	
	//验证码相关变量
	VerifyCode verifyCode;
	String strCurVerifyCode;
	QqClientConServer clientConServer=new QqClientConServer();
	
	public static void main(String[] args)
	{
		QqClientLogin qqClientLogin=new QqClientLogin();
	}
	
	public QqClientLogin()
	{
		verifyCode=new VerifyCode();
		//界面
		initGui();
	}

	/**
	 * 做界面
	 */
	private void initGui()
	{
		//处理北部
		//创建一个带有图像的label
		jbl1=new JLabel(new ImageIcon("image/tou_now.png"));
		
		//处理中部
		jp2=new JPanel(new GridLayout(4,3));
		//label全部居中对齐
		//第一行
		jp2_jbl1=new JLabel("QQ号码",JLabel.CENTER);
		jp2_jtf=new JTextField("1");
		jp2_jb1=new JButton(new ImageIcon("image/clear.gif"));
		//第二行
		jp2_jbl2=new JLabel("QQ密码",JLabel.CENTER);
		jp2_jpf=new JPasswordField("123456");
		jp2_jbl3=new JLabel("忘记密码",JLabel.CENTER);
		jp2_jbl3.setForeground(Color.blue);
		//第三行 验证码
		jp2_jLabelVerifyCode=new JLabel("验证码",JLabel.CENTER);
		jp2_jTxtFieldVerifyCode=new JTextField();
		jp2_imageVerifyCode=new JLabel(new ImageIcon((Image)(verifyCode.getImage())),
				JLabel.CENTER);
	    jp2_imageVerifyCode.addMouseListener(this);	
		strCurVerifyCode=verifyCode.getText();
		//第四行，几个checkbox
		jp2_jcb1=new JCheckBox("隐身登录");
		jp2_jcb2=new JCheckBox("记住密码");
		jp2_jbl4=new JLabel("申请密码保护",JLabel.CENTER);
		
		//把控件按照顺序（从上到下，从左至右）加入到jp2
		jp2.add(jp2_jbl1);
		jp2.add(jp2_jtf);
		jp2.add(jp2_jb1);
		jp2.add(jp2_jbl2);
		jp2.add(jp2_jpf);
		jp2.add(jp2_jbl3);
		//第三行，添加验证码
		jp2.add(jp2_jLabelVerifyCode);
		jp2.add(jp2_jTxtFieldVerifyCode);
		jp2.add(jp2_imageVerifyCode);
		jp2.add(jp2_jcb1);
		jp2.add(jp2_jcb2);
		jp2.add(jp2_jbl4);
		
		//创建选项卡窗口
		jtp=new JTabbedPane();
		jtp.add("QQ号码",jp2);
		jp3= new JPanel();
		jtp.add("手机号码",jp3);
		jp4=new JPanel();
		jtp.add("电子邮件",jp4);
		
		//处理南部
		jp1=new JPanel();
		jp1_jb1=new JButton(new ImageIcon("image/denglu.gif"));
		//响应用户点击登录
		jp1_jb1.addActionListener(this);
		jp1_jb2=new JButton(new ImageIcon("image/quxiao.gif"));
		//用户注册
		jp1_jb3=new JButton(new ImageIcon("image/xiangdao.gif"));
		jp1_jb3.addActionListener(this);
		//把三个按钮放入到jp1
		jp1.add(jp1_jb1);
		jp1.add(jp1_jb2);
		jp1.add(jp1_jb3);
		
		this.add(jbl1,"North");
		this.add(jtp,"Center");
		//..把jp1放在南部
		this.add(jp1,"South");
		this.setSize(480, 430);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) 
	{
		//如果用户点击登录
		if(arg0.getSource()==jp1_jb1)
		{
			User u=new User();
			u.setQqID(jp2_jtf.getText().trim());
			u.setPasswd(new String(jp2_jpf.getPassword()));
			u.setSysVerifyCode(strCurVerifyCode);
			u.setVerifyCodeInputed(jp2_jTxtFieldVerifyCode.getText().trim());
			u.setUserAction(UserAction.login);
			
			//用户信息正确以及验证码信息正确，登陆成功
			if(clientConServer.sendLoginInfoToServer(u))
			{   
				//关闭掉登录界面
				this.dispose();
				UDPCommunicationClient frame1 = new UDPCommunicationClient(""); // 创建一个实例对象
				frame1.setVisible(true);
			}
			//用户名和密码不正确
			else
			{
				JOptionPane.showMessageDialog(this,"用户名密码错误");
			}
		}
		//注册
		if(arg0.getSource()==jp1_jb3)
		{
			this.dispose();
			RegisteClient client=new RegisteClient(this.clientConServer);
			client.setVisible(true);
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(e.getSource().equals(jp2_imageVerifyCode))
		{
			jp2_imageVerifyCode.setIcon(new ImageIcon((Image)verifyCode.getImage()));
			strCurVerifyCode=verifyCode.getText();
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		
	}

	public void mouseExited(MouseEvent e)
	{
		
	}

	public void mousePressed(MouseEvent e)
	{
		
	}

	public void mouseReleased(MouseEvent e)
	{
		
	}

}
