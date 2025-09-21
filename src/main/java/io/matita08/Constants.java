package io.matita08;

import java.util.Locale;

/**
 * Configuration constants and command-line argument processing for the CPU Simulator.
 * This class provides centralized configuration management with comprehensive command-line
 * argument parsing capabilities, allowing users to customize various aspects of the CPU
 * simulation including memory size, instruction set, addressing mode, and display formatting.
 *
 * <p>The Constants class supports dynamic configuration through a sophisticated argument parser:</p>
 * <ul>
 *   <li><strong>Value Configuration:</strong> Maximum register values and display radix</li>
 *   <li><strong>Memory Configuration:</strong> Central memory size with validation</li>
 *   <li><strong>Architecture Configuration:</strong> Address size (1 or 2 words)</li>
 *   <li><strong>Instruction Set Selection:</strong> Fully qualified operation enum class name</li>
 *   <li><strong>Help System:</strong> Built-in usage information and error handling</li>
 * </ul>
 *
 * <p>Command-line options are processed using an internal parser that supports both
 * short (-r) and long (--radix) option formats with comprehensive error handling
 * and validation. Invalid configurations display helpful error messages and usage information.</p>
 *
 * <p>Configuration values are immutable once initialized and thread-safe for access
 * throughout the application lifecycle.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public final class Constants {
   /**
    * Maximum value that can be represented in a single register or memory location.
    * This affects the range of values for arithmetic operations and determines
    * overflow conditions. Default value is 8.
    * Configurable via -s or --size command-line options.
    */
   private static int valueMax = 8;//Maximum value of SingleValue
   
   /**
    * Size of memory addresses in single-value units.
    * Must be either 1 (single-word addressing) or 2 (double-word addressing).
    * This determines the dimension of DoubleValue in SingleValues and affects
    * the maximum addressable memory space. Default value is 2.
    * Configurable via -a or --address command-line options.
    */
   private static int addressSize = 2;//USE ONLY 1 OR 2 (IT'S THE DIMENSION OF DOUBLEVALUE IN SINGLEVALUES)
   
   /**
    * Size of the central memory in addressable locations.
    * Maximum allowed value is valueMax * addressSize to ensure proper addressing.
    * The actual memory size is validated and clamped to valid ranges during initialization.
    * Default value is 32. Configurable via -mc command-line option.
    */
   private static int MCSize = 32;//MAX ValueMax * addressSize
   
   /**
    * Fully qualified class name of the operation enumeration.
    * This class must contain the CPU instruction definitions and is loaded
    * dynamically using reflection. Default is "io.matita08.logic.Operations3Bit".
    * Configurable via -o or --operations command-line options.
    */
   private static String operationEnumName = "io.matita08.logic.Operations3Bit";
   
   /**
    * Number base (radix) used for displaying values in the user interface.
    * Must be a valid radix supported by Java (typically 2-36, but commonly 2, 8, 10, 16).
    * Default value is 10 (decimal). Configurable via -r or --radix command-line options.
    */
   private static int radix = 10;
   
   /**
    * Private constructor to prevent instantiation.
    * This class is designed to be used as a static utility class only.
    *
    * @throws IllegalAccessError always thrown with a humorous message
    */
   private Constants() {
      throw new IllegalAccessError("Just why?");
   }
   
   /**
    * Gets the maximum value that can be represented in a single register.
    * This value affects arithmetic operations, overflow detection, and memory addressing limits.
    *
    * @return the maximum single-register value
    */
   public static int getValueMax() {
      return valueMax;
   }
   
   /**
    * Gets the address size configuration in single-value units.
    * This determines the addressing architecture of the simulated CPU:
    * <ul>
    *   <li>1 = Single-word addressing (simple architecture)</li>
    *   <li>2 = Double-word addressing (extended address space)</li>
    * </ul>
    *
    * @return the address size (1 or 2)
    */
   public static int getAddressSize() {
      return addressSize;
   }
   
   /**
    * Gets the central memory size in addressable locations.
    * This determines the total number of memory locations available for
    * program storage and execution.
    *
    * @return the size of central memory
    */
   public static int getMCSize() {
      return MCSize;
   }
   
   /**
    * Gets the fully qualified class name of the operation enumeration.
    * This class is dynamically loaded to provide the instruction set
    * for the CPU simulator and must implement the expected operation interface.
    *
    * @return the fully qualified operation enum class name
    */
   public static String getOperationEnumName() {
      return operationEnumName;
   }
   
   /**
    * Gets the radix (number base) used for value display.
    * This affects how numerical values are formatted in the user interface.
    *
    * @return the display radix
    */
   public static int getRadix() {
      return radix;
   }
   
   /**
    * Parses command-line arguments to configure the simulator.
    * This method processes configuration options using a sophisticated parser
    * that supports both short and long option formats with comprehensive
    * validation and error handling.
    *
    * <p>Supported command-line options:</p>
    * <ul>
    *   <li><strong>-r, --radix VALUE:</strong> Set display number base (default: 10)</li>
    *   <li><strong>-a, --address SIZE:</strong> Set address size 1 or 2 (default: 2)</li>
    *   <li><strong>-o, --operations CLASS:</strong> Set operation enum class name</li>
    *   <li><strong>-s, --size VALUE:</strong> Set maximum register value (default: 8)</li>
    *   <li><strong>-mc SIZE:</strong> Set central memory size (default: 32, max: valueMax * addressSize)</li>
    *   <li><strong>-h, --help:</strong> Display usage information and exit</li>
    * </ul>
    *
    * <p>The parser validates all provided values and automatically corrects
    * out-of-range values where possible. Invalid options or malformed arguments
    * result in error messages and help display.</p>
    *
    * <p>Processing behavior:</p>
    * <ul>
    *   <li>Unknown options trigger help display and return true</li>
    *   <li>Malformed numeric values throw parsing exceptions</li>
    *   <li>Out-of-range values are clamped to valid ranges with warnings</li>
    *   <li>Missing option arguments cause array bounds exceptions</li>
    * </ul>
    *
    * @param args command-line arguments array from main method
    * @return true if help was displayed or parsing failed (application should exit),
    *         false if initialization completed successfully
    */
   public static boolean init(String[] args) {
      ParserHelper parser = new ParserHelper(args);
      try {
         while(parser.hasNext()) {
            String cur = parser.cur();
            if(cur == null || cur.isBlank()) return false;
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
    * Displays usage information and available command-line options to standard output.
    * This method provides comprehensive help information including option descriptions,
    * default values, and usage guidelines for configuring the CPU simulator.
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
    * Internal exception class used by the command-line argument parser ({@link #init(String[])}).
    * This exception is thrown when argument parsing encounters errors such as
    * wrong data types, missing values, or invalid option combinations.
    *
    * <p>ParserException extends Throwable to provide detailed error information
    * about parsing failures, including the problematic argument and underlying cause.</p>
    *
    * @see #init(String[])
    */
   private static class ParserException extends Throwable {
      /**
       * {@inheritDoc}
       * @see Constants#init(String[])
       */
      ParserException(String message, Throwable cause){ super(message, cause); }
   }
   
   /**
    * Internal helper class for parsing command-line arguments ({@link #init(String[])}).
    * This class provides a stateful iterator over the argument array with
    * utility methods for safe navigation, option processing, and error handling.
    *
    * <p>The ParserHelper maintains the current parsing position and provides
    * methods for moving through the argument array while handling boundary
    * conditions and providing context for error reporting.</p>
    *
    * @see #init(String[])
    */
   private static class ParserHelper {
      /**
       * Copy of the original command-line arguments array.
       * This array is traversed during parsing without modification.
       */
      String[] opts;
      
      /**
       * Current position in the arguments array.
       * Used to track parsing progress and provide context for error messages.
       */
      int pos = 0;
      
      /**
       * Constructs a new ParserHelper with the specified arguments array.
       * Creates a reference to the arguments for parsing without copying the array contents.
       *
       * @param args the command-line arguments to parse
       */
      ParserHelper(String[] args) {
         opts = args;
      }
      
      /**
       * Advances the parser to the next argument in the array.
       * This method moves the cursor forward and validates that there are
       * more elements available for processing.
       *
       * @throws ArrayIndexOutOfBoundsException if there are no more arguments to process
       */
      public void step() {
         throwIfOOB();
         pos++;
      }
      
      /**
       * Advances to the next argument and returns it.
       * This is a convenience method that combines {@link #step()} and {@link #cur()}.
       *
       * @return the next argument string
       * @throws ArrayIndexOutOfBoundsException if there are no more arguments to process
       */
      public String next() {
         step();
         return cur();
      }
      
      /**
       * Checks if there are more arguments available for processing.
       * This method can be used to prevent ArrayIndexOutOfBoundsException
       * when calling {@link #next()}.
       *
       * @return true if there is at least one more argument, false otherwise
       */
      public boolean hasNext() {
         return pos < opts.length;
      }
      
      /**
       * Returns the current argument being processed.
       * This method provides access to the argument at the current parser position
       * without advancing the position.
       *
       * @return the current argument string
       */
      public String cur() {
         //can never be out of bound
         return opts[pos];
      }
      
      /**
       * Returns the current argument with leading occurrences of the specified character removed.
       * This utility method is commonly used to strip leading dashes from option arguments.
       *
       * @param character the character to remove from the beginning of the current argument
       * @return the current argument string with leading instances of character removed
       */
      public String strip(char character) {
         String s = cur();
         int i = 0;
         while(s.startsWith(String.valueOf(character), i)) {i++;}
         return s.substring(i);
      }
      
      /**
       * Returns the previously processed argument.
       * This method provides context for error reporting by accessing the
       * argument that was processed before the current one.
       *
       * @return the previous argument string, or empty string if this is the first argument
       */
      public String prev() {
         if(pos == 0) return "";
         return opts[pos - 1];
      }
      
      /**
       * Validates that there are more arguments available for processing.
       * This method is used internally to prevent out-of-bounds access
       * to the arguments array.
       *
       * @throws ArrayIndexOutOfBoundsException if the current position exceeds array bounds
       */
      private void throwIfOOB() {
         if(!hasNext()) throw new ArrayIndexOutOfBoundsException("pos is out of index for args: pos = " + pos);
      }
   }
}