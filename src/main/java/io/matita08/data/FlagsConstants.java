package io.matita08.data;

public enum FlagsConstants {
   /**
    * True if the last arithmetic operation was zero
    */
   ZERO,
   /**
    * True if the last arithmetic operation overflowed (or underflowed in the case of subtraction)
    */
   OVERFLOW;
   
   /**
    * Constant equivalent to {@code 2^values().length-1}
    */
   public static final int all;
   
   static {
      int acc = 0;
      for (FlagsConstants fc: values()) {
         acc += fc.get();
      }
      all = acc;
   }
   
   public int get() {
      return 1 << ordinal();
   }
}
