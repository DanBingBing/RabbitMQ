package com.study.tx;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.utils.ConnectionUtils;

/**
 * 使用事务机制
 * tx模式相对于confirm模式性能较差
 * 
 * @author danbingbing
 *
 */
public class Producer {
	
private final static String QUEUE_NAME = "test_simple_queue_tx";
	
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
		
		String message = "Hello tx!";
		
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		try {
			channel.txSelect();
			// 必须使用一种编码格式（StandardCharsets类中封装了的编码常量字段）
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
			
			// 出现异常时，事务回滚
			//int x =1/0;
			System.out.println("Sent: '" + message + "'");
			channel.txCommit();
		} catch (Exception e) {
			channel.txRollback();
			System.out.println("Sent:throws exception, message rollback!");
		}
		
		channel.close();
		connection.close();
		
	}
}
