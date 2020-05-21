package net.munki.jServer.exception;

public class jServerException extends Throwable {
    public jServerException(String s) {
        super(s);
    }
    public jServerException(Throwable t) {
        super(t);
    }
}
