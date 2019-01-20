/*
 * TestEmbeddedServer.java
 *
 * Created on 18 September 2003, 15:11
 */

package net.munki.jServer;

/**
 *
 * @author  Warren Milburn
 */
public class EmbeddedServerRunner {
    
    /** Creates a new instance of TestEmbeddedServer */
    public EmbeddedServerRunner() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EmbeddedServer es = new EmbeddedServer();
        es.start();
    }
    
}
