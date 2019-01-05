package com.qq.model;

import java.util.*;

/**
 * 管理服务器接收到的所有的线程
 * @author 单凯
 *
 */
public class ManageClientThread
{

	public static HashMap hm=new HashMap();
	
	//向hm中添加一个客户端通讯线程
	public static  void addClientThread(String uid,SerConClientThread ct)
	{
		hm.put(uid, ct);
	}
	
	/**
	 * 得到用户id所对应的线程
	 * @param uid 用户id
	 * @return 返回id所对应的线程
	 */
	public static SerConClientThread getClientThread(String uid)
	{
		return (SerConClientThread)hm.get(uid);
	}
	
	//返回当前在线的人的情况
	public static String getAllOnLineUserid()
	{
		//使用迭代器完成
		Iterator it=hm.keySet().iterator();
		String res="";
		while(it.hasNext())
		{
			res+=it.next().toString()+" ";
		}
		return res;
	}
}
