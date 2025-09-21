/**
 * Data management package for the CPU simulator.
 *
 * <p>This package provides comprehensive data storage and management functionality
 * for the simulated CPU, including registers, memory, flags, and state tracking.
 * All CPU state information is centralized in this package to ensure consistent
 * access patterns and efficient GUI updates.</p>
 *
 * <p>Core Components:</p>
 * <ul>
 *   <li>{@link io.matita08.data.Registers} - Complete CPU register file and memory management</li>
 *   <li>{@link io.matita08.data.Flags} - CPU flags system for arithmetic/logic operation results</li>
 *   <li>{@link io.matita08.data.FlagsConstants} - Enumeration of available CPU flags</li>
 *   <li>{@link io.matita08.data.ControlUnit} - Control unit state and execution phase tracking</li>
 * </ul>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li><strong>Comprehensive Register Management:</strong> PC, IR, MAR, MDR, Accumulator, and more</li>
 *   <li><strong>Central Memory System:</strong> Configurable memory array with Value-based storage</li>
 *   <li><strong>I/O Buffer Management:</strong> Input/output buffers for external communication</li>
 *   <li><strong>Modification Tracking:</strong> Efficient GUI update optimization through change flags</li>
 *   <li><strong>Thread Safety:</strong> Safe concurrent access to CPU state information</li>
 *   <li><strong>Flag System:</strong> CPU condition flags with bitmask operations</li>
 * </ul>
 *
 * <p>Register Architecture:</p>
 * <p>The register system implements a realistic CPU register file with:</p>
 * <ul>
 *   <li><strong>Program Counter (PC):</strong> Address of next instruction to execute</li>
 *   <li><strong>Instruction Register (IR):</strong> Currently decoded instruction</li>
 *   <li><strong>Memory Address/Data Registers (MAR/MDR):</strong> Memory interface registers</li>
 *   <li><strong>General Purpose Registers:</strong> Accumulator and Register B for operations</li>
 *   <li><strong>Addressing Support:</strong> Pointer register for indirect addressing</li>
 * </ul>
 *
 * <p>Memory Management:</p>
 * <p>The central memory system provides:</p>
 * <ul>
 *   <li>Configurable memory size via {@link io.matita08.Constants}</li>
 *   <li>Value-based storage supporting both defined and undefined states</li>
 *   <li>Address validation and bounds checking</li>
 *   <li>Efficient bulk operations for program loading</li>
 * </ul>
 *
 * <p>Modification Tracking:</p>
 * <p>The package implements an efficient change tracking system using bitmask flags:</p>
 * <ul>
 *   <li><strong>Bit 0 (1):</strong> Central Memory modified</li>
 *   <li><strong>Bit 1 (2):</strong> CPU Registers modified</li>
 *   <li><strong>Bit 2 (4):</strong> ALU state modified</li>
 *   <li><strong>Bit 3 (8):</strong> I/O Buffers modified</li>
 * </ul>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
package io.matita08.data;