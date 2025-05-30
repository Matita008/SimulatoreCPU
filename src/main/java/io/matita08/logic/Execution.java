package io.matita08.logic;

import io.matita08.GUI.*;
import io.matita08.Utils;
import io.matita08.value.Value;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;

public class Execution {
   private static final int MAX_CYCLES = 3;
   
   public static boolean stepped = false;
   
   public static void step(int countMax) {
      for (int i = 0; i < countMax; i++) {
         if(!step()) return;
      }
   }
   
   /**
    @return whenever we were able to step or not
    */
   public static boolean step() {
      if(ControlUnit.current == Phase.Execute && ControlUnit.opcode == Operation.Halt) return false;
      ControlUnit.current = ControlUnit.next;
      ControlUnit.currentCycles = ControlUnit.nextCycles;
      ControlUnit.next.run();
      stepped = true;
      SwingUtilities.invokeLater(Display::update);
      return true;
   }
   
   public static void fetch() {
      setMarR(Registers.pc().getAndInc());
      Registers.setIr(Registers.getMDR());
      ControlUnit.next = Phase.Decode;
      ControlUnit.nextCycles = 1;
      ControlUnit.currentCycles = -1;
   }
   
   public static void decode() {
      ControlUnit.next = Phase.Execute;
      ControlUnit.opcode = Operation.get(Registers.getIr().get());
      ControlUnit.nextCycles = ControlUnit.opcode.cycles;
   }
   
   public static void execute() {
      ControlUnit.opcode.action.accept(ControlUnit.currentCycles);
      if((ControlUnit.nextCycles = ControlUnit.currentCycles - 1) == 0) {
         ControlUnit.next = Phase.Fetch;
         ControlUnit.opcode = Operation.Unknown;
      }
      
   }
   
   public static void setMarR(Value v) {
      Registers.setMAR(v);
      Registers.setMDR(Registers.getMC(v));
   }
   
   public static void step(ActionEvent ignored) {
      Utils.runOnNewThread(Execution::step);
   }
}
