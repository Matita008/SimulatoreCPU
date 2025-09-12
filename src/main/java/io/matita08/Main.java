package io.matita08;

import io.matita08.GUI.*;
import io.matita08.data.Registers;
import io.matita08.logic.Execution;
import io.matita08.value.SingleValue;

import javax.swing.SwingUtilities;

public class Main {
   public static void main(String[] args) {
      //Value v= new DoubleValue(1);
      //System.out.println(Registers.getMC(v).getSigned());
      if(Constants.init(args)) return;
      SwingUtilities.invokeLater(Display::init);
      Registers.setMC(0, new SingleValue(2));
      Registers.setMC(1, new SingleValue(7));
      Registers.setMC(2, new SingleValue(5));
      Registers.setMC(3, new SingleValue(15));
      Registers.setMC(4, new SingleValue(2));
      Registers.setMC(5, new SingleValue(15));
      Display.update();
      Registers.setAcc(new SingleValue(5));
      Execution.step();
      Display.update();
      System.out.println(Registers.getRegB());
      System.out.println(Registers.pc());
   }
}