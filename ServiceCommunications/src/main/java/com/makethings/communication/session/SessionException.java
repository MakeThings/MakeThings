package com.makethings.communication.session;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class SessionException extends AppRuntimeExeption {

    private static final long serialVersionUID = 1129926695249378715L;
    
    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionException(String message) {
        super(message);
    }

}
