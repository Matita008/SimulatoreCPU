package io.matita08.data;

import io.matita08.Constants;
import io.matita08.value.*;

import java.util.ArrayList;

public final class Registers {
   private static final ArrayList<Value> MC = new ArrayList<>(Constants.getMCSize());
   private static final DoubleValue pc = new DoubleValue(0);
   public static int modFlag = 0;
   
   private static Value ir = Value.getNewAddress();
   private static Value pointer = Value.getNewAddress();
   private static Value mar = Value.getNewAddress();
   private static Value mdr = Value.getNewAddress();
   
   private static Value Acc = Value.getNew();
   private static Value regB = Value.getNew();
   private static Value bufIn = Value.getNew();
   private static Value bufOut = Value.getNew();
   
   static {
      for (int i = 0; i < Constants.getMCSize(); i++) {
         MC.add(Value.getNew());
      }
   }
   
   private Registers() { throw new AssertionError("...\nPlease don't\n\nIk you're using reflection\n"); }
   
   public static DoubleValue pc() {
      return pc;
   }
   
   public static Value getIr() {
      return ir;
   }
   
   public static void setIr(Value ir) {
      Registers.ir = ir;
   }
   
   public static Value getMAR() {
      return mar;
   }
   
   public static void setMAR(Value mar) {
      modFlag = 2 | modFlag;
      Registers.mar = mar;
   }
   
   public static Value getMDR() {
      return mdr;
   }
   
   public static void setMDR(Value mdr) {
      modFlag = 2 | modFlag;
      Registers.mdr = mdr;
   }
   
   public static Value getAcc() {
      return Acc;
   }
   
   public static void setAcc(Value acc) {
      modFlag = 4 | modFlag;
      Acc = acc;
   }
   
   public static Value getRegB() {
      return regB;
   }
   
   public static void setRegB(Value regB) {
      modFlag = 4 | modFlag;
      Registers.regB = regB;
   }
   
   public static Value getMC(Value pos) {
      if(pos instanceof DoubleValue dv) return getMC(dv);
      System.out.println("here");
      return Value.nullValue;
   }
   
   public static Value getMC(DoubleValue pos) {
      return getMC(pos.get());
   }
   
   public static Value getMC(int pos) {
      if(pos >= Constants.getMCSize()) return Value.nullValue;
      while(MC.size() <= pos) MC.add(Value.getNew());
      return MC.get(pos);
   }
   
   public static void setMC(DoubleValue pos, Value val) {
      setMC(pos.get(), val);
   }
   
   public static void setMC(int index, Value val) {
      if(index >= Constants.getMCSize()) return;
      MC.set(index, val);
      modFlag = 1 | modFlag;
   }
   
   public static void setMC(Value pos, Value val) {
      if(pos instanceof DoubleValue dv) setMC(dv, val);
   }
   
   public static Value getPointer() {
      return pointer;
   }
   
   public static void setPointer(Value pointer) {
      modFlag = 2 | modFlag;
      Registers.pointer = pointer;
   }
   
   public static Value getBufIn() {
      return bufIn;
   }
   
   public static void setBufIn(Value bufIn) {
      modFlag = 8 | modFlag;
      Registers.bufIn = bufIn;
   }
   
   public static Value getBufOut() {
      return bufOut;
   }
   
   public static void setBufOut(Value bufOut) {
      modFlag = 8 | modFlag;
      Registers.bufOut = bufOut;
   }
   
   public static boolean getZero() {
      return Flags.get(FlagsConstants.ZERO);
   }
   
   public static void setZero(boolean zero) {
      Flags.set(FlagsConstants.ZERO, zero);
   }
   
   public static boolean getOverflow() {
      return Flags.get(FlagsConstants.OVERFLOW);
   }
   
   public static void setOverflow(boolean overflow) {
      Flags.set(FlagsConstants.OVERFLOW, overflow);
   }
}