//TODO redo
package io.matita08.value;

import io.matita08.Constants;

public class UndefinedSingleValue extends Value implements Cloneable{
   UndefinedSingleValue() {
   }
   
   private int rand() {
      return (int)(Math.random() * Constants.getValueMax() * 2);
   }
   
   @Override
   public int getSigned() {
      return rand() - Constants.getValueMax();
   }
   
   @Override
   public int getUnsigned() {
      return rand() % Constants.getValueMax();
   }
   
   @SuppressWarnings("MethodDoesntCallSuperMethod")
   @Override
   public UndefinedSingleValue clone() {
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
