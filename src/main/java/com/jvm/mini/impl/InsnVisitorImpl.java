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
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.bipush
	}

	@Override
	public void sipush(int operand) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.sipush
	}

	@Override
	public void ldc(Object constant) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc
	}

	@Override
	public void i2b() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2b
	}

	@Override
	public void i2s() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2s
	}

	@Override
	public void i2l() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2l
	}

	@Override
	public void l2i() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.l2i
	}

	@Override
	public void iadd() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iadd
	}

	@Override
	public void ladd() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ladd
	}

	@Override
	public void isub() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.isub
	}

	@Override
	public void lsub() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lsub
	}

	@Override
	public void imul() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.imul
	}

	@Override
	public void lmul() {
		// TODO https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lmul
	}

	@Override
	public void ineg() {
		// TODO https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ineg
	}

	@Override
	public void lneg() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lneg
	}

	@Override
	public void iload(int var) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iload
	}

	@Override
	public void lload(int var) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lload
	}

	@Override
	public void istore(int var) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.istore
	}

	@Override
	public void lstore(int var) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lstore
	}

	@Override
	public void iinc(int var, int increment) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iinc
	}

	@Override
	public void lcmp() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lcmp
	}

	@Override
	public void ifIcmpeq(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond
	}

	@Override
	public void ifIcmpne(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond
	}

	@Override
	public void ifIcmplt(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond
	}

	@Override
	public void ifIcmple(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond
	}

	@Override
	public void ifIcmpgt(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond
	}

	@Override
	public void ifIcmpge(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond
	}

	@Override
	public void ifEq(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond
	}

	@Override
	public void ifNe(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond
	}

	@Override
	public void ifLt(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond
	}

	@Override
	public void ifLe(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond
	}

	@Override
	public void ifGt(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond
	}

	@Override
	public void ifGe(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond
	}

	@Override
	public void goTo(AbstractInsnNode jumpTarget) {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.goto
	}

	@Override
	public void voidReturn() {
		// TODO see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.return
	}

}
