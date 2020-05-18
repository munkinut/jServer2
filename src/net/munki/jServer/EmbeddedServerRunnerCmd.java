/*
 * TestEmbeddedServer.java
 *
 * Created on 18 September 2003, 15:11
 */

package net.munki.jServer;

/**
 * @author Warren Milburn
 */
public class EmbeddedServerRunnerCmd {

    /**
     * Creates a new instance of TestEmbeddedServer
     */
    public EmbeddedServerRunnerCmd() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] services = new String[args.length];
        String servicesLoc = "net.munki.jServer.services";
        int index = 0;
        for(String arg:args) {
            String file = servicesLoc + "." + arg;
            services[index] = file;
            System.out.println(services[index]);
            index++;
        }
        EmbeddedServer es = new EmbeddedServer(12321, services);
        es.start();
    }

}
