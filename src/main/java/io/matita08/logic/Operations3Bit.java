package io.matita08.logic;

import io.matita08.Constants;
import io.matita08.data.Registers;
import io.matita08.value.*;

import java.util.function.Consumer;

@SuppressWarnings({"unused", "CodeBlock2Expr"})  //Loaded with reflection, Please keep code blocks, so if I want to edit I know how
public enum Operations3Bit {//Using prof default table
   sto(0, n->{
      if(n != 1) Operation.readPointer(n - 1);
      else Operation.setMC(Registers.getPointer(), Registers.getAcc());
   }, 1 + Operation.getAddressSize()),
   
   load(1, n->{
      if(n == 1) {
         Operation.readMC(Registers.getPointer());
         Registers.setAcc(Registers.getMDR());
      }
      else Operation.readPointer(n - 1);
   }, 1 + Operation.getAddressSize()),
   
   out(2, n->{Registers.setBufOut(Registers.getAcc());}, 1),
   in(3, n->{Registers.setAcc(Registers.getBufIn());}, 1),
   
   add(4, n->{
      Registers.setOverflow(Registers.getAcc().get() + Registers.getRegB().get() > Constants.getValueMax());
      Registers.setAcc(Registers.getAcc().add(Registers.getRegB()));
      Registers.setZero(Registers.getAcc().equals(0));
   }, 1),
   
   set(5, n->Registers.setRegB(Registers.getAcc()), 1),
   
   jpz(6, n->{
      if(n == Operation.getAddressSize() + 1) {
         if(Registers.getZero()) {
            Registers.pc().add(new SingleValue(Operation.getAddressSize(), false));
            Operation.setRemainingCycles(1);
         }
         else Operation.readPointer(n - 1);
      }
      else if(n != 1) Operation.readPointer(n - 1);
      else Registers.pc().set(Registers.getPointer());
   }, 1 + Operation.getAddressSize()),
   
   Halt(7, n->{}, 1),
   Unknown(n->{},1);
   
   public static final Operations3Bit[] all = values();
   public final Operation wrapper;
   
   Operations3Bit(int opcode, Consumer<Integer> act, int cycles) {
      wrapper = new Operation(opcode, act, cycles, name());
   }
   
   Operations3Bit(Consumer<Integer> act, int cycles) {
      wrapper = new Operation(-1, act, cycles, "");
   }
   
   public Operation get() {
      return wrapper;
   }
   
   public static Operation getHalt() {
      return Halt.wrapper;
   }
   
   public static Operation getUnknown() {
      return Unknown.wrapper;
   }
}
