package net.munki.jServer.listener;

/*
 * ListenerThread.java
 *
 * Created on 19 May 2003, 16:03
 */

import groovy.transform.Synchronized;
import net.munki.jServer.connection.ConnectionThread;
import net.munki.jServer.service.ScriptService;
import net.munki.util.string.StringTool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ListenerThread extends Thread implements ListenerThreadInterface {

    public static final int TIMEOUT = 15000 * 1000;
    public static final int DEFAULT_PORT = 12321;
    public static final int MAX_CONNECTIONS = 3;

    private int connectionCount;

    private ServerSocket socket;
    private int myPort;
    private CopyOnWriteArrayList<ConnectionThread> connections;
    // TODO Check if service needs some thread safety
    private ScriptService service;
    // TODO Consider using Locks instead of synchronizing on running
    private Boolean running;
    private Logger logger;

    public ListenerThread(ScriptService service) throws ListenerThreadException {
        initLogging();
        initConnectionCount();
        initSocket(DEFAULT_PORT);
        initConnectionManagement();
        initService(service);
        initRunning();
    }

    public ListenerThread(int port, ScriptService service) throws ListenerThreadException {
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
            logger.info("Initialising socket on port " + port);
            socket = new ServerSocket(port);
            socket.setSoTimeout(TIMEOUT);
        } catch (IOException | SecurityException ioe) {
            throw new ListenerThreadException(ioe);
        }
    }

    private void initConnectionManagement() {
        connections = new CopyOnWriteArrayList<>();
    }

    private void initService(ScriptService s) {
        service = s;
    }

    private void initRunning() {
        running = Boolean.FALSE;
    }

    private void initConnectionCount() {
        connectionCount = 0;
    }

    public void run() {
        setRunning(true);
        Socket client = null;
        while (isRunning()) {
            String serviceName;
            synchronized (service) {
                serviceName = service.getServiceName();
            }
            logger.info(StringTool.cat(new String[]{
                    "Listening for connection to ",
                    serviceName,
                    " on port ",
                    Integer.toString(myPort),
                    " ..."
            }));
            try {
                synchronized (socket) {
                    client = socket.accept();
                }
                logger.info("Connection accepted ...");
                startService(client);
            } catch (SocketTimeoutException ste) {
                logger.info(ste.getMessage());
            } catch (IOException | SecurityException ioe) {
                logger.warning(ioe.getMessage());
            } finally {
                cleanup();
            }
        }
        try {
            if (client != null) client.close();
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        }
        setRunning(false);
        String serviceName;
        synchronized (service) {
            serviceName = service.getServiceName();
        }
        logger.info(StringTool.cat(new String[]{
                "No longer listening for connection to ",
                serviceName,
                " on port ",
                Integer.toString(myPort),
                " ..."
        }));
        synchronized (this) {
            this.notifyAll();
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

    private void startService(Socket client) {
        if (connectionsAvailable()) {
            logger.info(StringTool.cat(new String[]{
                    "Connection starting for ",
                    client.getInetAddress().getHostAddress(),
                    " ..."
            }));
            ConnectionThread connection;
            synchronized (service) {
                connection = new ConnectionThread(client, service);
            }
            connection.start();
            addConnection(connection);
        } else {
            logger.warning(StringTool.cat(new String[]{
                    "Connection from ",
                    client.getInetAddress().getHostAddress(),
                    " rejected.  Connection limit reached."
            }));
        }
    }

    private synchronized boolean connectionsAvailable() {
        return connectionCount < MAX_CONNECTIONS;
    }

    private void addConnection(ConnectionThread ct) {
        logger.info("Adding connection to list...");
        if (connections.add(ct)) incrementConnectionCount();
    }

    private void removeConnection(ConnectionThread ct) {
        logger.info("Removing connection from list...");
        if (connections.remove(ct))  decrementConnectionCount();
    }

    private synchronized void incrementConnectionCount() {
        connectionCount++;
    }

    private synchronized void decrementConnectionCount() {
        connectionCount--;
    }

    private void cleanup() {
        logger.info("Cleaning up connections ...");
            for (int i = 0; i < connections.size(); i++) {
                ConnectionThread c = connections.get(i);
                if (!c.isAlive()) {
                    removeConnection(c);
                }
            }

    }

    public synchronized void kill() {
        String serviceName = "";
        synchronized(service) {
            if (service != null) serviceName = service.getServiceName();
            logger.info(StringTool.cat(new String[]{
                    "Kill requested for ",
                    serviceName,
                    " ..."
            }));
        }
        setRunning(false);
        interrupt();
    }

}
