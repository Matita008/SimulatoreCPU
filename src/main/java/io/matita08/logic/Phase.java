package io.matita08.logic;

/**
 * CPU Execution Phases - Instruction Cycle States.
 * <p>
 * This enum defines the three primary phases of CPU instruction execution
 * following the classical fetch-decode-execute cycle. Each phase is
 * associated with its corresponding execution logic.
 * </p>
 * <p>
 * The CPU execution cycle:
 * <ol>
 * <li><strong>Fetch</strong> - Retrieve instruction from memory</li>
 * <li><strong>Decode</strong> - Decode instruction and prepare operands</li>
 * <li><strong>Execute</strong> - Execute the instruction operation</li>
 * <li><strong>None</strong> - Default state</li>
 * </ol>
 * </p>
 *
 * @author Matita008
 * @version 1.5
 * @since 1.0
 */
public enum Phase{
   
   /**
    * FETCH Phase - Instruction Retrieval.
    * <p>
    * Retrieves the next instruction from memory at the address specified
    * by the Program Counter, loads it into the Instruction Register,
    * and increments the PC for the next instruction.
    * </p>
    */
   Fetch(Execution::fetch),
   
   /**
    * DECODE Phase - Instruction Interpretation.
    * <p>
    * Decodes the instruction in the IR, looks up the corresponding
    * operation, and prepares the control unit for instruction execution
    * including setting up cycle counts.
    * </p>
    */
   Decode(Execution::decode),
   
   /**
    * EXECUTE Phase - Instruction Execution.
    * <p>
    * Executes the decoded instruction operation, handling multi-cycle
    * instructions and coordinating with memory, ALU, and I/O systems
    * as required by the specific instruction.
    * </p>
    */
   Execute(Execution::execute),
   
   /**
    * NONE Phase - Default State.
    * <p>
    * Represents an inactive or uninitialized CPU state where no
    * operation is being performed. Used during system initialization.
    * </p>
    */
   None(()->{});
   
   /**
    * The execution logic associated with this phase.
    */
   private final Runnable action;
   
   /**
    * Constructs a Phase with its associated execution action.
    *
    * @param action the Runnable containing the phase's execution logic
    */
   Phase(Runnable action) {
      this.action = action;
   }
   
   /**
    * Executes the action associated with this phase.
    * <p>
    * Calls the phase-specific execution logic to perform the
    * appropriate CPU operations for this phase of the instruction cycle.
    * </p>
    */
   public void run() {action.run();}
}