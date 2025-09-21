/**
 * Graphical User Interface (GUI) package for the CPU Simulator application.
 *
 * <p>This package contains all the user interface components and classes
 * responsible for providing the visual interface and user interaction
 * capabilities of the CPU simulator. The GUI provides a comprehensive
 * view of the simulated processor's internal state and operation.</p>
 *
 * <p>Key components include:</p>
 * <ul>
 *   <li>{@link io.matita08.GUI.Display} - Main application window with CPU visualization,
 *       memory display, control panels, and I/O interfaces</li>
 *   <li>{@link io.matita08.GUI.Line} - Custom component for drawing visual separators</li>
 *   <li>Event listeners in the {@link io.matita08.GUI.listeners} sub-package for handling
 *       user interactions</li>
 * </ul>
 *
 * <p>The GUI architecture follows these design principles:</p>
 * <ul>
 *   <li><strong>Swing-based:</strong> Built using Java Swing components with custom layouts</li>
 *   <li><strong>Thread-safe:</strong> All GUI updates are performed on the Event Dispatch Thread</li>
 *   <li><strong>Modular design:</strong> Components are organized into logical sections (CPU, memory, I/O)</li>
 *   <li><strong>Real-time updates:</strong> Interface reflects the current state of the simulated CPU</li>
 * </ul>
 *
 * <p>The main GUI displays:</p>
 * <ul>
 *   <li>CPU registers (PC, IR, MAR, MDR, Accumulator, etc.)</li>
 *   <li>Arithmetic Logic Unit (ALU) status and operations</li>
 *   <li>Program Status Word (PSW) with CPU flags</li>
 *   <li>Central memory contents with scrollable address/value pairs</li>
 *   <li>Control unit information (current instruction, execution phases)</li>
 *   <li>I/O buffers and interactive numeric keypad</li>
 *   <li>Control buttons for program execution and file loading</li>
 * </ul>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
package io.matita08.GUI;