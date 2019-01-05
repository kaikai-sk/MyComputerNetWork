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
	
	//测试
	public static void main(String args[])
	{
		//明文
		String plainText = "ggg";
		//密码，长度要是8的倍数
		String sharedKey = "12345678";
		byte[] plainTextBytes = plainText.getBytes();
		//进行Des加密
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
		
		System.out.println("填充前：");
		for(int i = 0; i < cipherTextBytes.length; i++) System.out.print(cipherTextBytes[i]);
		System.out.println();
		
		System.out.println("填充之后：");
		for(int i = 0; i < databyteEND.length; i++) System.out.print(databyteEND[i]);
		System.out.println();
		
		//直接将如上内容解密
		try
		{
			byte[] decryResult = DES.decrypt(cipherTextBytes, sharedKey);
			System.out.println("解密后：\n" + new String(decryResult));
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

	}

	/**
	* 加密
	* @param datasource byte[]  明文的byte数组
	* @param sharedKey String   密钥
	* @return byte[]            密文的byte数组
	*/
	public static byte[] encrypt(byte[] datasource, String sharedKey)
	{
		try
		{
			//此类提供强加密随机数生成器 (RNG)。
			SecureRandom random = new SecureRandom();
			//DESKeySpec指定一个 DES 密钥。
			//创建一个 DESKeySpec 对象，使用 key 中的前 8 个字节作为 DES 密钥的密钥内容。
			DESKeySpec desKey = new DESKeySpec(sharedKey.getBytes());
			//创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			//Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			//用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			//现在，获取数据并加密,正式执行加密操作
			return cipher.doFinal(datasource);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* 解密
	* @param src byte[]  		密文的byte数组
	* @param sharedKey String   共享密钥
	* @return byte[]			明文的byte数组
	*/
	public static byte[] decrypt(byte[] cipherTextBytes, String sharedKey) throws Exception
	{
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(sharedKey.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(cipherTextBytes);
	}
}
