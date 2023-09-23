import java.util.Random;

/*
 * COMP 3240 Spring 2023 Programming Assignment 4
 * Author: Nhat Nguyen
 * Date: 4-12-2023
 */

public class PA4_Java {   
   /* Perform one instance of the Monty Hall Problem
    *
    * should_switch: set to true if running an experiment where the contestant should
    *    switch their guess every time. Set to false if not
    *
    * Returns true if the contestant selected the door with the car behind it. Returns
    *    false otherwise.
    */
   public static boolean run_trial(boolean should_switch) {
      //Initialization
      Random rand = new Random();
      //Bound is from 0 to 2 so 3 doors, so car could be in any of the 3 doors
      int doors_with_car = rand.nextInt(3);
      //Choosing the first door randomly
      int chosen_doors = rand.nextInt(3);
      int check_doors;
      int switch_doors;

         if (should_switch) {
            do {
               //Generate a random door that has a goat
               check_doors = rand.nextInt(3);
               //This loop will find a door that is not a car or not the first door
               // so that the player can switch
            } while (check_doors == chosen_doors || check_doors == doors_with_car);
   
            do {
               //Generate a switch door
               switch_doors = rand.nextInt(3);
               //This loop will find a door that is not the first one being selected
               // or the one that has been selected before
            } while (switch_doors == check_doors || switch_doors == chosen_doors);
            
            //Return true if the contestant select the door with car
            return switch_doors == doors_with_car;
         }
         else {
            return doors_with_car == chosen_doors;
         }
   
}
   
   /* Execute an entire experiment (i.e., execute the specified number of trials)
    * and return the desired results
    * 
    * num_trials: number of trials to execute in this experiment
    * should_switch: set to true if running an experiment where the contestant should
    *    switch their guess every time. Set to false if not
    *
    * Returns the percentage of games won (i.e., number of wins / number of trials)
    */
   public static double run_experiment(int num_trials, boolean should_switch) {
      //Initialization
      double percentage = 0;

      //Loop through all of the trials
      for (int i = 0; i < num_trials; i++) {
         // Contestant should switch every time
         if (should_switch) {
            boolean switches = run_trial(should_switch);
            if (switches) {
               percentage++;
            }
         }
         //Contestant should stay every time
         else {
            boolean stayed = run_trial(should_switch);
            if (stayed) {
               percentage++;
            }
         }
      }

      return percentage/num_trials;

   }
   
   /* This is a stub that you can use to test the rest of the program. The code in this
    * method will not be executed during grading, so you don't need to worry about user
    * input.
    */
   public static void main(String[] args) {
      int num_trials = 1000;
      boolean should_switch = true;
      
      double prob = PA4_Java.run_experiment(num_trials, should_switch);
      System.out.println("Win by switching: " + prob);

      should_switch = false;

      double borp = PA4_Java.run_experiment(num_trials, should_switch);
      System.out.println("Win by staying: " + borp);
   }
}