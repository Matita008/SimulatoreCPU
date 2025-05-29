package io.matita08;

import io.matita08.GUI.Registers;
import io.matita08.value.Value;

import java.io.*;
import java.util.*;

public class Utils {
   public static final Random rng = new Random();
   private final static ThreadGroup tg = new ThreadGroup("Utils-threads");
   
   private Utils() {throw new RuntimeException("You're an idiot");}
   
   public static void loadMC(File f) {runOnNewThread(()->loadMCImpl(f));}
   
   public static Thread runOnNewThread(Runnable run) {
      Thread t = new Thread(tg, run);
      t.start();
      return t;
   }
   
   private static void loadMCImpl(File f) {
      try {
         Scanner s = new Scanner(f);
         int pos = 0;
         while(s.hasNext()) {
            String st = s.nextLine();
            if(st.startsWith("?") || st.isEmpty()) {
               Registers.setMC(pos, Value.nullValue);
            } else {
               try {
                  int n = Integer.parseInt(st);
                  Registers.setMC(pos, Value.create(n));
               } catch (NumberFormatException ex) {
                  Registers.setMC(pos, Value.nullValue);
               }
            }
            pos++;
         }
      } catch (FileNotFoundException _) {
         System.out.println("The selected file (" + f.getName() + ") doesn't exist or i was unable to open it");
      }
   }
}
