/**
 * 这是用户信息类
 */
package com.qq.common;

import javax.jws.soap.SOAPBinding.Use;


public class User implements java.io.Serializable
{
	//昵称
	private String nickName;
	//QQ号
	private String qqID;
	//密码
	private String passwd;
	//验证码
	private String sysVerifyCode;
	private String verifyCodeInputed;
	//客户端的接收端口
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
