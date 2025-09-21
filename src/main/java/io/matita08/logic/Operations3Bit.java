package io.matita08.logic;

import io.matita08.Constants;
import io.matita08.data.Registers;
import io.matita08.value.*;

import java.util.function.Consumer;

/**
 * 3-Bit CPU Instruction Set Implementation.
 * <p>
 * This enum defines a complete instruction set for a 3-bit CPU architecture,
 * implementing common operations such as memory access, arithmetic, I/O,
 * and control flow instructions.
 * </p>
 * <p>
 * Instruction Set:
 * <ul>
 * <li><strong>STO (0)</strong> - Store accumulator to memory address</li>
 * <li><strong>LOAD (1)</strong> - Load value from memory address to accumulator</li>
 * <li><strong>OUT (2)</strong> - Output accumulator value to output buffer</li>
 * <li><strong>IN (3)</strong> - Input value from input buffer to accumulator</li>
 * <li><strong>ADD (4)</strong> - Add RegB to accumulator with flag updates</li>
 * <li><strong>SET (5)</strong> - Copy accumulator value to RegB</li>
 * <li><strong>JPZ (6)</strong> - Jump to address if zero flag is set</li>
 * <li><strong>HALT (7)</strong> - Stop CPU execution</li>
 * </ul>
 * </p>
 * <p>
 * Each instruction is implemented with multi-cycle execution support,
 * handling address reading and memory operations across multiple CPU cycles.
 * </p>
 *
 * @author Matita008
 * @version 1.5
 * @since 1.0
 */
@SuppressWarnings({"unused", "CodeBlock2Expr"})  //Loaded with reflection, Please keep code blocks, so if I want to edit I know how
public enum Operations3Bit { //Using prof default table
   
   /**
    * STO (Store) - Opcode 0.
    * <p>
    * Stores the current accumulator value to the memory address specified
    * by the pointer register. This is a multi-cycle operation that first
    * reads the target address, then performs the memory write.
    * </p>
    * <p>
    * Execution cycles: 1 + address size (typically 3 cycles total)
    * </p>
    * <ul>
    * <li>Cycles 2-N: Read pointer address from memory</li>
    * <li>Cycle 1: Store accumulator to memory[pointer]</li>
    * </ul>
    */
   sto(0, n->{
      if(n != 1) Operation.readPointer(n - 1);
      else Operation.setMC(Registers.getPointer(), Registers.getAcc());
   }, 1 + Operation.getAddressSize()),
   
   /**
    * LOAD (Load) - Opcode 1.
    * <p>
    * Loads a value from the memory address specified by the pointer register
    * into the accumulator. This is a multi-cycle operation that first reads
    * the target address, then performs the memory read.
    * </p>
    * <p>
    * Execution cycles: 1 + address size (typically 3 cycles total)
    * </p>
    * <ul>
    * <li>Cycles 2-N: Read pointer address from memory</li>
    * <li>Cycle 1: Load memory[pointer] into accumulator</li>
    * </ul>
    */
   load(1, n->{
      if(n == 1) {
         Operation.readMC(Registers.getPointer());
         Registers.setAcc(Registers.getMDR());
      }
      else Operation.readPointer(n - 1);
   }, 1 + Operation.getAddressSize()),
   
   /**
    * OUT (Output) - Opcode 2.
    * <p>
    * Copies the current accumulator value to the output buffer register.
    * This provides the interface between the CPU and output devices.
    * </p>
    * <p>
    * Execution cycles: 1 (single-cycle operation)
    * </p>
    */
   out(2, n->{Registers.setBufOut(Registers.getAcc());}, 1),
   
   /**
    * IN (Input) - Opcode 3.
    * <p>
    * Copies the current input buffer value to the accumulator register.
    * This provides the interface between input devices and the CPU.
    * </p>
    * <p>
    * Execution cycles: 1 (single-cycle operation)
    * </p>
    */
   in(3, n->{Registers.setAcc(Registers.getBufIn());}, 1),
   
