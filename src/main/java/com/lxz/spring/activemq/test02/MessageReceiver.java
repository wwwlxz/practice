package com.lxz.spring.activemq.test02;

import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Collections;

/**
 * Implement javax.jms.MessageListener interface
 *
 * @author Syamantak Mukhopadhyay
 */
public class MessageReceiver implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(MessageReceiver.class);

    private final OutputCollector outputCollector;

    public MessageReceiver(OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;
            try {
                String url = mapMessage.getString(MessageParams.URL_PARAM.toString());
                LOGGER.info(String.format("Received URL {%s}", url));
                outputCollector.setOutputMap(Collections.singletonMap(MessageParams.URL_PARAM.toString(), url));
            } catch (JMSException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
