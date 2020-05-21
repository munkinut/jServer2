package net.munki.jServer.listener;

/*
 * ListenerManager.java
 *
 * Created on 21 May 2003, 15:39
 */

import net.munki.jServer.service.ServiceListenerInterface;
import net.munki.jServer.service.ScriptService;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.logging.Logger;

@SuppressWarnings("SynchronizeOnNonFinalField")
public class ListenerManager extends Thread {

    private final Hashtable listeners;
    private Boolean running;
    private Logger logger;

    public ListenerManager() {
        listeners = new Hashtable();
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
            java.util.Enumeration keys = listeners.keys();
            while (keys.hasMoreElements()) {
                Integer key = (Integer) keys.nextElement();
                ListenerThread lt = (ListenerThread) listeners.get(key);
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
            ListenerThreadInterface lt = (ListenerThreadInterface) listeners.remove(port);
            lt.kill();
        }
    }

    private ScriptService loadService(String serviceName) throws ListenerManagerException {
        logger.info("Loading service " + serviceName + "...");

        ScriptService si;
        if (serviceName.startsWith("net.munki.jServer.services")) {
            try {
                Class c = Class.forName(serviceName);
                si = (ScriptService) c.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException cnfe) {
                logger.warning("Failed to load service " + serviceName + " ...");
                throw new ListenerManagerException("The service class " + serviceName + ".class could not be found.", cnfe);
            } catch (IllegalAccessException | InstantiationException iae) {
                logger.warning("Failed to load service " + serviceName + " ...");
                throw new ListenerManagerException("The service class " + serviceName + ".class could not be instantiated.", iae);
            } catch (NoSuchMethodException nme) {
                logger.warning("Failed to load service " + serviceName + " ...");
                throw new ListenerManagerException("The service class " + serviceName + ".class could not be instantiated.", nme);
            } catch (InvocationTargetException ite) {
                logger.warning("Failed to load service " + serviceName + " ...");
                throw new ListenerManagerException("The service class " + serviceName + ".class could not be instantiated.", ite);
            }
        }
        else {
            si = null;
        }


        return si;
    }

    private void killListeners() {
        logger.info("Killing listeners ...");
        synchronized (listeners) {
            java.util.Enumeration keys = listeners.keys();
            while (keys.hasMoreElements()) {
                Integer key = (Integer) keys.nextElement();
                ListenerThread lt = (ListenerThread) listeners.get(key);
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
