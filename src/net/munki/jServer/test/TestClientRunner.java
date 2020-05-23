package net.munki.jServer.test;

import net.munki.jServer.property.PropertyManager;

public class TestClientRunner {

    public static void main(String[] args) {

        PropertyManager pm = PropertyManager.getInstance();

        int maxTestClients = pm.getMaxConnections();

        TestClient[] testClients = new TestClient[maxTestClients];

        for (int i = 0; i < maxTestClients; i++) {
            TestClient tc = new TestClient("Thread " + i);
            testClients[i] = tc;
            tc.t.start();
         }

        for (int i = 0; i < maxTestClients; i++) {
            TestClient tc = testClients[i];
            System.out.println("Thread " + i + " is alive: " + tc.t.isAlive());
        }

        for (int i = 0; i < maxTestClients; i++) {
            try {
                TestClient tc = testClients[i];
                tc.t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < maxTestClients; i++) {
            TestClient tc = testClients[i];
            System.out.println("Thread " + i + " is alive: " + tc.t.isAlive());
        }

        System.out.println("Main thread exiting.");
    }

}
