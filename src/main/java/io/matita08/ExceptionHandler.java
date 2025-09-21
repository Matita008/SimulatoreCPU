package io.matita08;

import java.io.*;
import java.util.Locale;

/**
 * Custom uncaught exception handler for the CPU simulator application.
 * This class implements Thread.UncaughtExceptionHandler to provide centralized
 * exception handling with logging capabilities and graceful application shutdown.
 *
 * <p>The handler performs the following actions when an uncaught exception occurs:</p>
 * <ul>
 *   <li>Logs the exception to both console and a persistent log file</li>
 *   <li>Identifies whether the exception occurred in the Swing Event Dispatch Thread</li>
 *   <li>Provides different handling for GUI thread vs background thread exceptions</li>
 *   <li>Terminates the application if critical GUI thread exceptions occur</li>
 * </ul>
 *
 * <p>Log files are created in a platform-appropriate application data directory
 * with timestamps to ensure uniqueness and chronological organization.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
   /**
    * The log file where exceptions are persistently recorded.
    * Created in the platform-appropriate application data directory.
    */
   private static final File logFile = createLogFile();
   
   /**
    * Print stream for writing to the log file.
    * Provides formatted output for exception logging.
    */
   public static final PrintStream logStream;
   
   /**
    * Static initialization block that sets up the log file print stream.
    *
    * @throws RuntimeException if the log file cannot be opened for writing
    */
   static {
      try {
         logStream = new PrintStream(logFile);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
   }
   
   /**
    * Flag indicating whether this handler is attached to the Swing Event Dispatch Thread.
    * When true, exceptions are treated as critical GUI failures requiring application termination.
    */
   private final boolean isSwingEDT;
   
   /**
    * Constructs a new ExceptionHandler with the specified thread type.
    *
    * @param swingThread true if this handler is for the Swing Event Dispatch Thread,
    *                   false for background or utility threads
    */
   public ExceptionHandler(boolean swingThread) {
      isSwingEDT = swingThread;
   }
   
   /**
    * Handles uncaught exceptions by logging them and taking appropriate action.
    *
    * <p>For Swing EDT exceptions:</p>
    * <ul>
    *   <li>Logs a specific message indicating GUI thread failure</li>
    *   <li>Prints stack trace to both console and log file</li>
    *   <li>Terminates the application with exit code 3</li>
    * </ul>
    *
    * <p>For other thread exceptions:</p>
    * <ul>
    *   <li>Logs the thread information</li>
    *   <li>Prints stack trace to both console and log file</li>
    *   <li>Allows the application to continue running</li>
    * </ul>
    *
    * @param t the thread that had the uncaught exception
    * @param e the exception that was thrown
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
   
   /**
    * Creates a log file in the appropriate application data directory.
    * The file name includes a timestamp to ensure uniqueness.
    *
    * <p>Log file locations by platform:</p>
    * <ul>
    *   <li><strong>Windows:</strong> %APPDATA%\matita008\CPUSim\Logs\</li>
    *   <li><strong>macOS:</strong> ~/Library/Application Support/matita008/CPUSim/Logs/</li>
    *   <li><strong>Linux/Other:</strong> ~/.matita008/CPUSim/Logs/</li>
    * </ul>
    *
    * @return a File object representing the created log file
    * @throws RuntimeException if the log file cannot be created
    */
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
   
   /**
    * Determines the appropriate application data folder based on the operating system.
    *
    * @return the platform-specific application data directory path
    */
   private static String appFolder() {
      String OS = (System.getProperty("os.name")).toLowerCase(Locale.ROOT);
      if(OS.contains("win")) return System.getenv("AppData");
      if(OS.contains("mac")) return System.getProperty("user.home") + "/Library/Application Support";
      return System.getProperty("user.home");
   }
}