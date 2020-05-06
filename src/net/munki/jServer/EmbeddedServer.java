package net.munki.jServer;

// import java.io.PrintStream;
// import java.io.File;

import java.util.logging.Logger;
// import net.munki.util.string.StringTool;

public class EmbeddedServer {

    private static final String DEFAULT_SERVICE = "net.munki.jServer.DefaultService";
    private static final int DEFAULT_PORT = 12321;

    private int port;
    private final ListenerManager LM;
    private final Logger logger;

    public EmbeddedServer() {
        this.logger = Logger.getLogger(this.getClass().getName());
        LM = new ListenerManager();
        port = DEFAULT_PORT;
        addServices(null);
    }

    public EmbeddedServer(int port) {
        this.logger = Logger.getLogger(this.getClass().getName());
        LM = new ListenerManager();
        this.port = port;
        addServices(null);
    }

    public EmbeddedServer(int port, String service) {
        this.logger = Logger.getLogger(this.getClass().getName());
        LM = new ListenerManager();
        this.port = port;
        String[] services = {service};
        addServices(services);
    }

    public EmbeddedServer(int port, String[] services) {
        this.logger = Logger.getLogger(this.getClass().getName());
        LM = new ListenerManager();
        this.port = port;
        addServices(services);
    }

    public void start() {
        LM.start();
//        try {
//            Thread.sleep(1000 * 60 * 60);
//        }
//        catch (InterruptedException ie) {
//            logger.warning(ie.getMessage());
//        }
//        LM.kill();
    }

    private void addServices(String[] services) {
        if ((services == null) || (services.length == 0))
            addSpecifiedService(DEFAULT_SERVICE);
        else {
            for (String service : services) {
                addSpecifiedService(service);
            }
        }
    }

    private void addSpecifiedService(String serviceName) {
        try {
            LM.addListener(nextPort(), serviceName, null, null);
        } catch (ListenerManagerException lme) {
            logger.warning(lme.getMessage());
        }
    }

    private int nextPort() {
        int returnPort = port;
        port++;
        return returnPort;
    }

}
