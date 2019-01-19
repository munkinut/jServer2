package net.munki.jServer;

/*
 * ListenerManager.java
 *
 * Created on 21 May 2003, 15:39
 */

/**
 *
 * @author  Warren Milburn
 */

import java.util.Hashtable;
// import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

public class ListenerManager extends Thread {
    
    @SuppressWarnings("rawtypes")
	private Hashtable listeners;
    private Boolean running;
    private Logger logger;
    
    @SuppressWarnings("rawtypes")
	public ListenerManager() {
        listeners = new Hashtable();
        running = new Boolean(false);
        initLogging();
    }
    
    private void initLogging() {
        logger = Logger.getLogger(this.getClass().getName());
    }
    
    public void run() {
        setRunning(true);
        logger.fine("Listener manager running ...");
        while (isRunning()) {
            skimListeners();
            synchronized (this) {
                try {
                    logger.fine("Listener manager waiting ...");
                    this.wait();
                }
                catch (InterruptedException ie) {
                    logger.fine("Listener manager interrupted ...");
                }
            }
        }
    }
    
    @SuppressWarnings("rawtypes")
	private void skimListeners() {
        logger.fine("Skimming listeners ...");
        synchronized (listeners) {
            java.util.Enumeration keys = listeners.keys();
            while (keys.hasMoreElements()) {
                Integer key = (Integer)keys.nextElement();
                ListenerThread lt = (ListenerThread)listeners.get(key);
                if (!lt.isAlive()) {
                    listeners.remove(key);
                    logger.info("Listener thread on port " + key.intValue() + " removed ...");
                }
            }
        }
    }
    
    private void setRunning(boolean run) {
        if (run) {
            synchronized (running) {
                running = Boolean.TRUE;
                logger.fine("Running set to true ...");
            }
        }
        else {
            synchronized (running) {
                running = Boolean.FALSE;
                logger.fine("Running set to false ...");
            }
        }
    }
    
    private boolean isRunning() {
        synchronized (running) {
            return running.equals(Boolean.TRUE);
        }
    }
    
    @SuppressWarnings("unchecked")
	public void addListener(int port, String serviceName, ServiceListenerInterface sli, PrintStream output) throws ListenerManagerException {
        try {
            logger.fine("Adding listener for on port " + port + " ...");
            ServiceInterface service = loadService(serviceName);
            if (output != null) service.setOutput(output);
            else service.setOutput(System.out);
            service.addServiceListener(sli);
            ListenerThread lt = new ListenerThread(port, service);
            synchronized (listeners) {
                listeners.put(new Integer(port), lt);
            }
            lt.start();
        }
        catch (ListenerThreadException ioe) {
            logger.warning("Failed to add listener for " + serviceName + "on port " + port + " ...");
            throw new ListenerManagerException("The listener thread for service " + serviceName + " on port " + port + " could not be started.", ioe);
        }
    }
    
    public void removeListener(int port) throws ListenerManagerException {
        logger.fine("Removing listener from port " + port + " ...");
        synchronized (listeners) {
            ListenerThreadInterface lt = (ListenerThreadInterface)listeners.remove(new Integer(port));
            lt.kill();
        }
    }
    
    @SuppressWarnings("rawtypes")
	private ServiceInterface loadService(String serviceName) throws ListenerManagerException {
        logger.fine("Loading service " + serviceName + "...");
        try {
            Class c = Class.forName(serviceName);
            ServiceInterface service = (ServiceInterface)c.newInstance();
            return service;
        }
        catch (ClassNotFoundException cnfe) {
            logger.warning("Failed to load service " + serviceName + " ...");
            throw new ListenerManagerException("The service class " + serviceName + ".class could not be found.", cnfe);
        }
        catch (IllegalAccessException iae) {
            logger.warning("Failed to load service " + serviceName + " ...");
            throw new ListenerManagerException("The service class " + serviceName + ".class could not be instantiated.", iae);
        }
        catch (InstantiationException ie) {
            logger.warning("Failed to load service " + serviceName + " ...");
            throw new ListenerManagerException("The service class " + serviceName + ".class could not be instantiated.", ie);
        }
    }
    
    @SuppressWarnings("rawtypes")
	private void killListeners() {
        logger.fine("Killing listeners ...");
        synchronized (listeners) {
            java.util.Enumeration keys = listeners.keys();
            while (keys.hasMoreElements()) {
                Integer key = (Integer)keys.nextElement();
                ListenerThread lt = (ListenerThread)listeners.get(key);
                lt.kill();
                listeners.remove(key);
                logger.info("Listener thread on port " + key.intValue() + " removed ...");
            }
        }
    }
    
    public synchronized void kill() {
        logger.info("Kill requested for ListenerManager ...");
        killListeners();
        setRunning(false);
        interrupt();
    }
    
}