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
		// TODO Auto-generated method stub

	}

	@Override
	public void sipush(int operand) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ldc(Object constant) {
		// TODO Auto-generated method stub

	}

	@Override
	public void i2b() {
		// TODO Auto-generated method stub

	}

	@Override
	public void i2s() {
		// TODO Auto-generated method stub

	}

	@Override
	public void i2l() {
		// TODO Auto-generated method stub

	}

	@Override
	public void l2i() {
		// TODO Auto-generated method stub

	}

	@Override
	public void iadd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ladd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void isub() {
		// TODO Auto-generated method stub

	}

	@Override
	public void lsub() {
		// TODO Auto-generated method stub

	}

	@Override
	public void imul() {
		// TODO Auto-generated method stub

	}

	@Override
	public void lmul() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ineg() {
		// TODO Auto-generated method stub

	}

	@Override
	public void lneg() {
		// TODO Auto-generated method stub

	}

	@Override
	public void iload(int var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lload(int var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void istore(int var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lstore(int var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void iinc(int var, int increment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lcmp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifIcmpeq(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifIcmpne(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifIcmplt(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifIcmple(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifIcmpgt(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifIcmpge(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifEq(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifNe(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifLt(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifLe(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifGt(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ifGe(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void goTo(AbstractInsnNode jumpTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void voidReturn() {
		// TODO Auto-generated method stub

	}

}
