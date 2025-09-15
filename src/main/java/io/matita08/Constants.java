package io.matita08;

import java.util.Locale;

public final class Constants {
   /**
    *
    */
   private static int valueMax = 8;//Maximum value of SingleValue
   private static int addressSize = 2;//USE ONLY 1 OR 2 (IT'S THE DIMENSION OF DOUBLEVALUE IN SINGLEVALUES)
   private static int MCSize = 32;//MAX ValueMax * addressSize
   private static String operationEnumName = "io.matita08.logic.Operations3Bit";
   private static int radix = 10;
   
   private Constants() {
      throw new IllegalAccessError("Just why?");
   }
   
   public static int getValueMax() {
      return valueMax;
   }
   
   public static int getAddressSize() {
      return addressSize;
   }
   
   public static int getMCSize() {
      return MCSize;
   }
   
   public static String getOperationEnumName() {
      return operationEnumName;
   }
   
   public static int getRadix() {
      return radix;
   }
   
   /**
    * Parse the strings passed in search of options (each one must start with '-')
    * if the help option or an uncorrected formatted one is found the help dialog is shown and it returns false immediately
    *
    * @param args a String array containing the argument passed to parse
    * @return true if the help menu was shown
    */
   public static boolean init(String[] args) {
      ParserHelper parser = new ParserHelper(args);
      try {
         while(parser.hasNext()) {
            String cur = parser.cur();
            if(cur.charAt(0) != '-' || "-help".equals(cur) || "-h".equals(cur) || "--help".equals(cur) || "--h".equals(cur)) {
               helpMenu();
               return true;
            }
            //every case is formed of either a single letter or the name of the parameter to change
            switch(parser.strip('-').toLowerCase(Locale.ROOT)) {
               case "r":
               case "radix":
                  try {
                     radix = Integer.parseInt(parser.next());
                  } catch (NumberFormatException e) {
                     throw new ParserException("The passed value \"" + parser.cur() + "\" is not a number", e);
                  }
               break;
               case "mc":
                  try {
                     MCSize = Integer.parseInt(parser.next());
                     if(MCSize > (valueMax * addressSize)) {
                        MCSize = valueMax * addressSize;
                        System.err.println("The passed value for the size of the MC can be 1 - (valueMax * addressSize)");
                     }
                     if(MCSize < 1) {
                        MCSize = 1;
                        System.err.println("The passed value for the size of the MC can be 1 - (valueMax * addressSize)");
                     }
                  } catch (NumberFormatException e) {
                     throw new ParserException("The passed value \"" + parser.cur() + "\" is not a number", e);
                  }
               break;
               case "a":
               case "address":
                  try {
                     int newAddressSize = Integer.parseInt(parser.next());
                     if(newAddressSize < 0 || newAddressSize > 2) {
                        System.err.println("The address size must be either 1 or 2");
                     } else addressSize = newAddressSize;
                  } catch (NumberFormatException e) {
                     throw new ParserException("The passed value \"" + parser.cur() + "\" is not a number", e);
                  }
               break;
               case "o":
               case "operations":
                  operationEnumName = parser.next();
                  break;
               case "s":
               case "size":
                  try {
                     valueMax = Integer.parseInt(parser.next());
                  } catch (NumberFormatException e) {
                     throw new ParserException("The passed value \"" + parser.cur() + "\" is not a number", e);
                  }
               break;
               default:
                  System.err.println("The passed option \"" + parser.cur() + "\" is invalid");
                  System.out.println("Incorrect parameter used");
                  helpMenu();
                  return true;
            }
            parser.step();
         }
      } catch (ArrayIndexOutOfBoundsException e) {
         System.err.println("Got array out of bounds exception while parsing passed arguments");
         System.err.println(e.getMessage());
         //e.printStackTrace(System.err);
         System.out.println("Incorrect usage");
         helpMenu();
         return true;
      } catch (ParserException e) {
         System.err.println("Illegal argument passed as an option to " + parser.prev() + "in position " + parser.pos + ": " + parser.cur());
         System.err.println(e.getMessage());
         //e.printStackTrace(System.err);
         System.out.println("Incorrect usage");
         helpMenu();
         return true;
      }
      //I do recognize only now using a switch was a bad idea...
      return false;
   }
   
