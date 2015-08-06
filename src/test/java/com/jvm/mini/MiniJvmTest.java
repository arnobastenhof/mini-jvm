package com.jvm.mini;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

import com.jvm.mini.api.InsnVisitor;
import com.jvm.mini.api.JvmFacade;
import com.jvm.mini.api.JvmInterpreter;
import com.jvm.mini.data.ClassBuilder;
import com.jvm.mini.data.ClassMother;
import com.jvm.mini.impl.InsnVisitorImpl;
import com.jvm.mini.impl.JvmInterpreterImpl;
import com.jvm.mini.main.JvmInitializer;
import com.jvm.mini.util.Instructions;

// TODO Tests rely too much on String comparisons; very fragile!
public class MiniJvmTest {

	// Generated test classes
	private static final String CLASS_EXTENSION = ".class";
	private static final File TARGET_DIR =
			new File("target/generated-classes/" + ClassBuilder.PCKG);
	private static final File ARITHMETIC_CLASS =
			new File(TARGET_DIR, ClassMother.ARITHMETIC + CLASS_EXTENSION);
	private static final File LOAD_STORE_CLASS =
			new File(TARGET_DIR, ClassMother.LOAD_STORE + CLASS_EXTENSION);
	private static final File IINC_CLASS =
			new File(TARGET_DIR, ClassMother.IINC + CLASS_EXTENSION);
	private static final File IF_THEN_ELSE_CLASS =
			new File(TARGET_DIR, ClassMother.IF_THEN_ELSE + CLASS_EXTENSION);
	private static final File LOOP_CLASS =
			new File(TARGET_DIR, ClassMother.LOOP + CLASS_EXTENSION);

	@Before
	public void init() throws FileNotFoundException, IOException {
		// Generate target dir
		TARGET_DIR.mkdirs();

		// Generate test classes
		writeClass(ARITHMETIC_CLASS, ClassMother.arithmetic());
		writeClass(LOAD_STORE_CLASS, ClassMother.loadStore());
		writeClass(IINC_CLASS, ClassMother.iinc());
		writeClass(IF_THEN_ELSE_CLASS, ClassMother.ifThenElse());
		writeClass(LOOP_CLASS, ClassMother.loop());
	}

	// Ensures test classes are generated even if other tests are set to ignore
	@Test
	public void pseudoTest() { }

	@Ignore @Test
	public void arithmeticTest() throws IOException {
		JvmAssert.forClass(ARITHMETIC_CLASS)
			.state("", "", "BIPUSH").nextInsn()
			.state("I", "2", "BIPUSH").nextInsn()
			.state("II", "3", "IMUL").nextInsn()
			.state("I", "6", "BIPUSH").nextInsn()
			.state("II", "6", "INEG").nextInsn()
			.state("II", "-6", "SIPUSH").nextInsn()
			.state("III", "128", "ISUB").nextInsn()
			.state("II", "-134", "IADD").nextInsn()
			.state("I", "-128", "RETURN").nextInsn().isDone();
	}

	@Ignore @Test
	public void loadStoreTest() throws IOException {
		JvmAssert.forClass(LOAD_STORE_CLASS)
			.state("", "", "BIPUSH").nextInsn()
			.state("I", "21", "ISTORE").nextInsn()
			.state("", "", "ILOAD").nextInsn()
			.state("I", "21", "BIPUSH").nextInsn()
			.state("II", "2", "IMUL").nextInsn()
			.state("I", "42", "I2S").nextInsn()
			.state("I", "42", "ISTORE").nextInsn()
			.state("", "", "RETURN").nextInsn().isDone();
	}

	@Ignore @Test
	public void iincTest() throws IOException {
		JvmAssert.forClass(IINC_CLASS)
			.state("", "", "LDC").nextInsn()
			.state("I", "-889275715", "ISTORE").nextInsn()
			.state("", "", "IINC").nextInsn()
			.state("", "", "RETURN").nextInsn().isDone();
	}

	@Ignore @Test
	public void ifThenElseTest() throws IOException {
		JvmAssert.forClass(IF_THEN_ELSE_CLASS)
			.state("", "", "LDC").nextInsn()
			.state("J", "0", "LDC").nextInsn()
			.state("JJ", "1", "LCMP").nextInsn()
			.state("I", "-1", "IFEQ").nextInsn()
			.state("", "", "GOTO").nextInsn()
			.state("", "", "RETURN").nextInsn().isDone();
	}

	@Ignore @Test
	public void loopTest() throws IOException {
		JvmAssert.forClass(LOOP_CLASS)
			.state("", "", "BIPUSH").nextInsn()
			.state("I","0","ISTORE").nextInsn()
			.state("","","GOTO").nextInsn()
			.state("","","ILOAD").nextInsn()
			.state("I","0","BIPUSH").nextInsn()
			.state("II","3","IF_ICMPLT").nextInsn()
			.state("","","IINC").nextInsn()
			.state("","","ILOAD").nextInsn()
			.state("I","1","BIPUSH").nextInsn()
			.state("II","3","IF_ICMPLT").nextInsn()
			.state("","","IINC").nextInsn()
			.state("","","ILOAD").nextInsn()
			.state("I","2","BIPUSH").nextInsn()
			.state("II","3","IF_ICMPLT").nextInsn()
			.state("","","IINC").nextInsn()
			.state("","","ILOAD").nextInsn()
			.state("I","3","BIPUSH").nextInsn()
			.state("II","3","IF_ICMPLT").nextInsn()
			.state("","","RETURN").nextInsn().isDone();
	}

	private void writeClass(File target, byte[] clazz)
			throws FileNotFoundException, IOException {
		// Verify byte array
		try (StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw)) {
			CheckClassAdapter.verify(new ClassReader(clazz), false, pw);
			assertTrue(sw.toString(), sw.toString().length() == 0);
		}
		// Write the file
		try (FileOutputStream fos = new FileOutputStream(target)) {
			fos.write(clazz);
		}
	}

	private static class JvmAssert {

		private final JvmInterpreter interpreter;
		private final JvmFacade facade;

		private JvmAssert(JvmInterpreter interpreter) {
			assert interpreter != null;
			this.interpreter = interpreter;
			this.facade = interpreter.getFacade();
		}

		private static JvmAssert forClass(File classFile) throws IOException {
			JvmFacade facade = JvmInitializer.INSTANCE.init(classFile);
			InsnVisitor visitor = new InsnVisitorImpl(facade);
			JvmInterpreter interpreter = new JvmInterpreterImpl(facade, visitor);
			return new JvmAssert(interpreter);
		}

		private JvmAssert nextInsn() {
			assertTrue(this.interpreter.hasNext());
			this.interpreter.next();
			return this;
		}

		private JvmAssert state(String operandTypes, String operand,
				String mnemonic) {
			assertEquals(operandTypes, this.facade.getOperandTypes());
			assertEquals(operand, this.facade.peekOperand());
			String insn = Instructions.toString(this.facade.peekInstruction());
			assertTrue(insn.startsWith(mnemonic));
			return this;
		}

		private void isDone() {
			assertFalse(this.interpreter.hasNext());
		}
	}

}
