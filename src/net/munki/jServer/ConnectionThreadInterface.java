package net.munki.jServer;

/*
 * Created on 23-May-2003
 *
 */

// import java.io.PrintStream;

/**
 * @author Warren Milburn
 *
 */
public interface ConnectionThreadInterface {
    public abstract void run();
    public abstract void kill();
}