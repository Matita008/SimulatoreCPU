//TODO LowPriority: maybe better in a data package
package io.matita08.GUI;

import io.matita08.Constants;
import io.matita08.value.*;

import java.util.ArrayList;

public final class Registers {
   private static final ArrayList<Value> MC = new ArrayList<>(Constants.getMC_Size());
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
   
   private static Flag zero = new Flag();
   
   static {
      for (int i = 0; i < Constants.getMC_Size(); i++) {
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
      if(pos >= Constants.getMC_Size()) return Value.nullValue;
      while(MC.size() <= pos) MC.add(Value.getNew());
      return MC.get(pos);
   }
   
   public static void setMC(DoubleValue pos, Value val) {
      setMC(pos.get(), val);
   }
   
   public static void setMC(int index, Value val) {
      if(index >= Constants.getMC_Size()) return;
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
      Registers.bufIn = bufIn;
   }
   
   public static Value getBufOut() {
      return bufOut;
   }
   
   public static void setBufOut(Value bufOut) {
      Registers.bufOut = bufOut;
   }
   
   public static Flag getZero() {
      return zero;
   }
   
   public static void setZero(Flag zero) {
      Registers.zero = zero;
   }
}