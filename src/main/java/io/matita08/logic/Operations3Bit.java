package io.matita08.logic;

import io.matita08.GUI.Registers;
import io.matita08.value.*;

import java.util.function.Consumer;

@SuppressWarnings("unused")  //Loaded with reflection
public enum Operations3Bit {//Using prof default table
   sto(0, n->{}, 1 + Operation.getAddressSize()),
   load(1, n->{}, 1 + Operation.getAddressSize()),
   out(2, n->{Registers.setBufOut(Registers.getAcc());}, 1),
   in(3, n->{Registers.setAcc(Registers.getBufIn());}, 1),
   add(4, n->{
      Registers.setAcc(Registers.getAcc().add(Registers.getRegB()));
      Registers.setZero(new Flag(Registers.getRegB().equals(0)));
   }, 1),
   set(5, n->Registers.setRegB(Registers.getAcc()), 1),
   jpz(6, n->{
      if(n == Operation.getAddressSize() + 1) {
         if(Registers.getZero().equals(true)) Registers.pc().add(new SingleValue(2, false));
         else Operation.readPointer(n);
      } else if(n != 1) Operation.readPointer(n);
      else {
         Registers.pc().set(Registers.getPointer());
      }
   }, 1 + Operation.getAddressSize()),
   Halt(7, n->{}, 1),
   Unknown(8, n->{}, 1);
   
   public static final Operations3Bit[] all = values();
   public final Operation wrapper;
   
   Operations3Bit(int opcode, Consumer<Integer> act, int cycles) {
      wrapper = new Operation(opcode, act, cycles, name());
   }
   
   public static Operation getHalt() {
      return Halt.wrapper;
   }
   
   public static Operation getUnknown() {
      return Unknown.wrapper;
   }
}
