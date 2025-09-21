package io.matita08.logic;

import io.matita08.Constants;
import io.matita08.data.*;
import io.matita08.value.Value;

import java.lang.reflect.*;
import java.util.function.Consumer;

/**
 * Represents a CPU operation/instruction in the simulator with dynamic loading capabilities.
 * This class serves as a wrapper for CPU operations that can be executed by the simulation engine.
 *
 * <p>The Operation class uses reflection to dynamically load operation definitions from
 * an external enum class specified by {@link Constants#getOperationEnumName()}. This design
 * allows for flexible operation sets without modifying the core simulation logic.</p>
 *
 * <p>Each operation contains:</p>
 * <ul>
 *   <li><strong>Opcode:</strong> Numeric identifier for the operation</li>
 *   <li><strong>Action:</strong> Consumer function that implements the operation logic</li>
 *   <li><strong>Cycles:</strong> Number of CPU cycles required for execution</li>
 *   <li><strong>Name:</strong> Human-readable operation name</li>
 * </ul>
 *
 * <p>The class maintains static references to special operations like {@code Halt} and
 * {@code Unknown}, and provides utility methods for operation lookup and CPU interaction.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 * @see Constants#getOperationEnumName()
 */
public class Operation {
   
   /**
    * Special operation representing a halt/stop instruction.
    * This operation typically terminates program execution.
    */
   public static Operation Halt;
   
   /**
    * Default operation used when an invalid opcode is encountered.
    * This operation handles error cases gracefully.
    */
   public static Operation Unknown;
   
   /**
    * Array containing all available operations loaded from the external enum.
    * Operations are indexed by their position in the source enum.
    */
   public static Operation[] all;
   
   /**
    * The executable action for this operation.
    * Takes an integer parameter (typically an operand) and performs the operation.
    */
   public final Consumer<Integer> action;
   
   /**
    * The numeric opcode identifier for this operation.
    * Used to match instructions to their corresponding operations.
    */
   public final int opcode;
   
   /**
    * The number of CPU cycles required to execute this operation.
    * Used for accurate timing simulation.
    */
   public final int cycles;
   
   /**
    * The human-readable name of this operation.
    * Used for debugging and display purposes.
    */
   public final String name;
   
   /**
    * Static initialization block that dynamically loads operations from the external enum.
    * Uses reflection to instantiate operation objects based on the class name provided
    * by {@link Constants#getOperationEnumName()}.
    *
    * @throws AssertionError if the operation enum class is invalid or missing required fields/methods
    * @throws InternalError if the operation enum class or its components are not accessible
    * @throws RuntimeException if reflection operations fail unexpectedly
    */
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
   
   /**
    * Constructs a new Operation with the specified parameters.
    * This constructor is typically called during the static initialization process
    * when loading operations from the external enum.
    *
    * @param opcode the numeric opcode identifier
    * @param act the consumer function that implements the operation logic
    * @param cycles the number of CPU cycles required for execution
    * @param name the human-readable operation name
    */
   Operation(int opcode, Consumer<Integer> act, int cycles, String name) {
      action = act;
      this.opcode = opcode;
      this.cycles = cycles;
      this.name = name;
   }
   
   /**
    * Error tracking flag to prevent duplicate error output.
    * Used by {@link #get(int)} to avoid printing operation lists multiple times.
    */
   static boolean err;
   
   /**
    * Retrieves an operation by its opcode identifier.
    * If no matching operation is found, returns the {@link #Unknown} operation
    * and prints diagnostic information about available operations.
    *
    * @param opcode the opcode to search for
    * @return the matching Operation, or {@link #Unknown} if not found
    */
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
   
   /**
    * Sets the number of remaining execution cycles for the current operation.
    * When cycles reach 1, automatically sets the next phase to Fetch for the next instruction.
    *
    * @param remaining the number of cycles remaining for the current operation
    */
   public static void setRemainingCycles(int remaining) {
      ControlUnit.currentCycle = remaining;
      if(ControlUnit.currentCycle == 1) {
         ControlUnit.next = Phase.Fetch;
      }
   }
   
   //BEGIN SECTION Wrapper functions
   
   /**
    * Gets the address size used by the CPU architecture.
    * This is a convenience method that delegates to {@link Constants#getAddressSize()}.
    *
    * @return the address size in bytes/words
    */
   public static int getAddressSize() {
      return Constants.getAddressSize();
   }
   
   /**
    * Initiates a pointer read operation for the specified cycle.
    * This method delegates to the execution engine to handle pointer dereferencing.
    *
    * @param cycle the execution cycle when the read should occur
    */
   public static void readPointer(int cycle) {
      Execution.readPointer(cycle);
   }
   
   /**
    * Sets a value in central memory at the specified address.
    * This method updates both the MAR (Memory Address Register) and MDR (Memory Data Register)
    * before writing to central memory.
    *
    * @param address the memory address where the value should be stored
    * @param value the value to store in memory
    */
   public static void setMC(Value address, Value value) {
      Registers.setMAR(address);
      Registers.setMDR(value);
      Registers.setMC(address, value);
   }
   
   /**
    * Initiates a memory read operation from the specified address.
    * This method sets up the Memory Address Register for reading and
    * delegates to the execution engine.
    *
    * @param address the memory address to read from
    */
   public static void readMC(Value address) {
      Execution.setMarR(address);
   }
   
   /**
    * Gets the current Program Counter value and increments it for the next instruction.
    * This method is commonly used during instruction fetching to advance program execution.
    *
    * @return the current PC value before incrementing
    */
   public static Value getAndIncPc(){
      return Execution.next();
   }
}