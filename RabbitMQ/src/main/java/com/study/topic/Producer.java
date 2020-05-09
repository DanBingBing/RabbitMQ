package com.study.topic;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.utils.ConnectionUtils;

/**
 * 第一次启动时，要先创建队列，即先运行一次生产者
 * 然后启动消费者监听队列中有无消息，再次启动生产者，
 * 反之则测试不出多个消费者的作用，而是哪个消费者先启动就处理所有的生产者的消息
 * 
 * fanout(表示不处理路由键)，即通过交换机的所有消息都会转发给绑定该交换机的所有队列
 * direct(表示处理路由键)，即通过交换机的所有消息都会转发给绑定该交换机的所有队列
 * 消息中带有routing key，如果绑定该交换机的队列有路由键，则会把消息转发给他
 * 
 * @author danbingbing
 *
 */
public class Producer {
	
private final static String EXCHANGE_NAME = "test_exchance_topic";
	
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
		
		String message = "Hello topic!";
		
		// 声明交换机
		// direct(表示处理路由键)，即通过交换机的所有消息都会转发给绑定该交换机的所有队列
		// 消息中带有routing key，如果绑定该交换机的队列有路由键，则会把消息转发给他
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		
		//String routingKey = "goods.add";
		String routingKey = "goods.delete";
		
		// 必须使用一种编码格式（StandardCharsets类中封装了的编码常量字段）
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
		
		channel.close();
		connection.close();
		
		System.out.println("Sent: '" + message + "'");
		
	}
}
