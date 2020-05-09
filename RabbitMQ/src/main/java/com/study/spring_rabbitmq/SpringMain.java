package com.study.spring_rabbitmq;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
	
	public static void main(String[] args) throws Exception {
		// 加載配置文件
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-rabbitmq.xml");
		
		// rabbitmq模板
		RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
		
		// 發送消息
		template.convertAndSend("Hello spring-rabbitmq!");
		
		Thread.sleep(1000);
		ctx.close();
	}
}
