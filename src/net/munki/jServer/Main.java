package net.munki.jServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {

    public static void main(String[] args) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name =
                new ObjectName("net.munki.jServer:type=EmbeddedScriptServerRunner");

        EmbeddedScriptServerRunner mxbean = new EmbeddedScriptServerRunner();

        mbs.registerMBean(mxbean, name);

        System.out.println("Waiting...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
