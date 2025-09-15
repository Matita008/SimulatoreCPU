package io.matita08;

import java.io.File;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
   private final boolean isSwingEDT;
   private static final File logFolder = new File(appFolder());
   
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
         e.printStackTrace(System.err);
      } else {
         System.out.println("An exception occurred in the thread " + t.getName() + " " + t);
         e.printStackTrace(System.err);
      }
   }
   
   private static String appFolder(){
      return System.getenv("AppData") + "\\matita008\\CPUSim";
   }
}
