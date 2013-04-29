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
    private volatile ServiceSession serviceSession;
    private volatile RemoteServiceState state;
    private volatile boolean stopSingal;
    private ServiceManager serviceManager;
    private ProcessingTask processingTask;

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

    protected String getServiceName() {
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

            processingTask = new ProcessingTask();
            processingTask.start();

            if (state != RemoteServiceState.ERROR) {
                state = RemoteServiceState.RUNNING;
                serviceManager.reportServiceStatus(getSession().getId(), RemoteServiceState.RUNNING);
                LOG.info("Remote service: {} is up and running", serviceSession.getServiceName());
            }
        }
        else {
            throw new RemoteServiceException("Cannot start service: " + getServiceName() + ", expected state: "
                    + RemoteServiceState.WAITING_TO_STAT + ", but actual " + state);
        }
    }

    public void stop() {
        if (isStarted()) {
            if (isWorking()) {
                processingTask.stop();
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

    /**
     * Indicates if the service gets initialized or started.
     */
    protected boolean isStarted() {
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

    private class ProcessingTask implements Runnable {

        private RuntimeException error;
        private CountDownLatch processingThread;

        @Override
        public void run() {
            try {
                AbstractRemoteService.this.processing();
            }
            catch (RuntimeException e) {
                LOG.error("Error while running service: " + serviceSession.getServiceName(), e);
                error = e;
                AbstractRemoteService.this.state = RemoteServiceState.ERROR;
            }
            finally {
                LOG.info("Stop process thread of service: {}", serviceSession.getServiceName());
                processingThread.countDown();
            }
        }

        public RuntimeException getError() {
            return error;
        }

        public void stop() {
            stopSingal = true;
            try {
                processingThread.await();
            }
            catch (InterruptedException e) {
                LOG.warn("Awaiting of service stop was interupted: {}", serviceSession.getServiceName());
            }
        }

        public void start() {
            processingThread = new CountDownLatch(1);
            Executors.newSingleThreadExecutor().execute(this);
        }
    }
}
