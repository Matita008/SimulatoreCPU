package io.matita08;

import io.matita08.GUI.Display;

import javax.swing.SwingUtilities;

public class Main {
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