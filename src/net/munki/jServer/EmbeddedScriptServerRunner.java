/*
 * TestEmbeddedServer.java
 *
 * Created on 18 September 2003, 15:11
 */

package net.munki.jServer;

import net.munki.jServer.service.ScriptService;

/**
 * @author Warren Milburn
 */
public class EmbeddedScriptServerRunner {

    private static final int DEFAULT_PORT = 12321;
    /**
     * Creates a new instance of TestEmbeddedServer
     */
    public EmbeddedScriptServerRunner() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port;
        if ((args != null) && (args.length > 0))
            port = Integer.parseInt(args[0]);
        else port = DEFAULT_PORT;
        ScriptService service = new ScriptService();
        EmbeddedScriptServer es = new EmbeddedScriptServer(port, service);
        es.start();
    }

}
