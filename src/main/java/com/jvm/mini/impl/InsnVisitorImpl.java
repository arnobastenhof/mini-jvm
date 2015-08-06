package com.jvm.mini.impl;

import org.objectweb.asm.tree.AbstractInsnNode;

import com.jvm.mini.api.InsnVisitor;
import com.jvm.mini.api.JvmFacade;
import com.jvm.mini.util.Validate;

public final class InsnVisitorImpl implements InsnVisitor {

	private final JvmFacade facade;

	public InsnVisitorImpl(JvmFacade facade) {
		this.facade = Validate.notNull(facade);
	}

	@Override
	public void bipush(int operand) {
		this.facade.pushOperand(Integer.valueOf(operand));
	}

	@Override
	public void sipush(int operand) {
		this.facade.pushOperand(Integer.valueOf(operand));
	}

	@Override
	public void ldc(Object constant) {
		this.facade.pushOperand(constant);
	}

	@Override
	public void i2b() {
		Integer i = this.facade.popIntegerOperand();
		byte b = i.byteValue();
		this.facade.pushOperand(Integer.valueOf(b));
	}

	@Override
	public void i2s() {
		Integer i = this.facade.popIntegerOperand();
		short s = i.shortValue();
		this.facade.pushOperand(Integer.valueOf(s));
	}

	@Override
	public void i2l() {
		Integer i = this.facade.popIntegerOperand();
		long l = i.longValue();
		this.facade.pushOperand(Long.valueOf(l));
	}

	@Override
	public void l2i() {
		Long l = this.facade.popLongOperand();
		int i = l.intValue();
		this.facade.pushOperand(Integer.valueOf(i));
	}

	@Override
	public void iadd() {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		int result = op1 + op2;
		this.facade.pushOperand(Integer.valueOf(result));
	}

	@Override
	public void ladd() {
		long op2 = this.facade.popLongOperand();
		long op1 = this.facade.popLongOperand();
		long result = op1 + op2;
		this.facade.pushOperand(Long.valueOf(result));
	}

	@Override
	public void isub() {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		int result = op1 - op2;
		this.facade.pushOperand(Integer.valueOf(result));
	}

	@Override
	public void lsub() {
		long op2 = this.facade.popLongOperand();
		long op1 = this.facade.popLongOperand();
		long result = op1 - op2;
		this.facade.pushOperand(Long.valueOf(result));
	}

	@Override
	public void imul() {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		int result = op1 * op2;
		this.facade.pushOperand(Integer.valueOf(result));
	}

	@Override
	public void lmul() {
		long op2 = this.facade.popLongOperand();
		long op1 = this.facade.popLongOperand();
		long result = op1 * op2;
		this.facade.pushOperand(Long.valueOf(result));
	}

	@Override
	public void ineg() {
		int op = this.facade.popIntegerOperand();
		this.facade.pushOperand(Integer.valueOf(-op));
	}

	@Override
	public void lneg() {
		long op = this.facade.popLongOperand();
		this.facade.pushOperand(Long.valueOf(-op));
	}

	@Override
	public void iload(int var) {
		Integer i = this.facade.loadInteger(var);
		this.facade.pushOperand(i);
	}

	@Override
	public void lload(int var) {
		Long l = this.facade.loadLong(var);
		this.facade.pushOperand(l);
	}

	@Override
	public void istore(int var) {
		Integer i = this.facade.popIntegerOperand();
		this.facade.store(var, i);
	}

	@Override
	public void lstore(int var) {
		Long l = this.facade.popLongOperand();
		this.facade.store(var, l);
	}

	@Override
	public void iinc(int var, int increment) {
		int i = this.facade.loadInteger(var);
		this.facade.store(var, Integer.valueOf(++i));
	}

	@Override
	public void lcmp() {
		long op2 = this.facade.popLongOperand();
		long op1 = this.facade.popLongOperand();
		int cmp = Long.compare(op1, op2);
		this.facade.pushOperand(Integer.valueOf(cmp));
	}

	@Override
	public void ifIcmpeq(AbstractInsnNode jumpTarget) {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		if (op1 == op2) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifIcmpne(AbstractInsnNode jumpTarget) {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		if (op1 != op2) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifIcmplt(AbstractInsnNode jumpTarget) {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		if (op1 < op2) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifIcmple(AbstractInsnNode jumpTarget) {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		if (op1 <= op2) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifIcmpgt(AbstractInsnNode jumpTarget) {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		if (op1 > op2) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifIcmpge(AbstractInsnNode jumpTarget) {
		int op2 = this.facade.popIntegerOperand();
		int op1 = this.facade.popIntegerOperand();
		if (op1 >= op2) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifEq(AbstractInsnNode jumpTarget) {
		int op = this.facade.popIntegerOperand();
		if (op == 0) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifNe(AbstractInsnNode jumpTarget) {
		int op = this.facade.popIntegerOperand();
		if (op != 0) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifLt(AbstractInsnNode jumpTarget) {
		int op = this.facade.popIntegerOperand();
		if (op < 0) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifLe(AbstractInsnNode jumpTarget) {
		int op = this.facade.popIntegerOperand();
		if (op <= 0) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifGt(AbstractInsnNode jumpTarget) {
		int op = this.facade.popIntegerOperand();
		if (op > 0) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void ifGe(AbstractInsnNode jumpTarget) {
		int op = this.facade.popIntegerOperand();
		if (op >= 0) {
			this.facade.jump(jumpTarget);
		}
	}

	@Override
	public void goTo(AbstractInsnNode jumpTarget) {
		this.facade.jump(jumpTarget);
	}

	@Override
	public void voidReturn() {
		this.facade.popFrame();
	}

}
