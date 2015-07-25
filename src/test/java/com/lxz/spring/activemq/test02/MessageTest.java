package com.lxz.spring.activemq.test02;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import junit.framework.TestCase;

public class MessageTest extends TestCase {
	private MessageSender messageSender;
    private OutputCollector outputCollector;

    @BeforeMethod
    public void setUp() {
         messageSender = AppFactory.getInstance().getBean(MessageSender.class);
         outputCollector = AppFactory.getInstance().getBean(OutputCollector.class);
    }

    @Test
    public void shouldReceiveSameMessageFromSender() {
        //Given
        String url = "http://www/example.com";

        //When
        messageSender.simpleSend(url);

        //Then
        Map<String, String> outputMap = outputCollector.getOutputMap();
        assertEquals(outputMap.size(), 1);
        assertEquals(outputMap.get(MessageParams.URL_PARAM.toString()), url);
    }
}
