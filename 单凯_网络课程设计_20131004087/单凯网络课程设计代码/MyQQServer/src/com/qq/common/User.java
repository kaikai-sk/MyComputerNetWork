/**
 * �����û���Ϣ��
 */
package com.qq.common;

import javax.jws.soap.SOAPBinding.Use;


public class User implements java.io.Serializable
{
	//�ǳ�
	private String nickName;
	//QQ��
	private String qqID;
	//����
	private String passwd;
	//��֤��
	private String sysVerifyCode;
	private String verifyCodeInputed;
	//�ͻ��˵Ľ��ն˿�
	private int clientRcvPort=8001;
	
	public int getClientRcvPort()
	{
		return clientRcvPort;
	}

	public void setClientRcvPort(int clientRcvPort)
	{
		this.clientRcvPort = clientRcvPort;
	}

	public int getUserAction()
	{
		return userAction;
	}

	public void setUserAction(int userAction)
	{
		this.userAction = userAction;
	}

	private int userAction=UserAction.login;
	
	public User()
	{
		// TODO Auto-generated constructor stub
	}
	
	public User(String nickName)
	{
		this.nickName=nickName;
	}
	
	public  User(String qqId,String passWd)
	{
		this.qqID=qqId;
		this.passwd=passWd;
	}
	
	public  User(String nickName,String qqId,String passWd)
	{
		this.nickName=nickName;
		this.qqID=qqId;
		this.passwd=passWd;
	}
	
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
	public String getQqID()
	{
		return qqID;
	}
	public void setQqID(String qqID)
	{
		this.qqID = qqID;
	}
	public String getPasswd()
	{
		return passwd;
	}
	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}

	public String getSysVerifyCode()
	{
		return sysVerifyCode;
	}

	public void setSysVerifyCode(String sysVerifyCode)
	{
		this.sysVerifyCode = sysVerifyCode;
	}

	public String getVerifyCodeInputed()
	{
		return verifyCodeInputed;
	}

	public void setVerifyCodeInputed(String verifyCodeInputed)
	{
		this.verifyCodeInputed = verifyCodeInputed;
	}
}
