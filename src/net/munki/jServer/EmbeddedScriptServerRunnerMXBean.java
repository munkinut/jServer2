package net.munki.jServer;

public interface EmbeddedScriptServerRunnerMXBean {
    public void stop();
    public void start();
    public boolean isStarted();
}
