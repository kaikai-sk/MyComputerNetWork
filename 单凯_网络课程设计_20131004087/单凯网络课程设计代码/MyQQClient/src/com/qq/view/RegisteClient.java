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
	//���߱���
	JLabel labelTiteJLabel=new JLabel("�û�ע��",JLabel.CENTER);
	//�м����񲼾�
	JPanel jpCenterJPanel=new JPanel(new GridLayout(3,2));
	JLabel jLabel1=new JLabel("�û��ǳ�",JLabel.CENTER),
			jLabel2=new JLabel("���룺",JLabel.CENTER),
			jLabel3=new JLabel("ȷ������",JLabel.CENTER);
	JTextField jTNickName=new JTextField(20);
	JPasswordField passwordField=new JPasswordField(20);
	JPasswordField ackPasswordField=new JPasswordField(20);
	//�ϱ�����button
	JPanel jpSouth=new JPanel();
	JButton jbOK=new JButton("ȷ��"),jbCancel=new JButton("ȡ��");
	JButton jbClearButton=new JButton("���");
	
	QqClientConServer clientConServer;
	
	public RegisteClient(QqClientConServer clientConServer)
	{
		this.clientConServer=clientConServer;
		//��ʼ������
		initGui();
	}

	public static void main(String[] args)
	{
		RegisteClient client=new RegisteClient(new QqClientConServer());
	}
	
	//��ʼ������
	private void initGui()
	{
		//�в�����
		jpCenterJPanel.add(jLabel1);
		jpCenterJPanel.add(jTNickName);
		jpCenterJPanel.add(jLabel2);
		jpCenterJPanel.add(passwordField);
		jpCenterJPanel.add(jLabel3);
		jpCenterJPanel.add(ackPasswordField);
		
		//�ϲ�����
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
						"����д����Ϣ����Ϊ��", "ϵͳ��Ϣ", JOptionPane.ERROR_MESSAGE);
			}
			if(passWd.equals(ackPassWd))
			{
				User user=new User(nickNameString, "", new MD5().getMD5ofStr(ackPassWd));
				user.setUserAction(UserAction.registe);
				int qqId=this.clientConServer.sendRegisteInfoToServer(user);
				if(qqId>0)
				{   
					//�رյ�ע�����
					this.dispose();
					// ����һ��ʵ������
					UDPCommunicationClient frame1 = new UDPCommunicationClient(new Integer(qqId).toString()); 
					frame1.setVisible(true);
				}
			}
		}
	}
	
}
