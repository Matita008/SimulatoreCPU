package io.matita08.logic;

import io.matita08.Constants;
import io.matita08.data.ControlUnit;

import java.lang.reflect.*;
import java.util.function.Consumer;

public class Operation {
   
   public static Operation Halt;
   public static Operation Unknown;
   public static Operation[] all;
   public final Consumer<Integer> action;
   public final int opcode;
   public final int cycles;
   public final String name;

   static {
      try {
         Class<?> c = Class.forName(Constants.getOperationEnumName());
         Object allField = c.getField("all").get(null);
         Field wr = c.getField("wrapper");
         int sz = Array.getLength(allField);
         all = new Operation[sz];
         for (int i = 0; i < sz; i++) {
            all[i] = (Operation)wr.get(Array.get(allField, i));
         }
         Halt = (Operation)c.getMethod("getHalt").invoke(null);
         Unknown = (Operation)c.getMethod("getUnknown").invoke(null);
         
      } catch (ClassNotFoundException e) {
         throw new AssertionError("Constants.OperationEnumName is set to an invalid class name: " + Constants.getOperationEnumName(), e);
      } catch (NoSuchFieldException e) {
         throw new AssertionError("Constants.OperationEnumName is set to a class who doesn't have a static all[] field: " + Constants.getOperationEnumName(), e);
      } catch (IllegalAccessException e) {
         throw new InternalError("Constants.OperationEnumName or one of its component is not accessible while it should: " + Constants.getOperationEnumName(),e);
      } catch (NullPointerException npe) {
         throw new AssertionError("Constants.OperationEnumName is set to a class who doesn't have a static all[] field or doesn't have getHalt/getUnknown method: " + Constants.getOperationEnumName(), npe);
      } catch (InvocationTargetException e) {
         throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
         throw new AssertionError("Constants.OperationEnumName is set to a class who doesn't have a static getUnknown and static getHalt field: " + Constants.getOperationEnumName(), e);
      }
   }
   
   Operation(int opcode, Consumer<Integer> act, int cycles, String name) {
      action = act;
      this.opcode = opcode;
      this.cycles = cycles;
      this.name = name;
   }
   static boolean err;
   public static Operation get(int opcode) {
      for (Operation op: all) {
         if(op.opcode == opcode) return op;
      }
      System.out.println("Invalid opcode received: " + opcode);
      if(!err) {
         err = true;
         for(Operation op : all) {
            System.out.println(op.opcode + " " + op.name);
         }
      }
      return Unknown;
   }
   
   public static int getAddressSize() {
      return Constants.getAddressSize();
   }
   
   public static void readPointer(int cycle) {
      Execution.readPointer(cycle);
   }
   
   public static void setRemainingCycles(int remaining) {
      ControlUnit.currentCycle = remaining;
      if(ControlUnit.currentCycle == 1) {
         ControlUnit.next = Phase.Fetch;
      }
   }
}
