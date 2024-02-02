package edu.eci.arsw.threads;

/**
 * Class extending Thread
 * @author Daniel Fernando Moreno Cerón
 * @author Daniel Esteban Pérez Bohorquez
 * @author Juan Felipe Vivas Manrique
 * @author Sergio Daniel Lopez Vargas
 */
public class CountThread extends Thread {
    
    private final int initialValue;
    private final int endValue;

    /**
     * Constructor in charge of initialising the initial and final values.
     * @param initialValue
     * @param endValue 
     */
    public CountThread(int initialValue, int endValue) {
        this.initialValue = initialValue;
        this.endValue = endValue;
    }
    
    /**
     * Each time a CountThread thread executes it iterates over an interval of
     * initialValue and endValue, at each iteration it prints a number according
     * to the given interval.
     */
    @Override
    public void run(){
        for(int i = initialValue; i <= endValue; i++){
            System.out.println(i);
        }
    }
}
