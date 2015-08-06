package com.jvm.mini.impl;

import org.objectweb.asm.tree.AbstractInsnNode;

import com.jvm.mini.util.Operands;
import com.jvm.mini.util.Validate;

/**
 * Frames are allocated on the Java Virtual Machine stack and hold the operand
 * stack and local variables for a method invocation.
 * <p>
 * The current implementation follows [1] in leveraging the host JVM for
 * populating both the operand stack and local variables with Object
 * references. This leads to a number of fundamental deviations from the JVM
 * spec, documented in the Javadoc for {@link com.jvm.mini.api.JvmFacade}.
 * <p>
 * [1] Taivalsaari, Antero. "Implementing a Java Virtual Machine in the Java
 * Programming Language." (1998).
 *
 * @author Arno Bastenhof
 */
public final class Frame {

	private final AbstractInsnNode returnAddress;
	private final Object[] operands; // Operand stack
	private final Object[] locals;   // Local variables
	private final Frame previous;    // Previous frame, or null if there is none
	private int stackPtr;            // Operand stack pointer

	/**
	 *
	 * @param maxStack the maximum size of the operand stack
	 * @param maxLocals the maximum number of local variable slots
	 * @param previous the previous frame on the JVM stack; allowed to be null
	 * @param returnAddress the bytecode instruction to return control to
	 * @throws NullPointerException if {@code name == null}
	 * @throws IllegalArgumentException if {@code maxStack < 0 || maxLocals < 0}
	 */
	public Frame(int maxStack, int maxLocals, Frame previous,
			AbstractInsnNode returnAddress) {
		Validate.argument(maxStack >= 0 && maxLocals >= 0);
		this.operands = new Object[maxStack];
		this.locals = new Object[maxLocals];
		this.previous = previous;
		this.returnAddress = returnAddress;
	}

	/**
	 * Pushes the specified {@code value} on the operand stack.
	 *
	 * @throws IndexOutOfBoundsException if the operand stack is already filled
	 * to its maximum size.
	 */
	public void push(Object value) {
		this.operands[this.stackPtr++] = value;
	}

	/**
	 * Pops a value from the operand stack.
	 *
	 * @param clazz the expected runtime class of the popped value
	 * @throws NullPointerException if {@code clazz == null}
	 * @throws IndexOutOfBoundsException if the operand stack is empty
	 * @throws ClassCastException if the popped value cannot be cast to {@code
	 * clazz}
	 */
	public <T> T pop(Class<T> clazz) {
		Validate.notNull(clazz);
		return clazz.cast(this.operands[--this.stackPtr]);
	}

	/**
	 * Returns the String representation of the value on top of the operand
	 * stack without popping it. If the operand stack is empty, the empty
	 * String is returned. Intended for debugging purposes.
	 */
	public Object peek() {
		return this.stackPtr > 0 ? this.operands[this.stackPtr - 1] : "";
	}

	/**
	 * Stores the given {@code value} in the specified local variable.
	 *
	 * @throws NullPointerException if {@code value == null}
	 * @throws IndexOutOfBoundsException if {@code var < 0 || var >= maxLocals}
	 */
	public void store(int var, Object value) {
		this.locals[var] = Validate.notNull(value);
	}

	/**
	 * Returns the value stored in the specified local variable.
	 *
	 * @param clazz the expected runtime class of the returned value
	 * @throws NullPointerException if {@code clazz == null}
	 * @throws IndexOutOfBoundsException if {@code var < 0 || var >= maxLocals}
	 * @throws ClassCastException if the value loaded from {@code var} cannot
	 * be cast to {@code clazz}
	 */
	public <T> T load(int var, Class<T> clazz) {
		Validate.notNull(clazz);
		return clazz.cast(this.locals[var]);
	}

	/**
	 * Returns the preceding frame on the JVM stack, or null if there is none.
	 */
	public Frame getPrevious() {
		return this.previous;
	}

	/**
	 * Returns the address of the bytecode instruction to continue execution
	 * with after popping this frame.
	 */
	public AbstractInsnNode getReturnAddress() {
		return this.returnAddress;
	}

	/**
	 * Returns a String concatenation of the field descriptors for the values
	 * on the operand stack. Intended for debugging purposes.
	 * <p>
	 * This functionality is counterintuitive to the JVM spec, which restricts
	 * all type information to the instruction set for static class file
	 * verification. It is made possible, however, by our deviation from this
	 * point by our leveraging of the underlying host JVM in the use of Object
	 * references, which expose their classes at runtime.
	 */
	public String getOperandTypes() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < this.stackPtr; i++) {
			Object operand = this.operands[i];
			buffer.append(Operands.getFieldDescriptor(operand.getClass()));
		}
		return buffer.toString();
	}

}
