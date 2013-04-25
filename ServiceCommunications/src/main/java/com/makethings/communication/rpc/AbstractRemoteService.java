package com.makethings.communication.rpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public abstract class AbstractRemoteService implements RemoteService {
    // TODO: make a logger allocated to service name rather then to class name.
    private final static Logger LOG = LoggerFactory.getLogger(AbstractRemoteService.class);

    private ServiceSessionDefinition sessionDefinition;
    private ServiceSession serviceSession;
    private RemoteServiceState state;
    private Executor executor;
    private ServiceManager serviceManager;
    private CountDownLatch processingThreads;

    public AbstractRemoteService() {
        this.state = RemoteServiceState.CREATED;
    }

    @Override
    public void init() {
        if (state == RemoteServiceState.CREATED) {
            LOG.info("Initialising remote service: {}", sessionDefinition.getServiceName().getName());

            state = RemoteServiceState.INITIALISING;

            createSession();

            state = RemoteServiceState.WAITING_TO_STAT;
        }
        else {
            throw new RemoteServiceException("Cannot initialise service: " + getServiceName() + ", expected state: "
                    + RemoteServiceState.CREATED + ", but actual " + state);
        }
    }

    private String getServiceName() {
        return sessionDefinition.getServiceName().getName();
    }

    private void createSession() {
        try {
            LOG.info("Creating service session for remote service: {}", sessionDefinition.getServiceName().getName());

            serviceSession = serviceManager.openServiceSession(getSessionDefinition());
        }
        catch (RuntimeException e) {
            throw new RemoteServiceException("Remote service: " + sessionDefinition.getServiceName().getName() + " cannot be inited", e);
        }
    }

    @Override
    public ServiceSession getSession() {
        return serviceSession;
    }

    @Override
    public void start() {
        if (state == RemoteServiceState.WAITING_TO_STAT) {
            LOG.info("Starting remote service: {}", serviceSession.getServiceName());
            processingThreads = new CountDownLatch(1);
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    startProcessing();
                    LOG.info("Stop process thread of service: {}", serviceSession.getServiceName());
                    processingThreads.countDown();
                }
            });

            state = RemoteServiceState.RUNNING;
        }
        else {
            throw new RemoteServiceException("Cannot start service: " + getServiceName() + ", expected state: "
                    + RemoteServiceState.WAITING_TO_STAT + ", but actual " + state);
        }
    }

    public void stop() {
        serviceManager.closeServiceSession(serviceSession.getId());
        state = RemoteServiceState.STOPPED;
        try {
            processingThreads.await();
        }
        catch (InterruptedException e) {
            LOG.warn("Awaiting of service stop was interupted: {}", serviceSession.getServiceName());
        }

    }

    public RemoteServiceState getState() {
        return state;
    }

    protected abstract void startProcessing();

    public ServiceSessionDefinition getSessionDefinition() {
        return sessionDefinition;
    }

    public void setSessionDefinition(ServiceSessionDefinition sessionDefinition) {
        this.sessionDefinition = sessionDefinition;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

}