   /**
    * ADD (Addition) - Opcode 4.
    * <p>
    * Performs arithmetic addition of the accumulator and RegB, storing
    * the result in the accumulator. Updates the Zero and Overflow flags
    * based on the result.
    * </p>
    * <p>
    * Flag Updates:
    * <ul>
    * <li>Overflow flag: Set if result exceeds maximum value</li>
    * <li>Zero flag: Set if result equals zero</li>
    * </ul>
    * </p>
    * <p>
    * Execution cycles: 1 (single-cycle operation)
    * </p>
    */
   add(4, n->{
      Registers.setOverflow(Registers.getAcc().get() + Registers.getRegB().get() > Constants.getValueMax());
      Registers.setAcc(Registers.getAcc().add(Registers.getRegB()));
      Registers.setZero(Registers.getAcc().equals(0));
   }, 1),
   
   /**
    * SET (Set Register B) - Opcode 5.
    * <p>
    * Copies the current accumulator value to RegB. This is typically used
    * to prepare operands for arithmetic operations.
    * </p>
    * <p>
    * Execution cycles: 1 (single-cycle operation)
    * </p>
    */
   set(5, n->Registers.setRegB(Registers.getAcc()), 1),
   
   /**
    * JPZ (Jump if Zero) - Opcode 6.
    * <p>
    * Conditional jump instruction that jumps to the specified address if
    * the Zero flag is set. If the flag is not set, execution continues
    * with the next instruction.
    * </p>
    * <p>
    * Execution cycles: 1 + address size (typically 3 cycles total)
    * </p>
    * <ul>
    * <li>Cycles N-2: Read target address from memory</li>
    * <li>Cycle 1: Check Zero flag and jump if set, otherwise continue</li>
    * </ul>
    */
   jpz(6, n->{
      if(n == Operation.getAddressSize() + 1) {
         if(Registers.getZero()) {
            Registers.pc().getAndInc();
            Registers.pc().getAndInc();
            Operation.setRemainingCycles(1);
         }
         else Operation.readPointer(n - 1);
      }
      else if(n != 1) Operation.readPointer(n - 1);
      else Registers.pc().set(Registers.getPointer());
   }, 1 + Operation.getAddressSize()),
   
   /**
    * HALT (Halt Execution) - Opcode 7.
    * <p>
    * Stops CPU execution. This instruction causes the CPU to enter
    * a halted state where no further instructions are processed.
    * </p>
    * <p>
    * Execution cycles: 1 (single-cycle operation)
    * </p>
    */
   Halt(7, n->{}, 1),
   
   /**
    * UNKNOWN (Invalid Opcode Handler).
    * <p>
    * Special operation used as a fallback for invalid or unrecognized
    * opcodes. Provides graceful handling of instruction decode errors.
    * </p>
    */
   Unknown(n->{},1);
   
   /**
    * Array containing all available operations in this instruction set.
    */
   public static final Operations3Bit[] all = values();
   
   /**
    * The Operation wrapper object that provides the execution interface.
    */
   public final Operation wrapper;
   
   /**
    * Constructs an instruction with opcode, execution logic, and cycle count.
    *
    * @param opcode the numeric opcode identifier
    * @param act the execution logic as a Consumer accepting cycle number
    * @param cycles the number of CPU cycles required for execution
    */
   Operations3Bit(int opcode, Consumer<Integer> act, int cycles) {
      wrapper = new Operation(opcode, act, cycles, name());
   }
   
   /**
    * Constructs a special instruction without an opcode (e.g., Unknown).
    *
    * @param act the execution logic as a Consumer accepting cycle number
    * @param cycles the number of CPU cycles required for execution
    */
   Operations3Bit(Consumer<Integer> act, int cycles) {
      wrapper = new Operation(-1, act, cycles, "");
   }
   
   /**
    * Gets the Operation wrapper for this instruction.
    *
    * @return the Operation object containing execution logic and metadata
    */
   public Operation get() {
      return wrapper;
   }
   
   /**
    * Gets the HALT operation for CPU termination.
    *
    * @return the HALT operation wrapper
    */
   public static Operation getHalt() {
      return Halt.wrapper;
   }
   
   /**
    * Gets the UNKNOWN operation for invalid opcodes.
    *
    * @return the UNKNOWN operation wrapper
    */
   public static Operation getUnknown() {
      return Unknown.wrapper;
   }
}