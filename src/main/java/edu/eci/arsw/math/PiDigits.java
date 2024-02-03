package edu.eci.arsw.math;

///  <summary>

import java.io.ByteArrayOutputStream;
import java.io.IOException;

///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

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
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CalculatorThread[] calculatorThreads = createCalculatorThreads(start, count, n);
        for (CalculatorThread calculatorThread : calculatorThreads) {
            calculatorThread.start();
            
            outputStream.write(calculatorThread.getValues());
        }
        byte[] digits = outputStream.toByteArray();
        return digits;
    }

    public static CalculatorThread[] createCalculatorThreads(int start, int count, int numberOfThreads) {

        CalculatorThread[] calculatorThreads = new CalculatorThread[numberOfThreads];
        int threadElements = count / numberOfThreads;
        for (int threadNumber = 0; threadNumber < numberOfThreads; threadNumber++) {
            int firstIndex = threadNumber * threadElements;
            int lastIndex;
            if (threadNumber == numberOfThreads - 1) {
                lastIndex = count - 1;
            } else {
                lastIndex = firstIndex + threadElements - 1;
            }
            calculatorThreads[threadNumber] = new CalculatorThread(firstIndex, lastIndex+1, new byte[lastIndex-firstIndex]);
        }
        return calculatorThreads;
    }
}
