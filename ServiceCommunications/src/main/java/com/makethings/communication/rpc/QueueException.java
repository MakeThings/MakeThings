package com.makethings.communication.rpc;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class QueueException extends AppRuntimeExeption {

    private static final long serialVersionUID = -6936611329203171777L;

    public QueueException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueueException(String message) {
        super(message);
    }

}
