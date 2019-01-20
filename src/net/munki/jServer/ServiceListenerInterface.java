/*
 * ServiceListenerInterface.java
 *
 * Created on 18 September 2003, 19:57
 */

package net.munki.jServer;

/**
 *
 * @author  Warren Milburn
 */
public interface ServiceListenerInterface {
    
    public void notify(Object source, Object message);
}
