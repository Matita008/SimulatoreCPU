package io.matita08;

import java.io.*;
import java.util.Locale;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
   private static final File logFile = createLogFile();
   public static final PrintStream logStream;
   
   static {
      try {
         logStream = new PrintStream(logFile);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
   }
   
   private final boolean isSwingEDT;
   
   public ExceptionHandler(boolean swingThread) {
      isSwingEDT = swingThread;
   }
   
   /**
    * Method invoked when the given thread terminates due to the
    * given uncaught exception.
    * <p>Any exception thrown by this method will be ignored by the
    * Java Virtual Machine.
    *
    * @param t the thread
    * @param e the exception
    */
   @Override
   public void uncaughtException(Thread t, Throwable e) {
      if(isSwingEDT) {
         System.out.println("An exception occurred in the swing event dispatching thread, exiting");
         logStream.println("An exception occurred in the swing event dispatching thread");
      } else {
         System.out.println("An exception occurred in the thread " + t.getName() + " " + t);
         logStream.println("An exception occurred in the thread " + t.getName() + " " + t);
      }
      e.printStackTrace(System.err);
      e.printStackTrace(logStream);
      if(isSwingEDT) System.exit(3);
   }
   
   private static File createLogFile() {
      File logFolder = new File(appFolder() + "\\matita008\\CPUSim\\Logs");
      if(!logFolder.exists()) logFolder.mkdirs();
      File out = new File(logFolder, "log" + System.currentTimeMillis() + ".txt");
      try {
         out.createNewFile();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      return out;
   }
   
   private static String appFolder() {
      String OS = (System.getProperty("os.name")).toLowerCase(Locale.ROOT);
      if(OS.contains("win")) return System.getenv("AppData");
      if(OS.contains("mac")) return System.getProperty("user.home") + "/Library/Application Support";
      return System.getProperty("user.home");
   }
}
