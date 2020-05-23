package net.munki.jServer;

import net.munki.jServer.listener.ListenerManager;
import net.munki.jServer.listener.ListenerManagerException;
import net.munki.jServer.service.ScriptService;

import java.util.logging.Logger;

public class EmbeddedScriptServer {

    private int port;
    private ListenerManager LM = null;
    private Logger logger;

    public EmbeddedScriptServer() {
    }

    private void addService(ScriptService service) {
        logger.info("Adding service : "  + service);
        try {
            LM.addListener(nextPort(), service, null, null);
        } catch (ListenerManagerException lme) {
            logger.warning(lme.getMessage());
        }
    }

    public void start() {
    }

    private int nextPort() {
        int returnPort = port;
        logger.info("Port is " + port);
        port++;
        return returnPort;
    }

    public void stop() {
        if (LM != null) LM.kill();
        try {
            LM.join(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(int port, ScriptService service) {
        logger = Logger.getLogger(this.getClass().getName());
        logger.info("start() called.");
        LM = new ListenerManager();
        this.port = port;
        addService(service);
        LM.start();
    }
}
