package io.matita08.data;

import io.matita08.Constants;
import io.matita08.value.Value;
import io.matita08.value.DoubleValue;

import java.util.ArrayList;

/**
 * CPU Register and Memory Management System.
 * <p>
 * This class manages all CPU registers, Central Memory, and provides centralized
 * access to CPU state. It handles modification tracking for efficient GUI updates
 * and integrates with the flag system for arithmetic operations.
 * </p>
 *
 * <p>
 * <strong>Managed Components:</strong>
 * <ul>
 * <li><strong>Core Registers:</strong> PC, IR, MAR, MDR, Accumulator, RegB</li>
 * <li><strong>Memory:</strong> Central Memory (MC) with configurable size</li>
 * <li><strong>I/O:</strong> Input and Output buffer registers</li>
 * <li><strong>Addressing:</strong> Pointer register for indirect addressing</li>
 * <li><strong>Flags:</strong> Interface to CPU flags (Zero, Overflow)</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Modification Tracking:</strong>
 * The {@code modFlag} field uses bit flags to track which components have been
 * modified since the last GUI update:
 * <ul>
 * <li>Bit 0: Central Memory modified</li>
 * <li>Bit 1: MAR/MDR/Pointer modified</li>
 * <li>Bit 2: Accumulator/RegB modified</li>
 * <li>Bit 3: I/O buffers modified</li>
 * </ul>
 * </p>
 *
 * @author Matita008
 * @version 1.5
 * @since 1.0
 */
public final class Registers {
   
   /**
    * Central Memory storage as an ArrayList of Value objects.
    * <p>
    * Size is determined by {@link Constants#getMCSize()}. Each element
    * represents one memory location that can store a Value.
    * </p>
    */
   private static final ArrayList<Value> MC = new ArrayList<>(Constants.getMCSize());
   
   /**
    * Program Counter register - tracks the current instruction address.
    * <p>
    * Automatically incremented during instruction fetch. Supports
    * both sequential execution and jump operations.
    * </p>
    */
   private static final DoubleValue pc = new DoubleValue(0);
   
   /**
    * Modification flags for tracking which components need GUI updates.
    * <p>
    * Bit field structure:
    * <ul>
    * <li>Bit 0 (0x01): Central Memory modified</li>
    * <li>Bit 1 (0x02): MAR/MDR/Pointer modified</li>
    * <li>Bit 2 (0x04): Accumulator/RegB modified</li>
    * <li>Bit 3 (0x08): I/O buffers modified</li>
    * </ul>
    * </p>
    */
   public static int modFlag = 0;
   
   // CPU Registers
   /** Instruction Register - holds the current instruction being executed. */
   private static Value ir = Value.getNewAddress();
   
   /** Address Pointer - used for indirect addressing operations. */
   private static Value pointer = Value.getNewAddress();
   
   /** Memory Address Register - holds addresses for memory operations. */
   private static Value mar = Value.getNewAddress();
   
   /** Memory Data Register - holds data for memory read/write operations. */
   private static Value mdr = Value.getNewAddress();
   
   /** Accumulator register - primary arithmetic and logic operations register. */
   private static Value Acc = Value.getNew();
   
   /** General purpose Register B - secondary operand for arithmetic operations. */
   private static Value regB = Value.getNew();
   
   /** Input Buffer - receives data from input devices. */
   private static Value bufIn = Value.getNew();
   
   /** Output Buffer - sends data to output devices. */
   private static Value bufOut = Value.getNew();
   
   static {
      // Initialize Central Memory with default values
      for (int i = 0; i < Constants.getMCSize(); i++) {
         MC.add(Value.getNew());
      }
   }
   
   /**
    * Private constructor to prevent instantiation.
    * <p>
    * This is a utility class providing only static methods for register access.
    * </p>
    *
    * @throws AssertionError always, to prevent instantiation
    */
   private Registers() {
      throw new AssertionError("...\nPlease don't\n\nIk you're using reflection\n");
   }
   
   /**
    * Gets the Program Counter register.
    * <p>
    * The PC automatically advances during instruction fetch and can be
    * modified by jump instructions for flow control.
    * </p>
    *
    * @return the PC register as a DoubleValue
    */
   public static DoubleValue pc() {
      return pc;
   }
   
   /**
    * Gets the current Instruction Register value.
    * <p>
    * Contains the instruction currently being decoded or executed.
    * </p>
    *
    * @return the current IR value
    */
   public static Value getIr() {
      return ir;
   }
   
   /**
    * Sets the Instruction Register to a new value.
    * <p>
    * Typically called during the instruction fetch phase when loading
    * the next instruction from memory.
    * </p>
    *
    * @param ir the new instruction value to store in IR
    */
   public static void setIr(Value ir) {
      Registers.ir = ir;
   }
   
