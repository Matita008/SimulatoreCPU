package io.matita08.logic;

import io.matita08.*;
import io.matita08.GUI.*;
import io.matita08.data.*;
import io.matita08.value.Value;

import java.awt.event.ActionEvent;

/**
 * CPU Execution Engine - Instruction Cycle Management.
 * <p>
 * This class manages the CPU's instruction execution cycle, implementing
 * the fetch-decode-execute paradigm. It coordinates instruction processing,
 * memory access, and program counter management.
 * </p>
 * <p>
 * Key responsibilities:
 * <ul>
 * <li>Single-step and multi-step execution control</li>
 * <li>Instruction fetch from memory</li>
 * <li>Instruction decoding and operation lookup</li>
 * <li>Execution phase coordination</li>
 * <li>Memory access operations</li>
 * <li>Program counter advancement</li>
 * </ul>
 * </p>
 *
 * @author Matita008
 * @version 1.5
 * @since 1.0
 */
public class Execution {
   
   /**
    * Flag indicating whether a step operation has been performed.
    * Used for GUI update coordination and execution tracking.
    */
   public static boolean stepped = false;
   
   /**
    * Executes multiple CPU instruction steps up to the specified maximum.
    * <p>
    * Continues executing CPU steps until either the maximum count is reached
    * or the CPU enters a halted state (unable to step further).
    * </p>
    *
    * @param countMax the maximum number of steps to execute
    */
   public static void step(int countMax) {
      for (int i = 0; i < countMax; i++) {
         if(!step()) return;
      }
   }
   
   /**
    * Executes a single CPU instruction step.
    * <p>
    * Advances the CPU through one phase of the instruction cycle
    * (Fetch, Decode, or Execute). Handles halt conditions and
    * coordinates GUI updates.
    * </p>
    *
    * @return true if the step was successful, false if CPU is halted
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
   
   /**
    * FETCH Phase - Retrieves the next instruction from memory.
    * <p>
    * Performs the instruction fetch cycle:
    * <ol>
    * <li>Clears ALU operation display</li>
    * <li>Sets MAR to current PC value and increments PC</li>
    * <li>Loads instruction from memory into IR</li>
    * <li>Prepares for decode phase</li>
    * </ol>
    * </p>
    */
   public static void fetch() {
      ControlUnit.ALUOpcode = "";
      setMarR(next());
      Registers.setIr(Registers.getMDR());
      ControlUnit.next = Phase.Decode;
      ControlUnit.currentCycle = 0;
      ControlUnit.totalCycles = -1;
      ControlUnit.opcode = Operation.Unknown;
   }
   
   /**
    * DECODE Phase - Decodes the instruction and prepares for execution.
    * <p>
    * Performs the instruction decode cycle:
    * <ol>
    * <li>Looks up the operation based on IR contents</li>
    * <li>Sets up cycle counts for the instruction</li>
    * <li>Prepares for execute phase</li>
    * </ol>
    * </p>
    */
   public static void decode() {
      ControlUnit.next = Phase.Execute;
      ControlUnit.opcode = Operation.get(Registers.getIr().get());
      ControlUnit.totalCycles = ControlUnit.currentCycle = ControlUnit.opcode.cycles;
      ControlUnit.currentCycle++;
   }
   
   /**
    * EXECUTE Phase - Executes the decoded instruction.
    * <p>
    * Performs the instruction execution cycle:
    * <ol>
    * <li>Calls the instruction's execution logic for current cycle</li>
    * <li>Handles multi-cycle instruction coordination</li>
    * <li>Returns to fetch phase when execution completes</li>
    * </ol>
    * </p>
    */
   public static void execute() {
      ControlUnit.opcode.action.accept(ControlUnit.currentCycle);
      if(ControlUnit.currentCycle == 1) {
         ControlUnit.next = Phase.Fetch;
      }
   }
   
   /**
    * GUI event handler for single-step execution.
    * <p>
    * ActionListener implementation for GUI step buttons.
    * Executes a single CPU step in a background thread.
    * </p>
    *
    * @param ignored the ActionEvent (not used)
    */
   public static void step(ActionEvent ignored) {
      Utils.runOnNewThread(Execution::step);
   }
   
   /**
    * Gets the next instruction address and increments the program counter.
    * <p>
    * This atomic operation is crucial for proper instruction sequencing.
    * Returns the current PC value and then increments it for the next
    * instruction fetch.
    * </p>
    *
    * @return the current PC value before increment
    */
   public static Value next(){
      return Registers.pc().getAndInc();
   }
   
   /**
    * Sets MAR and performs a memory read operation.
    * <p>
    * Helper method that combines MAR setting with memory data retrieval:
    * <ol>
    * <li>Sets the Memory Address Register to the specified value</li>
    * <li>Reads the data from that memory location into MDR</li>
    * </ol>
    * </p>
    *
    * @param v the memory address to read from
    */
   public static void setMarR(Value v) {
      Registers.setMAR(v);
      Registers.setMDR(Registers.getMC(v));
   }
   
   /**
    * Reads address pointer data during multi-cycle instruction execution.
    * <p>
    * This method handles the address reading phase of instructions that
    * require memory address operands (like LOAD, STO, JPZ). It reads
    * address components from consecutive memory locations and assembles
    * them into the pointer register.
    * </p>
    *
    * @param cycle the current cycle number within the address reading phase
    * @throws AssertionError if cycle number is out of valid bounds
    */
   public static void readPointer(int cycle){
      if(cycle < 0 || cycle > Constants.getAddressSize())
         throw new AssertionError("An error occurred\nDetails: readPointer cycle is OOB, value: " + cycle);
      
      setMarR(next());
      if(Constants.getAddressSize() == 1) {
         Registers.setPointer(Registers.getMDR());
      } else {
         Registers.getPointer().set(Registers.getMDR());
      }
   }
}