package io.matita08;

public final class Constants {
   private Constants() {
      throw new IllegalAccessError("Just why?");
   }
   
   /**
    *
    */
   private static int ValueMax = 8;//Size of SingleValue
   private static int AddressSize = 2;//USE ONLY 1 OR 2 (IT'S THE DIMENSION OF DOUBLEVALUE IN SINGLEVALUES)
   private static int MCSize = 32;//MAX Value_max * addressSize
   private static String OperationEnumName = "io.matita08.logic.Operations3Bit";
   
   public static void setRadix(int radix) {
      Constants.radix = radix;
   }
   
   private static int radix = 10;
   
   public static int getValueMax() {
      return ValueMax;
   }
   
   public static int getAddressSize() {
      return AddressSize;
   }
   
   public static int getMCSize() {
      return MCSize;
   }
   
   public static String getOperationEnumName() {
      return OperationEnumName;
   }
   
   public static int getRadix() {
      return radix;
   }
   
   /**
    Parse the strings passed in search of options (each one must start with '-')
    if the help option or an uncorrected formatted one is found the help dialog is shown and it returns false immediately
    @param args a String array containing the argument passed to parse
    @return true if the help menu was shown
    */
   public static boolean init(String[] args) {
      for (int i = 0; i < args.length; i++) {
         String cur = args[i];
         if(cur.charAt(0) != '-' || "-help".equals(cur) || "-h".equals(cur)) {
            return true;
         }
         
      }
      return false;
   }
}
