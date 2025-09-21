package io.matita08.value;

import io.matita08.Constants;

/**
 * Concrete implementation of Value representing a multi-word value.
 * This class stores values that span multiple memory locations, typically used
 * for addresses in architectures with multi-word addressing or for extended precision values.
 *
 * <p>DoubleValue can operate in two modes based on {@link Constants#getAddressSize()}:</p>
 * <ul>
 *   <li><strong>Single-word mode:</strong> Uses one Value component (index 0)</li>
 *   <li><strong>Double-word mode:</strong> Uses two Value components (index 0 = MSB, index 1 = LSB)</li>
 * </ul>
 *
 * <p>The class provides automatic increment functionality useful for pointer arithmetic
 * and address calculations, with proper overflow handling across word boundaries.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public class DoubleValue extends Value {
   
   /**
    * Constant representing the value 1, used for increment operations.
    */
   private static final Value c1 = new SingleValue(1, false);
   
   /**
    * Array storing the value components.
    * Index 0 is the Most Significant Byte/Word, index 1 (if present) is the Least Significant Byte/Word.
    */
   private final Value[] v = new Value[Constants.getAddressSize()];  //index 0 MSB, index 1 (if present) LSB
   
   /**
    * Constructs a new DoubleValue with all components initialized to undefined values.
    * The number of components depends on the current address size configuration.
    */
   public DoubleValue() {
      for (int i = 0; i < Constants.getAddressSize(); i++) {
         v[i] = Value.getNew();
      }
   }
   
   /**
    * Constructs a new DoubleValue from a single integer value.
    * For double-word mode, the value is split across MSB and LSB components.
    * For single-word mode, the entire value is stored in component 0.
    *
    * @param n the integer value to store
    */
   public DoubleValue(int n) {
      if(v.length == 2) {
         v[0] = new SingleValue(n %Constants.getValueMax());
         v[1] = new SingleValue(n /Constants.getValueMax());
      } else v[0] = new SingleValue(n);
   }
   
   /**
    * Constructs a new DoubleValue by copying from another Value.
    * If the source is a DoubleValue, copies its components.
    * Otherwise, stores the source value in component 0.
    *
    * @param v1 the value to copy from
    */
   public DoubleValue(Value v1) {
      if(v1 instanceof DoubleValue dv) {
         v[0] = dv.v[0];
         if(v.length == 2) v[1] = dv.v[1];
      } else v[0] = v1;
   }
   
   /**
    * Constructs a new DoubleValue from two separate Value components.
    * This constructor is only valid for double-word mode.
    *
    * @param v1 the value for component 0 (MSB)
    * @param v2 the value for component 1 (LSB)
    * @throws IllegalArgumentException if either parameter is already a DoubleValue
    */
   public DoubleValue(Value v1, Value v2) {
      if(v1 instanceof DoubleValue || v2 instanceof DoubleValue) throw new IllegalArgumentException("One of the passed argument is already a DoubleValue: " + v1 + " " + v2);
      v[0] = v1;
      v[1] = v2;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, returns a string representation of undefined state
    * with question marks for each address component.</p>
    */
   public static String unset() {
      return SingleValue.unset().repeat(Constants.getAddressSize());
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, creates a new DoubleValue with the specified numeric value.</p>
    */
   @Override
   public Value set(int n) {
      return new DoubleValue(n);
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, updates the internal components based on the source value type.
    * Handles DoubleValue-to-DoubleValue copying, undefined value initialization,
    * and SingleValue integration with intelligent component selection.</p>
    */
   @Override
   public void set(Value va) {
      if(va instanceof DoubleValue dv) {
         v[0] = dv.v[0];
         if(v.length == 2) v[1] = dv.v[1];
         return;
      } else if(va instanceof UndefinedSingleValue){
         v[0] = Value.getNew();
         if(v.length == 2) v[1] = Value.getNew();
         return;
      }
      assert va instanceof SingleValue;
      SingleValue sv = (SingleValue)va;
      if(v.length == 2) {
         if(v[1].isUndefined()) v[1] = sv;
         else if(v[0].isUndefined()) v[0] = sv;
         else {
            v[0] = Value.getNew();
            v[1] = sv;
         }
      } else v[0] = sv;
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, performs addition by creating a new DoubleValue
    * with the sum of both operands.</p>
    */
   @Override
   public Value add(Value v2) {
      return new DoubleValue(v2.get() + get());
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, performs subtraction by creating a new DoubleValue
    * with the difference of both operands.</p>
    */
   @Override
   public Value sub(Value v2) {
      return new DoubleValue(get() - v2.get());
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, multiplication is not intended to be used.
    * This method throws an AssertionError if called.</p>
    *
    * @throws AssertionError always, as this method is not intended for use
    */
   @Override
   public Value mul(Value v2) {
      throwAss();
      return v[0].mul(v2);
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, combines all components into a single integer.
    * In double-word mode, calculates MSB * ValueMax + LSB.
    * In single-word mode, returns the value of component 0.</p>
    */
   @Override
   public int get() {
      if(v.length == 2) return v[0].get() * Constants.getValueMax() + v[1].get();
      else return v[0].get();
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, delegates to {@link #get()} as signed interpretation
    * is the same as unsigned for the composite value.</p>
    */
   @Override
   public int getSigned() {
      return get();
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, delegates to {@link #get()} for unsigned interpretation.</p>
    */
   @Override
   public int getUnsigned() {
      return get();
   }
   
   /**
    * Returns the current value and then increments this DoubleValue.
    * This method is useful for pointer arithmetic where you need both
    * the current address and want to advance to the next location.
    *
    * <p>Increment behavior:</p>
    * <ul>
    *   <li><strong>Single-word mode:</strong> Increments component 0 with overflow to 0</li>
    *   <li><strong>Double-word mode:</strong> Increments LSB, carries over to MSB on overflow</li>
    * </ul>
    *
    * @return a copy of this DoubleValue before incrementing
    */
   public Value getAndInc() {
      try {
         return new DoubleValue(this);
      } finally {
         if(v.length == 1){
            if(v[0].get() == Constants.getValueMax()-1) v[0] = v[0].set(0);
            else v[0] = v[0].add(c1);
         } else {
            if(v[1].get() == Constants.getValueMax()-1) {
               v[1] = v[1].set(0);
               if(v[0].get() == Constants.getValueMax()-1) v[0] = v[0].set(0);
               else v[0] = v[0].add(c1);
            } else v[1] = v[1].add(c1);
         }
      }
   }
   
   /**
    * {@inheritDoc}
    *
    * <p>For DoubleValue, returns a string representation that shows undefined
    * components as "?" and combines defined components appropriately based
    * on the current radix setting.</p>
    */
   @Override
   public String toString() {
      if(v.length == 2)  {
         if(v[0].isUndefined()) {
            if(v[1].isUndefined()) return "??";
            else return "?" + string(v[1].get());
         }
         else return string(v[0].get() * Constants.getValueMax() + v[1].get());
      } else return v[0].isUndefined() ? "?" : string(v[0].get());
      
   }
   
   /**
    * Converts an integer to string representation using the configured radix.
    *
    * @param n the integer to convert
    * @return the string representation in the configured radix
    */
   private static String string(int n) {
      return Integer.toString(n, Constants.getRadix());
   }
   
   /**
    * Utility method that throws an AssertionError for unsupported operations.
    * Used by methods that are not intended to be called.
    *
    * @throws AssertionError always
    */
   private static void throwAss(){throw new AssertionError("This method wasn't intended to be called");}
}