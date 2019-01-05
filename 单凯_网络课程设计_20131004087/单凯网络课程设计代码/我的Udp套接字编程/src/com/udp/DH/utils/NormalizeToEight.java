package com.udp.DH.utils;

import java.math.BigInteger;

public class NormalizeToEight
{	
	/*
	 * ��һ������8λ��sharedKey�����Ϊһ��8λ���ַ���
	 */
	public static String normalize(String sharedKey)
	{
		StringBuffer normalizedKey = new StringBuffer();
		try
		{
			int sharedKeyInt = Integer.parseInt(sharedKey);
			BigInteger billion = new BigInteger("1000000000");
			BigInteger tmp = new BigInteger(sharedKey);
			while (tmp.compareTo(billion) == -1)  // �� tmp < billion
			{
				tmp = tmp.pow(sharedKeyInt);
			} 
			String tmpStr = tmp.toString();
			normalizedKey.append(tmpStr.substring(0, 4));
			normalizedKey.append(tmpStr.substring(tmpStr.length()-4, tmpStr.length()));
		
			return normalizedKey.toString();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		String string = "2";
		System.out.println(normalize(string));
	}
}
