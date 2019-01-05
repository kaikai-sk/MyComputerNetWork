package com.udp.transObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class Client_test
{
	public static void main(String[] args)
	{
		try
		{
			DatagramSocket da = new DatagramSocket();
			Bytearray b = new Bytearray();
			b.setMsg("ÄãºÃ°¡£¡");
			byte[] by = new byte[1024 * 1024];
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream bo = new ObjectOutputStream(bs);
			bo.writeObject(b);
			by = bs.toByteArray();
			DatagramPacket data = new DatagramPacket(by, by.length,
					new InetSocketAddress("localhost", 9099));
			da.send(data);
		} 
		catch (SocketException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