   /**
    * Gets the current Address Pointer value.
    * <p>
    * Used for indirect addressing in instructions like LOAD and STORE.
    * </p>
    *
    * @return the current pointer value
    */
   public static Value getPointer() {
      return pointer;
   }
   
   /**
    * Sets the Address Pointer and updates modification flags.
    * <p>
    * Updates the pointer register and marks address registers as modified
    * for GUI update coordination.
    * </p>
    *
    * @param pointer the new pointer address value
    */
   public static void setPointer(Value pointer) {
      modFlag = 2 | modFlag;  // Set bit 1
      Registers.pointer = pointer;
   }
   
   /**
    * Gets the current Memory Address Register value.
    * <p>
    * Contains the address for the current or next memory operation.
    * </p>
    *
    * @return the current MAR value
    */
   public static Value getMAR() {
      return mar;
   }
   
   /**
    * Sets the Memory Address Register and updates modification flags.
    * <p>
    * Sets the target address for memory read/write operations and
    * triggers GUI update flags.
    * </p>
    *
    * @param mar the memory address to set
    */
   public static void setMAR(Value mar) {
      modFlag = 2 | modFlag;  // Set bit 1
      Registers.mar = mar;
   }
   
   /**
    * Gets the current Memory Data Register value.
    * <p>
    * Contains data that was read from or will be written to memory.
    * </p>
    *
    * @return the current MDR value
    */
   public static Value getMDR() {
      return mdr;
   }
   
   /**
    * Sets the Memory Data Register and updates modification flags.
    * <p>
    * Sets the data value for memory operations and triggers GUI updates.
    * </p>
    *
    * @param mdr the data value to set
    */
   public static void setMDR(Value mdr) {
      modFlag = 2 | modFlag;  // Set bit 1
      Registers.mdr = mdr;
   }
   
   /**
    * Gets the current Accumulator register value.
    * <p>
    * The accumulator is the primary register for arithmetic and logic
    * operations, serving as both operand and result storage.
    * </p>
    *
    * @return the current Accumulator value
    */
   public static Value getAcc() {
      return Acc;
   }
   
   /**
    * Sets the Accumulator register and updates modification flags.
    * <p>
    * Updates the accumulator value and marks arithmetic registers
    * as modified for GUI updates.
    * </p>
    *
    * @param acc the new Accumulator value
    */
   public static void setAcc(Value acc) {
      modFlag = 4 | modFlag;  // Set bit 2
      Acc = acc;
   }
   
   /**
    * Gets the current Register B value.
    * <p>
    * Register B serves as the secondary operand for arithmetic operations
    * and general-purpose temporary storage.
    * </p>
    *
    * @return the current Register B value
    */
   public static Value getRegB() {
      return regB;
   }
   
   /**
    * Sets Register B and updates modification flags.
    * <p>
    * Updates RegB and triggers GUI update flags for arithmetic registers.
    * </p>
    *
    * @param regB the new Register B value
    */
   public static void setRegB(Value regB) {
      modFlag = 4 | modFlag;  // Set bit 2
      Registers.regB = regB;
   }
   
   /**
    * Gets the current Input Buffer value.
    * <p>
    * The input buffer receives data from external input devices
    * and makes it available to the CPU via the IN instruction.
    * </p>
    *
    * @return the current input buffer value
    */
   public static Value getBufIn() {
      return bufIn;
   }
   
   /**
    * Sets the Input Buffer and updates modification flags.
    * <p>
    * Updates the input buffer (typically from external devices)
    * and marks I/O buffers as modified.
    * </p>
    *
    * @param bufIn the new input buffer value
    */
   public static void setBufIn(Value bufIn) {
      modFlag = 8 | modFlag;  // Set bit 3
      Registers.bufIn = bufIn;
   }
   
   /**
    * Gets the current Output Buffer value.
    * <p>
    * The output buffer holds data sent from the CPU to external
    * output devices via the OUT instruction.
    * </p>
    *
    * @return the current output buffer value
    */
   public static Value getBufOut() {
      return bufOut;
   }
   
   /**
    * Sets the Output Buffer and updates modification flags.
    * <p>
    * Updates the output buffer (typically for external devices)
    * and marks I/O buffers as modified.
    * </p>
    *
    * @param bufOut the new output buffer value
    */
   public static void setBufOut(Value bufOut) {
      modFlag = 8 | modFlag;  // Set bit 3
      Registers.bufOut = bufOut;
   }
   
   // Central Memory Access Methods
   
