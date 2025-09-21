package io.matita08;

import io.matita08.GUI.Display;
import io.matita08.data.Registers;
import io.matita08.value.Value;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class providing common functionality for the CPU simulator.
 * This class contains helper methods for file operations, threading,
 * and other shared utilities used throughout the application.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li><strong>File Loading:</strong> Loads program files into the simulator's central memory</li>
 *   <li><strong>Thread Management:</strong> Provides utilities for running tasks on separate threads</li>
 *   <li><strong>Random Number Generation:</strong> Shared random number generator for consistent behavior</li>
 * </ul>
 *
 * <p>The class uses a dedicated thread group for temporary tasks to provide
 * better organization and debugging capabilities. All file operations are
 * performed asynchronously to avoid blocking the GUI thread.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
public class Utils {
   /**
    * Shared random number generator used throughout the application.
    * This provides consistent random behavior across all components.
    */
   public static final Random rng = new Random();
   
   /**
    * {@link ThreadGroup} where temporary tasks are executed.
    * This provides better organization and allows for easier monitoring
    * of background tasks.
    */
   public final static ThreadGroup taskGroup = new ThreadGroup("Utils-threads");
   
   /**
    * Atomic counter for generating unique thread names.
    * This tracks the number of temporary tasks that have been scheduled,
    * ensuring each background thread has a unique identifier.
    */
   private final static AtomicInteger tc = new AtomicInteger(1);
   
   /**
    * Private constructor to prevent instantiation.
    * This class is designed to be used as a static utility class only.
    *
    * @throws RuntimeException always, with an insulting message
    */
   private Utils() {throw new RuntimeException("You're an idiot");}
   
   /**
    * Loads a program file into the simulator's central memory asynchronously.
    * This method starts a new background thread to perform the file loading
    * operation without blocking the GUI.
    *
    * <p>The file is parsed line by line, with each line representing a memory value.
    * Lines starting with "?" or empty lines are treated as undefined values.
    * Numeric lines are parsed and stored as defined values. Invalid numeric
    * formats are treated as undefined values.</p>
    *
    * @param f the file to load into central memory
    * @see #loadMCImpl(File)
    */
   public static void loadMC(File f) {runOnNewThread(()->loadMCImpl(f));}
   
   /**
    * Starts a new thread in the dedicated task group for executing background operations.
    * Each thread is given a unique name for easier identification during debugging.
    *
    * @param run the task to execute on the new thread
    * @return the started Thread object
    */
   @SuppressWarnings("UnusedReturnValue")
   public static Thread runOnNewThread(Runnable run) {
      Thread t = new Thread(taskGroup, run, taskGroup.getName() + "-" + tc.getAndIncrement());
      t.start();
      return t;
   }
   
   /**
    * Internal implementation of file loading into central memory.
    * This method performs the actual file parsing and memory updates.
    *
    * <p>File format:</p>
    * <ul>
    *   <li>Each line represents one memory location</li>
    *   <li>Lines starting with "?" are treated as undefined values</li>
    *   <li>Empty lines are treated as undefined values</li>
    *   <li>Numeric lines are parsed and stored as values</li>
    *   <li>Invalid numeric formats default to undefined values</li>
    * </ul>
    *
    * <p>After loading completes, the GUI display is updated to reflect
    * the new memory contents.</p>
    *
    * @param f the file to load
    */
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
      Display.update();
   }
}