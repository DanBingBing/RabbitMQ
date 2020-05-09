package com.study.publishAndSubscribe;

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
 * 第一次启动时，要先创建队列，即先运行一次生产者
 * 然后启动消费者监听队列中有无消息，再次启动生产者，
 * 反之则测试不出多个消费者的作用，而是哪个消费者先启动就处理所有的生产者的消息
 * 
 * @author danbingbing
 *
 */
public class Consumer2 {

	private final static String QUEUE_NAME = "test_queue_fanout_SMS";
	
	private final static String EXCHANGE_NAME = "test_exchance_fanout";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 获取连接
		Connection connection = ConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    
	    // 绑定队列到交换机（转发器）
	    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
	    
	    // 在消费者处理完一条消息之后会给消息队列一个反馈消息，之后才能接受下一条消息
	    // 也就是每次只分发一条消息
	    channel.basicQos(1);
	    System.out.println(" [*] Waiting for messages.");
	    
	    DefaultConsumer consumer = new DefaultConsumer(channel) {
	    	@Override
	    	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
	    			throws IOException {
	    		super.handleDelivery(consumerTag, envelope, properties, body);
	    		
	    		String message = new String(body,StandardCharsets.UTF_8);
	    		System.out.println(" [x] Received '" + message + "'");
	    		
	    		try {
	    			// 设置消息延时处理，时间长则表示处理慢，则分发的消息少
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					// 处理完一条消息后的反馈消息（手动回执）
					channel.basicAck(envelope.getDeliveryTag(), false);
					System.out.println("[2s] done");
				}
	    	}
	    };
	    
	    // 使用公平分发必须关闭自动应答（默认是false）
	    // 如果有一个消费者宕机就会将该消息交给其他消费者处理，
	    // 也就是消息应答，消费者接收到消息处理完后会给消息队列一个应答，此时才能从内存中删除该消息
	    boolean autoAck = false;
	    // 监听队列（如果队列中有新的消息就执行consumer中的方法）
	    channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}

}
