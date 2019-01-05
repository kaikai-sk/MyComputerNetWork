package com.qq.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.PasswordField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.oracle.jrockit.jfr.UseConstantPool;
import com.qq.common.User;
import com.qq.common.UserAction;
import com.qq.model.QqClientConServer;

import com.qq.utils.MD5;

public class RegisteClient extends JFrame implements ActionListener
{
	//北边标题
	JLabel labelTiteJLabel=new JLabel("用户注册",JLabel.CENTER);
	//中间网格布局
	JPanel jpCenterJPanel=new JPanel(new GridLayout(3,2));
	JLabel jLabel1=new JLabel("用户昵称",JLabel.CENTER),
			jLabel2=new JLabel("密码：",JLabel.CENTER),
			jLabel3=new JLabel("确认密码",JLabel.CENTER);
	JTextField jTNickName=new JTextField(20);
	JPasswordField passwordField=new JPasswordField(20);
	JPasswordField ackPasswordField=new JPasswordField(20);
	//南边三个button
	JPanel jpSouth=new JPanel();
	JButton jbOK=new JButton("确定"),jbCancel=new JButton("取消");
	JButton jbClearButton=new JButton("清空");
	
	QqClientConServer clientConServer;
	
	public RegisteClient(QqClientConServer clientConServer)
	{
		this.clientConServer=clientConServer;
		//初始化界面
		initGui();
	}

	public static void main(String[] args)
	{
		RegisteClient client=new RegisteClient(new QqClientConServer());
	}
	
	//初始化界面
	private void initGui()
	{
		//中部地区
		jpCenterJPanel.add(jLabel1);
		jpCenterJPanel.add(jTNickName);
		jpCenterJPanel.add(jLabel2);
		jpCenterJPanel.add(passwordField);
		jpCenterJPanel.add(jLabel3);
		jpCenterJPanel.add(ackPasswordField);
		
		//南部地区
		jpSouth.add(jbOK);
		jbOK.addActionListener(this);
		jpSouth.add(jbCancel);
		jpSouth.add(jbClearButton);
		
		this.add(labelTiteJLabel, BorderLayout.NORTH);
		this.add(jpCenterJPanel,BorderLayout.CENTER);
		this.add(jpSouth, BorderLayout.SOUTH);
		
		this.setSize(400, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(jbOK))
		{
			String nickNameString=jTNickName.getText().trim();
			String passWd=passwordField.getText().trim();
			String ackPassWd=ackPasswordField.getText().trim();
			if(nickNameString==null || passWd==null 
					|| ackPassWd==null || "".equals(ackPassWd) || 
					"".equals(passWd) || "".equals(nickNameString))
			{
				JOptionPane.showMessageDialog(this.getContentPane(),
						"所填写的信息不能为空", "系统信息", JOptionPane.ERROR_MESSAGE);
			}
			if(passWd.equals(ackPassWd))
			{
				User user=new User(nickNameString, "", new MD5().getMD5ofStr(ackPassWd));
				user.setUserAction(UserAction.registe);
				int qqId=this.clientConServer.sendRegisteInfoToServer(user);
				if(qqId>0)
				{   
					//关闭掉注册界面
					this.dispose();
					// 创建一个实例对象
					UDPCommunicationClient frame1 = new UDPCommunicationClient(new Integer(qqId).toString()); 
					frame1.setVisible(true);
				}
			}
		}
	}
	
}
