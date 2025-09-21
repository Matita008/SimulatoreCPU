/**
 * Event listener implementations for GUI components in the CPU simulator.
 *
 * <p>This sub-package contains all event listener classes that handle
 * user interactions with the graphical user interface components.
 * The listeners follow standard Java event handling patterns and
 * integrate seamlessly with the simulator's core functionality.</p>
 *
 * <p>Available listeners:</p>
 * <ul>
 *   <li>{@link io.matita08.GUI.listeners.Load} - Handles file loading operations
 *       for program files into the simulator's central memory</li>
 * </ul>
 *
 * <p>Listener characteristics:</p>
 * <ul>
 *   <li><strong>Reusable:</strong> Designed to be instantiated and attached to multiple components</li>
 *   <li><strong>Integrated:</strong> Work directly with the simulator's memory and execution systems</li>
 *   <li><strong>User-friendly:</strong> Provide appropriate feedback and error handling</li>
 *   <li><strong>Cross-platform:</strong> Support different operating systems and look-and-feels</li>
 * </ul>
 *
 * <p>These listeners bridge the gap between user interface events and the
 * underlying simulator operations, ensuring that user actions are properly
 * translated into simulator commands and state changes.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
package io.matita08.GUI.listeners;