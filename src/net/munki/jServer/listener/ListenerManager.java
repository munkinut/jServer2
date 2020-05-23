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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ListenerManager extends Thread {

    private final ConcurrentHashMap<Integer, ListenerThread> listeners;
    private AtomicBoolean running = new AtomicBoolean(false);
    private Logger logger;

    public ListenerManager() {
        listeners = new ConcurrentHashMap<>();
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
                    this.wait(15000);
                } catch (InterruptedException ie) {
                    logger.info("Listener manager interrupted ...");
                }
            }
        }
        synchronized (this) {
            this.notifyAll();
        }
    }

    private void skimListeners() {
        logger.info("Skimming listeners ...");
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

    private void setRunning(boolean run) {
        synchronized (running) {
            running.set(run);
        }
    }

    private boolean isRunning() {
        synchronized (running) {
            return running.get();
        }
    }

    public void addListener(int port, ScriptService service, ServiceListenerInterface sli, PrintStream output) throws ListenerManagerException {
        try {
            logger.info("Adding listener on port " + port + " ...");

            if (output != null) service.setOutput(output);
            else service.setOutput(System.out);
            service.addServiceListener(sli);
            ListenerThread lt = new ListenerThread(port, service);
            listeners.put(port, lt);
            lt.start();
        } catch (ListenerThreadException ioe) {
            String serviceName = service.getServiceName();
            logger.warning("Failed to add listener for " + serviceName + " on port " + port + " ...");
            throw new ListenerManagerException("The listener thread for service " + serviceName + " on port " + port + " could not be started.", ioe);
        }
    }

    public void removeListener(int port) {
        logger.info("Removing listener from port " + port + " ...");
        ListenerThread lt = listeners.remove(port);
        lt.kill();
        try {
            lt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void killListeners() {
        logger.info("Killing listeners ...");
        java.util.Enumeration<Integer> keys = listeners.keys();
        while (keys.hasMoreElements()) {
            Integer key = keys.nextElement();
            ListenerThread lt = listeners.remove(key);
            lt.kill();
            logger.info("Listener thread on port " + key + " removed ...");
        }
   }

    public synchronized void kill() {
        logger.info("Kill requested for ListenerManager ...");
        killListeners();
        setRunning(false);
        // interrupt();
    }

}
