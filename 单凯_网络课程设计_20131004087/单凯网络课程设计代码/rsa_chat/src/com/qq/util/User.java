/**
 * 这是用户信息类
 */
package com.qq.util;

import javax.jws.soap.SOAPBinding.Use;

public class User implements java.io.Serializable
{
	public User(String ip,String nickName)
	{
		this.IP=ip;
		this.nickName=nickName;
	}
	
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
	public String getIP()
	{
		return IP;
	}
	public void setIP(String iP)
	{
		IP = iP;
	}
	
	//昵称
	private String nickName;
	//QQ号
	private String IP;

}
