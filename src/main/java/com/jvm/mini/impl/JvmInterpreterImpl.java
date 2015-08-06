package com.jvm.mini.impl;

import org.objectweb.asm.tree.AbstractInsnNode;

import com.jvm.mini.api.InsnVisitor;
import com.jvm.mini.api.JvmFacade;
import com.jvm.mini.api.JvmInterpreter;
import com.jvm.mini.util.Instructions;
import com.jvm.mini.util.Validate;

public class JvmInterpreterImpl implements JvmInterpreter {

	private final JvmFacade facade;
	private final InsnVisitor visitor;

	public JvmInterpreterImpl(JvmFacade facade, InsnVisitor visitor) {
		this.facade = Validate.notNull(facade);
		this.visitor = Validate.notNull(visitor);
	}

	@Override
	public void next() {
		AbstractInsnNode insn = this.facade.readInstruction();
		Validate.state(insn != null);
		Instructions.switchOnInsn(this.visitor, insn);
	}

	@Override
	public boolean hasNext() {
		return this.facade.peekInstruction() != null;
	}

	@Override
	public JvmFacade getFacade() {
		return this.facade;
	}

}
