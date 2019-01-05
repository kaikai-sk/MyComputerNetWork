package com.banko.rsachat;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame{
	
	private JTextArea jta;
	private JTextField jtf;
	private JButton jb;
	private JLabel name;
	private Socket s;
	private PrintWriter pw;
	private BufferedReader br;
	
	public ChatClient() {
		this.setSize(300, 350);
		this.setLocation(350,200);
		this.setTitle("聊天室");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jta = new JTextArea(15, 20);
		jtf = new JTextField(15);
		jb = new JButton("发送");
		name = new JLabel(String.valueOf(new Random().nextInt(100)));
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		jp.add(name);
		jp.add(jtf);
		jp.add(jb);
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				String message = jtf.getText();
				
//				message = jtf.getText();
				String message = name.getText() + "说: " + jtf.getText();
				message = RSAChinese.encrypt(message, RSAChinese.key[0], RSAChinese.key[1]);
				pw.println(message);
				pw.flush();          //清空缓冲区
				jtf.setText("");
			}
			
		});
		
		this.setLayout(new BorderLayout());
		this.add(jta, BorderLayout.CENTER);
		this.add(jp, BorderLayout.SOUTH);
		getMessage();
		this.setVisible(true);
	}

	private void getMessage() {
		try {
			s = new Socket("127.0.0.1", 8888);
			pw = new PrintWriter(s.getOutputStream());
			new ClientThread().start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class ClientThread extends Thread{
		public void run() {
			while(true) {
				try {
					br = new BufferedReader(new InputStreamReader(s.getInputStream()));
					String message = br.readLine();
					message = RSAChinese.decrypt(message, RSAChinese.key[2], RSAChinese.key[1]);
					jta.append(message + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}
	
	public static void main(String[] args) {
		new ChatClient();
	}
}
