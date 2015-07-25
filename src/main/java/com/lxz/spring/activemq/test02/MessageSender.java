package com.lxz.spring.activemq.test02;

import java.util.Collections;
import java.util.Map;

import org.springframework.jms.core.JmsTemplate;

/**
 * @author Syamantak Mukhopadhyay
 */
public class MessageSender {
    private final JmsTemplate jmsTemplate;

    /**
     * Inject a JMS Template
     * @param jmsTemplate
     */
    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void simpleSend(final String url) {
        Map<String, String> map = Collections.singletonMap(MessageParams.URL_PARAM.toString(), url);
        this.jmsTemplate.convertAndSend(map);
    }
}
