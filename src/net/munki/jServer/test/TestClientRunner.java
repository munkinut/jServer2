package net.munki.jServer.test;

public class TestClientRunner {

    public static void main(String[] args) {
        TestClient nm1 = new TestClient("One");
        TestClient nm2 = new TestClient("Two");
        TestClient nm3 = new TestClient("Three");

        nm1.t.start();
        nm2.t.start();
        nm3.t.start();

        System.out.println("Thread One is alive: " + nm1.t.isAlive());
        System.out.println("Thread two is alive: " + nm2.t.isAlive());
        System.out.println("Thread Three is alive: " + nm3.t.isAlive());

        try {
            System.out.println("Waiting for threads to finish.");
            nm1.t.join();
            nm2.t.join();
            nm3.t.join();
        }
        catch (InterruptedException e) {
            System.out.println("Main thread was interrupted.");
        }

        System.out.println("Thread One is alive: " + nm1.t.isAlive());
        System.out.println("Thread two is alive: " + nm2.t.isAlive());
        System.out.println("Thread Three is alive: " + nm3.t.isAlive());

        System.out.println("Main thread exiting.");
    }

}
