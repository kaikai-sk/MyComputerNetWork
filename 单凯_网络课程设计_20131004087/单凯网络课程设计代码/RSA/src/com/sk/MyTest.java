package com.sk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class MyTest
{
	private static String publicKey;
	private static String privateKey;

	public static void main(String[] args) throws Exception
	{
		Map<String, Object> keyMap = RSACoder.initKey();
		publicKey = RSACoder.getPublicKey(keyMap);
		privateKey = RSACoder.getPrivateKey(keyMap);
		// System.err.println("��Կ: \n\r" + publicKey);
		// System.err.println("˽Կ�� \n\r" + privateKey);

		// System.err.println("��Կ���ܡ���˽Կ����");
		// String inputStr = "abc";
		// byte[] data = inputStr.getBytes();
		// byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);
		// byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData,
		// privateKey);
		// String outputStr = new String(decodedData);
		// System.err.println("����ǰ: " + inputStr + "\n\r" + "���ܺ�: " +
		// outputStr);
		// System.err.println("���ܺ��byte���飺"+new String(encodedData));
		// System.err.println("���ܺ��byte���飺"+new String(decodedData));

		System.err.println("˽Կ���ܡ�����Կ����");
		String inputStr = "sign";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);

		byte[] decodedData = RSACoder.decryptByPublicKey(encodedData, publicKey);

		String outputStr = new String(decodedData);
		System.err.println("����ǰ: " + inputStr + "\n\r" + "���ܺ�: " + outputStr);
	
		System.err.println("˽Կǩ��������Կ��֤ǩ��");
		// ����ǩ��
		String sign = RSACoder.sign(encodedData, privateKey);
		System.err.println("ǩ��:\r\n" + sign);

		// ��֤ǩ��
		boolean status = RSACoder.verify(encodedData, publicKey, sign);
		System.err.println("״̬:\r\n" + status);
	}
}
