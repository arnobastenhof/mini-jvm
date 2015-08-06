package com.jvm.mini.main;

import java.io.File;
import java.io.IOException;

import com.jvm.mini.api.JvmFacade;
import com.jvm.mini.api.JvmInterpreter;
import com.jvm.mini.impl.InsnVisitorImpl;
import com.jvm.mini.impl.JvmInterpreterImpl;
import com.jvm.mini.util.Instructions;

/**
 * Main Class.
 *
 * @author Arno Bastenhof
 */
public final class Main {

	private static final String USAGE = "Usage: "
			+ "java -jar mini-jvm-<version>.jar class (to execute a class)";
	private static final String CLASS_EXTENSION = ".class";

	// Private constructor to prevent instantiation
	private Main() {
		throw new AssertionError();
	}

	// TODO Error handling
	public static void main(String[] args) throws IOException {
		// Verify no. of command-line arguments
		if (args.length == 0) {
			System.out.println(USAGE);
			return;
		}

		// Create a new JVM instance
		File classFile = new File(getFileName(args[0]));
		JvmFacade facade = JvmInitializer.INSTANCE.init(classFile);

		// Execute
		execute(facade);
	}

	// Converts a fully qualified class name to a relative path
	private static String getFileName(String fqc) {
		return fqc.replaceAll("\\.", File.separator).concat(CLASS_EXTENSION);
	}

	private static void execute(JvmFacade facade) throws IOException {
		JvmInterpreter interpreter = new JvmInterpreterImpl(
				facade,	new InsnVisitorImpl(facade));
		while (interpreter.hasNext()) {
			// Print debugging information
			String line = new StringBuilder()
				.append(String.format("%15s ", facade.getOperandTypes()))
				.append(String.format("%20s    ", facade.peekOperand()))
				.append(Instructions.toString(facade.peekInstruction()))
				.toString();
			System.out.println(line);

			// Execute
			interpreter.next();
		}
	}
}
