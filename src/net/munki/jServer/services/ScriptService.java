package net.munki.jServer.services;

/*
 * ScriptService.java
 *
 * Created on 18 May 2020, 21:57
 */

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import net.munki.jServer.*;

import java.io.*;
import java.util.Objects;
import java.util.logging.Logger;

// import java.io.PrintStream;

public class ScriptService {

    private Logger log;
    private String scriptName;
    private String scriptDescription;
    private final String scriptPath;

    // For Groovy scripts
    private GroovyScriptEngine gse;
    //Binding binding;

    private void initLogging() {
        log = Logger.getLogger(this.getClass().getName());
    }

    /** Creates new ScriptHandler */
    public ScriptService() {
        initLogging();
        log.info("ScriptService() called");
        PropertyManager pm = PropertyManager.getInstance();
        scriptPath = pm.getScriptsLocation();
        log.info("scriptPath = " + scriptPath);
    }

    public void serve(InputStream i, OutputStream o) {
        log.info(getServiceName() + " running ...");
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        PrintWriter pw = null;
        isr = new InputStreamReader(i);
        osw = new OutputStreamWriter(o);
        pw = new PrintWriter(new BufferedWriter(osw));
        pw.println("Connected to " + getServiceName() + " ...");
        pw.flush();

            // TODO Load a script and run it
        ScriptHandler sh = new ScriptHandler();

        sh.handleScript(scriptName, scriptDescription, i, o);

        pw.println("Disconnecting from " + getServiceName() + " ...");
        pw.flush();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            log.warning(ie.toString());
        } finally {
            try {
                if (pw != null) pw.close();
                if (osw != null) osw.close();
                if (isr != null) isr.close();
                log.info(getServiceName() + " finished ...");
            } catch (IOException ioe) {
                log.warning(ioe.toString());
            }
        }
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public void setScriptDescription(String scriptDescription) {
        this.scriptDescription = scriptDescription;
    }

    public String getServiceName() {
        return "Script Service";
    }

    public String getServiceDescription() {
        return "Script service - loads a script and runs it.";
    }

    public void addServiceListener(ServiceListenerInterface sli) {
    }

    public void setOutput(PrintStream ps) {
    }

}
