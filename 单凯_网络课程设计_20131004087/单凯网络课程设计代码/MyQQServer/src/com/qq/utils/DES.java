package com.qq.utils;

import java.security.SecureRandom;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

public class DES
{
	public DES()
	{
	}
	
	//����
	public static void main(String args[])
	{
		//����
		String plainText = "ggg";
		//���룬����Ҫ��8�ı���
		String sharedKey = "12345678";
		byte[] plainTextBytes = plainText.getBytes();
		//����Des����
		byte[] cipherTextBytes = DES.encrypt(plainTextBytes, sharedKey);
		
		int length = cipherTextBytes.length + 4;
		byte[] databyteEND = new byte[length];
		for(int i = 0; i < cipherTextBytes.length; i++)
		{
			databyteEND[i] = cipherTextBytes[i];
		}
		databyteEND[length - 4] = 2;
		databyteEND[length - 3] = 7;  // ESC: 27
		databyteEND[length - 2] = 0;
		databyteEND[length - 1] = 3;  // EOT: 03
		
		System.out.println("���ǰ��");
		for(int i = 0; i < cipherTextBytes.length; i++) System.out.print(cipherTextBytes[i]);
		System.out.println();
		
		System.out.println("���֮��");
		for(int i = 0; i < databyteEND.length; i++) System.out.print(databyteEND[i]);
		System.out.println();
		
		//ֱ�ӽ��������ݽ���
		try
		{
			byte[] decryResult = DES.decrypt(cipherTextBytes, sharedKey);
			System.out.println("���ܺ�\n" + new String(decryResult));
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

	}

	/**
	* ����
	* @param datasource byte[]  ���ĵ�byte����
	* @param sharedKey String   ��Կ
	* @return byte[]            ���ĵ�byte����
	*/
	public static byte[] encrypt(byte[] datasource, String sharedKey)
	{
		try
		{
			//�����ṩǿ��������������� (RNG)��
			SecureRandom random = new SecureRandom();
			//DESKeySpecָ��һ�� DES ��Կ��
			//����һ�� DESKeySpec ����ʹ�� key �е�ǰ 8 ���ֽ���Ϊ DES ��Կ����Կ���ݡ�
			DESKeySpec desKey = new DESKeySpec(sharedKey.getBytes());
			//����һ���ܳ׹�����Ȼ��������DESKeySpecת����
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			//Cipher����ʵ����ɼ��ܲ���
			Cipher cipher = Cipher.getInstance("DES");
			//���ܳ׳�ʼ��Cipher����
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			//���ڣ���ȡ���ݲ�����,��ʽִ�м��ܲ���
			return cipher.doFinal(datasource);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* ����
	* @param src byte[]  		���ĵ�byte����
	* @param sharedKey String   ������Կ
	* @return byte[]			���ĵ�byte����
	*/
	public static byte[] decrypt(byte[] cipherTextBytes, String sharedKey) throws Exception
	{
		// DES�㷨Ҫ����һ�������ε������Դ
		SecureRandom random = new SecureRandom();
		// ����һ��DESKeySpec����
		DESKeySpec desKey = new DESKeySpec(sharedKey.getBytes());
		// ����һ���ܳ׹���
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// ��DESKeySpec����ת����SecretKey����
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher����ʵ����ɽ��ܲ���
		Cipher cipher = Cipher.getInstance("DES");
		// ���ܳ׳�ʼ��Cipher����
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// ������ʼ���ܲ���
		return cipher.doFinal(cipherTextBytes);
	}
}
