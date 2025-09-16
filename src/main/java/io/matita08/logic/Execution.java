package io.matita08.logic;

import io.matita08.*;
import io.matita08.GUI.*;
import io.matita08.data.*;
import io.matita08.value.Value;

import java.awt.event.ActionEvent;

public class Execution {
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
      ControlUnit.next.run();
      ControlUnit.currentCycle--;
      stepped = true;
      Utils.runOnNewThread(Display::update);
      return true;
   }
   
   public static void fetch() {
      ControlUnit.ALUOpcode = "";
      setMarR(Registers.pc().getAndInc());
      Registers.setIr(Registers.getMDR());
      ControlUnit.next = Phase.Decode;
      ControlUnit.currentCycle = 0;
      ControlUnit.totalCycles = -1;
      ControlUnit.opcode = Operation.Unknown;
   }
   
   public static void decode() {
      ControlUnit.next = Phase.Execute;
      ControlUnit.opcode = Operation.get(Registers.getIr().get());
      ControlUnit.totalCycles = ControlUnit.currentCycle = ControlUnit.opcode.cycles;
      ControlUnit.currentCycle++;
   }
   
   public static void execute() {
      ControlUnit.opcode.action.accept(ControlUnit.currentCycle);
      if(ControlUnit.currentCycle == 1) {
         ControlUnit.next = Phase.Fetch;
      }
      
   }
   
   public static void setMarR(Value v) {
      Registers.setMAR(v);
      Registers.setMDR(Registers.getMC(v));
   }
   
   public static void step(ActionEvent ignored) {
      Utils.runOnNewThread(Execution::step);
   }
   
   public static void readPointer(int cycle){
      if(cycle < 0 || cycle > Constants.getAddressSize()) throw new AssertionError("An error occurred\nDetails:\treadPointer cycle is OOB, value: " + cycle);
      
   }
}
