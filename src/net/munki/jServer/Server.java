package net.munki.jServer;

import jargs.gnu.CmdLineParser;

public class Server {
    
    private static final String DEFAULT_SERVICE = "net.munki.jServer.DefaultService";
    //private static final String DEFAULT_SERVICE = "net.munki.jServer.PingPongService";
    private static int port = 12321;

    private static final ListenerManager LM = new ListenerManager();
    
    public static void main(String[] args) {
        
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option help = parser.addBooleanOption('h', "help");
        CmdLineParser.Option startPort = parser.addIntegerOption('p', "port");
        CmdLineParser.Option service = parser.addStringOption('s', "service");
        
        try {
            parser.parse(args);
        }
        catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            usage();
            exit(1);
        }
        
        Boolean helpValue = (Boolean)parser.getOptionValue(help);
        help(helpValue);
        
        Integer startPortValue = (Integer)parser.getOptionValue(startPort);
        port(startPortValue);
        
        String serviceValue = (String)parser.getOptionValue(service);
        service(serviceValue);
        
    }
    
    private static void help(Boolean helpValue) {
        if ((helpValue != null) && (helpValue)) {
            usage();
            exit(0);
        }
    }
    
    private static void port(Integer startPortValue) {
        if (startPortValue != null) {
            int spv = startPortValue;
            if ((spv > 0) && (spv < 65536)) port = spv;
        }
    }
    
    private static void service(String serviceValue) {
        if (serviceValue == null) addDefaultService();
        else {
            String[] services = parseServices(serviceValue);
            for (String service : services) {
                addSpecifiedService(service);
            }
        }
        startServices();
    }
    
    private static void exit(int exitStatus) {
        System.exit(exitStatus);
    }
    
    private static void addDefaultService() {
        addSpecifiedService(DEFAULT_SERVICE);
    }
    
    private static void addSpecifiedService(String serviceName) {
        try {
            LM.addListener(nextPort(), serviceName, null, System.out);
        }
        catch (ListenerManagerException lme) {
            System.err.println(lme.getMessage());
            usage();
            exit(1);
        }
    }
    
    private static void startServices() {
        LM.start();
        try {
            Thread.sleep(1000 * 60 * 60);
        }
        catch (InterruptedException ie) {
            //
        }
        LM.kill();
    }
    
    private static int nextPort() {
        int returnPort = port;
        port++;
        return returnPort;
    }
    
    private static String[] parseServices(String serviceList) {
        String unpackedServiceList = serviceList.replaceAll(" ", "");
        java.util.StringTokenizer st = new java.util.StringTokenizer(unpackedServiceList, ",");
        String[] services = new String[st.countTokens()];
        int count = 0;
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            services[count] = s;
            count++;
        }
        return services;
    }
    
    private static void usage() {
        System.err.println("usage: java Server [{-h,--help}] [{-p,--port start_port}] [{-s,--service} service_name, service_name, ...]");
    }
    
}
