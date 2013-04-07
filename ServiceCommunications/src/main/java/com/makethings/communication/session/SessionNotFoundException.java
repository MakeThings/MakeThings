package com.makethings.communication.session;

import com.makethings.commons.exeption.AppRuntimeExeption;

public class SessionNotFoundException extends AppRuntimeExeption {

    private static final long serialVersionUID = -6718291583373487349L;

    private final String sessionId;

    public SessionNotFoundException(String message, String sessionId) {
        super(message);
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
