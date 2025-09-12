package io.matita08.data;

import io.matita08.*;

/**
 * Class to manage various flags
 */
public class Flags {
   /**
    * Variable to store the flags, using {@link FlagsConstants} as indexes.
    * It defaults to a random state
    */
   private static int flags = Utils.rng.nextInt();
   
   /**
    *
    * @param flag the {@link FlagsConstants flag} to retrive
    * @return the current flag status
    */
   public static boolean get(FlagsConstants flag) {
      return get(flag.get());
   }
   
   /**
    *
    * @param flag the bitmask of the flag(s) to retrive
    * @return the current flag status
    */
   public static boolean get(int flag) {
      return (flags & flag) == flag;
   }
   
   /**
    * Sets/unsets a flag
    * @param flag the {@link FlagsConstants flag} to modify
    * @param value the new state of the flag
    */
   public static void set(FlagsConstants flag, boolean value) {
      set(flag.get(), value);
   }
   
   /**
    * Sets/unsets a flag
    * @param flag the bitmask of the flag(s) to modify
    * @param value the new state of the flag
    */
   public static void set(int flag, boolean value) {
      if(value) flags = flags | flag;
      else if((flags & flag) == flag) flags = flags - flag;
   }
}
