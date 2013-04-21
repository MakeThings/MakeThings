package com.makethings.communication.session.service;

public class SimpleServiceName implements ServiceName {

    private final String name;

    public SimpleServiceName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
