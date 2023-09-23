/**
 * Programming Assignment 2
 * Author: Nhat Nguyen - nhn0001@auburn.edu
 * Date: 2/19/2023
 */

public class Assignment2_Java {
    /* This method accept the number to test and the maximum number of iterations
     * to try before halting execution. If num is NOT magic (or the maximum number
     * of iterations has been reached), return (-1 * num) (i.e., the negative of num).
     * If num IS magic, return the number of iterations it took to reduce num to 1.
     * int num can be reduced to 1 by dividing it by 2 if it is even or 
     * multiplying it by 3 and adding 1 if it is odd.
     */ 
    public static int IsMagic(int num, int max_iterations) {
        //check to see whether num is positive. If num is not positive, set a flag by returning -1.
        int reduced_iterations = max_iterations;
        if (num < 1) {
            return -1;
        }
        //iteration and calculation to reduce a number to one.
        while (num != 1) {
            if (num % 2 == 0) {
                num /= 2;
            }
            else if (num % 2 == 1) {
                num = (3 * num) + 1;
            }
            reduced_iterations--;
        }
        //check if the max iterations has been reached and return the negative of the number 
        //if it has been.
        if (reduced_iterations <= 0) {
            return num * (-1);
        }

        return max_iterations - reduced_iterations;
    }
    
    /* This method check if each number in the range [start, stop]
     * is a magic number. If all numbers in the range are magic, return the number of
     * iterations that it took to reduce the number passed into "stop" to 1. If it 
     * finds a number that is NOT magic, this method returns the negative of
     * that number.
     */
    public static int TestRange(int start, int stop, int max_iterations) {
        //iterate through the range of number between start and stop. Return the negative of the number 
        //if a number is not magic.
        for (int i = start; i <= stop; i++) {
            if (IsMagic(i, max_iterations) < 0) {
                return (-1)*i;
            }
        }
        
        return IsMagic(stop, max_iterations);
    }
    
    public static void main(String[] args) {
       int start = 30;
       int stop = 58;
       int max_iterations = 500;
       
       int result = TestRange(start, stop, max_iterations);
       System.out.println(result);
    }
 }