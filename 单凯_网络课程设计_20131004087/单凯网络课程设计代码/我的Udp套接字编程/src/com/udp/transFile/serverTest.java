package com.udp.transFile;

public class serverTest
{
	public static void main(String[] args)
	{
		// �ļ�����������������
		new Server(Constants.SENDIP, Constants.RECEIVEPORT, Constants.SENDPORT,
				Constants.RECEIVEPATH).start();
	}
}
