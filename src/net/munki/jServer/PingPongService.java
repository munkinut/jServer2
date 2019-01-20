package net.munki.jServer;

/*
 * PingPongService.java
 *
 * Created on 20 May 2003, 18:36
 */

/**
 *
 * @author  Warren Milburn
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.util.logging.Logger;

public class PingPongService implements ServiceInterface {
    
    private Logger logger;
	@SuppressWarnings("unused")
	private PrintStream out;
    
    public PingPongService() {
        initLogging();
    }
    
    private void initLogging() {
        logger = Logger.getLogger(this.getClass().getName());
    }
    
    public void serve(java.io.InputStream i, java.io.OutputStream o) {
        logger.info("Service " + getServiceName() + " running ...");
        BufferedReader br = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(new InputStreamReader(i));
            bw = new BufferedWriter(new OutputStreamWriter(o));
            pw = new PrintWriter(bw);
            pw.println("Connected to " + getServiceName() + " ...");
            pw.flush();
            String s = null;
            while ((s = br.readLine()) != null) {
                if (s.equals("PING")) {
                    pw.println("PONG");
                    pw.flush();
                    break;
                }
            }
            pw.println("Disconnecting from " + getServiceName() + " ...");
            pw.flush();
            Thread.sleep(1000);
        }
        catch (InterruptedException ie) {
            logger.warning(ie.toString());
        }
        catch (IOException ioe) {
            logger.warning(ioe.toString());
        }
        finally {
            try {
                if (pw != null) pw.close();
                if (bw != null) bw.close();
                if (br != null) br.close();
                logger.info("Service " + getServiceName() + " finished ...");
            }
            catch (IOException ioe) {
                logger.warning(ioe.toString());
            }
        }
    }

    public String getServiceName() {
        return "Ping Pong Service";
    }
    
    public String getServiceDescription() {
        return "Returns a PONG for a PING.";
    }
    
    public void setOutput(PrintStream ps) {
        out = ps;
    }
    
    public void addServiceListener(ServiceListenerInterface sli) {
    }
    
}
