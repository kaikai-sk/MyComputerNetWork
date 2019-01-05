package com.udp.transFile;

public class serverTest
{
	public static void main(String[] args)
	{
		// 文件接收者启动并传参
		new Server(Constants.SENDIP, Constants.RECEIVEPORT, Constants.SENDPORT,
				Constants.RECEIVEPATH).start();
	}
}
