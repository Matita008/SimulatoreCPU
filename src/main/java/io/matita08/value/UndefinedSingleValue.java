//TODO redo
package io.matita08.value;

import io.matita08.Constants;

/**
 * Concrete implementation of Value representing an undefined single-word value.
 * This class simulates uninitialized memory or register contents by returning
 * random values for all operations, helping to identify programming errors
 * where uninitialized values are used.
 *
 * <p>UndefinedSingleValue implements the Cloneable interface but returns the same
 * instance when cloned, as all undefined values exhibit the same random behavior.</p>
 *
 * <p>All arithmetic operations with undefined values result in undefined values,
 * and comparison operations always return false to prevent undefined behavior
 * from appearing as valid comparisons.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public class UndefinedSingleValue extends Value implements Cloneable{
   /**
    * Package-private constructor to prevent external instantiation.
    * Undefined values should be created through the Value factory methods.
    */
   UndefinedSingleValue() {
   }
   
   /**
    * Generates a random integer value within the valid value range.
    * This method simulates the unpredictable nature of uninitialized memory.
    *
    * @return a random integer in the range [0, 2*ValueMax)
    */
   private int rand() {
      return (int)(Math.random() * Constants.getValueMax() * 2);
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, returns a random value in the signed range
    * by subtracting ValueMax from the random value to center it around zero.</p>
    */
   @Override
   public int getSigned() {
      return rand() - Constants.getValueMax();
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, returns a random value in the unsigned range
    * by taking the modulo with ValueMax.</p>
    */
   @Override
   public int getUnsigned() {
      return rand() % Constants.getValueMax();
   }
   
   /**
    * Returns the same UndefinedSingleValue instance.
    * Since all undefined values exhibit the same behavior, cloning
    * returns the same instance for efficiency.
    *
    * @return this instance
    */
   @SuppressWarnings("MethodDoesntCallSuperMethod")
   @Override
   public UndefinedSingleValue clone() {
      return this;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, setting a concrete value creates a new
    * SingleValue with that value, effectively "defining" the undefined value.</p>
    */
   public Value set(int n) {
      return new SingleValue(n);
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, always returns true since the value
    * is not defined.</p>
    */
   @Override
   public boolean isUndefined() {return true;}
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, any arithmetic operation results in
    * an undefined value, so returns this instance.</p>
    */
   @Override
   public Value add(Value v2) {
      return this;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, any arithmetic operation results in
    * an undefined value, so returns this instance.</p>
    */
   @Override
   public Value sub(Value v2) {
      return this;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, any arithmetic operation results in
    * an undefined value, so returns this instance.</p>
    */
   @Override
   public Value mul(Value v2) {
      return this;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, returns "?" to visually indicate
    * the undefined state.</p>
    */
   @Override
   public String toString() {
      return "?";
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For UndefinedSingleValue, always returns false since equality
    * with an undefined value is undefined and should not be relied upon.</p>
    */
   @Override
   public boolean equals(int n) {
      return false;
   }
}