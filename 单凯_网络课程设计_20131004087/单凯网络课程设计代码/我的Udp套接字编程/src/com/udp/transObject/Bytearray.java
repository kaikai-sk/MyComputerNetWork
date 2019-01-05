package com.udp.transObject;
import java.io.Serializable;

public class Bytearray implements Serializable
{
	private String msg;

	public String getMsg()
	{
		return msg;
	}

	public String toString()
	{
		return "Bytearray [msg=" + msg + "]";
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

}
