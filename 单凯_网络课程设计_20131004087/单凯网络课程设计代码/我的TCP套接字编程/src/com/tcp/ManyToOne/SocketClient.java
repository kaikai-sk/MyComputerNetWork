package com.tcp.ManyToOne;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient
{
	private static String serverIP="127.0.0.1";
	private static int serverPort=2016;
	
	public static void main(String[] args)
	{
		try
		{
			Socket socket = new Socket(serverIP, serverPort);
			socket.setSoTimeout(60000);

			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),
					true);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			String result = "";
			while (result.indexOf("bye") == -1)
			{
				BufferedReader sysBuff = new BufferedReader(
						new InputStreamReader(System.in));
				printWriter.println(sysBuff.readLine());
				printWriter.flush();

				result = bufferedReader.readLine();
				System.out.println("Server say : " + result);
			}

			printWriter.close();
			bufferedReader.close();
			socket.close();
		} 
		catch (Exception e)
		{
			System.out.println("Exception:" + e);
		}
	}
}