package net.munki.jServer.property;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class PropertyManager {

    final Logger log = Logger.getLogger(this.getClass().getName());

    // TODO PM could be called by a number of threads
    // therefore needs to synchronize busy. Consider
    // using Locks or Semaphores.
    /** Busy flag to aid in thread synchronization
     */
    private AtomicBoolean busy = new AtomicBoolean(false);

    /** Properties object for the bot
     */
    private PropertiesConfiguration properties = null;

    /** The singleton instance
     */
    private static PropertyManager propertyManager;

    /** Creates new PropertyManager */
    private PropertyManager() {
        log.info("PropertyManager() called");
        Configurations configs = new Configurations();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder("config/jServer.properties");
        try {
            properties = builder.getConfiguration();
        } catch (ConfigurationException e) {
            log.warning(e.getMessage());
        }
    }

    /** Provides the singleton instance
     * @return singleton instance of PropertyManager
     */
    public static synchronized PropertyManager getInstance() {
        if (propertyManager == null) propertyManager = new PropertyManager();
        return propertyManager;
    }

    public synchronized String getScriptsLocation() {
        log.info("getScriptsLocation() called");
        while (busy.get()) {
            try {
                log.info("waiting");
                wait();
            }
            catch (InterruptedException ie) {
                log.warning("Thread interrupted " + ie.getMessage());
            }
        }
        busy.set(true);
        String scriptsLocation = properties.getString("Scripts_Location");
        busy.set(false);
        notifyAll();
        return scriptsLocation;
    }

    public synchronized String getHost() {
        log.info("getHost() called");
        while (busy.get()) {
            try {
                log.info("waiting");
                wait();
            }
            catch (InterruptedException ie) {
                log.warning("Thread interrupted " + ie.getMessage());
            }
        }
        busy.set(true);
        String host = properties.getString("Host");
        busy.set(false);
        notifyAll();
        return host;
    }

    public synchronized String getCommand() {
        log.info("getCommand() called");
        while (busy.get()) {
            try {
                log.info("waiting");
                wait();
            }
            catch (InterruptedException ie) {
                log.warning("Thread interrupted " + ie.getMessage());
            }
        }
        busy.set(true);
        String command = properties.getString("Command");
        busy.set(false);
        notifyAll();
        return command;
    }

    public synchronized int getMaxConnections() {
        log.info("getMaxConnections() called");
        while (busy.get()) {
            try {
                log.info("waiting");
                wait();
            }
            catch (InterruptedException ie) {
                log.warning("Thread interrupted " + ie.getMessage());
            }
        }
        busy.set(true);
        int maxConnections = properties.getInt("Max_Connections");
        busy.set(false);
        notifyAll();
        return maxConnections;
    }

    public synchronized int getTimeout() {
        log.info("getTimeout() called");
        while (busy.get()) {
            try {
                log.info("waiting");
                wait();
            }
            catch (InterruptedException ie) {
                log.warning("Thread interrupted " + ie.getMessage());
            }
        }
        busy.set(true);
        int timeout = properties.getInt("Timeout");
        busy.set(false);
        notifyAll();
        return timeout;
    }

    public synchronized int getDefaultPort() {
        log.info("getDefaultPort() called");
        while (busy.get()) {
            try {
                log.info("waiting");
                wait();
            }
            catch (InterruptedException ie) {
                log.warning("Thread interrupted " + ie.getMessage());
            }
        }
        busy.set(true);
        int defaultPort = properties.getInt("Default_Port");
        busy.set(false);
        notifyAll();
        return defaultPort;
    }

}
