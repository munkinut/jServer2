package net.munki.jServer;

// import java.io.PrintStream;
// import java.io.File;

import net.munki.jServer.services.ScriptService;

import java.util.logging.Logger;
// import net.munki.util.string.StringTool;

public class EmbeddedScriptServer {

    private int port;
    private final ListenerManager LM;
    private final Logger logger;

    public EmbeddedScriptServer(int port, ScriptService[] services) {
        this.logger = Logger.getLogger(this.getClass().getName());
        LM = new ListenerManager();
        this.port = port;
        addServices(services);
    }

    private void addServices(ScriptService[] services) {
        logger.info("Adding services");
        for (ScriptService service : services) {
            addSpecifiedService(service);
            }
    }

    private void addSpecifiedService(ScriptService service) {
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

}
