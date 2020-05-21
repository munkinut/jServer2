package net.munki.jServer.listener;

/*
 * ListenerManager.java
 *
 * Created on 21 May 2003, 15:39
 */

import net.munki.jServer.service.ServiceListenerInterface;
import net.munki.jServer.service.ScriptService;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.logging.Logger;

public class ListenerManager extends Thread {

    private final Hashtable<Integer, ListenerThread> listeners;
    private Boolean running;
    private Logger logger;

    public ListenerManager() {
        listeners = new Hashtable<>();
        running = false;
        initLogging();
    }

    private void initLogging() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public void run() {
        setRunning(true);
        logger.info("Listener manager running ...");
        while (isRunning()) {
            skimListeners();
            synchronized (this) {
                try {
                    logger.info("Listener manager waiting ...");
                    this.wait();
                } catch (InterruptedException ie) {
                    logger.info("Listener manager interrupted ...");
                }
            }
        }
    }

    private void skimListeners() {
        logger.info("Skimming listeners ...");
        synchronized (listeners) {
            java.util.Enumeration<Integer> keys = listeners.keys();
            while (keys.hasMoreElements()) {
                Integer key = keys.nextElement();
                ListenerThread lt = listeners.get(key);
                if (!lt.isAlive()) {
                    listeners.remove(key);
                    logger.info("Listener thread on port " + key + " removed ...");
                }
            }
        }
    }

    private void setRunning(boolean run) {
        if (run) {
            synchronized (running) {
                running = Boolean.TRUE;
                logger.info("Running set to true ...");
            }
        } else {
            synchronized (running) {
                running = Boolean.FALSE;
                logger.info("Running set to false ...");
            }
        }
    }

    private boolean isRunning() {
        synchronized (running) {
            return running.equals(Boolean.TRUE);
        }
    }

    public void addListener(int port, ScriptService service, ServiceListenerInterface sli, PrintStream output) throws ListenerManagerException {
        try {
            logger.info("Adding listener on port " + port + " ...");

            if (output != null) service.setOutput(output);
            else service.setOutput(System.out);
            service.addServiceListener(sli);
            ListenerThread lt = new ListenerThread(port, service);
            synchronized (listeners) {
                listeners.put(port, lt);
            }
            lt.start();
        } catch (ListenerThreadException ioe) {
            String serviceName = service.getServiceName();
            logger.warning("Failed to add listener for " + serviceName + "on port " + port + " ...");
            throw new ListenerManagerException("The listener thread for service " + serviceName + " on port " + port + " could not be started.", ioe);
        }
    }

    public void removeListener(int port) {
        logger.info("Removing listener from port " + port + " ...");
        synchronized (listeners) {
            ListenerThreadInterface lt = listeners.remove(port);
            lt.kill();
        }
    }

    private void killListeners() {
        logger.info("Killing listeners ...");
        synchronized (listeners) {
            java.util.Enumeration<Integer> keys = listeners.keys();
            while (keys.hasMoreElements()) {
                Integer key = keys.nextElement();
                ListenerThread lt = listeners.get(key);
                lt.kill();
                listeners.remove(key);
                logger.info("Listener thread on port " + key + " removed ...");
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
