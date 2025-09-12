package io.matita08;

import io.matita08.data.Registers;
import io.matita08.value.Value;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
   public static final Random rng = new Random();
   /**
    * {@link ThreadGroup} where temporary task are run
    */
   public final static ThreadGroup taskGroup = new ThreadGroup("Utils-threads");
   /**
    * Current temporary thread number, it's the same as the number of temporary task scheduled.
    */
   private final static AtomicInteger tc = new AtomicInteger(1);
   
   private Utils() {throw new RuntimeException("You're an idiot");}
   
   public static void loadMC(File f) {runOnNewThread(()->loadMCImpl(f));}
   
   /**
    * Start a new thread in the {@link #taskGroup}
    *
    * @param run The task to schedule for running
    * @return a started {@link Thread}
    */
   @SuppressWarnings("UnusedReturnValue")
   public static Thread runOnNewThread(Runnable run) {
      Thread t = new Thread(taskGroup, run, taskGroup.getName() + "-" + tc.getAndIncrement());
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
      } catch (FileNotFoundException fnf) {
         System.out.println("The selected file (" + f.getName() + ") doesn't exist or i was unable to open it");
         //noinspection CallToPrintStackTrace
         fnf.printStackTrace();
      }
   }
}
