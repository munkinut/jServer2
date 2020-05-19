/*
 * TestEmbeddedServer.java
 *
 * Created on 18 September 2003, 15:11
 */

package net.munki.jServer;

import net.munki.jServer.services.ScriptService;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Warren Milburn
 */
public class EmbeddedScriptServerRunner {

    private static String scriptLocation = "scripts";
    /**
     * Creates a new instance of TestEmbeddedServer
     */
    public EmbeddedScriptServerRunner() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] scripts = loadScripts();
        ScriptService[] services = getServices(scripts);
        EmbeddedScriptServer es = new EmbeddedScriptServer(12321, services);
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

    private static ScriptService[] getServices(String[] scripts) {
        ScriptService[] services = new ScriptService[scripts.length];
        int index = 0;
        for(String script:scripts) {
            ScriptService s = new ScriptService();
            s.setScriptName(script);
            services[index] = s;
            index++;
        }
        return services;
    }

}
