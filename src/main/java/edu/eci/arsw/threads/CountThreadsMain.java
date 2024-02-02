package edu.eci.arsw.threads;

/**
 * Main class for programme execution
 * @author Daniel Fernando Moreno Cerón
 * @author Daniel Esteban Pérez Bohorquez
 * @author Juan Felipe Vivas Manrique
 * @author Sergio Daniel Lopez Vargas
 */
public class CountThreadsMain {

    /**
     * Main method in charge of running the program, creates three threads which
     * print numbers from interval A to B.
     * @param a 
     */
    public static void main(String a[]) {

        CountThread firstThread = new CountThread(0, 99);
        CountThread secondThread = new CountThread(99, 199);
        CountThread thirdThread = new CountThread(200, 299);

        System.out.println("Hilo principal: " + Thread.currentThread().getName());
        firstThread.start();
        secondThread.start();
        thirdThread.start();

    }

}