   /**
    * Reads a value from Central Memory at the specified address.
    * <p>
    * Supports both single values and double values as addresses.
    * Returns null value if the address is invalid or out of bounds.
    * </p>
    *
    * @param pos the memory address as a Value object
    * @return the value stored at that address, or null value if invalid
    */
   public static Value getMC(Value pos) {
      if (pos instanceof DoubleValue dv) return getMC(dv);
      System.out.println("Invalid address type in getMC: " + pos.getClass());
      return Value.nullValue;
   }
   
   /**
    * Reads a value from Central Memory using a DoubleValue address.
    * <p>
    * Converts the DoubleValue address to an integer and retrieves
    * the corresponding memory contents.
    * </p>
    *
    * @param pos the memory address as a DoubleValue
    * @return the value stored at that address
    */
   public static Value getMC(DoubleValue pos) {
      return getMC(pos.get());
   }
   
   /**
    * Reads a value from Central Memory at the specified integer address.
    * <p>
    * Provides bounds checking and dynamic memory expansion if necessary.
    * Returns null value for out-of-bounds addresses.
    * </p>
    *
    * @param pos the memory address as an integer
    * @return the value stored at that address, or null value if out of bounds
    */
   public static Value getMC(int pos) {
      if (pos >= Constants.getMCSize()) {
         System.err.println("Memory address out of bounds: " + pos + " >= " + Constants.getMCSize());
         return Value.nullValue;
      }
      if (pos < 0) {
         System.err.println("Negative memory address: " + pos);
         return Value.nullValue;
      }
      
      // Expand memory if necessary (defensive programming)
      while (MC.size() <= pos) MC.add(Value.getNew());
      
      return MC.get(pos);
   }
   
   /**
    * Writes a value to Central Memory using a DoubleValue address.
    * <p>
    * Updates both the memory contents and the modification flags
    * for GUI coordination.
    * </p>
    *
    * @param pos the memory address as a DoubleValue
    * @param val the value to write to memory
    */
   public static void setMC(DoubleValue pos, Value val) {
      setMC(pos.get(), val);
   }
   
   /**
    * Writes a value to Central Memory at the specified integer address.
    * <p>
    * Performs bounds checking and updates modification flags.
    * Silently ignores writes to out-of-bounds addresses.
    * </p>
    *
    * @param index the memory address as an integer
    * @param val the value to write to memory
    */
   public static void setMC(int index, Value val) {
      if (index >= Constants.getMCSize() || index < 0) {
         System.err.println("Attempted to write to invalid memory address: " + index);
         return;
      }
      
      // Ensure memory is large enough
      while (MC.size() <= index) MC.add(Value.getNew());
      
      MC.set(index, val);
      modFlag = 1 | modFlag;  // Set bit 0 (memory modified)
   }
   
   /**
    * Writes a value to Central Memory using a generic Value address.
    * <p>
    * Handles type checking and delegates to the appropriate overloaded method.
    * </p>
    *
    * @param pos the memory address as a Value object
    * @param val the value to write to memory
    */
   public static void setMC(Value pos, Value val) {
      if (pos instanceof DoubleValue dv) {
         setMC(dv, val);
      } else {
         System.err.println("Invalid address type for setMC: " + pos.getClass());
      }
   }
   
   // CPU Flags Interface Methods
   
   /**
    * Gets the current state of the Zero flag.
    * <p>
    * The Zero flag is set when arithmetic operations result in zero.
    * Used by conditional jump instructions like JPZ.
    * </p>
    *
    * @return true if the Zero flag is set, false otherwise
    */
   public static boolean getZero() {
      return Flags.get(FlagsConstants.ZERO);
   }
   
   /**
    * Sets the Zero flag state.
    * <p>
    * Typically called by arithmetic instructions to indicate
    * whether their result was zero.
    * </p>
    *
    * @param zero the new state of the Zero flag
    */
   public static void setZero(boolean zero) {
      Flags.set(FlagsConstants.ZERO, zero);
   }
   
   /**
    * Gets the current state of the Overflow flag.
    * <p>
    * The Overflow flag is set when arithmetic operations exceed
    * the maximum representable value.
    * </p>
    *
    * @return true if the Overflow flag is set, false otherwise
    */
   public static boolean getOverflow() {
      return Flags.get(FlagsConstants.OVERFLOW);
   }
   
   /**
    * Sets the Overflow flag state.
    * <p>
    * Typically called by arithmetic instructions to indicate
    * whether their result caused an overflow condition.
    * </p>
    *
    * @param overflow the new state of the Overflow flag
    */
   public static void setOverflow(boolean overflow) {
      Flags.set(FlagsConstants.OVERFLOW, overflow);
   }
}