package com.makethings.communication.queue;

public class SimpleServiceRequestQueueName implements ServiceRequestQueueName {

    private final String name;

    public SimpleServiceRequestQueueName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
