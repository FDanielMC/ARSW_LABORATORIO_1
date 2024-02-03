/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.math;

/**
 *
 * @author moren
 */
public class CalculatorThread extends Thread {
    
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private final Object lock;
    private int start;
    private int count;
    private int state;
    private byte[] values;
    private int numberDigits;

    public CalculatorThread(int start, int count, Object lock) {
        this.start = start;
        this.count = count;
        this.values = new byte[count];
        this.lock = lock;
        this.state = 0;
        this.numberDigits = 0;
    }
    
    @Override
    public void run(){
        double sum = 0;
        for (int i = 0; i < count; i++) {
            try {
                stopCurrentThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }
            sum = 16 * (sum - Math.floor(sum));
            values[i] = (byte) sum;
            numberDigits++;
        }
        state = 2;    
    }
    
    public byte[] getValues(){
        return this.values;
    }
    
    public void setValues(byte[] values){
        this.values = values;
    }
    
    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public byte[] getDigits() {
        return values;
    }
    
    /**
     * Method that is executed at each iteration of the for loop, it asks if the
     * thread's state variable is 1 (waiting),if this happens it prints out the 
     * number of numbers calculated so far by the thread and sleeps it with lock.wait()
     * @throws InterruptedException 
     */
    private void stopCurrentThread() throws InterruptedException{
        synchronized (lock) {
            while (state == 1) {
                System.out.println("Cantidad de dígitos calculados por " + Thread.currentThread().getName() + ": " + numberDigits);
                lock.wait();
            }
        }
    }
    
    public void changeState(int state) {
        this.state = state;
    }
    
    public boolean isRunning() {
        return state == 0;
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }
}
