package com.sk.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;  

public class MyDownImage
{
	static String imageURL="http://www.cug.edu.cn/new/image/logonew.png";
	
	public static void main(String[] args)
	{
		try
		{
			URL url=new URL(imageURL);
			HttpURLConnection httpsURLConnection=(HttpURLConnection)url.openConnection();
			InputStream inputStream=httpsURLConnection.getInputStream();
			byte[] imageData=readInputStream(inputStream);
			
			File imageFile=new File("sk100.jpg");
			FileOutputStream fosFileOutputStream=new FileOutputStream(imageFile);
			fosFileOutputStream.write(imageData);
			fosFileOutputStream.close();
			System.err.println("download images success");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private static byte[] readInputStream(InputStream inputStream) throws IOException
	{
		byte[] buffer=new byte[1024];
		int len=0;
		//ByteArrayOutputStream:可以捕获内存缓冲区的数据，转换成字节数组。
		ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
		while((len=inputStream.read(buffer))!=-1)
		{
			arrayOutputStream.write(buffer, 0, len);
		}
		arrayOutputStream.close();
		return arrayOutputStream.toByteArray();
	}
}
