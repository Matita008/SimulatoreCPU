package io.matita08;

import io.matita08.GUI.Display;

import javax.swing.SwingUtilities;

/**
 * Main entry point for the CPU Simulator application.
 * This class initializes the application by processing command-line arguments,
 * setting up exception handling, and launching the graphical user interface.
 *
 * <p>The Main class coordinates the complete application startup sequence:</p>
 * <ul>
 *   <li>Initializes debug logging streams for troubleshooting</li>
 *   <li>Processes command-line configuration options through {@link Constants#init(String[])}</li>
 *   <li>Launches the GUI on the Swing Event Dispatch Thread</li>
 *   <li>Configures global exception handling for unhandled errors</li>
 * </ul>
 *
 * <p>The application supports various command-line options for configuring
 * the simulator's behavior, including memory size, instruction set selection,
 * address size, and display radix. If invalid arguments are provided or help
 * is requested, the application will display usage information and exit.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 * @see Constants#init(String[])
 * @see Display#init()
 * @see ExceptionHandler
 */
public class Main {
   
   /**
    * Main entry point for the CPU Simulator application.
    * Processes command-line arguments, initializes configuration,
    * and launches the graphical user interface.
    *
    * <p>Startup sequence:</p>
    * <ol>
    *   <li>Outputs debug information about stream configurations</li>
    *   <li>Processes command-line arguments via {@link Constants#init(String[])}</li>
    *   <li>If argument parsing requests help or fails, exits early</li>
    *   <li>Launches GUI on Swing EDT using {@link SwingUtilities#invokeLater(Runnable)}</li>
    *   <li>Sets up global exception handler for background threads</li>
    * </ol>
    *
    * <p>Debug output includes information about standard output, error,
    * and log file streams to aid in troubleshooting startup issues.</p>
    *
    * @param args command-line arguments for configuring the simulator
    *             (see {@link Constants#init(String[])} for supported options)
    * @see Constants#init(String[])
    */
   public static void main(String[] args) {
      System.out.println("[DEBUG] Stdout stream: " + System.out);
      System.out.println("[DEBUG] Error stream: " + System.err);
      System.out.println("[DEBUG] File stream: " + ExceptionHandler.logStream);
      System.err.println("[DEBUG] Stdout stream: " + System.out);
      System.err.println("[DEBUG] Error stream: " + System.err);
      System.err.println("[DEBUG] File stream: " + ExceptionHandler.logStream);
      ExceptionHandler.logStream.println("[DEBUG] Stdout stream: " + System.out);
      ExceptionHandler.logStream.println("[DEBUG] Error stream: " + System.err);
      ExceptionHandler.logStream.println("[DEBUG] File stream: " + ExceptionHandler.logStream);
      if(Constants.init(args)) return;
      SwingUtilities.invokeLater(Display::init);
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(false));
   }
}