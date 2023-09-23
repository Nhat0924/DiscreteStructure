import java.util.Scanner;
/**
 * COMP 3240 Spring 2023 - Programming Assignment 3
 * Author: Nhat Nguyen
 * Date: 4-4-2023
 */
public class PA3_Java {
   /* Multiplies matrix 1 by matrix 2
    * return a multiplication of 2 matrices
    */
   public static float[][] matrix_mult(float[][] mat1, float[][] mat2) {
      //Simple matrix multiplication using for loop

      //Initialization
      int r1 = mat1.length;
      int c1 = mat1[0].length;
      int r2 = mat2.length;
      int c2 = mat2[0].length;

      //Throwing exception for illegal matrix multiplication
      if (c1 != r2){
         throw new IllegalArgumentException("Illegal Matrix Multiplication");
      }

      //Multiplies matrix using nested for loop
      float[][] result = new float[r1][c2];
      for (int i = 0; i < r1; i++) {
         for (int j = 0; j < c2; j++) {
            for (int k = 0; k < r2; k++) {
               result[i][j] += mat1[i][k] * mat2[k][j];
            }
         }
      }

      return result;
   }
   
   /* Create the transition matrix from the given observation points
    */
   public static float[][] calc_transition_matrix(String observation_string) {
      //Catch error in string input and missing transitions
      observation_string = observation_string.toUpperCase();
      char[] data = observation_string.toCharArray();

      boolean[] all_transitions = new boolean[4];
      for (int i = 0; i < data.length-1; i++) {
         if (data[i] == 'D') {
            if (data[i+1] == 'R') {
               all_transitions[0] = true;
            }
            else if (data[i+1] == 'D') {
               all_transitions[1] = true;
            }
         }
         else if (data[i] == 'R') {
            if (data[i+1] == 'R') {
               all_transitions[2] = true;
            }
            else if (data[i+1] == 'D') {
               all_transitions[3] = true;
            }
         }
      } 

      for (boolean check : all_transitions) {
         if (!check) {
            throw new IllegalArgumentException("Missing transitions in the observation string.");
         }
      }

      //initialization
      float[][] transition_matrix = new float[2][2];
      float total_D = 0;
      float total_R = 0;
      float R_D = 0;
      float R_R = 0;
      float D_R = 0;
      float D_D = 0;
      
      // Find the total of D and R in a string
      for (int i = 0; i < data.length - 1; i++) {
         if (data[i] == 'D') {
            total_D++;
         }
      }
      total_R = data.length - total_D - 1;

      // Find the number of D to R, R to D, R to R, and D to D
      for (int i = 1; i < data.length; i++) {
         if (data[i] == 'D') {
            if (data[i - 1] == 'R') {
               R_D++;
            }
            else {
               D_D++;
            }
         }
         if (data[i] == 'R') {
            if (data[i-1] == 'R') {
               R_R++;
            }
            else {
               D_R++;
            } 
         }
      }

      //Adding values to the matrix (My apologies for this low effort approach)
      transition_matrix[0][0] = D_D / total_D;
      transition_matrix[0][1] = R_D / total_R;
      transition_matrix[1][0] = D_R / total_D;
      transition_matrix[1][1] = R_R / total_R;

      return transition_matrix;
   }
   
   /* Generates the forecast for the next 7 days given the transition matrix and the current weather
      The forecast should be a 7x2 matrix where each row is a forecast for a day
    */
   public static float[][] generate_forecast(float[][] transition_matrix, char curr_weather) {
      //Initialization 

      float[][] forecast = new float[7][2];
      float[][] case_D = new float[2][1];
      float[][] case_R = new float[2][1];

      case_R[0][0] = 0;
      case_R[1][0] = 1;

      case_D[0][0] = 1;
      case_D[1][0] = 0;

      float[][] exponent_matrix = transition_matrix;
      //Today's weather is D
      if (curr_weather == 'D') {
         //for each row in the forecast matrix
         for (int i = 0; i < 7; i++) {
         //multiply the transition matrix with case D
            float[][] mult = matrix_mult(exponent_matrix, case_D);
            for (int j = 0; j < 2; j++) {
               forecast[i][j] = mult[j][0];
            }
            //Increment the transition matrix exponentially by multiplying itself
            exponent_matrix = matrix_mult(exponent_matrix, transition_matrix);
         }
      }
      //Today's weather is R (calculate the same way)
      else {

         for (int i = 0; i < 7; i++) {
            float[][] mult = matrix_mult(exponent_matrix, case_R);
            for (int j = 0; j < 2; j++) {
               forecast[i][j] = mult[j][0];
            }
            exponent_matrix = matrix_mult(exponent_matrix, transition_matrix);
         }
      }
      
      return forecast;
   }
   
