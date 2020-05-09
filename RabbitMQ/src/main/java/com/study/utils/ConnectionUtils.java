package com.study.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 获取rabbitMQ连接工具类
 * 
 * @author danbingbing
 *
 */
public class ConnectionUtils {

	/**
	 * 获取rabbitMQ连接
	 * 
	 * @return Connection
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static Connection getConnection() throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
		
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setVirtualHost("/vhosts_test");
		factory.setUsername("rabbitmq_user");
		factory.setPassword("rabbitmq_user");
		
		return factory.newConnection();
	}
}
