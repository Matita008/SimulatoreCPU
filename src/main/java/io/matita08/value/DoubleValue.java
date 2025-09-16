package io.matita08.value;

import io.matita08.Constants;

public class DoubleValue extends Value {
   
   private static final Value c1 = new SingleValue(1, false);
   private final Value[] v = new Value[Constants.getAddressSize()];  //index 0 MSB, index 1 (if present) LSB
   
   public DoubleValue() {
      for (int i = 0; i < Constants.getAddressSize(); i++) {
         v[i] = Value.getNew();
      }
   }
   
   public DoubleValue(int n) {
      if(v.length == 2) {
         v[0] = new SingleValue(n %Constants.getValueMax());
         v[1] = new SingleValue(n /Constants.getValueMax());
      } else v[0] = new SingleValue(n);
   }
   
   public DoubleValue(Value v1) {
      if(v1 instanceof DoubleValue dv) {
         v[0] = dv.v[0];
         if(v.length == 2) v[1] = dv.v[1];
      } else v[0] = v1;
   }
   
   public DoubleValue(Value v1, Value v2) {
      if(v1 instanceof DoubleValue || v2 instanceof DoubleValue) throw new IllegalArgumentException("One of the passed argument is already a DoubleValue: " + v1 + " " + v2);
      v[0] = v1;
      v[1] = v2;
   }
   
   public static String unset() {
      return SingleValue.unset().repeat(Constants.getAddressSize());
   }
   
   @Override
   public Value set(int n) {
      return new DoubleValue(n);
   }
   
   @Override
   public void set(Value va) {
      if(va instanceof DoubleValue dv) {
         v[0] = dv.v[0];
         if(v.length == 2) v[1] = dv.v[1];
      } else if(va instanceof UndefinedSingleValue){
         v[0] = Value.getNew();
         if(v.length == 2) v[1] = Value.getNew();
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
   
   @Override
   public Value add(Value v2) {
      return new DoubleValue(v2.get() + get());
   }
   
   @Override
   public Value sub(Value v2) {
      return new DoubleValue(get() - v2.get());
   }
   
   @Override
   public Value mul(Value v2) {
      throwAss();
      return v[0].mul(v2);
   }
   
   @Override
   public int get() {
      if(v.length == 2) return v[0].get() * Constants.getValueMax() + v[1].get();
      else return v[0].get();
   }
   
   @Override
   public int getSigned() {
      return get();
   }
   
   @Override
   public int getUnsigned() {
      return get();
   }
   
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
   
   private static String string(int n) {
      return Integer.toString(n, Constants.getRadix());
   }
   
   private static void throwAss(){throw new AssertionError("This method wasn't intended to be called");}
}
