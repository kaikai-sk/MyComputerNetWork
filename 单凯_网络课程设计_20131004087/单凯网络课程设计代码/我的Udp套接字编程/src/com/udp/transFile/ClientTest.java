package com.udp.transFile;

public class ClientTest
{

	public static void main(String[] args)
	{
		// �ļ�����������������
		new Client(Constants.RECEIVEPORT, Constants.SENDPORT,
				Constants.SENDPATH, Constants.RECEIVEIP).start();
	}
}
