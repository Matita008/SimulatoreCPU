/**
 * CPU logic and execution engine package for the simulator.
 *
 * <p>This package implements the core execution logic of the CPU simulator,
 * providing the fetch-decode-execute cycle, operation management, and
 * instruction processing capabilities. It serves as the "brain" of the
 * simulated processor.</p>
 *
 * <p>Core Components:</p>
 * <ul>
 *   <li>{@link io.matita08.logic.Execution} - Main execution engine implementing CPU cycle</li>
 *   <li>{@link io.matita08.logic.Operation} - Operation wrapper with dynamic instruction loading</li>
 *   <li>{@link io.matita08.logic.Phase} - Execution phase enumeration (Fetch, Decode, Execute)</li>
 *   <li>{@link io.matita08.logic.Operations3Bit} - 3-bit instruction set implementation</li>
 *   <li>{@link io.matita08.logic.Operations4Bit} - 4-bit instruction set implementation</li>
 * </ul>
 *
 * <p>Execution Model:</p>
 * <ul>
 *   <li><strong>Step-by-Step Execution:</strong> Single-step and continuous execution modes</li>
 *   <li><strong>Multi-Cycle Operations:</strong> Realistic timing with variable cycle counts</li>
 *   <li><strong>Phase-Based Processing:</strong> Clear separation of fetch, decode, and execute phases</li>
 *   <li><strong>Dynamic Instruction Loading:</strong> Reflection-based operation set loading</li>
 *   <li><strong>Memory Integration:</strong> Seamless interaction with register and memory systems</li>
 * </ul>
 *
 * <p>Supported Instruction Sets:</p>
 * <ul>
 *   <li><strong>3-Bit ISA:</strong> Basic instruction set with essential operations (LOAD, STO, ADD, etc.)</li>
 *   <li><strong>4-Bit ISA:</strong> Extended instruction set with additional operations (SUB, JMP, conditional jumps)</li>
 * </ul>
 *
 * <p>Fetch-Decode-Execute Cycle:</p>
 * <ol>
 *   <li><strong>Fetch Phase:</strong>
 *       <ul>
 *         <li>Read instruction from memory at PC address</li>
 *         <li>Store instruction in IR register</li>
 *         <li>Increment PC to next instruction</li>
 *       </ul>
 *   </li>
 *   <li><strong>Decode Phase:</strong>
 *       <ul>
 *         <li>Interpret instruction opcode</li>
 *         <li>Look up operation definition</li>
 *         <li>Prepare operands and timing</li>
 *       </ul>
 *   </li>
 *   <li><strong>Execute Phase:</strong>
 *       <ul>
 *         <li>Perform operation-specific actions</li>
 *         <li>Update registers and memory</li>
 *         <li>Set appropriate CPU flags</li>
 *       </ul>
 *   </li>
 * </ol>
 *
 * <p>Dynamic Operation Loading:</p>
 * <p>The execution engine uses Java reflection to dynamically load instruction set
 * definitions, allowing for different CPU architectures without modifying the core
 * execution logic. Operation classes must implement the expected interface with
 * proper static methods and fields.</p>
 *
 * <p>Integration with Other Packages:</p>
 * <ul>
 *   <li><strong>Data Package:</strong> Direct access to registers, memory, and CPU state</li>
 *   <li><strong>GUI Package:</strong> Execution control through step buttons and state display</li>
 *   <li><strong>Value Package:</strong> Arithmetic operations and data type handling</li>
 * </ul>
 *
 * <p>Educational Value:</p>
 * <p>The logic package demonstrates fundamental computer science concepts:</p>
 * <ul>
 *   <li>How instructions are fetched from memory and decoded</li>
 *   <li>The role of the program counter in sequential execution</li>
 *   <li>Multi-cycle instruction timing and pipeline concepts</li>
 *   <li>Control unit coordination of CPU components</li>
 *   <li>Memory addressing modes and data access patterns</li>
 * </ul>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
package io.matita08.logic;