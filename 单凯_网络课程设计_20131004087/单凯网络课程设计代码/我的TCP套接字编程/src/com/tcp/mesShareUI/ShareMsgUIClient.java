package com.tcp.mesShareUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ShareMsgUIClient extends JFrame implements ActionListener,KeyListener
{
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 2016;
    
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    
    //gui
    JTextArea jta=new JTextArea();
	JTextField jtf=new JTextField(20);
	JButton jb=new JButton("发送");
	JScrollPane jsp;
	JPanel jp1=new JPanel();
    
    /**
     * 与服务器连接，并输入发送消息
     */
    public ShareMsgUIClient() throws Exception
    {
        client = new Socket(SERVER_IP, SERVER_PORT);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        setInterface();
        new readLineThread();
        
//        while(true)
//        {
//            in = new BufferedReader(new InputStreamReader(System.in));
//            String input = in.readLine();
//            out.println(input);
//        }
    }
    
    /**
	 * 设置界面
	 */
	private void setInterface()
	{
		jsp=new JScrollPane(jta);
		this.add(jsp,"Center");
		jp1.add(jtf);
		jp1.add(jb);
		this.add(jp1, "South");
		
		this.setTitle("简单聊天  	客户端");
		this.setSize(400,300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jb.addActionListener(this);
		jtf.addKeyListener(this);
	}
    
    /**
     * 用于监听服务器端向客户端发送消息线程类
     */
    class readLineThread extends Thread
    {    
        private BufferedReader buff;
        public readLineThread()
        {
            try 
            {
                buff = new BufferedReader(new InputStreamReader(client.getInputStream()));
                start();
            }
            catch (Exception e) 
            {
            }
        }
        
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					String result = buff.readLine();
					if ("byeClient".equals(result))
					{
						// 客户端申请退出，服务端返回确认退出
						break;
					}
					else
					{
						// 输出服务端发送消息
						jta.append(result+"\r\n");
//						System.out.println(result);
					}
				}
				in.close();
				out.close();
				client.close();
			} 
			catch (Exception e)
			{
			}
		}
	}

    public static void main(String[] args) 
    {
        try 
        {
            new ShareMsgUIClient();//启动客户端
        }
        catch (Exception e) 
        {
        	
        }
    }
    
    public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(jb))
		{
			SendMsg();
		}
	}

	private void SendMsg()
	{
		PrintWriter pw;
		try
		{
			
			out.println("客户端说："+jtf.getText()+"\r\n");
			jta.append("客户端说："+jtf.getText()+"\r\n");
			jtf.setText("");
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent arg0)
	{		
	}

	public void keyReleased(KeyEvent arg0)
	{
		if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
		{
			SendMsg();
		}
	}

	public void keyTyped(KeyEvent arg0)
	{
		
	}
}