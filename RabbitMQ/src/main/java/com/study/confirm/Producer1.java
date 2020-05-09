package com.study.confirm;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.utils.ConnectionUtils;

/**
 * 使用confirm模式(串行)
 * tx模式相对于confirm模式性能较差
 * 
 * @author danbingbing
 *
 */
public class Producer1 {
	
private final static String QUEUE_NAME = "test_simple_queue_confirm1";
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 获取连接
		Connection connection = ConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		
		String message = "Hello confirm!";
		
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.confirmSelect();
		
		//channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
		//System.out.println("Sent: '" + message + "'");
		// 批量发送
		for(int i=0;i<10;i++) {
			message = "Hello confirm!"+i;
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
			System.out.println("Sent: '" + message + "'");
		}
		
		if(!channel.waitForConfirms()) {
			System.out.println("Sent: failed!");
		}else {
			System.out.println("Sent: success!");
		}
		
		channel.close();
		connection.close();
		
	}
}
