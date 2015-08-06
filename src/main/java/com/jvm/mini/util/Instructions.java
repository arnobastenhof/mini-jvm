package com.jvm.mini.util;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.jvm.mini.api.InsnVisitor;

/**
 * Utility class for Java bytecode instructions.
 *
 * @author Arno Bastenhof
 */
public final class Instructions {

	// Private constructor to prevent instantiation
	private Instructions() {
		throw new AssertionError();
	}

	// Flyweight factory, populated by the static initializer
	private static final Map<Integer,OpcodeInfo> OPCODES;

	static {
		OPCODES = new HashMap<>();
		for (OpcodeInfo info : OpcodeInfo.values()) {
			OPCODES.put(Integer.valueOf(info.opcode), info);
		}
	}

	/**
	 * Returns the next {@link AbstractInsnNode} from {@code insn} that does
	 * not coincide with a {@link LabelNode} or {@link FrameNode}, or null
	 * if there is none.
	 */
	public static AbstractInsnNode getNext(AbstractInsnNode insn) {
		AbstractInsnNode next = insn.getNext();
		while (next instanceof LabelNode || next instanceof FrameNode) {
				next = next.getNext();
		} // Note: if next == null the loop condition becomes false
		return next;
	}

	/**
	 * Returns the String representation of the specified JVM instruction. If
	 * the latter is null, the empty String is returned.
	 *
	 * @throws IllegalArgumentException if {@code insn.opcode} is not supported
	 * by this implementation
	 */
	public static String toString(AbstractInsnNode insn) {
		StringBuilder buffer = new StringBuilder();
		if (insn == null) {
			return buffer.toString();
		}

		// Opcode mnemonic
		{
			OpcodeInfo info = OPCODES.get(Integer.valueOf(insn.getOpcode()));
			Validate.argument(info != null);
			buffer.append(info.mnemonic).append(" ");
		}

		// Instruction arguments
		if (insn instanceof IntInsnNode) {
			buffer.append(Integer.toString(((IntInsnNode)insn).operand));
		} else if (insn instanceof LdcInsnNode) {
			buffer.append(((LdcInsnNode)insn).cst.toString());
		} else if (insn instanceof VarInsnNode) {
			buffer.append(Integer.toString(((VarInsnNode)insn).var));
		} else if (insn instanceof IincInsnNode) {
			IincInsnNode node = (IincInsnNode)insn;
			String var = Integer.toString(node.var);
			String incr = Integer.toString(node.incr);
			buffer.append(var + ", " + incr);
		} else if (insn instanceof JumpInsnNode) {
			buffer.append(((JumpInsnNode)insn).label.getLabel().toString());
		}
		return buffer.toString();
	}

	/**
	 * Selects and executes the method on the supplied {@code visitor} for the
	 * given instruction.
	 *
	 * @throws NullPointerException if {@code visitor} or {@code insn} is null
	 * @throws IllegalArgumentException if {@code insn} uses an opcode that is
	 * not supported by this implementation
	 */
	public static void switchOnInsn(InsnVisitor visitor, AbstractInsnNode insn) {
		Validate.notNull(visitor);
		Validate.notNull(insn);
		OpcodeInfo info = OPCODES.get(Integer.valueOf(insn.getOpcode()));
		Validate.argument(info != null);
		info.execute(visitor, insn);
	}

	/*
	 * Strategy interface (cf. #execute), used to mimick function pointers.
	 * (See Item 21 of Bloch's Effective Java, 2nd ed.) Instance-controlled
	 * through use of the Flyweight pattern, realized by an enum.
	 */
	private enum OpcodeInfo {

