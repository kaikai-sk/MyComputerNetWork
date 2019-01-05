package com.qq.model;

import com.qq.view.UDPCommunicationServer;

public class UdpCommunicationThread implements Runnable
{
	private String oppositeIp;
	private int oppositePort;
	private boolean isExit=false;
	
	public UdpCommunicationThread(String oppositeIp,int oppositePort)
	{
		this.oppositeIp=oppositeIp;
		this.oppositePort=oppositePort;
		this.run();
	}

	public void run()
	{
		UDPCommunicationServer server=new UDPCommunicationServer();
		server.setVisible(true);
	}
}
