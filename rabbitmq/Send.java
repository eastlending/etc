package org.luckystars.rabbitmq;

import java.io.IOException;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
	static String QUEUE_NAME = "hello";
	static boolean durable = true;//持久化
	static boolean exclusive=false; 
	static boolean autoDelete=false;
	public static void main(String[] args) throws Throwable {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("guest");
		factory.setPassword("guest");
//		factory.setVirtualHost(5672);
		factory.setHost("*****");
		factory.setPort(5672);
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();

	    channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, null);
	    for (int i = 0; i < 1000; i++) {
	    	String message = "Hello World!" + System.currentTimeMillis();
		    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
		    System.out.println(" [x] Sent '" + message + "'");
		}
	    

	    channel.close();
	    conn.close();
		
	}
}
