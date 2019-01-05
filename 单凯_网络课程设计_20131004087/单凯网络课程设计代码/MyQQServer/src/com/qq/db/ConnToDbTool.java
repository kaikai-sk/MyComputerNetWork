package com.qq.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ConnToDbTool
{
	static DBHelper db1 = null;
	static ResultSet ret = null;

	/**
	 * 根据QQ号检查用户的密码
	 * @param qqId
	 * @return
	 */
	public static boolean checkUser(String qqId,String passWd)
	{
		boolean isOK=false;
		String sqlString=MessageFormat.format("select passwd from user where qq_id={0}", new Integer[]{new Integer(qqId)});
		db1=new DBHelper(sqlString);
		String tempPwdString="";
		try
		{
			ret=db1.pst.executeQuery();
			while(ret.next())
			{
				tempPwdString=ret.getString(1);
			}
			if(passWd.equals(tempPwdString))
			{
				return true;
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 向用户表中添加用户
	 * @param nickName   用户昵称
	 * @param passWd	 用户密码
	 * @return 用户的QQ号
	 */
	public static int addUserToDB(String nickName, String passWd)
	{
		String sql2=MessageFormat.format("insert into user(nick_name,passwd) value({0},{1})",new String[]{"'"+nickName+"'","'"+passWd+"'"});
		try
		{
			db1 = new DBHelper(sql2);// 创建DBHelper对象
			db1.pst.execute(sql2);
			return getMaxId();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 得到最大的qq号
	 * @return
	 */
	public static int getMaxId()
	{
		String sqlString="select max(qq_id) from user";
		db1=new DBHelper(sqlString);
		try
		{
			ret=db1.pst.executeQuery();
			String maxId="";
			while(ret.next())
			{
				maxId=ret.getString(1);
			}
			return Integer.parseInt(maxId);
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 打印User表中的内容
	 */
	public static void printUserTable()
	{
		String sql = "select *from user";// SQL语句
		db1 = new DBHelper(sql);// 创建DBHelper对象

		try
		{
			ret = db1.pst.executeQuery();// 执行语句，得到结果集
			// 显示数据
			while (ret.next())
			{
				String uid = ret.getString(1);
				String uname = ret.getString(2);
				String upasswd = ret.getString(3);
				System.out.println(uid + "\t" + uname + "\t" + upasswd+"\r\n");
			}
			ret.close();
			db1.close();// 关闭连接
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println(addUserToDB("sk", "sk"));
		printUserTable();
	}
}
