package com.jvm.mini.api;

import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Interface collecting methods for implementing the JVM's instruction set.
 * <p>
 * The Javadoc in this interface provides an informal exposition on the
 * semantics for the various instructions, and is in particular not meant as a
 * replacement for the JVM Spec itself. See in particular its Chapter 6, "The
 * Java Virtual Machine Instruction Set" (SE8 edition).
 *
 * @author Arno Bastenhof
 */
public interface InsnVisitor {

	/**
	 * Pushes a byte as an integer on the operand stack.
	 *
	 * @param operand a byte value, padded with leading zeroes to the size of
	 * an integer
	 */
	void bipush(int operand);

	/**
	 * Pushes a short as an integer on the operand stack.
	 *
	 * @param operand a short value, padded with leading zeroes to the size of
	 * an integer
	 */
	void sipush(int operand);

	/**
	 * Pushes an entry from the constant pool on the operand stack.
	 *
	 * @param constant an Integer-, Long- or String value
	 */
	void ldc(Object constant);

	/**
	 * Narrows an integer value on top of the operand stack to a byte.
	 */
	void i2b();

	/**
	 * Narrows an integer value on top of the operand stack to a short.
	 */
	void i2s();

	/**
	 * Widens an integer value on top of the operand stack to a long.
	 */
	void i2l();

	/**
	 * Narrows a long value on top of the operand stack to an integer.
	 */
	void l2i();

	/**
	 * Applies addition to two integer values on top of the operand stack.
	 */
	void iadd();

	/**
	 * Applies addition to two long values on top of the operand stack.
	 */
	void ladd();

	/**
	 * Applies subtraction to two integer values on top of the operand stack.
	 */
	void isub();

	/**
	 * Applies subtraction to two long values on top of the operand stack.
	 */
	void lsub();

	/**
	 * Applies multiplication to two integer values on top of the operand stack.
	 */
	void imul();

	/**
	 * Applies multiplication to two long values on top of the operand stack.
	 */
	void lmul();

	/**
	 * Negates an integer value on top of the operand stack.
	 */
	void ineg();

	/**
	 * Negates a long value on top of the operand stack.
	 */
	void lneg();

	/**
	 * Loads an integer value from the specified local variable on the operand
	 * stack.
	 */
	void iload(int var);

	/**
	 * Loads a long value from the specified local variable on the operand
	 * stack.
	 */
	void lload(int var);

	/**
	 * Stores an integer value from the top of the operand stack in the
	 * specified local variable.
	 */
	void istore(int var);

	/**
	 * Stores a long value from the top of the operand stack in the specified
	 * local variable.
	 */
	void lstore(int var);

	/**
	 * Increments the specified local variable with the given byte value.
	 *
	 * @param var a local variable
	 * @param increment a byte value, padded with leading zeroes to the size of
	 * an integer
	 */
	void iinc(int var, int increment);

	/**
	 * Compares the top two Long values on the operand stack.
	 */
	void lcmp();

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i!=j}
	 * for {@code i, j} the top two integer values on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifIcmpeq(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i==j}
	 * for {@code i, j} the top two integer values on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifIcmpne(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i<j}
	 * for {@code i, j} the top two integer values on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifIcmplt(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i<=j}
	 * for {@code i, j} the top two integer values on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifIcmple(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i>j}
	 * for {@code i, j} the top two integer values on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifIcmpgt(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i>=j}
	 * for {@code i, j} the top two integer values on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifIcmpge(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i==0}
	 * for {@code i} the top integer value on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifEq(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i!=0}
	 * for {@code i} the top integer value on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifNe(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i<0}
	 * for {@code i} the top integer value on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifLt(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i<=0}
	 * for {@code i} the top integer value on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifLe(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i>0}
	 * for {@code i} the top integer value on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifGt(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget} if {@code i>=0}
	 * for {@code i} the top integer value on the operand stack.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void ifGe(AbstractInsnNode jumpTarget);

	/**
	 * Jumps to the instruction at the given {@code jumpTarget}.
	 *
	 * @param jumpTarget the next instruction to execute
	 */
	void goTo(AbstractInsnNode jumpTarget);

	/**
	 * Returns from a method with return type void.
	 *
	 * @return the next instruction to execute
	 */
	void voidReturn();

}
