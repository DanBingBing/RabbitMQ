package com.study.workQueue.pollDistribution;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.study.utils.ConnectionUtils;

/**
 * 先启动消费者监听队列中有无消息，再启动生产者，
 * 反之则测试不出多个消费者的作用，而是哪个消费者先启动就处理所有的生产者的消息
 * 轮询分发：
 * 	无论C1、C2的消费处理能力如何，最后他们都会按照一人一个的方式都拿到所有的消息
 * 
 * @author danbingbing
 *
 */
public class Consumer1 {

	private final static String QUEUE_NAME = "test_work_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 获取连接
		Connection connection = ConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages.");
	    
	    DefaultConsumer consumer = new DefaultConsumer(channel) {
	    	@Override
	    	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
	    			throws IOException {
	    		super.handleDelivery(consumerTag, envelope, properties, body);
	    		
	    		String message = new String(body,StandardCharsets.UTF_8);
	    		System.out.println(" [x] Received '" + message + "'");
	    		
	    		try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					System.out.println("[1s] done");
				}
	    	}
	    };
	    
	    // 使用轮询分发必须开启自动应答
	    // 一旦消息队列将消息发给消费者，就会将该消息从内存中删除
	    boolean autoAck = true;
	    // 监听队列（如果队列中有新的消息就执行consumer中的方法）
	    channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}

}
