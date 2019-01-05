package com.udp.transFile;

public class ClientTest
{

	public static void main(String[] args)
	{
		// 文件发送者启动并传参
		new Client(Constants.RECEIVEPORT, Constants.SENDPORT,
				Constants.SENDPATH, Constants.RECEIVEIP).start();
	}
}