   /* Generates the climate prediction (i.e., steady state vector) given the transition matrix, current 
	  weather, and precision
    * I despise the way I wrote this method. It is not sustainable.
    */
   public static float[] generate_climate_prediction(float[][] transition_matrix, char curr_weather, float precision) {
      //Round 2: same thing but with a large size matrix for precision and output to an array size 2

      //catch error in precision
      if (precision > 0.1 || Float.isNaN(precision)) {
         throw new IllegalArgumentException("Precision must be a number less than .1.");
      }
      float resize = 10 / precision;
      //Initialization 
      float[] steady_state = new float[2];
      float[][] forecast = new float[(int) resize][2];
      float[][] case_D = new float[2][1];
      float[][] case_R = new float[2][1];

      case_R[0][0] = 0;
      case_R[1][0] = 1;

      case_D[0][0] = 1;
      case_D[1][0] = 0;

      float[][] exponent_matrix = transition_matrix;

      //Today's weather is D
      if (curr_weather == 'D') {
         //for each row in the forecast matrix
         for (int i = 0; i < forecast.length; i++) {
         //multiply the transition matrix with case D
            float[][] mult = matrix_mult(exponent_matrix, case_D);
            for (int j = 0; j < 2; j++) {
               forecast[i][j] = mult[j][0];
            }
            //Increment the transition matrix exponentially by multiplying itself
            exponent_matrix = matrix_mult(exponent_matrix, transition_matrix);
         }
      }
      //Today's weather is R (calculate the same way)
      else {

         for (int i = 0; i < forecast.length; i++) {
            float[][] mult = matrix_mult(exponent_matrix, case_R);
            for (int j = 0; j < 2; j++) {
               forecast[i][j] = mult[j][0];
            }
            exponent_matrix = matrix_mult(exponent_matrix, transition_matrix);
         }
      }

      //Loop through the forecast matrix and calculate with precision
      for (int i = 1; i < forecast.length; i++) {
         float difference1 = forecast[i][0] - forecast[i-1][0];
         float difference2 = forecast[i][1] - forecast[i-1][1];
         if (difference1 <= precision && difference2 <= precision) {
            steady_state[0] = forecast[i][0];
            steady_state[1] = forecast[i][1];
            break;
         }
      }

      return steady_state;
   }
   
   /* Print the forecasted weather predictions 
    */
   public static void print_predictions(float[][] forecast) {
      // Print first line
      System.out.println("[[" + forecast[0][0] + "," + forecast[0][1] + "],");
      
      // Print middle 5 lines
      for (int i = 1; i < forecast.length - 1; i++) {
         System.out.println(" [" + forecast[i][0] + "," + forecast[i][1] + "],");
      }
       
      // Print the last line
      System.out.println(" [" + forecast[6][0] + "," + forecast[6][1] + "]]");
   }
   
   /* Print the steady state vector containing the climate prediction
    */
   public static void print_steady_state(float[] steady_state) {
      System.out.println(steady_state[0]);
      System.out.println(steady_state[1]);
   }
   
   /* Main function
    */
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      System.out.print("Input forecast information: ");
      String observation = input.nextLine();
      System.out.print("Input precision: ");
      float precision = input.nextFloat();
      input.close();
      char last_char = observation.charAt(observation.length() - 1);
      float[][] transition_matrix = calc_transition_matrix(observation);
      float[][] generate_forecast = generate_forecast(transition_matrix, last_char);
      print_predictions(generate_forecast);
      float[] steadystate = generate_climate_prediction(transition_matrix, last_char, precision);
      print_steady_state(steadystate);

   }
}