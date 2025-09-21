/**
 * Value system package for the CPU simulator.
 *
 * <p>This package provides a comprehensive value representation system that handles
 * different data types and states within the CPU simulation. The package implements
 * a hierarchy of value types that can represent both defined and undefined states,
 * supporting various arithmetic operations and conversions.</p>
 *
 * <p>Core Components:</p>
 * <ul>
 *   <li>{@link io.matita08.value.Value} - Abstract base class defining the common interface</li>
 *   <li>{@link io.matita08.value.SingleValue} - Concrete single-word values with sign interpretation</li>
 *   <li>{@link io.matita08.value.DoubleValue} - Multi-word values for extended addressing</li>
 *   <li>{@link io.matita08.value.UndefinedSingleValue} - Simulates uninitialized single values</li>
 *   <li>{@link io.matita08.value.UndefinedDoubleValue} - Simulates uninitialized double values</li>
 * </ul>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li><strong>Type Safety:</strong> Strong typing with runtime type checking</li>
 *   <li><strong>Undefined State Simulation:</strong> Realistic simulation of uninitialized memory</li>
 *   <li><strong>Flexible Arithmetic:</strong> Support for signed/unsigned arithmetic operations</li>
 *   <li><strong>Address Scaling:</strong> Automatic adaptation to different address sizes</li>
 *   <li><strong>Random Behavior:</strong> Undefined values exhibit non-deterministic behavior</li>
 * </ul>
 *
 * <p>The value system is designed to help identify programming errors by simulating
 * the unpredictable behavior of uninitialized memory, while providing efficient
 * arithmetic operations for defined values.</p>
 *
 * @author Matita008
 * @version 1.0
 * @since 1.0
 */
package io.matita08.value;