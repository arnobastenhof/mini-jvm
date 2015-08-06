package com.jvm.mini.data;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.V1_7;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.jvm.mini.util.Validate;

/**
 * Class file Builder written on top of ASM's {@link ClassWriter} and {@link
 * MethodVisitor}, offering a higher level of abstraction and restricting its
 * output to the subset of the JVM spec supported by this implementation.
 *
 * @author Arno Bastenhof
 */
public final class ClassBuilder {

	// Private constructor to force instantiation through static factory method
	private ClassBuilder() { }

	/**
	 * The package name used for generated test classes.
	 */
	public static final String PCKG = "com/jvm/mini/";

	private static final String SUPER_NAME = "java/lang/Object";
	private static final String METHOD_NAME = "main";
	private static final String METHOD_DESC = "([Ljava/lang/String;)V";

	private static final Map<Class<? extends Number>, Type> JVM_TYPES;

	static {
		JVM_TYPES = new HashMap<>();
		for (Type jvmType : Type.values()) {
			JVM_TYPES.put(jvmType.clazz, jvmType);
		}
	}

	private final ClassWriter cw = new ClassWriter(COMPUTE_FRAMES+COMPUTE_MAXS);
	private MethodVisitor mv;

	/**
	 * Static factory method.
	 *
	 * @param className unqualified class name
	 */
	public static ClassBuilder forClass(String className) {
		ClassBuilder cb = new ClassBuilder();
		cb.cw.visit(V1_7, ACC_PUBLIC, PCKG + className, null, SUPER_NAME, null);
		cb.mv = cb.cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
				METHOD_NAME, METHOD_DESC, null, null);
		return cb;
	}

	/**
	 * Writes an instruction for pushing a constant on the operand stack.
	 *
	 * @param clazz the constant type
	 * @param cst the constant to be pushed
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public ClassBuilder push(Class<? extends Number> clazz, Object cst) {
		getJvmType(clazz).push(this.mv, cst);
		return this;
	}

	/**
	 * Writes an addition instruction for the specified {@code clazz}.
	 *
	 * @param clazz the value type
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public ClassBuilder add(Class<? extends Number> clazz) {
		this.mv.visitInsn(getJvmType(clazz).add);
		return this;
	}

	/**
	 * Writes a subtraction instruction for the specified {@code clazz}.
	 *
	 * @param clazz the value type
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public ClassBuilder subtract(Class<? extends Number> clazz) {
		this.mv.visitInsn(getJvmType(clazz).sub);
		return this;
	}

	/**
	 * Writes a multiplication instruction for the specified {@code clazz}.
	 *
	 * @param clazz the value type
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public ClassBuilder multiply(Class<? extends Number> clazz) {
		this.mv.visitInsn(getJvmType(clazz).mul);
		return this;
	}

	/**
	 * Writes a unary negation instruction for the specified {@code clazz}.
	 *
	 * @param type the value type
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public ClassBuilder negate(Class<? extends Number> clazz) {
		this.mv.visitInsn(getJvmType(clazz).neg);
		return this;
	}

	/**
	 * Writes an instruction for loading from a local variable.
	 *
	 * @param clazz the type of the value at {@code address}
	 * @param address the memory address to load from
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public ClassBuilder load(Class<? extends Number> clazz, int address) {
		this.mv.visitVarInsn(getJvmType(clazz).load, address);
		return this;
	}

	/**
	 * Writes an instruction for writing to a local variable.
	 *
	 * @param clazz the type of the value popped from the operand stack
	 * @param address the memory address to store the popped value at
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public ClassBuilder store(Class<? extends Number> clazz, int address) {
		this.mv.visitVarInsn(getJvmType(clazz).store, address);
		return this;
	}

	/**
	 * Writes an instruction for performing a type cast.
	 *
	 * @param from the type to cast from
	 * @param to the type to cast to
	 * @throws IllegalArgumentException if {@code from} or {@code to} is
	 * unsupported
	 */
	public ClassBuilder cast(Class<? extends Number> from, Class<?> to) {
		getJvmType(from).castTo(this.mv, to);
		return this;
	}

	/**
	 * Writes an instruction for incrementing the specified local variable with
	 * the given amount.
	 */
	public ClassBuilder increment(int var, int amount) {
		this.mv.visitIincInsn(var, amount);
		return this;
	}

	/**
	 * Inserts a label.
	 */
	public ClassBuilder label(Label label) {
		this.mv.visitLabel(label);
		return this;
	}

	/**
	 * Writes an instruction performing a conditional jump based on the top
	 * two numeric values on the operand stack.
	 */
	public ClassBuilder ifCmpThen(Class<? extends Number> clazz,
			Comparison cmp, Label target) {
		getJvmType(clazz).ifCmpThen(this.mv, cmp, target);
		return this;
	}

	/**
	 * Writes an instruction performing a condition jump based on the top
	 * integer value on the operand stack, comparing it to 0.
	 */
	public ClassBuilder ifCmpZeroThen(Comparison cmp, Label target) {
		this.mv.visitJumpInsn(cmp.unaryOp, target);
		return this;
	}

	/**
	 * Writes a goto instruction for performing an unconditional jump.
	 */
	public ClassBuilder goTo(Label target) {
		this.mv.visitJumpInsn(Opcodes.GOTO, target);
		return this;
	}

	/**
	 * Writes the RETURN instruction, builds the class and returns the result
	 * as a byte array.
	 */
	public byte[] build() {
		this.mv.visitInsn(Opcodes.RETURN);
		this.mv.visitMaxs(0, 0); // Calculated by ASM
		this.mv.visitEnd();
		this.cw.visitEnd();
		return this.cw.toByteArray();
	}

	private Type getJvmType(Class<? extends Number> clazz) {
		Type jvmType = JVM_TYPES.get(clazz);
		if (jvmType == null) {
			throw new IllegalArgumentException();
		}
		return jvmType;
	}

	private enum Type {

		INTEGER(Integer.class, Opcodes.IADD, Opcodes.ISUB, Opcodes.IMUL,
				Opcodes.INEG, Opcodes.ILOAD, Opcodes.ISTORE) {
			@Override
			public void push(MethodVisitor mv, Object cst) {
				int nmbr = this.clazz.cast(cst).intValue();
				if (nmbr >= Byte.MIN_VALUE && nmbr <= Byte.MAX_VALUE) {
					mv.visitIntInsn(BIPUSH, nmbr);
				}
				else if (nmbr >= Short.MIN_VALUE && nmbr <= Short.MAX_VALUE) {
					mv.visitIntInsn(SIPUSH, nmbr);
				}
				else {
					mv.visitLdcInsn(cst);
				}
			}

			@Override
			protected void castTo(MethodVisitor mv, Class<?> clazz) {
				int opcode = Validate.notNull(I2.get(clazz));
				mv.visitInsn(opcode);
			}

			@Override
			protected void ifCmpThen(MethodVisitor mv, Comparison cmp,
					Label target) {
				mv.visitJumpInsn(cmp.binaryOp, target);
			}
		},

		LONG(Long.class, Opcodes.LADD, Opcodes.LSUB, Opcodes.LMUL,
				Opcodes.LNEG, Opcodes.LLOAD, Opcodes.LSTORE) {
			@Override
			public void push(MethodVisitor mv, Object cst) {
				mv.visitLdcInsn(cst);
			}

			@Override
			protected void castTo(MethodVisitor mv, Class<?> clazz) {
				Validate.argument(clazz.equals(Integer.class));
				mv.visitInsn(Opcodes.L2I);
			}

			@Override
			protected void ifCmpThen(MethodVisitor mv, Comparison cmp,
					Label target) {
				mv.visitInsn(Opcodes.LCMP);
				mv.visitJumpInsn(cmp.unaryOp, target);
			}
		};

		private static final Map<Class<?>, Integer> I2;

		static {
			I2 = new HashMap<>();
			I2.put(Byte.class, Integer.valueOf(Opcodes.I2B));
			I2.put(Short.class, Integer.valueOf(Opcodes.I2S));
			I2.put(Character.class, Integer.valueOf(Opcodes.I2C));
			I2.put(Long.class, Integer.valueOf(Opcodes.I2L));
		}

		final Class<? extends Number> clazz;
		private final int add;
		private final int sub;
		private final int mul;
		private final int neg;
		private final int load;
		private final int store;

		private Type(Class<? extends Number> clazz, int add, int sub, int mul,
				int neg, int load, int store) {
			this.clazz = clazz;
			this.add = add;
			this.sub = sub;
			this.mul = mul;
			this.neg = neg;
			this.load = load;
			this.store = store;
		}

		/**
		 * Writes the instruction for pushing a constant.
		 *
		 * @param cst the constant to be pushed
		 */
		protected abstract void push(MethodVisitor mv, Object cst);

		/**
		 * Writes a cast instruction.
		 * @param clazz the type to cast to
		 */
		protected abstract void castTo(MethodVisitor mv, Class<?> clazz);

		/**
		 * Writes an instruction performing a conditional jump based on the top
		 * two numeric values on the operand stack.
		 */
		protected abstract void ifCmpThen(MethodVisitor mv, Comparison cmp,
				Label target);

	}

	/**
	 * Supported comparison operations for performing conditional jumps.
	 *
	 * @author Arno Bastenhof
	 */
	public enum Comparison {

		EQ(Opcodes.IF_ICMPEQ, Opcodes.IFEQ),
		NE(Opcodes.IF_ICMPNE, Opcodes.IFNE),
		LT(Opcodes.IF_ICMPLT, Opcodes.IFLT),
		LE(Opcodes.IF_ICMPLE, Opcodes.IFLE),
		GT(Opcodes.IF_ICMPGT, Opcodes.IFGT),
		GE(Opcodes.IF_ICMPGE, Opcodes.IFGE);

		private final int binaryOp;
		private final int unaryOp;

		private Comparison(int binaryOp, int unaryOp) {
			this.binaryOp = binaryOp;
			this.unaryOp = unaryOp;
		}
	}
}
