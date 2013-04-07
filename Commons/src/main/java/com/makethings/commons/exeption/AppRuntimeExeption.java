package com.makethings.commons.exeption;

public class AppRuntimeExeption extends RuntimeException {

    private static final long serialVersionUID = 470663755080719791L;

    public AppRuntimeExeption() {
        super();
    }

    public AppRuntimeExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public AppRuntimeExeption(String message) {
        super(message);
    }

    public AppRuntimeExeption(Throwable cause) {
        super(cause);
    }

}
