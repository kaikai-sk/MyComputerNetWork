package com.qq.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyUdpCommunicationTool
{
	/**
	 * 发送UDP数据报
	 * @param sendSocket
	 * @param transInfo     String类型的文本
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
	 * 发送UDP数据报
	 * @param sendSocket
	 * @param transInfo		byte[]类型的文本
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
