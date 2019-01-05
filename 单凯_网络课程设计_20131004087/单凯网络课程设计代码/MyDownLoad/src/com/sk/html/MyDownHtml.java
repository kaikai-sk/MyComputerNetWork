package com.sk.html;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MyDownHtml
{
	private static String rootUrl;
	//包含css文件或者src的语句的开头
	private static String urlSign = "<link href=";       
	private static String urlSign2 = "src=";   
	    
	public static void main(String[] args)
	{
		String urlString="http://www.cug.edu.cn/new/";
		downPureHtml(urlString);
	}
	
	private static void downPureHtml(String url)
	{
		try
		{
			rootUrl=setRootUrl (url);  
			URL url2=new URL(url);
			InputStream inputStream=url2.openStream();
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
			File file=createDownloadFile("sk.html");
			BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
			String s;
			while((s=bufferedReader.readLine())!=null)
			{
				if(hasSubUrl(s))
				{
					downloadChild (getSubUrl (s));  
				}
				bufferedWriter.write(s);
				bufferedWriter.newLine();
			}
			inputStream.close();
			bufferedReader.close();
			bufferedWriter.close();
			System.out.println("download html successfully");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private static void downloadChild(String subUrl) throws MalformedURLException
	{
		if (!subUrl.isEmpty())  
        {  
			String forUrl = subUrl.replace (" ", "%20");  
            if (!forUrl.startsWith ("/"))  
            {  
                forUrl = "/" + forUrl;  
            }  
            URL u = new URL (rootUrl + forUrl);  
            
            if (subUrl.startsWith ("http:"))  
            {  
                downPureHtml(subUrl);
            }  
            else  
            {  
                try  
                {  
                    InputStream reader = u.openStream ();  
                    File f = createDownloadFile (subUrl);  
                    FileOutputStream writer = new FileOutputStream (f);  
                    byte[] buff = new byte[1024];  
                    int size = -1;  
                    while ((size = reader.read (buff)) != -1)  
                    {  
                        writer.write (buff, 0, size);  
                    }  
                    reader.close ();  
                    writer.close ();  
                }  
                catch (Exception e)  
                {  
                    e.printStackTrace ();  
                }  
            }  
        }  
        else  
        {  
            System.out.println ("subUrl is Empty.");  
        }  
	}

	private static String getSubUrl(String s)
	{
		int pos=s.indexOf(urlSign);
		if(pos==-1)
		{
			pos=s.indexOf(urlSign2);
		}
		s=s.substring(pos);
		String[] strings=s.split("\"");
		return strings[1];
	}

	/**
	 * 如果有子url
	 * @param s		一行http语句
	 * @return
	 */
	private static boolean hasSubUrl(String s)
	{
		if(s.contains(urlSign)|| s.contains(urlSign2))
		{
			return true;
		}
		return false;
	}

	//得到最后一个/之前的所有url
	private static String setRootUrl(String url)
	{
		int pos=url.lastIndexOf("/");
		String rootUrlString=url.substring(0,pos);
		System.out.println("root Url:    "+rootUrlString);
		return rootUrlString;
	}

	private static File createDownloadFile(String string)
	{
		File file=new File("resources",string);
		file.getParentFile().mkdir();
		return file;
	}
}
