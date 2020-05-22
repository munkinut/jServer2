package net.munki.jServer.test;

import net.munki.jServer.property.PropertyManager;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient implements Runnable {

    String name;
    Thread t;
    Socket socket;
    DataOutputStream outbound;
    BufferedReader inbound;

    PropertyManager pm;
    String host;
    int defaultPort;
    String command;

    TestClient(String threadName) {
        pm = PropertyManager.getInstance();
        host = pm.getHost();
        defaultPort = pm.getDefaultPort();
        command = pm.getCommand();
        name = threadName;
        t = new Thread(this, name);
        System.out.println("New Thread: " + t.getName());
    }

    @Override
    public void run() {
        try {

            // Get a connection to the server
            socket = new Socket(host, defaultPort);
            outbound = new DataOutputStream(socket.getOutputStream());
            inbound = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
            // Read a line
            String greeting = inbound.readLine() + "\n";
            System.out.println("Received: " + greeting);
            // Send a command
            outbound.write((command + "\n").getBytes());
            // Read lines until server disconnects
            String inboundMessage;
            while ((inboundMessage = inbound.readLine()) != null) {
                System.out.println(inboundMessage);
                Thread.sleep(100);
            }
            // Close our connection
            inbound.close();
            outbound.close();

        } catch (InterruptedException e) {
            System.out.println(name + " Interrupted: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println(name + " Can't find localhost: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(name + " An IO Error occured: " + e.getMessage());
        }
        System.out.println(name + " exiting.");
    }
}

