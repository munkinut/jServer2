/*
 * TestEmbeddedServer.java
 *
 * Created on 18 September 2003, 15:11
 */

package net.munki.jServer;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Warren Milburn
 */
public class EmbeddedServerScriptRunner {

    private static String scriptLocation = "scripts";
    /**
     * Creates a new instance of TestEmbeddedServer
     */
    public EmbeddedServerScriptRunner() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] scripts = loadScripts();
        String[] services = getServices(scripts);
        EmbeddedServer es = new EmbeddedServer(12321, services);
        es.start();
    }

    private static String[] loadScripts() {
        File dir = new File(scriptLocation);
        File[] files = dir.listFiles();
        String[] filenames = new String[files.length];
        int index = 0;
        for(File file: files) {
            String filename = file.getName();
            filenames[index] = filename;
            index++;
        }
        return filenames;
    }

    private static String[] getServices(String[] args) {
        String[] services = new String[args.length];
        String servicesLoc = scriptLocation;
        int index = 0;
        for(String arg:args) {
            String file = servicesLoc + "\\" + arg;
            services[index] = file;
            index++;
        }
        return services;
    }

}
