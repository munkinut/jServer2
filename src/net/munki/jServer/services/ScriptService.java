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

public class ScriptService {

    private Logger log;
    private final String scriptPath;
    private ScriptHandler sh;

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
        sh = new ScriptHandler();
    }

    public void serve(InputStream i, OutputStream o) {
        log.info(getServiceName() + " running ...");

        InputStreamReader isr = new InputStreamReader(i);
        OutputStreamWriter osw = new OutputStreamWriter(o);
        PrintWriter pw = new PrintWriter(new BufferedWriter(osw));
        BufferedReader inbound = new BufferedReader(isr);

        String command = "";
        try {
            command = inbound.readLine();
            log.info("Read line. Command was " + command);
            pw.println("Connected to " + command + " ...");
            pw.flush();
        }
        catch (IOException e) {
            log.warning("Could not read line from client: " + e.getMessage());
        }

        sh.handleScript(command, "Could be anything!!", i, o);

        pw.println("Disconnecting from " + getServiceName() + " ...");
        pw.flush();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            log.warning(ie.toString());
        } finally {
            try {
                if (inbound != null) inbound.close();
                if (pw != null) pw.close();
                if (osw != null) osw.close();
                if (isr != null) isr.close();
                log.info(getServiceName() + " finished ...");
            } catch (IOException ioe) {
                log.warning(ioe.toString());
            }
        }

    }


    public String getServiceName() {
        return this.getClass().getName();
    }

    public void addServiceListener(ServiceListenerInterface sli) {
    }

    public void setOutput(PrintStream ps) {
    }

}
