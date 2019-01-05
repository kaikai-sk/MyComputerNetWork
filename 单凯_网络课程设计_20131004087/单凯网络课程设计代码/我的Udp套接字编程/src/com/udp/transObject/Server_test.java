package com.udp.transObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class Server_test
{

	public static void main(String[] args) throws ClassNotFoundException
	{
		try
		{
			DatagramSocket da = new DatagramSocket(9099);
			byte[] by = new byte[1024 * 1024];

			DatagramPacket data = new DatagramPacket(by, by.length);
			da.receive(data);
			ByteArrayInputStream bs = new ByteArrayInputStream(data.getData());
			ObjectInputStream os = new ObjectInputStream(bs);
			Bytearray m = (Bytearray) os.readObject();
			System.out.println(m.getMsg());

		} catch (SocketException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}