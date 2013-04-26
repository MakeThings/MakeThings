package com.makethings.communication.rpc;


public interface RemoteService {
    void init() throws RemoteServiceException;
    void start() throws RemoteServiceException;
    void stop();
    RemoteServiceState getState();
}
