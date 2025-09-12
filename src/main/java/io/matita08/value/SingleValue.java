package io.matita08.value;

public class SingleValue extends Value {
   
   int value;
   boolean signed = false;
   
   public SingleValue(int n) {
      value = n;
   }
   
   public SingleValue(int n, boolean sign) {
      value = n;
      signed = sign;
   }
   
   public int getSigned() {
      if(signed) return value;
      else return toSigned();
   }
   
   public int getUnsigned() {
      if(signed) return toUnsigned();
      else return value;
   }
   
   @Override
   public Value set(int n) {
      value = n;
      return this;
   }
   
   @Override
   public Value add(Value v2) {
      return new SingleValue(getSigned() + v2.getSigned());
   }
   
   @Override
   public Value sub(Value v2) {
      return new SingleValue(getSigned() - v2.getSigned());
   }
   
   @Override
   public Value mul(Value v2) {
      return new SingleValue(getSigned() * v2.getSigned());
   }
   
   public static String unset(){return "?";}
   //---------------------------------------------------------------------------------------
   
   private int toUnsigned(){
      return Math.abs(value);
   }
   
   private int toSigned() {
      return value;
   }
}
