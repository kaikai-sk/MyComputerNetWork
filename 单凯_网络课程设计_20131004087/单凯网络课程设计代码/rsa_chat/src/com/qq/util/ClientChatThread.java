package com.qq.util;

import com.banko.rsachat.ChatClient;

public class ClientChatThread extends Thread
{
	User commToUser;
	User meUser;
	
	public ClientChatThread(User commToUser,User meUser)
	{
		this.commToUser=commToUser;
		this.meUser=meUser;
	}
	
	@Override
	public void run()
	{
		super.run();
		ChatClient client=new ChatClient(commToUser,meUser);
		client.setVisible(true);
		System.err.println("sk thread test");
	}
}
