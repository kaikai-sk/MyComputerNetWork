package com.qq.common;

public class Message implements java.io.Serializable
{
	//消息类型
	private String mesType;
	//发送方
	private User sender;
	//接收方
	private User getter;
	//发送时间
	private String sendTime;
	//消息内容
	private String info;
	
	public String getMesType()
	{
		return mesType;
	}
	public void setMesType(String mesType)
	{
		this.mesType = mesType;
	}
	public User getSender()
	{
		return sender;
	}
	public void setSender(User sender)
	{
		this.sender = sender;
	}
	public User getGetter()
	{
		return getter;
	}
	public void setGetter(User getter)
	{
		this.getter = getter;
	}
	public String getSendTime()
	{
		return sendTime;
	}
	public void setSendTime(String sendTime)
	{
		this.sendTime = sendTime;
	}
	public String getInfo()
	{
		return info;
	}
	public void setInfo(String info)
	{
		this.info = info;
	}
	
	
}
