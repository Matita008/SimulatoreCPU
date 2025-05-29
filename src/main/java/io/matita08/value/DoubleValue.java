package io.matita08.value;

public class DoubleValue extends Value {
   
   private static final Value c1 = new SingleValue(1, false);
   private Value v1 = new SingleValue(0, false);
   private Value v2;
   
   public DoubleValue() { }
   
   public DoubleValue(int n) {
      v1 = new SingleValue(n);
   }
   
   public DoubleValue(Value v1, Value v2) {
      if(v1 instanceof DoubleValue || v2 instanceof DoubleValue) throw new IllegalArgumentException("one of the passed argument is already a DoubleValue: " + v1 + " " + v2);
      this.v1 = v1;
      this.v2 = v2;
   }
   
   public static String unset() {return SingleValue.unset();}
   
   @Override
   public Value set(int n) {
      return v1.set(n);
   }
   
   @Override
   public Value add(Value v2) {
      return v1.add(v2);
   }
   
   @Override
   public Value sub(Value v2) {
      return v1.sub(v2);
   }
   
   @Override
   public Value mul(Value v2) {
      return v1.mul(v2);
   }
   
   @Override
   public int get() {
      return v1.getUnsigned();
   }
   
   @Override
   public int getSigned() {
      return v1.getSigned();
   }
   
   @Override
   public int getUnsigned() {
      return v1.getUnsigned();
   }
   
   public Value getAndInc() {
      int before = v1.get();
      v1 = v1.add(c1);
      return new DoubleValue(before);
   }
}
