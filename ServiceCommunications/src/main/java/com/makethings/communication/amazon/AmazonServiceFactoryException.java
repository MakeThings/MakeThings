package com.makethings.communication.amazon;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class AmazonServiceFactoryException extends AppRuntimeExeption {

    private static final long serialVersionUID = 6498981759900497953L;

    public AmazonServiceFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmazonServiceFactoryException(String message) {
        super(message);
    }
}
