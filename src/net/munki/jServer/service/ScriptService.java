package net.munki.jServer.service;

/*
 * ScriptService.java
 *
 * Created on 18 May 2020, 21:57
 */

import net.munki.jServer.property.PropertyManager;
import net.munki.jServer.script.ScriptHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
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

        String commandLine = "";
        String cmd = "";
        ArrayList<String> paramList = null;

        try {
            commandLine = inbound.readLine();
            StringTokenizer st = new StringTokenizer(commandLine);
            paramList = new ArrayList<>();
            while (st.hasMoreTokens()) {
                paramList.add(st.nextToken());
            }
            if (!paramList.isEmpty()) {
                cmd = paramList.remove(0);
            }

            log.info("Read line. Command was " + commandLine);
            pw.println("Connecting to " + cmd + " ...");
            pw.flush();
        }
        catch (IOException e) {
            log.warning("Could not read line from client: " + e.getMessage());
        }

        String[] params = new String[paramList.size()];
        params = paramList.toArray(params);

        sh.handleScript(cmd, "Could be anything!!", i, o, params);

        pw.println("Disconnecting from " + cmd + " ...");
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
