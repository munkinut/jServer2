package net.munki.jServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class ServiceWrapper implements ServiceInterface {

    // Load the script using Groovy or Beanshell
    // Pass it service name & description, the printstream,
    // the inputstream and the outputstream as a script
    // resource. serve()?

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public String getServiceDescription() {
        return null;
    }

    @Override
    public void setOutput(PrintStream ps) {

    }

    @Override
    public void serve(InputStream i, OutputStream o) {

    }

    @Override
    public void addServiceListener(ServiceListenerInterface sli) {

    }
}
