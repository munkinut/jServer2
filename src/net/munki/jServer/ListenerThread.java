package net.munki.jServer;

/*
 * ListenerThread.java
 *
 * Created on 19 May 2003, 16:03
 */

import java.io.IOException;
// import java.io.PrintStream;
// import java.io.File;
// import java.io.FileReader;
// import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;
// import java.util.regex.*;
import java.util.logging.Logger;
import net.munki.util.string.StringTool;

@SuppressWarnings("SynchronizeOnNonFinalField")
public class ListenerThread extends Thread implements ListenerThreadInterface {
    
    public static final int TIMEOUT = 15000;
    public static final int DEFAULT_PORT = 10000;
    public static final int MAX_CONNECTIONS = 5;
    
    private int connectionCount;
    
    private ServerSocket socket;
    private int myPort;
    @SuppressWarnings("rawtypes")
	private Vector connections;
    private ServiceInterface service;
    // private PrintStream out;
    private Boolean running;
    private Logger logger;
    
    public ListenerThread() throws ListenerThreadException {
        initLogging();
        initConnectionCount();
        initSocket(DEFAULT_PORT);
        initConnectionManagement();
        initService(new DefaultService());
        initRunning();
    }
    
    public ListenerThread(int port) throws ListenerThreadException {
        initLogging();
        initConnectionCount();
        initSocket(port);
        initConnectionManagement();
        initService(new DefaultService());
        initRunning();
    }
    
    public ListenerThread(ServiceInterface service) throws ListenerThreadException {
        initLogging();
        initConnectionCount();
        initSocket(DEFAULT_PORT);
        initConnectionManagement();
        initService(service);
        initRunning();
    }
    
    public ListenerThread(int port, ServiceInterface service) throws ListenerThreadException {
        initLogging();
        initConnectionCount();
        initSocket(port);
        initConnectionManagement();
        initService(service);
        initRunning();
    }
    
    private void initLogging() {
        logger = Logger.getLogger(this.getClass().getName());
    }
    
    private void initSocket(int port) throws ListenerThreadException {
        myPort = port;
        try {
        socket = new ServerSocket(port);
        socket.setSoTimeout(TIMEOUT);
        }
        catch (IOException | SecurityException ioe) {
            throw new ListenerThreadException(ioe);
        }
    }
    
    @SuppressWarnings("rawtypes")
	private void initConnectionManagement() {
        connections = new Vector();
    }
    
    private void initService(ServiceInterface s) {
        service = s;
    }
    
    private void initRunning() {
        running = Boolean.FALSE;
    }
    
    private void initConnectionCount() {
        connectionCount = 0;
    }
    
    @SuppressWarnings("unused")
	public void run() {
        setRunning(true);
        Socket client = null;
        while (isRunning()) {
            logger.info(StringTool.cat(new String[] {
                "Listening for connection to ",
                service.getServiceName(),
                " on port ",
                Integer.toString(myPort),
                " ..."
            }));
            try {
                synchronized (socket) {
                    client = socket.accept();
                }
                String clientAddr = client.getInetAddress().getHostAddress();
                logger.info("Connection accepted ...");
                startService(client);
            }
            catch (SocketTimeoutException ste) {
                logger.info(ste.getMessage());
            }
            catch (IOException | SecurityException ioe) {
                logger.warning(ioe.getMessage());
            } finally {
                cleanup();
            }
        }
        try {
            if (client != null) client.close();
        }
        catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        }
        setRunning(false);
        logger.info(StringTool.cat(new String[] {
            "No longer listening for connection to ",
            service.getServiceName(),
            " on port ",
            Integer.toString(myPort),
            " ..."
        }));
        synchronized (this) { this.notifyAll(); }
    }
    
    private void setRunning(boolean run) {
        if (run) {
            synchronized (running) {
                running = Boolean.TRUE;
                logger.info("Running set to true ...");
            }
        }
        else {
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
    
    private void startService(Socket client) {
        if (connectionsAvailable()) {
            logger.info(StringTool.cat(new String[] {
                "Connection starting for ",
                client.getInetAddress().getHostAddress(),
                " ..."
            }));
            ConnectionThread connection = new ConnectionThread(client, service);
            connection.start();
            addConnection(connection);
        }
        else {
            logger.warning(StringTool.cat(new String[] {
                "Connection from ",
                client.getInetAddress().getHostAddress(),
                " rejected.  Connection limit reached."
            }));
        }
    }
    
    private synchronized boolean connectionsAvailable() {
        return connectionCount < MAX_CONNECTIONS;
    }
    
    @SuppressWarnings("unchecked")
	private void addConnection(ConnectionThread ct) {
        logger.info("Adding connection to list...");
        synchronized (connections) {
            connections.add(ct);
        }
        incrementConnectionCount();
    }
    
    private void removeConnection(ConnectionThread ct) {
        logger.info("Removing connection from list...");
        synchronized (connections) {
            connections.remove(ct);
        }
        decrementConnectionCount();
    }
    
    private synchronized void incrementConnectionCount() {
        connectionCount++;
    }
    
    private synchronized void decrementConnectionCount() {
        connectionCount--;
    }
    
    private void cleanup() {
        logger.info("Cleaning up connections ...");
        synchronized (connections) {
            for (int i = 0; i < connections.size(); i++) {
                ConnectionThread c = (ConnectionThread)connections.elementAt(i);
                if (!c.isAlive()) {
                    removeConnection(c);
                }
            }
        }
    }
    
    public synchronized void kill() {
        String serviceName = "";
        if (service != null) serviceName = service.getServiceName();
        logger.info(StringTool.cat(new String[] {
            "Kill requested for ",
            serviceName,
            " ..."
        }));
        setRunning(false);
        interrupt();
    }
    
}
