package com.qq.common;

public class Message implements java.io.Serializable
{
	//��Ϣ����
	private String mesType;
	//���ͷ�
	private User sender;
	//���շ�
	private User getter;
	//����ʱ��
	private String sendTime;
	//��Ϣ����
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
