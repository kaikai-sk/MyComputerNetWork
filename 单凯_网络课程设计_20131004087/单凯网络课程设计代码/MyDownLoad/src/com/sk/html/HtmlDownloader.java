package com.sk.html;
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.FileWriter;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.net.URL;  
   
/**
 * 可下载页面所依赖的CSS/JS,图片等引用，目前不包含下载关联HTML页面这个功能
 */
public class HtmlDownloader  
{  
    //URL will be downloaded.  
    private static String url = "http://www.cug.edu.cn/new/";  
    //放置下载的资源的文件夹 
    private static String workspace = "download";  
    //sub css and js resources sign   
    private static String urlSign = "<link href=";       
    //sub image resources sign  
    private static String urlSign2 = "src=";   
    //URL parent.  
    private static String rootUrl = null;  
  
    public static void main (String[] args) throws Exception  
    {  
    	//开始时的纳秒
        long start = System.nanoTime ();  
        setRootUrl ();  
        URL u = new URL (url);  
        InputStream is = u.openStream ();  
        BufferedReader reader = new BufferedReader (new InputStreamReader (is));  
        File f = createDownloadFile ("download.html");  
        BufferedWriter writer = new BufferedWriter (new FileWriter (f));  
        String s;  
        while ((s = reader.readLine ()) != null)  
        {  
            writer.write (s);  
            writer.newLine ();  
            if (hasSubUrl (s))  
            {  
                downloadChild (getSubUrl (s));  
            }  
        }  
        is.close ();  
        reader.close ();  
        writer.close ();  
        System.out.println ("Download time(s):" + String.format ("%.3f", (double)(System.nanoTime () - start)/ 1000000000.00));  
    }
  
    /** 
     * set root url for the downloading html 
     */  
    private static void setRootUrl ()  
    {  
        int pos = url.lastIndexOf ("/");  
        rootUrl = url.substring (0, pos);  
        System.out.println ("Root Url is:" + rootUrl);  
    }  
    
    /** 
     * download sub resources,<b>Note: don't use Java Character Writers, 
     * otherwise you can't get pictures correctly.</b> 
     *  
     * @param subUrl 
     */  
    private static void downloadChild (String subUrl)  
    {  
        if (!subUrl.isEmpty())  
        {  
        	//现在还不支持下载html页面
            if (subUrl.startsWith ("http:"))  
            {  
                System.out.println ("subUrl not support yet.");  
            }  
            else  
            {  
            	//开始时间
                long start = System.nanoTime ();  
                try  
                {  
                    String forUrl = subUrl.replace (" ", "%20");  
                    if (!forUrl.startsWith ("/"))  
                    {  
                        forUrl = "/" + forUrl;  
                    }  
                    URL u = new URL (rootUrl + forUrl);  
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
                System.out.println ("Source:" + subUrl +"download time(s):" + String.format ("%.3f", (double)(System.nanoTime () - start)/ 1000000000.00));  
            }  
        }  
        else  
        {  
            System.out.println ("subUrl is Empty.");  
        }  
    }  
  
    /** 
     * generate sub url from line content. 
     */  
    private static String getSubUrl (String text)  
    {  
    	//<link href="../css/CSS.CSS" rel="stylesheet" type="text/css" />
    	//<img src="../image/sub_logo.jpg" width="394" height="129" />
    	//检查有没有css文件和图片资源
        int pos = text.indexOf (urlSign);  
        pos = (pos == -1) ? text.indexOf (urlSign2) : pos;  
        text = text.substring (pos);  
        String[] ps = text.split ("\"");  
        System.out.println ("subUrl is :" + ps[1]);  
        return ps[1];  
    }  
    
    /** 
     * 检查是否包含css文件或者图片等资源
     * @param  一行html语句
     * @return Yes or Not 
     */  
    private static boolean hasSubUrl (String text)  
    {  
        if (!text.isEmpty())  
        {  
        	//如果这一行html语句中包含css文件或者src
            if (text.contains (urlSign) || text.contains (urlSign2))  
            {  
            	//有
                return true;  
            }  
            //没有
            return false;  
        }  
        else  
        {  
            return false;  
        }  
    }  
    
    /** 
     * 创建一个文件，必要的时候创建父文件夹     *  
     * @param url 一个url的相关路径。
     * @return  创建好的文件对象
     */  
    private static File createDownloadFile (String url)  
    {  
    	//通过指定父目录和文件名来创建文件对象
        File f = new File (workspace, url);  
        //根据路径名创建父文件夹
        f.getParentFile ().mkdirs();  
        return f;  
    }  
  
}  