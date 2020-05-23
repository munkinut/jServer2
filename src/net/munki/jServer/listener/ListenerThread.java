package net.munki.jServer.listener;

/*
 * ListenerThread.java
 *
 * Created on 19 May 2003, 16:03
 */

import net.munki.jServer.connection.ConnectionThread;
import net.munki.jServer.property.PropertyManager;
import net.munki.jServer.service.ScriptService;
import net.munki.util.string.StringTool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ListenerThread extends Thread implements ListenerThreadInterface {

    private static final PropertyManager pm = PropertyManager.getInstance();

    private int connectionCount;

    private ServerSocket socket;
    private int myPort;
    private CopyOnWriteArrayList<ConnectionThread> connections;
    // TODO Check if service needs some thread safety
    private ScriptService service;
    private AtomicBoolean running = new AtomicBoolean(false);
    private Logger logger;

    public ListenerThread(ScriptService service) throws ListenerThreadException {
        initLogging();
        initConnectionCount();
        initSocket(pm.getDefaultPort());
        initConnectionManagement();
        initService(service);
    }

    public ListenerThread(int port, ScriptService service) throws ListenerThreadException {
        initLogging();
        initConnectionCount();
        initSocket(port);
        initConnectionManagement();
        initService(service);
    }

    private void initLogging() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    private void initSocket(int port) throws ListenerThreadException {
        myPort = port;
        try {
            logger.info("Initialising socket on port " + port);
            socket = new ServerSocket(port);
            socket.setSoTimeout(pm.getTimeout());
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
            }
            cleanup();
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
                running.set(true);
                logger.info("Running set to true ...");
        } else {
                running.set(false);
                logger.info("Running set to false ...");
        }
    }

    private boolean isRunning() {
        return running.get();
    }

    private void startService(Socket client) {
        if (connectionsAvailable()) {
            logger.info("Connections are available...");
            logger.info(StringTool.cat(new String[]{
                    "Connection starting for ",
                    client.getInetAddress().getHostAddress(),
                    " ..."
            }));
            ConnectionThread connection;
            synchronized (service) {
                connection = new ConnectionThread(client, service);
            }
            addConnection(connection);
            connection.start();
        } else {
            logger.warning("There are no more connections available...");
        }
    }

    private synchronized boolean connectionsAvailable() {
        return connectionCount < pm.getMaxConnections();
    }

    private synchronized void addConnection(ConnectionThread ct) {
        logger.info("Adding connection to list...");
        if (connections.add(ct)) incrementConnectionCount();
    }

    private synchronized void removeConnection(ConnectionThread ct) {
        logger.info("Removing connection from list...");
        if (connections.remove(ct))  decrementConnectionCount();
    }

    private synchronized void incrementConnectionCount() {
        connectionCount++;
    }

    private synchronized void decrementConnectionCount() {
        connectionCount--;
    }

    private synchronized void cleanup() {
        logger.info("Cleaning up connections ...");
            for (int i = 0; i < connections.size(); i++) {
                ConnectionThread c = connections.get(i);
                if (!c.isAlive()) {
                    removeConnection(c);
                    c.kill();
                    try {
                        c.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

    }

    public synchronized void kill() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setRunning(false);
        //  this interrupt doesn't do anything because accept() is blocking
        //  and does not throw an InterruptedException
        //interrupt();
    }

}
