package net.munki.jServer;

/*
 * Connection.java
 *
 * Created on 21 May 2003, 11:23
 */

/**
 *
 * @author  Warren Milburn
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
// import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionThread extends Thread implements ConnectionThreadInterface {
    
    private ServiceInterface service;
    private Socket client;
    private Boolean running;
    private java.util.logging.Logger logger;
    
    public ConnectionThread(Socket c, ServiceInterface s) {
        service = s;
        client = c;
        initLogging();
        initRunning();
    }
    
    private void initLogging() {
        logger = Logger.getLogger(this.getClass().getName());
    }
    
    private void initRunning() {
        running = Boolean.FALSE;
    }
    
    public void run() {
        setRunning(true);
        InputStream i = null;
        OutputStream o = null;
        if ((client != null) && (!client.isClosed())) {
            logger.info("Client " + client.getInetAddress().getHostAddress() + " connected ...");
            try {
                i = client.getInputStream();
                o = client.getOutputStream();
                service.serve(i, o);
            }
            catch (IOException ioe) {
                logger.warning(ioe.toString());
            }
            finally {
                try {
                    if (i != null) i.close();
                    if (o != null) o.close();
                    logger.info("Client " + client.getInetAddress().getHostAddress() + " disconnected ...");
                }
                catch (IOException ioe) {
                    logger.warning(ioe.toString());
                }
            }
        }
        setRunning(false);
        synchronized (this) {
            this.notifyAll();
        }
    }
    
    private void setRunning(boolean run) {
        if (run) {
            synchronized (running) {
                running = Boolean.TRUE;
            }
        }
        else {
            synchronized (running) {
                running = Boolean.FALSE;
            }
        }
    }
    
    public synchronized void kill() {
        String serviceName = "";
        if (service != null) serviceName = service.getServiceName();
        logger.info("Kill requested for " + serviceName + " connection ...");
        setRunning(false);
        interrupt();
    }
    
}
