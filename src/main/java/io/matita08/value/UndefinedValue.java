package io.matita08.value;

import io.matita08.Constants;

public class UndefinedValue extends Value implements Cloneable{
   UndefinedValue() {
   }
   
   private int rand() {
      return (int)(Math.random() * Constants.getValue_max() * 2);
   }
   
   @Override
   public String getString() {
      return "?";
   }
   
   @Override
   public int getSigned() {
      return rand() - Constants.getValue_max();
   }
   
   @Override
   public int getUnsigned() {
      return rand() % Constants.getValue_max();
   }
   
   @SuppressWarnings("MethodDoesntCallSuperMethod")
   @Override
   public UndefinedValue clone() {
      return this;
   }
   
   public Value set(int n) {
      return new SingleValue(n);
   }
   
   @Override
   public boolean isUndefined() {return true;}
   
   @Override
   public Value add(Value v2) {
      return this;
   }
   
   @Override
   public Value sub(Value v2) {
      return this;
   }
   
   @Override
   public Value mul(Value v2) {
      return this;
   }
   
   @Override
   public boolean equals(int n) {
      return false;
   }
}
