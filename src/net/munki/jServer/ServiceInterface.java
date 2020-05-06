package net.munki.jServer;

/*
 * Service.java
 *
 * Created on 20 May 2003, 13:50
 */

/**
 * @author Warren Milburn
 */

@SuppressWarnings("EmptyMethod")
public interface ServiceInterface {

    String getServiceName();

    String getServiceDescription();

    void setOutput(java.io.PrintStream ps);

    void serve(java.io.InputStream i, java.io.OutputStream o);

    @SuppressWarnings("EmptyMethod")
    void addServiceListener(ServiceListenerInterface sli);
}