   /**
    * Function to display the help menu to stdout
    */
   private static void helpMenu() {
      System.out.println("Available options:");
      System.out.println(" -r --radix: Change the radix used to display, will default to 10 if not set or greater than " + Character.MAX_RADIX);
      System.out.println(" -a --address: either 1 or 2 (default), the size of an address relative to a register size");
      System.out.println(" -o --operations: the fully qualified name of the class used as instruction set. Defaults to \"io.matita08.logic.Operations3Bit\"");
      System.out.println(" -s --size: The size of a register. defaults to 8. please keep it low, or you may experience issue");
      System.out.println(" -mc: the size of the Central Memory can be 1 - (size * address)");
      System.out.println(" -h --help: Shows this guide");
   }
   
   /**
    * Class internally used by {@link #init(String[])}
    * Exception used to segnalate a parsing error occurred like wrong type passed, no value
    */
   private static class ParserException extends Throwable {
      /**
       * {@inheritDoc}
       * @see io.matita08.Constants#init(String[])
       */
      ParserException(String message, Throwable cause){ super(message, cause); }
   }
   
   /**
    * Class internally used by {@link #init(String[])}
    * Wrapper class for the args passed to the program
    */
   private static class ParserHelper {
      /**
       * A copy of the array passed to {@link #ParserHelper(String[])}
       */
      String[] opts;
      /**
       * The position of the current string being parsed
       */
      int pos = 0;
      
      /**
       * Create a new instance of {@code ParserHelper}
       *
       * @param args the option argument
       */
      ParserHelper(String[] args) {
         opts = args;
      }
      
      /**
       * Function used to move the cursor to the next string
       *
       * @throws ArrayIndexOutOfBoundsException Throws {@link java.lang.ArrayIndexOutOfBoundsException} if there are no more element to be parsed in {@link #opts}
       */
      public void step() {
         throwIfOOB();
         pos++;
      }
      
      /**
       * Function used to move to the next string in {@link #opts} if applicable and return it
       *
       * @return The next string in the parsing array
       * @throws ArrayIndexOutOfBoundsException Throws {@link java.lang.ArrayIndexOutOfBoundsException} if there are no more element to be parsed in {@code opts}
       */
      public String next() {
         step();
         return cur();
      }
      
      /**
       * Check if there's one or more element in the array.
       * Also indicates if the next call to {@link #next()} will throw {@link java.lang.ArrayIndexOutOfBoundsException}
       *
       * @return If there is at least another element in {@link #opts}
       */
      public boolean hasNext() {
         return pos < opts.length;
      }
      
      /**
       * Function used to return the current string being parsed
       *
       * @return The current string
       */
      public String cur() {
         //can never be out of bound
         return opts[pos];
      }
      
      /**
       * Utility function to remove leading character from the string returned by {@link #cur()}
       *
       * @param character The character to remove
       * @return The string returned from {@code cur()} without any leading {@code character}
       */
      public String strip(char character) {
         String s = cur();
         int i = 0;
         while(s.startsWith(String.valueOf(character), i)) {i++;}
         return s.substring(i);
      }
      
      /**
       * Function to return the previous element emitted from {@link #next()}
       *
       * @return an empty string if {@code next()} was never called, else the last string returned from it
       */
      public String prev() {
         if(pos == 0) return "";
         return opts[pos - 1];
      }
      
      /**
       * Function to check if there are more element in {@link #opts} (or {@link #pos} is less than {@code opts.length})
       *
       * @throws ArrayIndexOutOfBoundsException Throws {@link java.lang.ArrayIndexOutOfBoundsException} if there are no more element to be parsed
       */
      private void throwIfOOB() {
         if(!hasNext()) throw new ArrayIndexOutOfBoundsException("pos is out of index for args: pos = " + pos);
      }
   }
}
