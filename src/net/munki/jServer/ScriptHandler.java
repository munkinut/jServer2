package net.munki.jServer;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.*;
import java.util.Objects;
import java.util.logging.Logger;

public class ScriptHandler {

    final Logger log = Logger.getLogger(this.getClass().getName());

    private final String scriptPath;

    // For Groovy scripts
    GroovyScriptEngine gse;
    //Binding binding;

    /** Creates new ScriptHandler */
    public ScriptHandler() {
        log.info("ScriptHandler() called");
        PropertyManager pm = PropertyManager.getInstance();
        scriptPath = pm.getScriptsLocation();
        log.info("scriptPath = " + scriptPath);
    }

    public void handleScript(String name, String description, InputStream is, OutputStream os) {
        log.info("Script name comes in as : " + name);
        String command = name;
        if(isGroovyScript(command)) {
            String scriptName = this.pathToScript(command);
            if (!(command.endsWith(".groovy")))
                command = command + ".groovy";
            ScriptResource scriptResource = new ScriptResource(name, description, is, os);
            String[] roots = new String[]{scriptPath};
            try {
                gse = new GroovyScriptEngine(roots);
            } catch (IOException e) {
                log.warning("IOException thrown : " + e.getMessage());
            }
            Binding binding = new Binding();
            binding.setProperty("scriptResource", scriptResource);
            try {
                gse.run(command, binding);
            } catch (ResourceException e) {
                log.warning("ResourceException thrown : " + e.getMessage());
                e.printStackTrace();
            } catch (ScriptException e) {
                log.warning("ScriptException thrown : " + e.getMessage());
            }
        }
        else {
            log.warning("Script was not Groovy.");
        }
    }

    private boolean isGroovyScript(String command) {
        File scriptDir = new File(scriptPath);
        File[] scriptFiles = scriptDir.listFiles();
        boolean success = false;
        for (File scriptFile : Objects.requireNonNull(scriptFiles)) {
            String filename = scriptFile.getName();
            log.info("Script filename = " + filename);
            if(filename.startsWith(command)) {
                log.info("filename starts with " + command);
                success = true;
                break;
            }
        }
        return success;
    }

    private String pathToScript(String command) {

        String totalPath = command;
        log.info("Looking for script at " + totalPath);
        return totalPath;
    }

}
