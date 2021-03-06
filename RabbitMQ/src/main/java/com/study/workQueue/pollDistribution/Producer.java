package com.study.workQueue.pollDistribution;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.utils.ConnectionUtils;

public class Producer {
	
private final static String QUEUE_NAME = "test_work_queue";
	
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
		
		for(int i=0;i<20;i++) {
			String message = "Hello poll distribution! " + i;
			// 声明队列
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			// 必须使用一种编码格式（StandardCharsets类中封装了的编码常量字段）
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
			
			// 消息发送很快，设置线程睡眠
			Thread.sleep(i*20);
			System.out.println("Sent: '" + message + "'");
		}
		
		channel.close();
		connection.close();
		
	}
}
