package net.munki.jServer;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.logging.Logger;

public class PropertyManager {

    final Logger log = Logger.getLogger(this.getClass().getName());

    /** Busy flag to aid in thread synchronization
     */
    private boolean busy;

    /** Properties object for the bot
     */
    private PropertiesConfiguration properties = null;

    private final FileBasedConfigurationBuilder<PropertiesConfiguration> builder;

    /** The singleton instance
     */
    private static PropertyManager propertyManager;

    /** Creates new PropertyManager */
    private PropertyManager() {
        log.info("PropertyManager() called");
        busy = false;
        Configurations configs = new Configurations();
        builder = configs.propertiesBuilder("config/jServer.properties");
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
        while (busy) {
            try {
                log.info("waiting");
                wait();
            }
            catch (InterruptedException ie) {
                log.warning("Thread interrupted " + ie.getMessage());
            }
        }
        busy = true;
        String scriptsLocation = properties.getString("Scripts_Location");
        busy = false;
        notifyAll();
        return scriptsLocation;
    }


}
