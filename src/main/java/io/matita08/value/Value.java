package io.matita08.value;

import io.matita08.Constants;

public abstract class Value {
   
   @SuppressWarnings("StaticInitializerReferencesSubClass") public static final UndefinedSingleValue nullValue = new UndefinedSingleValue();
   
   public static Value getNew() {return nullValue.clone();}
   
   public static Value getNewAddress() {return new DoubleValue(getNew(), getNew());}
   
   public static Value create(int n) {return new SingleValue(n);}
   
   public static Value create(int n, boolean signed) {return new SingleValue(n, signed);}
   
   public boolean isUndefined() {return false;}
   
   public abstract Value set(int n);
   
   public void set(Value v) {
      set(v.get());
   }
  
   //TODO: default should be using ints, not Values
   public abstract Value add(Value v2);
   
   public abstract Value sub(Value v2);
   
   public abstract Value mul(Value v2);
   
   @Override
   public String toString() {
      return Integer.toString(get(), Constants.getRadix());
   }
   
   public int get() {
      return getUnsigned();
   }
   
   public static String unset(){return "?";}
   
   public abstract int getSigned();
   
   public abstract int getUnsigned();
   
   public boolean equals(int n) {
      return getUnsigned() == n || getSigned() == n;
   }
}