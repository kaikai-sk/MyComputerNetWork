package com.qq.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class VerifyCode 
{
	//图片的宽度和高度
	private int w = 150;
	private int h = 35;
 	private Random r = new Random();
	private String[] fontNames  = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};
	private String codes  = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	private Color bgColor  = new Color(255, 255, 255);
	private String text ;
	
	/**
	 * 随即返回一个颜色
	 * @return
	 */
	private Color randomColor ()
	{
		//该方法的作用是生成一个随机的int值，该值介于[0,n)的区间，也就是0到n之间的随机int值，包含0而不包含n。
		int red = r.nextInt(150);
		int green = r.nextInt(150);
		int blue = r.nextInt(150);
		return new Color(red, green, blue);
	}
	
	/**
	 * 随机返回字体
	 * @return
	 */
	private Font randomFont () 
	{
		int index = r.nextInt(fontNames.length);
		String fontName = fontNames[index];
		int style = r.nextInt(4);
		int size = r.nextInt(5) + 24; 
		/**
		 * style ：斜体，下划线。。。。。。
		 */
		return new Font(fontName, style, size);
	}
	
	/**
	 * 随机画线，扰乱视线
	 * @param image
	 */
	private void drawLine (BufferedImage image) 
	{
		int num  = 3;
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		for(int i = 0; i < num; i++) 
		{
			int x1 = r.nextInt(w);
			int y1 = r.nextInt(h);
			int x2 = r.nextInt(w);
			int y2 = r.nextInt(h); 
			g2.setStroke(new BasicStroke(1.5F)); 
			g2.setColor(Color.BLUE); 
			g2.drawLine(x1, y1, x2, y2);
		}
	}
	
	private char randomChar () 
	{
		int index = r.nextInt(codes.length());
		return codes.charAt(index);
	}
	
	private BufferedImage createImage () 
	{
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB); 
		Graphics2D g2 = (Graphics2D)image.getGraphics(); 
		g2.setColor(this.bgColor);
		g2.fillRect(0, 0, w, h);
 		return image;
	}
	
	/**
	 * 获取一次性验证码图片
	 * @return
	 */
	public BufferedImage getImage () 
	{
		BufferedImage image = createImage(); 
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		StringBuilder sb = new StringBuilder();
		// 向图片中画6个字符
		for(int i = 0; i < 6; i++)  
		{
			String s = randomChar() + ""; 
			sb.append(s); 
			float x = i * 1.0F * w / 6; 
			g2.setFont(randomFont()); 
			g2.setColor(randomColor()); 
			g2.drawString(s, x, h-5); 
		}
		this.text = sb.toString(); 
		drawLine(image); 
		return image;		
	}
	
	public String getText () 
	{
		return text;
	}
	
	/**
	 * 该方法必须在getImage()方法之后来调用
	 * @param image 自己画出的图片
	 * @param out 指定的输出流
	 * @throws IOException
	 */
	public static void output (BufferedImage image, OutputStream out) 
				throws IOException 
	{
		ImageIO.write(image, "JPEG", out);
	}
}



