/**
 * Root package for the CPU Simulator application.
 *
 * <p>This package contains the main application entry point and core utility
 * classes that provide fundamental functionality used throughout the simulator.
 * It serves as the foundation for the entire CPU simulation system.</p>
 *
 * <p>Core Components:</p>
 * <ul>
 *   <li>{@link io.matita08.Main} - Application entry point and startup coordination</li>
 *   <li>{@link io.matita08.Constants} - Configuration management and command-line processing</li>
 *   <li>{@link io.matita08.Utils} - Utility functions for file operations and threading</li>
 *   <li>{@link io.matita08.ExceptionHandler} - Global exception handling and logging</li>
 * </ul>
 *
 * <p>The simulator is organized into several specialized sub-packages:</p>
 * <ul>
 *   <li><strong>{@link io.matita08.GUI}:</strong> Graphical user interface components</li>
 *   <li><strong>{@link io.matita08.data}:</strong> CPU registers, flags, and memory management</li>
 *   <li><strong>{@link io.matita08.logic}:</strong> Execution engine and CPU operations</li>
 *   <li><strong>{@link io.matita08.value}:</strong> Value representation and arithmetic system</li>
 * </ul>
 *
 * <p>The CPU Simulator provides a complete educational environment for understanding
 * computer architecture concepts including:</p>
 * <ul>
 *   <li>Fetch-decode-execute cycle implementation</li>
 *   <li>Register and memory management</li>
 *   <li>Arithmetic and logic operations</li>
 *   <li>Control flow and branching</li>
 *   <li>I/O operations and user interaction</li>
 * </ul>
 *
 * <p>Configuration and Customization:</p>
 * <p>The simulator supports extensive customization through command-line arguments
 * processed by the {@link io.matita08.Constants} class, allowing users to adjust
 * memory size, instruction sets, addressing modes, and display formatting to
 * explore different architectural concepts.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
package io.matita08;