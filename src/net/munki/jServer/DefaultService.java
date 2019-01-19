package net.munki.jServer;

/*
 * DefaultService.java
 *
 * Created on 20 May 2003, 18:36
 */

/**
 *
 * @author  Warren Milburn
 */

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
// import java.io.PrintStream;
import java.io.IOException;
import java.util.logging.Logger;

public class DefaultService implements ServiceInterface {
    
    private Logger logger;
    
    public DefaultService() {
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
            for (int j = 0; j < 20; j++) {
                pw.println(j);
                pw.flush();
                Thread.sleep(1000);
            }
            pw.println("Disconnecting from " + getServiceName() + " ...");
            pw.flush();
            Thread.sleep(1000);
        }
        catch (InterruptedException ie) {
            logger.warning(ie.toString());
        }
        finally {
            try {
                if (pw != null) pw.close();
                if (osw != null) osw.close();
                if (isr != null) isr.close();
                logger.info(getServiceName() + " finished ...");
            }
            catch (IOException ioe) {
                logger.warning(ioe.toString());
            }
        }
    }
    
    public String getServiceName() {
        return "Default Service";
    }
    
    public String getServiceDescription() {
        return "Demo service - counts from 0 to 19";
    }
    
    public void addServiceListener(ServiceListenerInterface sli) {
    }
    
    public void setOutput(java.io.PrintStream ps) {
    }
    
}
