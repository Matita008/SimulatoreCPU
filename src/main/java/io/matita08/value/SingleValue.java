package io.matita08.value;

/**
 * Concrete implementation of Value representing a single-word value.
 * This class stores a single integer value with optional sign interpretation,
 * making it suitable for most CPU register and memory operations.
 *
 * <p>SingleValue supports both signed and unsigned interpretations of the same
 * bit pattern, allowing for flexible arithmetic operations and proper handling
 * of different data types within the CPU simulation.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public class SingleValue extends Value {
   
   /**
    * The numeric value stored in this instance.
    */
   int value;
   
   /**
    * Flag indicating whether this value should be interpreted as signed.
    * When false, the value is treated as unsigned.
    */
   boolean signed = false;
   
   /**
    * Constructs a new SingleValue with the specified numeric value.
    * The value is treated as unsigned by default.
    *
    * @param n the numeric value to store
    */
   public SingleValue(int n) {
      value = n;
   }
   
   /**
    * Constructs a new SingleValue with the specified numeric value and sign interpretation.
    *
    * @param n the numeric value to store
    * @param sign true for signed interpretation, false for unsigned
    */
   public SingleValue(int n, boolean sign) {
      value = n;
      signed = sign;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For SingleValue, returns the stored value with sign interpretation applied.
    * If this value is marked as signed, returns the value as-is.
    * Otherwise, converts from unsigned to signed representation.</p>
    */
   public int getSigned() {
      if(signed) return value;
      else return toSigned();
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For SingleValue, returns the stored value as an unsigned integer.
    * If this value is marked as signed, converts to unsigned representation.
    * Otherwise, returns the value as-is.</p>
    */
   public int getUnsigned() {
      if(signed) return toUnsigned();
      else return value;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For SingleValue, updates the stored value and returns this instance
    * for method chaining.</p>
    */
   @Override
   public Value set(int n) {
      value = n;
      return this;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For SingleValue, performs signed addition and returns a new SingleValue
    * containing the result.</p>
    */
   @Override
   public Value add(Value v2) {
      return new SingleValue(getSigned() + v2.getSigned());
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For SingleValue, performs signed subtraction and returns a new SingleValue
    * containing the result.</p>
    */
   @Override
   public Value sub(Value v2) {
      return new SingleValue(getSigned() - v2.getSigned());
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For SingleValue, performs signed multiplication and returns a new SingleValue
    * containing the result.</p>
    */
   @Override
   public Value mul(Value v2) {
      return new SingleValue(getSigned() * v2.getSigned());
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For SingleValue, returns "?" to indicate an uninitialized state.</p>
    */
   public static String unset(){return "?";}
   
   //Private section -----------------------------------------------------------------------------
   
   /**
    * Converts the current value from signed to unsigned representation.
    *
    * @return the absolute value of the stored value
    */
   private int toUnsigned(){
      return Math.abs(value);
   }
   
   /**
    * Converts the current value to signed representation.
    * For SingleValue, this is a no-op since the value is already stored as-is.
    *
    * @return the stored value
    */
   private int toSigned() {
      return value;
   }
}