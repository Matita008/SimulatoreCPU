package io.matita08.data;

/**
 * Enumeration of CPU flag constants used in the simulator's flag management system.
 * This enum defines the available CPU flags that indicate the results of arithmetic
 * and logical operations performed by the simulated processor.
 *
 * <p>Each flag constant represents a specific CPU condition and is implemented
 * as a power-of-2 bitmask to allow efficient bitwise operations for flag
 * manipulation and testing.</p>
 *
 * <p>The flags are used throughout the simulator to:</p>
 * <ul>
 *   <li>Track the results of ALU operations</li>
 *   <li>Control conditional branching and execution flow</li>
 *   <li>Provide status information for debugging and monitoring</li>
 * </ul>
 *
 * <p>Flag states are managed by the {@link Flags} class, which provides
 * methods for setting, clearing, and querying individual flags or combinations
 * of flags using the bitmask values provided by this enumeration.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 * @see Flags
 */
public enum FlagsConstants {
   /**
    * Zero Flag - Set to true when the result of the last arithmetic or logical
    * operation equals zero. This flag is commonly used by conditional jump
    * instructions to test for equality conditions.
    *
    * <p>Bitmask value: 1 (2^0)</p>
    */
   ZERO,
   
   /**
    * Overflow Flag - Set to true when the last arithmetic operation resulted
    * in an overflow (exceeds maximum representable value) or underflow
    * (falls below minimum representable value in the case of subtraction).
    *
    * <p>This flag indicates that the result cannot be accurately represented
    * in the current data format and may require special handling.</p>
    *
    * <p>Bitmask value: 2 (2^1)</p>
    */
   OVERFLOW;
   
   /**
    * Constant equivalent to {@code 2^values().length-1}.
    * It represents the sum of all flag bitmasks and can be used
    * for operations that need to affect all flags simultaneously.
    *
    * <p>This value is computed during class initialization by summing
    * all individual flag bitmasks.</p>
    */
   public static final int all;
   
   static {
      int acc = 0;
      for (FlagsConstants fc: values()) {
         acc += fc.get();
      }
      all = acc;
   }
   
   /**
    * Gets the bitmask value for this flag constant.
    * The bitmask is calculated as 2^ordinal(), ensuring each flag
    * has a unique power-of-2 value suitable for bitwise operations.
    *
    * <p>For example:</p>
    * <ul>
    *   <li>ZERO.get() returns 1 (2^0)</li>
    *   <li>OVERFLOW.get() returns 2 (2^1)</li>
    * </ul>
    *
    * @return the bitmask value for this flag (2^ordinal)
    */
   public int get() {
      return 1 << ordinal();
   }
}