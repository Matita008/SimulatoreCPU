package io.matita08.value;

import io.matita08.Constants;

public class DoubleValue extends Value {
   
   private static final Value c1 = new SingleValue(1, false);
   private Value[] v;
   
   public DoubleValue() {
      v = new Value[Constants.AddressSize];
      for (int i = 0; i < Constants.AddressSize; i++) {
         v[i] = Value.getNew();
      }
   }
   
   public DoubleValue(int n) {
      v = new Value[]{new SingleValue(n)};
   }
   
   public DoubleValue(Value v1, Value v2) {
      if(v1 instanceof DoubleValue || v2 instanceof DoubleValue) throw new IllegalArgumentException("one of the passed argument is already a DoubleValue: " + v1 + " " + v2);
      this.v = new Value[]{v1, v2};
   }
   
   public static String unset() {
      return SingleValue.unset().repeat(Constants.AddressSize);
   }
   
   @Override
   public Value set(int n) {
      return v[0].set(n);
   }
   
   @Override
   public void set(Value va) {
      v[0].set(va);
   }
   
   @Override
   public Value add(Value v2) {
      return v[0].add(v2);
   }
   
   @Override
   public Value sub(Value v2) {
      return v[0].sub(v2);
   }
   
   @Override
   public Value mul(Value v2) {
      return v[0].mul(v2);
   }
   
   @Override
   public int get() {
      return v[0].getUnsigned();
   }
   
   @Override
   public int getSigned() {
      return v[0].getSigned();
   }
   
   @Override
   public int getUnsigned() {
      return v[0].getUnsigned();
   }
   
   public Value getAndInc() {
      int before = v[0].get();
      v[0] = v[0].add(c1);
      return new DoubleValue(before);
   }
}
