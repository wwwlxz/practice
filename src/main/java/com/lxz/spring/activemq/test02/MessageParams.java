package com.lxz.spring.activemq.test02;

/**
 * @author Syamantak Mukhopadhyay
 */
public enum MessageParams {
    URL_PARAM("url");

    private String param;

    private MessageParams(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return param;
    }
}

