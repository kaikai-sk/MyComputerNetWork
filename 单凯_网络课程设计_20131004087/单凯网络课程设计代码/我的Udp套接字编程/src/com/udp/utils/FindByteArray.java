package com.udp.DH.utils;

public class FindByteArray
{

	/*
	 * �� �ֽ�����data��Ѱ���ֽ�����target ����ҵ������ص�һ�γ��ֵ�index ���û���ҵ�������-1
	 */
	public static int findByteArray(byte[] data, byte[] target)
	{
		byte[] tmp = new byte[4];
		for (int i = 0; i < data.length; i++)
		{
			if (i + 3 < data.length)
			{
				tmp[0] = data[i];
				tmp[1] = data[i + 1];
				tmp[2] = data[i + 2];
				tmp[3] = data[i + 3];
			} 
			else
			{
				break; // ��ʱ�϶��Ҳ�����
			}

			if (compare(tmp, target))
			{
				return i; // �ҵ��ˣ����ص�һ�γ��ֵ������±�
			}

		}
		return -1;
	}

	/*
	 * ֱ������array1��array2��Ƚ� ���һģһ��������true ����в�ͬ������false
	 */
	public static boolean compare(byte[] array1, byte[] array2)
	{
		if (array1.length != array2.length) // ���ȶ���һ��������false
			return false;

		for (int i = 0; i < array1.length; i++)
		{
			if (array1[i] != array2[i])
				return false;
		}
		return true;
	}

	public static void main(String[] args)
	{
		byte[] target = new byte[4];
		target[0] = 2;
		target[1] = 7;
		target[2] = 0;
		target[3] = 3;

		byte[] databyte = new byte[10];
		databyte[0] = 1;
		databyte[1] = 2;
		databyte[2] = 3;
		databyte[3] = 4;
		databyte[4] = 2;
		databyte[5] = 7;
		databyte[6] = 0;
		databyte[7] = 3;
		databyte[8] = 3;
		databyte[9] = 3;

		System.out.println(findByteArray(databyte, target));
	}

}
