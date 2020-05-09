package com.study.simpleQueue;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.utils.ConnectionUtils;

public class Producer {
	
private final static String QUEUE_NAME = "test_simple_queue";
	
	/**
	 * 发送消息
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 获取连接
		Connection connection = ConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		
		String message = "Hello simple!";
		
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 必须使用一种编码格式（StandardCharsets类中封装了的编码常量字段）
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
		
		channel.close();
		connection.close();
		
		System.out.println("Sent: '" + message + "'");
		
	}
}
