package com.lxz.spring.activemq.test02;

import java.util.Map;

/**
 * A placeholder to collect output from message receiver.
 * Used for testing
 *
 * @author Syamantak Mukhopadhyay
 */
public class OutputCollector {
    private Map<String, String> outputMap;

    public void setOutputMap(Map<String, String> map) {
        this.outputMap = map;
    }

    public Map<String, String> getOutputMap() {
        return outputMap;
    }
}
