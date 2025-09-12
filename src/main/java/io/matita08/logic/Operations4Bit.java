package io.matita08.logic;

import io.matita08.data.Registers;

import java.util.function.Consumer;

@SuppressWarnings("unused")  //Loaded with reflection
public enum Operations4Bit {
   load(0,n->{}, 2),
   sto(1, n->{}, 2),
   set(2, n->{Registers.setRegB(Registers.getAcc());}, 1),
   in(3, n->{}, 1),
   out(4, n->{}, 1),
   add(5, n->{}, 1),
   sub(6, n->{}, 1),
   jmp(7, n->{
      if(n == 1) {
         Registers.pc().set(Registers.getPointer().get());
      } else {
         Execution.setMarR(Registers.pc().getAndInc());
         Registers.setPointer(Registers.getMDR());
      }
   }, 2),
   jpz(8, n->{}, 1),
   jpo(9, n->{}, 1),
   Halt(15, n->{}, 1),
   Unknown(16, n->{}, 1);
   
   public static final Operations4Bit[] all = values();
   public final Operation wrapper;
   
   Operations4Bit(int opcode, Consumer<Integer> act, int cycles) {
      wrapper = new Operation(opcode, act, cycles, name());
   }
   
   public static Operation getHalt(){
      return Halt.wrapper;
   }
   
   public static Operation getUnknown(){
      return Unknown.wrapper;
   }
}
