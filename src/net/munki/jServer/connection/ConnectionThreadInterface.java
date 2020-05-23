package net.munki.jServer.connection;

/*
 * Created on 23-May-2003
 *
 */

// import java.io.PrintStream;

/**
 * @author Warren Milburn
 */
public interface ConnectionThreadInterface {
    void run();

    void kill();
}