		BIPUSH(Opcodes.BIPUSH, "BIPUSH", 2) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.bipush(((IntInsnNode)insn).operand);
			}
		},

		SIPUSH(Opcodes.SIPUSH, "SIPUSH", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.sipush(((IntInsnNode)insn).operand);
			}
		},

		LDC(Opcodes.LDC, "LDC", 2) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ldc(((LdcInsnNode)insn).cst);
			}
		},

		I2B(Opcodes.I2B, "I2B", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.i2b();
			}
		},

		I2S(Opcodes.I2S, "I2S", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.i2s();
			}
		},

		I2L(Opcodes.I2L, "I2L", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.i2l();
			}
		},

		L2I(Opcodes.L2I, "L2I", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.l2i();
			}
		},

		IADD(Opcodes.IADD, "IADD", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.iadd();
			}
		},

		LADD(Opcodes.LADD, "LADD", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ladd();
			}
		},

		ISUB(Opcodes.ISUB, "ISUB", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.isub();
			}
		},

		LSUB(Opcodes.LSUB, "LSUB", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.lsub();
			}
		},

		IMUL(Opcodes.IMUL, "IMUL", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.imul();
			}
		},

		LMUL(Opcodes.LMUL, "LMUL", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.lmul();
			}
		},

		INEG(Opcodes.INEG, "INEG", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ineg();
			}
		},

		LNEG(Opcodes.LNEG, "LNEG", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.lneg();
			}
		},

		ILOAD(Opcodes.ILOAD, "ILOAD", 2) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.iload(((VarInsnNode)insn).var);
			}
		},

		LLOAD(Opcodes.LLOAD, "LLOAD", 2) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.lload(((VarInsnNode)insn).var);
			}
		},

		ISTORE(Opcodes.ISTORE, "ISTORE", 2) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.istore(((VarInsnNode)insn).var);
			}
		},

		LSTORE(Opcodes.LSTORE, "LSTORE", 2) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.lstore(((VarInsnNode)insn).var);
			}
		},

		IINC(Opcodes.IINC, "IINC", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				IincInsnNode node = (IincInsnNode)insn;
				visitor.iinc(node.var, node.incr);
			}
		},

		IF_ICMPEQ(Opcodes.IF_ICMPEQ, "IF_ICMPEQ", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifIcmpeq(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IF_ICMPNE(Opcodes.IF_ICMPNE, "IF_ICMPNE", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifIcmpne(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IF_ICMPLT(Opcodes.IF_ICMPLT, "IF_ICMPLT", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifIcmplt(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IF_ICMPLE(Opcodes.IF_ICMPLE, "IF_ICMPLE", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifIcmple(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IF_ICMPGT(Opcodes.IF_ICMPGT, "IF_ICMPGT", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifIcmpgt(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IF_ICMPGE(Opcodes.IF_ICMPGE, "IF_ICMPGE", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifIcmpge(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		LCMP(Opcodes.LCMP, "LCMP", 1) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.lcmp();
			}
		},

		IFEQ(Opcodes.IFEQ, "IFEQ", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifEq(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IFNE(Opcodes.IFNE, "IFNE", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifNe(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IFLT(Opcodes.IFLT, "IFLT", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifLt(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IFLE(Opcodes.IFLE, "IFLE", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifLe(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IFGT(Opcodes.IFGT, "IFGT", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifGt(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		IFGE(Opcodes.IFGE, "IFGE", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.ifGe(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		GOTO(Opcodes.GOTO, "GOTO", 3) {
			@Override
			public void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.goTo(Instructions.getNext(((JumpInsnNode)insn).label));
			}
		},

		RETURN(Opcodes.RETURN, "RETURN", 1) {
			@Override
			protected void execute(InsnVisitor visitor, AbstractInsnNode insn) {
				visitor.voidReturn();
			}
		};

		private final int opcode;
		private final String mnemonic;
		private final int instructionSize; // TODO Unused

		private OpcodeInfo(int opcode, String mnemonic, int instructionSize) {
			this.opcode = opcode;
			this.mnemonic = mnemonic;
			this.instructionSize = instructionSize;
		}

		/**
		 * Applies a method on the specified {@code visitor}, supplying
		 * parameters from the given {@code insn}.
		 */
		protected abstract void execute(InsnVisitor visitor, AbstractInsnNode insn);

	}
}
