package net.munki.jServer;

import net.munki.jServer.listener.ListenerManager;
import net.munki.jServer.listener.ListenerManagerException;
import net.munki.jServer.service.ScriptService;

import java.util.logging.Logger;

public class EmbeddedScriptServer {

    private int port;
    private final ListenerManager LM;
    private final Logger logger;

    public EmbeddedScriptServer(int port, ScriptService service) {
        this.logger = Logger.getLogger(this.getClass().getName());
        LM = new ListenerManager();
        this.port = port;
        addService(service);
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
        logger.info("start() called.");
        LM.start();
    }

    private int nextPort() {
        int returnPort = port;
        logger.info("Port is " + port);
        port++;
        return returnPort;
    }

    public void stop() {
        // TODO nothing for now
    }
}
