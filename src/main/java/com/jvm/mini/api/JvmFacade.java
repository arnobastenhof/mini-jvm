package com.jvm.mini.api;

import org.objectweb.asm.tree.AbstractInsnNode;

import com.jvm.mini.impl.Frame;

/**
 * A Facade for the JVM's runtime data structures.
 * <p>
 * The current implementation, following a suggestion in [1], leverages the
 * host JVM by populating both the operand stack and local variables of a frame
 * with object references. Thus, integer- and long values translate to
 * instances of {@link Integer} and {@link Long} values respectively. This
 * deviates strongly from the JVM spec, dictating two subsequent operand- or
 * local variable slots should be used for storing long values. A less obvious
 * consequence of the current approach is that the use of Java objects exposes
 * the types of operands at runtime. The JVM spec instead uses raw bit patterns,
 * performing type checking only statically during class file verification on
 * the basis of the type information integrated into opcodes.
 * <p>
 * [1] Taivalsaari, Antero. "Implementing a Java Virtual Machine in the Java
 * Programming Language." (1998).
 *
 * @author Arno Bastenhof
 */
public interface JvmFacade {

	// === JVM Stack ===

	/**
	 * Pushes a new {@link Frame} on the JVM stack.
	 *
	 * @param maxStack the maximum size of the operand stack for the new frame
	 * @param maxLocals the maximum number of local variables for the new frame
	 * @param args method arguments to be loaded on the operand stack
	 * @throws IllegalArgumentException if {@code maxStack < 0 || maxLocals < 0}
	 */
	void pushFrame(int maxStack, int maxLocals, Object... args);

	/**
	 * Pops a {@link Frame} from the JVM stack.
	 *
	 * @throws IllegalStateException if the JVM stack is empty.
	 */
	Frame popFrame();

	// === Program counter ===

	/**
	 * Reads an instruction from code memory, advancing the program counter as
	 * a side effect.
	 */
	AbstractInsnNode readInstruction();

	/**
	 * Sets the program counter to the specified {@code instruction}.
	 *
	 * @throws NullPointerException if {@code instruction == null}
	 */
	void jump(AbstractInsnNode instruction);

	// === Operand stack ===

	/**
	 * Pushes the specified {@code value} on the current frame's operand stack.
	 *
	 * @throws IndexOutOfBoundsException if the current frame's operand stack
	 * is already filled to its maximum size.
	 */
	void pushOperand(Object value);

	/**
	 * Pops an {@link Integer} value from the current frame's operand stack.
	 *
	 * @throws IndexOutOfBoundsException if the current frame's operand stack
	 * is empty.
	 * @throws ClassCastException if the top value on the operand stack cannot
	 * be cast to an {@link Integer}.
	 */
	Integer popIntegerOperand();

	/**
	 * Pops a {@link Long} value from the current frame's operand stack.
	 *
	 * @throws IndexOutOfBoundsException if the current frame's operand stack
	 * is empty.
	 * @throws ClassCastException if the top value on the operand stack cannot
	 * be cast to a {@link Long}.
	 */
	Long popLongOperand();

	// === Local variables ===

	/**
	 * Stores the given {@code value} in the specified local variable.
	 *
	 * @throws NullPointerException if {@code value == null}
	 * @throws IndexOutOfBoundsException if {@code var < 0 || var >= maxLocals}
	 */
	void store(int var, Object value);

	/**
	 * Returns the {@link Integer} value stored at the specified local variable.
	 *
	 * @param var the local variable to load from
	 * @throws IndexOutOfBoundsException if {@code var} does not fall within
	 * the address space for local variables in the current frame
	 * @throws ClassCastException if the value loaded from {@code var} cannot be
	 * cast to {@link Integer}
	 */
	Integer loadInteger(int var);

	/**
	 * Returns the {@link Long} value stored at the specified local variable.
	 *
	 * @param var the local variable to load from
	 * @throws IndexOutOfBoundsException if {@code var} does not fall within
	 * the address space for local variables in the current frame
	 * @throws ClassCastException if the value loaded from {@code var} cannot
	 * be cast to {@link Long}
	 */
	Long loadLong(int var);

	// === Debugging ===

	/**
	 * Returns the field descriptors for the entries on the operand stacks.
	 * Intended for debugging purposes.
	 */
	String getOperandTypes();

	/**
	 * Returns the String representation of the top value in the operand stack.
	 * Intended for debugging purposes.
	 */
	String peekOperand();

	/**
	 * Returns the next instruction to be executed, or null if there is none.
	 * Intended for debugging purposes.
	 */
	AbstractInsnNode peekInstruction();

}
