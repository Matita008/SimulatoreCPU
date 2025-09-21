//TODO this is *NOT* completed, so use at your own risk
package io.matita08.logic;

import io.matita08.data.Registers;

import java.util.function.Consumer;

/**
 * 4-Bit CPU Instruction Set Implementation.
 * This enum defines a more comprehensive instruction set for a 4-bit CPU architecture,
 * implementing extended operations including subtraction, unconditional jumps,
 * and additional conditional branching capabilities.
 *
 * <p>Extended Instruction Set:</p>
 * <ul>
 *   <li><strong>LOAD (0)</strong> - Load value from memory address to accumulator</li>
 *   <li><strong>STO (1)</strong> - Store accumulator to memory address</li>
 *   <li><strong>SET (2)</strong> - Copy accumulator value to RegB</li>
 *   <li><strong>IN (3)</strong> - Input value from input buffer to accumulator</li>
 *   <li><strong>OUT (4)</strong> - Output accumulator value to output buffer</li>
 *   <li><strong>ADD (5)</strong> - Add RegB to accumulator</li>
 *   <li><strong>SUB (6)</strong> - Subtract RegB from accumulator</li>
 *   <li><strong>JMP (7)</strong> - Unconditional jump to address</li>
 *   <li><strong>JPZ (8)</strong> - Jump to address if zero flag is set</li>
 *   <li><strong>JPO (9)</strong> - Jump to address if overflow flag is set</li>
 *   <li><strong>HALT (15)</strong> - Stop CPU execution</li>
 * </ul>
 *
 * <p>This instruction set provides enhanced control flow capabilities
 * compared to the 3-bit version, including unconditional jumps and
 * overflow-based conditional branching.</p>
 *
 * <p><strong>Note:</strong> This is an alternative instruction set that can be
 * selected via command-line arguments. Many operations are currently implemented
 * as stubs and require completion for full functionality.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 * @see Operations3Bit
 * @see Operation
 */
@SuppressWarnings("unused")  //Loaded with reflection
public enum Operations4Bit {
   
   /**
    * LOAD (Load) - Opcode 0.
    * Loads a value from the memory address specified by the pointer register
    * into the accumulator. This is a multi-cycle operation.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 2</p>
    */
   load(0,n->{}, 2),
   
   /**
    * STO (Store) - Opcode 1.
    * Stores the current accumulator value to the memory address specified
    * by the pointer register. This is a multi-cycle operation.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 2</p>
    */
   sto(1, n->{}, 2),
   
   /**
    * SET (Set Register B) - Opcode 2.
    * Copies the current accumulator value to RegB. This is typically used
    * to prepare operands for arithmetic operations.
    *
    * <p>Execution cycles: 1 (single-cycle operation)</p>
    */
   set(2, n->{Registers.setRegB(Registers.getAcc());}, 1),
   
   /**
    * IN (Input) - Opcode 3.
    * Copies the current input buffer value to the accumulator register.
    * This provides the interface between input devices and the CPU.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 1 (single-cycle operation)</p>
    */
   in(3, n->{}, 1),
   
   /**
    * OUT (Output) - Opcode 4.
    * Copies the current accumulator value to the output buffer register.
    * This provides the interface between the CPU and output devices.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 1 (single-cycle operation)</p>
    */
   out(4, n->{}, 1),
   
   /**
    * ADD (Addition) - Opcode 5.
    * Performs arithmetic addition of the accumulator and RegB, storing
    * the result in the accumulator. Should update CPU flags.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 1 (single-cycle operation)</p>
    */
   add(5, n->{}, 1),
   
   /**
    * SUB (Subtraction) - Opcode 6.
    * Performs arithmetic subtraction of RegB from the accumulator, storing
    * the result in the accumulator. Should update CPU flags.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 1 (single-cycle operation)</p>
    */
   sub(6, n->{}, 1),
   
   /**
    * JMP (Unconditional Jump) - Opcode 7.
    * Unconditionally jumps to the address specified by the pointer register.
    * This is a multi-cycle operation that reads the target address then jumps.
    *
    * <p>Execution logic:</p>
    * <ul>
    *   <li>Cycle 2: Read target address from memory into pointer</li>
    *   <li>Cycle 1: Set PC to pointer value</li>
    * </ul>
    *
    * <p>Execution cycles: 2</p>
    */
   jmp(7, n->{
      if(n == 1) {
         Registers.pc().set(Registers.getPointer().get());
      } else {
         Execution.setMarR(Registers.pc().getAndInc());
         Registers.setPointer(Registers.getMDR());
      }
   }, 2),
   
   /**
    * JPZ (Jump if Zero) - Opcode 8.
    * Conditional jump instruction that jumps to the specified address if
    * the Zero flag is set. If the flag is not set, execution continues
    * with the next instruction.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 1</p>
    */
   jpz(8, n->{}, 1),
   
   /**
    * JPO (Jump if Overflow) - Opcode 9.
    * Conditional jump instruction that jumps to the specified address if
    * the Overflow flag is set. If the flag is not set, execution continues
    * with the next instruction.
    *
    * <p><strong>Status:</strong> Stub implementation - requires completion</p>
    * <p>Execution cycles: 1</p>
    */
   jpo(9, n->{}, 1),
   
   /**
    * HALT (Halt Execution) - Opcode 15.
    * Stops CPU execution. This instruction causes the CPU to enter
    * a halted state where no further instructions are processed.
    *
    * <p>Execution cycles: 1 (single-cycle operation)</p>
    */
   Halt(15, n->{}, 1),
   
   /**
    * UNKNOWN (Invalid Opcode Handler) - Opcode 16.
    * Special operation used as a fallback for invalid or unrecognized
    * opcodes. Provides graceful handling of instruction decode errors.
    */
   Unknown(16, n->{}, 1);
   
   /**
    * Array containing all available operations in this instruction set.
    */
   public static final Operations4Bit[] all = values();
   
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
   Operations4Bit(int opcode, Consumer<Integer> act, int cycles) {
      wrapper = new Operation(opcode, act, cycles, name());
   }
   
   /**
    * Gets the HALT operation for CPU termination.
    * Used by the Operation class during dynamic loading to identify
    * the halt instruction.
    *
    * @return the HALT operation wrapper
    */
   public static Operation getHalt(){
      return Halt.wrapper;
   }
   
   /**
    * Gets the UNKNOWN operation for invalid opcodes.
    * Used by the Operation class during dynamic loading to handle
    * unrecognized instruction codes.
    *
    * @return the UNKNOWN operation wrapper
    */
   public static Operation getUnknown(){
      return Unknown.wrapper;
   }
}