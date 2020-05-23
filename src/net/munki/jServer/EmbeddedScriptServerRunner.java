/*
 * TestEmbeddedServer.java
 *
 * Created on 18 September 2003, 15:11
 */

package net.munki.jServer;

import net.munki.jServer.property.PropertyManager;
import net.munki.jServer.service.ScriptService;

/**
 * @author Warren Milburn
 */
public class EmbeddedScriptServerRunner implements EmbeddedScriptServerRunnerMXBean{

    private final PropertyManager pm;
    EmbeddedScriptServer es;
    boolean started = false;
    /**
     * Creates a new instance of TestEmbeddedServer
     */
    public EmbeddedScriptServerRunner() {
        pm = PropertyManager.getInstance();
    }

    @Override
    public void stop() {
        System.out.println("stop() called...");
            started = false;
             if (es != null) es.stop();
             else {
                 System.out.println("ES WAS NULL!!");
                 //es.stop();
             }
            //es = null;
            //System.exit(0);
    }

    @Override
    public void start() {
        System.out.println("start() called...");
        int port = pm.getDefaultPort();
        ScriptService service = new ScriptService();
        es = new EmbeddedScriptServer();
        started = true;
        es.start(port, service);
    }

    @Override
    public void quit() {
        stop();
        System.exit(0);
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}
