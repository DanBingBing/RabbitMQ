package com.study.publishAndSubscribe;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.utils.ConnectionUtils;

/**
 * fanout(表示不处理路由键)，即通过交换机的所有消息都会转发给绑定该交换机的所有队列
 * direct(表示处理路由键)，即通过交换机的所有消息都会转发给绑定该交换机的所有队列
 * 消息中带有routing key，如果绑定该交换机的队列有路由键，则会把消息转发给他
 * 
 * @author danbingbing
 *
 */
public class Producer {
	
private final static String EXCHANGE_NAME = "test_exchance_fanout";
	
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
		
		String message = "Hello publish and subscribe!";
		
		// 声明交换机
		// fanout(表示不处理路由键)，即通过交换机的所有消息都会转发给绑定该交换机的所有队列
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		// 必须使用一种编码格式（StandardCharsets类中封装了的编码常量字段）
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
		
		channel.close();
		connection.close();
		
		System.out.println("Sent: '" + message + "'");
		
	}
}
