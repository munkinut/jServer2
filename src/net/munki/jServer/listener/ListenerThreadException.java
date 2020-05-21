package net.munki.jServer.listener;

/*
 * Created on 22-May-2003
 *
 */

/**
 * @author Warren Milburn
 */
public class ListenerThreadException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -300066622399130273L;

    public ListenerThreadException() {
        super();
    }

    public ListenerThreadException(String arg0) {
        super(arg0);
    }

    public ListenerThreadException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ListenerThreadException(Throwable arg0) {
        super(arg0);
    }

}
