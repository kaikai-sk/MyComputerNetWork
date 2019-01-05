package com.sk.image;
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  
  
public class ImageRequest 
{  
	static String imageURL="http://www.cug.edu.cn/new/image/logonew.png";
	
    public static void main(String[] args) throws IOException 
    {  
        URL url = new URL(imageURL);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        //通过输入流获得图片数据  
        InputStream inputStream = conn.getInputStream();   
        //获得图片的二进制数据  
        byte[] getData = readInputStream(inputStream);     
          
        File imageFile = new File("sk.jpg");    
        FileOutputStream fos = new FileOutputStream(imageFile);     
        fos.write(getData);  
        fos.close();  
          
        System.out.println(" read picture success");  
    }  
  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException 
    {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) 
        {  
            bos.write(buffer, 0, len);  
        }    
        bos.close();  
        return bos.toByteArray();  
    }  
}  