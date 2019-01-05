package com.banko.rsachat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;


public class ChatServer {
	private HashSet<Socket> clients = new HashSet<Socket>();

	public ChatServer() {
		try {
			ServerSocket ss = new ServerSocket(8888);
			while(true) {
				Socket s = ss.accept();
				clients.add(s);
				new ServerThread(s,clients).start();
			}
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
		}
	}
	
	class ServerThread extends Thread{
		private Socket s;
		private HashSet <Socket>clients ;
		public ServerThread(Socket s, HashSet <Socket>clients) {
			this.s = s;
			this.clients = clients;
		}
		
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				while(true) {
					String message = br.readLine();
//					message = RSAChinese.decrypt(message.trim(), RSAChinese.key[2], RSAChinese.key[1]);
					sendMessage(message + "\n");
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void sendMessage(String str) {
			Iterator <Socket>it = clients.iterator();
			while(it.hasNext()) {
				Socket s = (Socket) it.next();
				try {
					PrintWriter pw = new PrintWriter(s.getOutputStream());
					pw.println(str);
					pw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println("服务器已启动");
		new ChatServer();
	}
}
