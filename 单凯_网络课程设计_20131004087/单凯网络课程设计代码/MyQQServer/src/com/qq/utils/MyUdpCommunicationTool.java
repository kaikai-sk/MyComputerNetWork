package com.qq.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyUdpCommunicationTool
{
	/**
	 * ����UDP���ݱ�
	 * @param sendSocket
	 * @param transInfo     String���͵��ı�
	 * @param dstIP
	 * @param dstPort
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void sendUdpDatagram(DatagramSocket sendSocket,String transInfo,String dstIP,int dstPort) throws UnknownHostException, IOException
	{
		byte[] databyte = transInfo.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(databyte,
					databyte.length,
					java.net.InetAddress.getByName(dstIP), dstPort);
		sendSocket.send(sendPacket);
	}
	
	/**
	 * ����UDP���ݱ�
	 * @param sendSocket
	 * @param transInfo		byte[]���͵��ı�
	 * @param dstIP
	 * @param dstPort
	 * @throws Exception
	 */
	public static void sendUdpDatagram(DatagramSocket sendSocket,byte[] transInfo,String dstIP,int dstPort)
		throws Exception
	{
		DatagramPacket sendPacket = new DatagramPacket(transInfo,
				transInfo.length,
				java.net.InetAddress.getByName(dstIP), dstPort);
		sendSocket.send(sendPacket);
	}
}
