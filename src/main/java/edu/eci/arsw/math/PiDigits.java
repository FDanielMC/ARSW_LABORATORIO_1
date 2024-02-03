package edu.eci.arsw.math;

///  <summary>

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Scanner;

///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {
    
    private static final Object lock = new Object();

    /**
     * Returns a range of hexadecimal digits of pi.
     *
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int n) throws IOException {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        if (n < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        
        CalculatorThread[] calculatorThreads = createCalculatorThreads(start, count, n);
        for (CalculatorThread calculatorThread : calculatorThreads) {
            calculatorThread.start();
        }
        
        stopCalculatorThreads(calculatorThreads);
        return consolidateDigits(calculatorThreads, count);
    }

    /**
     * Depending on the number of digits and threads, each thread is assigned 
     * which digit and how many digits it has to calculate.
     * @param start
     * @param count
     * @param numberOfThreads
     * @return CalculatorThread[]
     */
    public static CalculatorThread[] createCalculatorThreads(int start, int count, int numberOfThreads) {

        CalculatorThread[] calculatorThreads = new CalculatorThread[numberOfThreads];
        int threadElements = count / numberOfThreads;
        for (int threadNumber = 0; threadNumber < numberOfThreads; threadNumber++) {
            if (threadNumber == numberOfThreads - 1) {
                calculatorThreads[threadNumber] = new CalculatorThread(start + 
                        threadNumber * threadElements, threadElements, lock);
            } else {
                calculatorThreads[threadNumber] = new CalculatorThread(start + 
                        threadNumber * threadElements, count - threadNumber * threadElements
                , lock);
            }
        }
        return calculatorThreads;
    }
    
    /**
     * Stops the threads every 5 seconds and resumes them once the enter key
     * has been pressed.
     * @param calculatorThreads 
     */
    private static void stopCalculatorThreads(CalculatorThread[] calculatorThreads) {
        int numberOfThreads = calculatorThreads.length;
     
        while (calculatorThreads[numberOfThreads - 1].isRunning()) {
            try {
           
                Thread.sleep(5000);
             
                if (calculatorThreads[numberOfThreads - 1].isRunning()) {
                   
                    for (CalculatorThread calculatorThread : calculatorThreads) calculatorThread.changeState(1);
                    (new Scanner(System.in)).nextLine();
                   
                    for (CalculatorThread calculatorThread : calculatorThreads) calculatorThread.changeState(0);
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * With all the threads finished, the digits calculated for each one are 
     * obtained and consolidated in a single array of bytes.of bytes, this is 
     * done with their position within the array and the amount of digits that 
     * each thread had to calculate.
     * @param calculatorThreads
     * @param count
     * @return 
     */
    private static byte[] consolidateDigits(CalculatorThread[] calculatorThreads, int count) {
        int numberOfThreads = calculatorThreads.length;
        byte[] digits = new byte[count];
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                
                calculatorThreads[i].join();

                int initialIndex = i * (count / numberOfThreads);
             
                byte[] addDigits = calculatorThreads[i].getDigits();
                
                System.arraycopy(addDigits, 0, digits, initialIndex, addDigits.length);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        return digits;
    }
    
}
