/**
 * ����:qq�ͻ��˵�¼����
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
 * ������һ���߽磨�����ϱ��У�����
 * �м������񲼾�
 * �����Ǹ�jlabel
 * @author ����
 *
 */
public class QqClientLogin extends JFrame implements ActionListener,MouseListener
{

	//���山����Ҫ�����
	JLabel jbl1;
	
	//�����в���Ҫ�����
	//.�в�������JPanel,��һ����ѡ����ڹ���
	JTabbedPane jtp;
	JPanel jp2,jp3,jp4;
	//��̬�ı���
	JLabel jp2_jbl1,jp2_jbl2,jp2_jbl3,jp2_jbl4,jp2_jLabelVerifyCode,jp2_imageVerifyCode;
	//���ѡ��
	JButton jp2_jb1;
	//�ı���������
	JTextField jp2_jtf;
	JTextField jp2_jTxtFieldVerifyCode;
	JPasswordField jp2_jpf;
	
	//��ס���룬�����¼
	JCheckBox jp2_jcb1,jp2_jcb2;
	
	//�����ϲ���Ҫ�����
	//��������ʽ����
	JPanel jp1;
	JButton jp1_jb1,jp1_jb2,jp1_jb3;
	
	//��֤����ر���
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
		//����
		initGui();
	}

	/**
	 * ������
	 */
	private void initGui()
	{
		//������
		//����һ������ͼ���label
		jbl1=new JLabel(new ImageIcon("image/tou_now.png"));
		
		//�����в�
		jp2=new JPanel(new GridLayout(4,3));
		//labelȫ�����ж���
		//��һ��
		jp2_jbl1=new JLabel("QQ����",JLabel.CENTER);
		jp2_jtf=new JTextField("1");
		jp2_jb1=new JButton(new ImageIcon("image/clear.gif"));
		//�ڶ���
		jp2_jbl2=new JLabel("QQ����",JLabel.CENTER);
		jp2_jpf=new JPasswordField("123456");
		jp2_jbl3=new JLabel("��������",JLabel.CENTER);
		jp2_jbl3.setForeground(Color.blue);
		//������ ��֤��
		jp2_jLabelVerifyCode=new JLabel("��֤��",JLabel.CENTER);
		jp2_jTxtFieldVerifyCode=new JTextField();
		jp2_imageVerifyCode=new JLabel(new ImageIcon((Image)(verifyCode.getImage())),
				JLabel.CENTER);
	    jp2_imageVerifyCode.addMouseListener(this);	
		strCurVerifyCode=verifyCode.getText();
		//�����У�����checkbox
		jp2_jcb1=new JCheckBox("�����¼");
		jp2_jcb2=new JCheckBox("��ס����");
		jp2_jbl4=new JLabel("�������뱣��",JLabel.CENTER);
		
		//�ѿؼ�����˳�򣨴��ϵ��£��������ң����뵽jp2
		jp2.add(jp2_jbl1);
		jp2.add(jp2_jtf);
		jp2.add(jp2_jb1);
		jp2.add(jp2_jbl2);
		jp2.add(jp2_jpf);
		jp2.add(jp2_jbl3);
		//�����У������֤��
		jp2.add(jp2_jLabelVerifyCode);
		jp2.add(jp2_jTxtFieldVerifyCode);
		jp2.add(jp2_imageVerifyCode);
		jp2.add(jp2_jcb1);
		jp2.add(jp2_jcb2);
		jp2.add(jp2_jbl4);
		
		//����ѡ�����
		jtp=new JTabbedPane();
		jtp.add("QQ����",jp2);
		jp3= new JPanel();
		jtp.add("�ֻ�����",jp3);
		jp4=new JPanel();
		jtp.add("�����ʼ�",jp4);
		
		//�����ϲ�
		jp1=new JPanel();
		jp1_jb1=new JButton(new ImageIcon("image/denglu.gif"));
		//��Ӧ�û������¼
		jp1_jb1.addActionListener(this);
		jp1_jb2=new JButton(new ImageIcon("image/quxiao.gif"));
		//�û�ע��
		jp1_jb3=new JButton(new ImageIcon("image/xiangdao.gif"));
		jp1_jb3.addActionListener(this);
		//��������ť���뵽jp1
		jp1.add(jp1_jb1);
		jp1.add(jp1_jb2);
		jp1.add(jp1_jb3);
		
		this.add(jbl1,"North");
		this.add(jtp,"Center");
		//..��jp1�����ϲ�
		this.add(jp1,"South");
		this.setSize(480, 430);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) 
	{
		//����û������¼
		if(arg0.getSource()==jp1_jb1)
		{
			User u=new User();
			u.setQqID(jp2_jtf.getText().trim());
			u.setPasswd(new String(jp2_jpf.getPassword()));
			u.setSysVerifyCode(strCurVerifyCode);
			u.setVerifyCodeInputed(jp2_jTxtFieldVerifyCode.getText().trim());
			u.setUserAction(UserAction.login);
			
			//�û���Ϣ��ȷ�Լ���֤����Ϣ��ȷ����½�ɹ�
			if(clientConServer.sendLoginInfoToServer(u))
			{   
				//�رյ���¼����
				this.dispose();
				UDPCommunicationClient frame1 = new UDPCommunicationClient(""); // ����һ��ʵ������
				frame1.setVisible(true);
			}
			//�û��������벻��ȷ
			else
			{
				JOptionPane.showMessageDialog(this,"�û����������");
			}
		}
		//ע��
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
