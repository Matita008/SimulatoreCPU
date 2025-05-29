package io.matita08.logic;

import io.matita08.GUI.Registers;
import io.matita08.value.Flag;

import java.util.function.Consumer;

@SuppressWarnings("unused")  //Loaded with reflection
public enum Operations3Bit {//Using prof default table
   sto(0, n->{}, 2),
   load(1, n->{}, 2),
   out(2, n->{}, 1),
   in(3, n->{}, 1),
   add(4, n->{
      Registers.setAcc(Registers.getAcc().add(Registers.getRegB()));
      Registers.setZero(new Flag(Registers.getRegB().equals(0)));
   }, 1),
   set(5, n->Registers.setRegB(Registers.getAcc()), 1),
   /*jmp(10, n->{
      if(n == 1) {
         Registers.pc().set(Registers.getPointer().get());
      } else {
         Execution.setMarR(Registers.pc().getAndInc());
         Registers.setPointer(Registers.getMDR());
      }
   }, 2),*/
   jpz(6, n->{}, 3),
   Halt(7, n->{}, 1),
   Unknown(8, n->{}, 1);
   
   public static final Operations3Bit[] all = values();
   public final Operation wrapper;
   
   Operations3Bit(int opcode, Consumer<Integer> act, int cycles) {
      wrapper = new Operation(opcode, act, cycles, name());
   }
   
   public static Operation getHalt(){
      System.out.println(jpz.wrapper.opcode + " " + Halt.wrapper.opcode + " " + Unknown.wrapper.opcode);
      return Halt.wrapper;
   }
   
   public static Operation getUnknown(){
      return Unknown.wrapper;
   }
}
