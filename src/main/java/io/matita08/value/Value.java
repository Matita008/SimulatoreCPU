package io.matita08.value;

import io.matita08.Constants;

/**
 * Abstract base class representing a value in the CPU simulator.
 * This class defines the common interface for all value types used throughout
 * the simulation, including single values, double values, and undefined values.
 *
 * <p>The Value hierarchy supports different value representations:</p>
 * <ul>
 *   <li><strong>SingleValue:</strong> Standard single-word values with optional sign interpretation</li>
 *   <li><strong>DoubleValue:</strong> Multi-word values for larger address spaces or extended precision</li>
 *   <li><strong>UndefinedSingleValue:</strong> Uninitialized single values with random behavior</li>
 *   <li><strong>UndefinedDoubleValue:</strong> Uninitialized double values with random behavior</li>
 * </ul>
 *
 * <p>All values support arithmetic operations, conversion between signed/unsigned representations,
 * and proper handling of undefined states. The class provides factory methods for creating
 * appropriate value types based on the current CPU configuration.</p>
 *
 * <p>Undefined values simulate the behavior of uninitialized memory or registers,
 * returning random values when accessed to help identify programming errors.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 * @see SingleValue
 * @see DoubleValue
 * @see UndefinedSingleValue
 * @see UndefinedDoubleValue
 */
public abstract class Value {
   
   /**
    * Singleton instance representing an undefined single value.
    * This constant is used as a prototype for creating new undefined values.
    */
   @SuppressWarnings("StaticInitializerReferencesSubClass") public static final UndefinedSingleValue nullValue = new UndefinedSingleValue();
   
   /**
    * Creates a new undefined single value.
    * The returned value will exhibit random behavior when accessed,
    * simulating uninitialized memory.
    *
    * @return a new undefined single value instance
    */
   public static Value getNew() {return nullValue.clone();}
   
   /**
    * Creates a new undefined address-sized value.
    * The size and structure depend on the current address size configuration.
    * For single-word addresses, returns a DoubleValue with one undefined component.
    * For multi-word addresses, returns a DoubleValue with multiple undefined components.
    *
    * @return a new undefined address-sized value
    * @see Constants#getAddressSize()
    */
   public static Value getNewAddress() {
      if(Constants.getAddressSize() == 2) return new DoubleValue(getNew(), getNew());
      else return new DoubleValue(getNew());
   }
   
   /**
    * Creates a new single value with the specified numeric value.
    * The value is treated as unsigned by default.
    *
    * @param n the numeric value to wrap
    * @return a new SingleValue containing the specified number
    */
   public static Value create(int n) {return new SingleValue(n);}
   
   /**
    * Creates a new single value with the specified numeric value and sign interpretation.
    *
    * @param n the numeric value to wrap
    * @param signed true if the value should be interpreted as signed, false for unsigned
    * @return a new SingleValue with the specified value and sign interpretation
    */
   public static Value create(int n, boolean signed) {return new SingleValue(n, signed);}
   
   /**
    * Checks if this value is in an undefined state.
    * Undefined values represent uninitialized memory or registers.
    *
    * @return true if this value is undefined, false for concrete values
    */
   public boolean isUndefined() {return false;}
   
   /**
    * Sets this value to the specified numeric value.
    * The behavior depends on the concrete value type - some types may return
    * a new instance while others modify the current instance.
    *
    * @param n the new numeric value
    * @return the value instance (may be this instance or a new one)
    */
   public abstract Value set(int n);
   
   /**
    * Sets this value to match another value.
    * The default implementation extracts the numeric value and calls {@link #set(int)}.
    * Subclasses may override this for more sophisticated value copying.
    *
    * @param v the value to copy from
    */
   public void set(Value v) {
      set(v.get());
   }
   
   //TODO: default should be using ints, not Values (i need to think on this)
   /**
    * Performs addition with another value.
    * The operation is performed using the appropriate numeric representation
    * and may handle sign extension or overflow based on the value types involved.
    *
    * @param v2 the value to add to this value
    * @return a new value containing the sum
    */
   public abstract Value add(Value v2);
   
   /**
    * Performs subtraction with another value.
    * The operation is performed using the appropriate numeric representation
    * and may handle sign extension or underflow based on the value types involved.
    *
    * @param v2 the value to subtract from this value
    * @return a new value containing the difference
    */
   public abstract Value sub(Value v2);
   
   /**
    * Performs multiplication with another value.
    * The operation is performed using the appropriate numeric representation
    * and may handle overflow based on the value types involved.
    *
    * @param v2 the value to multiply with this value
    * @return a new value containing the product
    */
   public abstract Value mul(Value v2);
   
   /**
    * Returns a string representation of this value.
    * The representation uses the radix specified by {@link Constants#getRadix()}
    * and may show special indicators for undefined values.
    *
    * @return a string representation of this value
    */
   @Override
   public String toString() {
      return Integer.toString(get(), Constants.getRadix());
   }
   
   /**
    * Gets the numeric value as an integer.
    * The default implementation delegates to {@link #getUnsigned()}.
    *
    * @return the numeric value as an integer
    */
   public int get() {
      return getUnsigned();
   }
   
   /**
    * Returns the string representation of an unset/undefined value.
    * This is used for display purposes when a value hasn't been initialized.
    *
    * @return the string "?" representing an undefined value
    */
   public static String unset(){return "?";}
   
   /**
    * Gets the value interpreted as a signed integer.
    * The sign interpretation depends on the value type and configuration.
    *
    * @return the value as a signed integer
    */
   public abstract int getSigned();
   
   /**
    * Gets the value interpreted as an unsigned integer.
    * All negative representations are treated as positive values.
    *
    * @return the value as an unsigned integer
    */
   public abstract int getUnsigned();
   
   /**
    * Checks if this value equals the specified numeric value.
    * The comparison is performed against both signed and unsigned interpretations
    * to handle cases where the same bit pattern can represent different values.
    *
    * @param n the numeric value to compare against
    * @return true if either the signed or unsigned interpretation matches the specified value
    */
   public boolean equals(int n) {
      return getUnsigned() == n || getSigned() == n;
   }
}