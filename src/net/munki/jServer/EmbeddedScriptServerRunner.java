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

    private static String scriptsLocation;
    /**
     * Creates a new instance of TestEmbeddedServer
     */
    public EmbeddedScriptServerRunner() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ScriptService service = new ScriptService();
        EmbeddedScriptServer es = new EmbeddedScriptServer(12321, service);
        es.start();
    }

}
