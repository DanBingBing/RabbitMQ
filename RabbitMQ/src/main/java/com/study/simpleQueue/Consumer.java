package com.study.simpleQueue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.study.utils.ConnectionUtils;

public class Consumer {

	private final static String QUEUE_NAME = "test_simple_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		//Receive1();
		Receive2();
	}

	/**
	 * 使用Java1.8中新增的箭头函数接收消息
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static void Receive1() throws IOException, TimeoutException {
		// 获取连接
		Connection connection = ConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();

		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages.");

        // Java1.8可使用
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        
        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		 
	}

	/**
	 * 接收消息
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static void Receive2() throws IOException, TimeoutException {
		// 获取连接
		Connection connection = ConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages.");
	    
	    DefaultConsumer consumer = new DefaultConsumer(channel) {
	    	// 监听到新消息时，触发该方法
	    	@Override
	    	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
	    			throws IOException {
	    		super.handleDelivery(consumerTag, envelope, properties, body);
	    		
	    		String message = new String(body,StandardCharsets.UTF_8);
	    		System.out.println(" [x] Received '" + message + "'");
	    	}
	    };
	    
	    // 监听队列（如果队列中有新的消息就执行consumer中的方法）
	    channel.basicConsume(QUEUE_NAME, true, consumer);
		
	}

}
