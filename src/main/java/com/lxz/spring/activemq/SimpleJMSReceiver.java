package com.lxz.spring.activemq;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.JmsException;

public class SimpleJMSReceiver {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-receive.xml");
		while(true) {
		}
	}
	
	public void receive(Message message) throws JmsException, JMSException {
		System.out.println(message.getStringProperty("phrCode"));
	}
}
