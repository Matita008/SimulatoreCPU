package io.matita08;

import io.matita08.GUI.*;
import io.matita08.data.Registers;
import io.matita08.logic.Execution;
import io.matita08.value.SingleValue;

import javax.swing.SwingUtilities;

public class Main {
   public static void main(String[] args) {
      System.out.println("[DEBUG] Stdout stream: " + System.out);
      System.out.println("[DEBUG] Error stream: " + System.err);
      System.err.println("[DEBUG] Stdout stream: " + System.out);
      System.err.println("[DEBUG] Error stream: " + System.err);
      if(Constants.init(args)) return;
      SwingUtilities.invokeLater(Display::init);
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(false));
   }
}