package io.matita08;

import io.matita08.GUI.*;
import io.matita08.data.Registers;
import io.matita08.logic.Execution;
import io.matita08.value.SingleValue;

import javax.swing.SwingUtilities;

public class Main {
   public static void main(String[] args) {
      if(Constants.init(args)) return;
      SwingUtilities.invokeLater(Display::init);
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(false));
   }
}