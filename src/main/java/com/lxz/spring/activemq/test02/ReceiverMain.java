package com.lxz.spring.activemq.test02;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

public class ReceiverMain {
	public static void main(String[] args){
		ActiveMQConnectionFactory amqConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(amqConnectionFactory);
		OutputCollector outputCollector = new OutputCollector();
		MessageReceiver messageReceiver = new MessageReceiver(outputCollector);
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		//simpleMessageListenerContainer.destin
//		MessageReceiver messageReceiver;
//		OutputCollector outputCollector;
//		outputCollector = AppFactory.getInstance().getBean(OutputCollector.class);
//		messageReceiver = AppFactory.getInstance().getBean(MessageReceiver.class);
////		Message
////		messageReceiver.onMessage();
//		Map<String, String> outputMap = outputCollector.getOutputMap();
//		System.out.println(outputMap);
	}
	

}
