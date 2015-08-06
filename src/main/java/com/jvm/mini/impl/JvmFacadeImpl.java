package com.jvm.mini.impl;

import org.objectweb.asm.tree.AbstractInsnNode;

import com.jvm.mini.api.JvmFacade;
import com.jvm.mini.util.Instructions;
import com.jvm.mini.util.Validate;

/**
 * {@link JvmFacade} implementation.
 *
 * @author Arno Bastenhof
 */
public class JvmFacadeImpl implements JvmFacade {

	private Frame framePtr;              // JVM Stack pointer
	private AbstractInsnNode programCtr; // Program counter

	@Override
	public AbstractInsnNode readInstruction() {
		AbstractInsnNode current = this.programCtr;
		this.programCtr = Instructions.getNext(current);
		return current;
	}

	@Override
	public void jump(AbstractInsnNode instruction) {
		this.programCtr = Validate.notNull(instruction);
	}

	@Override
	public void pushFrame(int maxStack, int maxLocals, Object... args) {
		Validate.argument(maxStack >= 0 && maxLocals >= 0);
		this.framePtr = new Frame(maxStack, maxLocals,
				this.framePtr, this.programCtr);
		if (args == null) {
			return;
		}
		for (Object arg : args) {
			this.framePtr.push(arg);
		}
	}

	@Override
	public Frame popFrame() {
		Validate.state(this.framePtr != null);
		Frame result = this.framePtr;
		this.programCtr = result.getReturnAddress(); // Note: can be null
		this.framePtr = result.getPrevious();
		return result;
	}

	@Override
	public void pushOperand(Object value) {
		this.framePtr.push(value);
	}

	@Override
	public Integer popIntegerOperand() {
		return this.framePtr.pop(Integer.class);
	}

	@Override
	public Long popLongOperand() {
		return this.framePtr.pop(Long.class);
	}

	@Override
	public void store(int var, Object value) {
		this.framePtr.store(var, value);
	}

	@Override
	public Integer loadInteger(int var) {
		return this.framePtr.load(var, Integer.class);
	}

	@Override
	public Long loadLong(int var) {
		return this.framePtr.load(var, Long.class);
	}

	@Override
	public String getOperandTypes() {
		return this.framePtr.getOperandTypes();
	}

	@Override
	public String peekOperand() {
		return this.framePtr.peek().toString();
	}

	@Override
	public AbstractInsnNode peekInstruction() {
		return this.programCtr;
	}

}
