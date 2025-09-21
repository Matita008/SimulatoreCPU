package io.matita08.data;

import io.matita08.logic.Operation;
import io.matita08.logic.Phase;

/**
 * CPU Control Unit State Management.
 * <p>
 * This class maintains the state of the CPU's control unit, tracking
 * execution phases, instruction cycles, and current operations. It serves
 * as the central coordinator for instruction execution flow.
 * </p>
 *
 * <p>
 * <strong>State Components:</strong>
 * <ul>
 * <li><strong>Execution Phases:</strong> Current and next phases (Fetch/Decode/Execute)</li>
 * <li><strong>Cycle Management:</strong> Current cycle within instruction execution</li>
 * <li><strong>Operation Tracking:</strong> Current instruction being executed</li>
 * <li><strong>ALU Status:</strong> Current arithmetic/logic operation display</li>
 * </ul>
 * </p>
 *
 * <p>
 * The control unit coordinates with the execution engine to manage the
 * fetch-decode-execute cycle and handle multi-cycle instruction execution.
 * </p>
 *
 * @author Matita008
 * @version 1.5
 * @since 1.0
 */
public final class ControlUnit {
   
   /**
    * Current execution phase of the CPU.
    * <p>
    * Represents the current state in the instruction cycle:
    * Fetch, Decode, Execute, or None (inactive).
    * </p>
    */
   public static Phase current = Phase.Fetch;
   
   /**
    * Next execution phase to transition to.
    * <p>
    * Set by the current phase to determine the next phase
    * in the instruction execution cycle.
    * </p>
    */
   public static Phase next = Phase.Fetch;
   
   /**
    * Current cycle number within the current instruction execution.
    * <p>
    * Counts down from the total cycles required for the current instruction.
    * When it reaches 0, the instruction execution is complete.
    * </p>
    */
   public static int currentCycle = -1;
   
   /**
    * Total number of cycles required for the current instruction.
    * <p>
    * Set during the decode phase based on the instruction being executed.
    * Used for progress tracking and multi-cycle instruction coordination.
    * </p>
    */
   public static int totalCycles = -1;
   
   /**
    * Current operation being executed.
    * <p>
    * Set during decode phase and used throughout execution.
    * Contains the instruction's execution logic and metadata.
    * </p>
    */
   public static Operation opcode = Operation.Unknown;
   
   /**
    * String representation of current ALU operation for display.
    * <p>
    * Used by the GUI to show which arithmetic/logic operation
    * is currently being performed. Set by arithmetic instructions.
    * </p>
    */
   public static String ALUOpcode = "";
   
   /**
    * Private constructor to prevent instantiation.
    * <p>
    * This is a state management class providing only static fields
    * and methods for control unit coordination.
    * </p>
    *
    * @throws AssertionError always, to prevent instantiation
    */
   private ControlUnit() {
      throw new AssertionError("ControlUnit should not be instantiated");
   }
}