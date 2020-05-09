package com.study.confirm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.study.utils.ConnectionUtils;

/**
 * 使用confirm模式（异步）
 * tx模式相对于confirm模式性能较差
 * 
 * @author danbingbing
 *
 */
public class Producer2 {
	
private final static String QUEUE_NAME = "test_simple_queue_confirm2";
	
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
		
		// 生产者调用confirmSelect将channel设置为confirm模式
		channel.confirmSelect();
		
		// 未确认的消息标识
		SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
		
		// 添加监听通道
		channel.addConfirmListener(new ConfirmListener() {
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				if(multiple) {
					System.err.println("--handleNack--multiple");
					confirmSet.headSet(deliveryTag+1).clear();
				}else {
					System.err.println("--handleNack--multiple--false");
					confirmSet.headSet(deliveryTag);
				}
				
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				if(multiple) {
					System.err.println("--handleAck--multiple");
					confirmSet.headSet(deliveryTag+1).clear();
				}else {
					System.err.println("--handleAck--multiple--false");
					confirmSet.headSet(deliveryTag);
				}
			}
		});
		
		// 确认消息
		String msgString = "success！";
		
		while(true) {
			long seqNo = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, msgString.getBytes(StandardCharsets.UTF_8));
			confirmSet.add(seqNo);
		}
		
	}
}
