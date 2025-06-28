package io.matita08;

import org.jetbrains.annotations.*;

public final class Constants {
   private Constants() {
      throw new IllegalAccessError("Just why?");
   }
   private static int Value_max = 8;//Size of SingleValue
   private static int AddressSize = 2;//USE ONLY 1 OR 2 (IT'S THE DIMENSION OF DOUBLEVALUE IN SINGLEVALUES)
   private static int MC_Size = 32;//MAX Value_max * addressSize
   private static String OperationEnumName = "io.matita08.logic.Operations3Bit";
   @SuppressWarnings("Unused")//TODO Remove me ;) //TODO Better implement some cfg system
   public static int Value_signed_flag = 1 | 2;//1 is enabled\disabled signed mode, 2 is default read from MC as signed(set) or not(cleared)
   
   public static int getValue_max() {
      return Value_max;
   }
   
   public static void setValue_max(int value_max) {
      Value_max = value_max;
   }
   
   public static int getAddressSize() {
      return AddressSize;
   }
   
   public static void setAddressSize(int addressSize) {
      AddressSize = addressSize;
   }
   
   public static int getMC_Size() {
      return MC_Size;
   }
   
   public static void setMC_Size(int MC_Size) {
      Constants.MC_Size = MC_Size;
   }
   
   public static String getOperationEnumName() {
      return OperationEnumName;
   }
   
   /**
    Parse the strings passed in search of options (each one must start with '-')
    if the help option or an uncorrected formatted one is found the help dialog is shown and it returns false immediately
    @param args a String array containing the argument passed to parse
    @return true if the help menu was shown
    */
   @Contract(pure = true)
   public static boolean init(@NotNull String[] args) {
      for (int i = 0; i < args.length; i++) {
         String cur = args[i];
         if(cur.charAt(0) != '-' || "-help".equals(cur) || "-h".equals(cur)) {
            return true;
         }
      }
      return false;
   }
}
