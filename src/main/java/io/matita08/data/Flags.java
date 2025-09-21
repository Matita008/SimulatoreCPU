package io.matita08.data;

import io.matita08.*;

/**
 * CPU Flags Management System.
 * <p>
 * This class manages CPU flags that indicate the results of arithmetic and
 * logic operations. Flags are used by conditional instructions and provide
 * status information about the current CPU state.
 * </p>
 *
 * <p>
 * <strong>Supported Flags:</strong>
 * <ul>
 * <li><strong>Zero Flag:</strong> Set when an operation result equals zero</li>
 * <li><strong>Overflow Flag:</strong> Set when an operation exceeds maximum value</li>
 * </ul>
 * </p>
 *
 * <p>
 * Flags are stored as a bitmask int and accessed using {@link FlagsConstants} as indexes.
 * The flags system uses bitwise operations for efficient flag manipulation and supports
 * both individual flag operations and bulk flag queries.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 * // Set the zero flag
 * Flags.set(FlagsConstants.ZERO, true);
 *
 * // Check if overflow occurred
 * boolean overflow = Flags.get(FlagsConstants.OVERFLOW);
 *
 * // Get string representation of all flags
 * String flagState = Flags.get();
 * </pre>
 *
 * @author Matita008
 * @version 1.5
 * @since 1.0
 * @see FlagsConstants
 */
public class Flags {
   /**
    * Variable to store the flags, using {@link FlagsConstants} as indexes.
    * It defaults to a random state
    */
   private static int flags = Utils.rng.nextInt();
   
   /**
    * Indicates whether any flag has been explicitly set or unset since initialization.
    * Used to determine if flag state should be displayed or shown as unknown.
    */
   private static boolean set = false;
   
   /**
    * Get the string representation of all flags
    * @return the bitmask of all the flags if any flag was set as a string or a question mark if on flag was ever set/unset
    */
   public static String get() {
      return set ? Integer.toString(flags, Constants.getRadix()) : "?";
   }
   
   /**
    * Gets the current state of a specific flag.
    *
    * @param flag the {@link FlagsConstants flag} to retrieve
    * @return the current flag status (true if set, false if not set)
    */
   public static boolean get(FlagsConstants flag) {
      return get(flag.get());
   }
   
   /**
    * Gets the current state of flag(s) using a bitmask.
    * This method allows checking multiple flags simultaneously by combining their bitmasks.
    *
    * @param flag the bitmask of the flag(s) to retrieve
    * @return true if all specified flags are set, false otherwise
    */
   public static boolean get(int flag) {
      return (flags & flag) == flag;
   }
   
   /**
    * Sets or unsets a specific flag.
    * This method updates the flag state and marks the flag system as having been modified.
    *
    * @param flag the {@link FlagsConstants flag} to modify
    * @param value the new state of the flag (true to set, false to unset)
    */
   public static void set(FlagsConstants flag, boolean value) {
      set(flag.get(), value);
   }
   
   /**
    * Sets or unsets flag(s) using a bitmask.
    * This method allows setting multiple flags simultaneously by combining their bitmasks.
    *
    * @param flag the bitmask of the flag(s) to modify
    * @param value the new state of the flag(s) (true to set, false to unset)
    */
   public static void set(int flag, boolean value) {
      if(set) {
         if(value) flags = flags | flag;
         else if((flags & flag) == flag) flags = flags - flag;
      } else {
         set = true;
         flags = flag;
      }
   }
}