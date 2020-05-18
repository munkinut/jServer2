package net.munki.jServer.services;

/*
 * DefaultService.java
 *
 * Created on 20 May 2003, 18:36
 */

import net.munki.jServer.ServiceInterface;
import net.munki.jServer.ServiceListenerInterface;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

// import java.io.PrintStream;

public class DateService implements ServiceInterface {

    private Logger logger;

    public DateService() {
        initLogging();
    }

    private void initLogging() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public void serve(java.io.InputStream i, java.io.OutputStream o) {
        logger.info(getServiceName() + " running ...");
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        PrintWriter pw = null;
        try {
            isr = new InputStreamReader(i);
            osw = new OutputStreamWriter(o);
            pw = new PrintWriter(new BufferedWriter(osw));
            pw.println("Connected to " + getServiceName() + " ...");
            pw.flush();
            pw.println(new GregorianCalendar().getTime().toString());
            pw.println("Disconnecting from " + getServiceName() + " ...");
            pw.flush();
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            logger.warning(ie.toString());
        } finally {
            try {
                if (pw != null) pw.close();
                if (osw != null) osw.close();
                if (isr != null) isr.close();
                logger.info(getServiceName() + " finished ...");
            } catch (IOException ioe) {
                logger.warning(ioe.toString());
            }
        }
    }

    public String getServiceName() {
        return "Default Service";
    }

    public String getServiceDescription() {
        return "Returns the date to the client.";
    }

    public void addServiceListener(ServiceListenerInterface sli) {
    }

    public void setOutput(java.io.PrintStream ps) {
    }

}
