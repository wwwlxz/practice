package com.lxz.spring.activemq.test02;

public class SenderMain {
	public static void main(String[] args){
		MessageSender messageSender;
		//OutputCollector outputCollector;
		messageSender = AppFactory.getInstance().getBean(MessageSender.class);
		//outputCollector = AppFactory.getInstance().getBean(OutputCollector.class);
		String url = "http://www.lxz.com";
		messageSender.simpleSend(url);
		System.out.println(url);
	}
}
