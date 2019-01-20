package net.munki.jServer;

/*
 * Service.java
 *
 * Created on 20 May 2003, 13:50
 */

/**
 *
 * @author  Warren Milburn
 */

public interface ServiceInterface {
    
    public abstract String getServiceName();
    public abstract String getServiceDescription();
    public abstract void setOutput(java.io.PrintStream ps);
    public abstract void serve(java.io.InputStream i, java.io.OutputStream o);
    public abstract void addServiceListener(ServiceListenerInterface sli);
}
