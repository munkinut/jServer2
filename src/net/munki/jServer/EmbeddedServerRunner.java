/*
 * TestEmbeddedServer.java
 *
 * Created on 18 September 2003, 15:11
 */

package net.munki.jServer;

/**
 * @author Warren Milburn
 */
public class EmbeddedServerRunner {

    /**
     * Creates a new instance of TestEmbeddedServer
     */
    public EmbeddedServerRunner() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] services = {"net.munki.jServer.services.DefaultService",
                                "net.munki.jServer.services.DateService",
                                "net.munki.jServer.services.PingPongService"};
        EmbeddedServer es = new EmbeddedServer(12321, services);
        es.start();
    }

}
