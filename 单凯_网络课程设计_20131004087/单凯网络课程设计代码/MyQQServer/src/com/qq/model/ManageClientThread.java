package com.qq.model;

import java.util.*;

/**
 * ������������յ������е��߳�
 * @author ����
 *
 */
public class ManageClientThread
{

	public static HashMap hm=new HashMap();
	
	//��hm�����һ���ͻ���ͨѶ�߳�
	public static  void addClientThread(String uid,SerConClientThread ct)
	{
		hm.put(uid, ct);
	}
	
	/**
	 * �õ��û�id����Ӧ���߳�
	 * @param uid �û�id
	 * @return ����id����Ӧ���߳�
	 */
	public static SerConClientThread getClientThread(String uid)
	{
		return (SerConClientThread)hm.get(uid);
	}
	
	//���ص�ǰ���ߵ��˵����
	public static String getAllOnLineUserid()
	{
		//ʹ�õ��������
		Iterator it=hm.keySet().iterator();
		String res="";
		while(it.hasNext())
		{
			res+=it.next().toString()+" ";
		}
		return res;
	}
}
