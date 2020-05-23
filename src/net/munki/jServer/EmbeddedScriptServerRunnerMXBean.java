package net.munki.jServer;

public interface EmbeddedScriptServerRunnerMXBean {
    public void stop();
    public void start();
    public void quit();
    public boolean isStarted();
}
