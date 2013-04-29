package com.makethings.communication.rpc;

import static com.makethings.communication.rpc.RemoteServiceState.INITIALISING;
import static com.makethings.communication.rpc.RemoteServiceState.RUNNING;
import static com.makethings.communication.rpc.RemoteServiceState.STARTING;
import static com.makethings.communication.rpc.RemoteServiceState.WAITING_TO_STAT;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.makethings.communication.session.service.ServiceSession;
import com.makethings.communication.session.service.ServiceSessionDefinition;

public abstract class AbstractRemoteService implements RemoteService {
    // TODO: make a logger allocated to service name rather then to class name.
    private final static Logger LOG = LoggerFactory.getLogger(AbstractRemoteService.class);

    private ServiceSessionDefinition sessionDefinition;
    private ServiceSession serviceSession;
    private volatile RemoteServiceState state;
    private volatile boolean stopSingal;
    private ServiceManager serviceManager;
    private CountDownLatch processingThreads;

    public AbstractRemoteService() {
        this.state = RemoteServiceState.CREATED;
        this.stopSingal = false;  
    }

    @Override
    public void init() {
        if (state == RemoteServiceState.CREATED) {
            LOG.info("Initialising remote service: {}", sessionDefinition.getServiceName().getName());

            state = RemoteServiceState.INITIALISING;

            createSession();

            onInit();

            state = RemoteServiceState.WAITING_TO_STAT;
            
            LOG.info("Remote service: {} has been initialised", getSession().getServiceName());
        }
        else {
            throw new RemoteServiceException("Cannot initialise service: " + getServiceName() + ", expected state: "
                    + RemoteServiceState.CREATED + ", but actual " + state);
        }
    }

    protected void onInit() {

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

    protected ServiceSession getSession() {
        return serviceSession;
    }

    @Override
    public void start() {
        if (state == RemoteServiceState.WAITING_TO_STAT) {
            LOG.info("Starting remote service: {}", serviceSession.getServiceName());

            state = RemoteServiceState.STARTING;

            doStartService();

            state = RemoteServiceState.RUNNING;
            
            LOG.info("Remote service: {} is up and running", serviceSession.getServiceName());
        }
        else {
            throw new RemoteServiceException("Cannot start service: " + getServiceName() + ", expected state: "
                    + RemoteServiceState.WAITING_TO_STAT + ", but actual " + state);
        }
    }
    
    private void doStartService() {
        processingThreads = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                processing();
                LOG.info("Stop process thread of service: {}", serviceSession.getServiceName());
                processingThreads.countDown();
            }
        });
    }

    public void stop() {
        stopSingal = true;
        if (isStarted()) {
            if (isWorking()) {
                doStopService();
            }
            doCloseSession();
        }
        state = RemoteServiceState.STOPPED;
    }

    private void doCloseSession() {
        if (serviceSession != null) {
            serviceManager.closeServiceSession(serviceSession.getId());
            serviceSession = null;
        }
    }
    
    private void doStopService() {
        try {
            processingThreads.await();
        }
        catch (InterruptedException e) {
            LOG.warn("Awaiting of service stop was interupted: {}", serviceSession.getServiceName());
        }
    }
    
    /**
     * Indicates if the service gets initialized or started.
     */
    private boolean isStarted() {
        return getState() == INITIALISING || getState() == RUNNING || getState() == STARTING || getState() == WAITING_TO_STAT;
    }

    /**
     * Indicates if the service is running.
     */
    protected boolean isWorking() {
        return getState() == RUNNING || getState() == STARTING;
    }

    public RemoteServiceState getState() {
        LOG.debug("Service State is {}", state);
        return state;
    }

    protected abstract void processing();

    public ServiceSessionDefinition getSessionDefinition() {
        return sessionDefinition;
    }

    public void setSessionDefinition(ServiceSessionDefinition sessionDefinition) {
        this.sessionDefinition = sessionDefinition;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
    
    protected boolean isStopSignalReceived() {
        return stopSingal;
    }

}
