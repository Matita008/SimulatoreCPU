package io.matita08;

public final class Constants {
   private Constants() {
      throw new IllegalAccessError("Just why?");
   }
   public static int Value_max = 8;//Size of SingleValue
   public static int AddressSize = 2;//USE ONLY 1 OR 2 (IT'S THE DIMENSION OF DOUBLEVALUE IN SINGLEVALUES)
   public static int MC_Size = 32;//MAX Value_max * addressSize
   public static String OperationEnumName = "io.matita08.logic.Operations3Bit";
   @SuppressWarnings("Unused")//TODO Remove me ;) //TODO Better implement some cfg system
   public static int Value_signed_flag = 1 | 2;//1 is enabled\disabled signed mode, 2 is default read from MC as signed(set) or not(cleared)
}
