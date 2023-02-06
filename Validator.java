// DO NOT DELETE THIS LINE!!!
package com.gradescope.validator;
// Nhat Nguyen 
// COMP 3240 Section 001 Programming Assignment 1
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Hashtable;

// Notes on how to use evaluator():
// Call the evaluator with Evaluator.evaluate(<premise>, <variable_dict>). "premise"
// is a single string defining the premise or conclusion to test. "variable_dict" is a
// Hashtable<Character, Boolean>() object with the variable name as the key and true/false
// as the value. 

// The only valid operators for premise strings are '^' (and), 'V' (or--CAPITAL v), '~' (not),
// and '>' (implies), and you can use parentheses to override the order of operations as usual.
// All variables should be lowercase letters and each should only be one character long. Finally,
// do not include spaces in the string.

// For example, if you want to test the premise 'p implies q or not r', you should use 'p>qV~r' as
// your premise string.

class Validator {
   // All of the logic to complete this assignment should be written in this function.
   // This method accepts two things: An array of strings called premiseList and a 
   // single string called conclusion. These strings should be formatted according to 
   // the structure definded above. Also, this needs to return a boolean variable: true if
   // the argument form is valid, and false if it is not valid.
   public boolean validate(String[] premiseList, String conclusion) {
   
      //Check to see if premiseList is an empty array or conclusion is null
      for (String prm : premiseList) {
         if (prm == null) {
            throw new IllegalArgumentException();
         }
      }
      if (conclusion == null) {
         throw new IllegalArgumentException();
      }
      
      // Instantiate a list for variable of premises and conclusion.
      LinkedList<Character> premisCharacters = new LinkedList<Character>();
      LinkedList<Character> vCharacters = new LinkedList<Character>();
   
      // Get the list of variables and operators.
      for (String arg : premiseList) {
         for (int i = 0; i < arg.length(); i++) {
            if (!premisCharacters.contains(arg.charAt(i))) {
               premisCharacters.add(arg.charAt(i));
            }  
         }
      }
     
      //Get a list of only variables for future hashing.
      for (Character chr : premisCharacters) {
         if (Character.isLetter(chr)) {
            if (chr == 'V') {
               continue;
            }
            else {
               vCharacters.add(chr);
            }
         }
         else {
            continue;
         }
      }
      int numberOfCharacters = vCharacters.size();
      
      // Create truth table in accordance 
      ArrayList<Hashtable<Character, Boolean>> arrayHashTable = new ArrayList<Hashtable<Character, Boolean>>();
      arrayHashTable = truthTable(0, new boolean[numberOfCharacters], numberOfCharacters, vCharacters, arrayHashTable);
      
      // Evalutate the premises and conclusions based on the truth table.
      for (Hashtable<Character, Boolean> row : arrayHashTable) {
         boolean check = true;
         //Evaluate the premises based on each row of the truth table.
         for (String prm : premiseList) {
            try {
               Boolean valid = Evaluator.evaluate(prm, row);
            
               if (valid == false) {
                  check = false;
                  break;
               }
            }
            
            catch (Exception e) {
               System.out.print(e.toString());
               return false;
            }
         
         }
         
         //If both premises are true, evaluate the conclusion based on the
         // same row.
         if (check == true) {
            try {
               Boolean conclu = Evaluator.evaluate(conclusion, row);
               return conclu;
            }
               
            catch(Exception e) {
               System.out.println(e.toString());
            }
         }
        
      }
      
      //return true if all premises fail before reaching conclusion.
      return true;
   }

   private static ArrayList<Hashtable<Character, Boolean>> truthTable(int index, boolean[] current, int size, LinkedList<Character> list, ArrayList<Hashtable<Character, Boolean>> table) {
      //Instantiate a boolean as value for the truth table.
      boolean b = false;
      
      //Use recursive to create a truth table. Index + 1 and current[index] help alternate
      // true and false for each column on the table. 
      if (index != size) {
         truthTable(index + 1, current, size, list, table);
         current[index] = !b;
         truthTable(index + 1, current, size, list, table);
         current[index] = b;
      }
      
      else {
         //Instantiate a Hashtable row and for hashing key/value with help from TA Jason.
         Hashtable<Character, Boolean> row  = new Hashtable<Character, Boolean>();
      
         for (int i = 0; i < current.length; i++) {
            //Add key/value pair to row
            row.put(list.get(i), current[i]);
         
         }
         
         // Add row to table
         table.add(row);
      }  
      
      return table;
   }
}