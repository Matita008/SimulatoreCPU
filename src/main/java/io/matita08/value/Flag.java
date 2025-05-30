package io.matita08.value;

import io.matita08.Utils;

public class Flag {
   private boolean defined;
   private boolean value;
   
   public Flag(){
      defined = false;
   }
   
   public Flag(Boolean value){
      defined = true;
      this.value = value;
   }
   
   public boolean isDefined() {
      return defined;
   }
   
   public void set(boolean v) {
      defined = true;
      value = v;
   }
   
   public boolean get() {
      return defined ? value : Utils.rng.nextBoolean();
   }
   
   public boolean comp(Flag other){return equals(other);}
   
   @Override
   public final boolean equals(Object o) {
      if(this == o) return true;
      if(!(o instanceof Flag flag)) return false;
      return defined == flag.defined && value == flag.value;
   }
   
   public final boolean equals(boolean o) {
      return defined && value == o;
   }
   
   @Override
   public int hashCode() {
      int result = Boolean.hashCode(defined);
      result = 31 * result + Boolean.hashCode(value);
      return result;
   }
   
   @Override
   public String toString() {
      return defined ? (value ? "1" : "0") : "?";
   }
   
}
