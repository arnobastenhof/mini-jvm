package com.jvm.mini.api;

/**
 * Interface for an interpreter-based implementation of the JVM specification,
 * set up as an (external) Iterator over the sequence of executed instructions.
 *
 * While the use of the Iterator pattern makes the Client code responsible for
 * driving the fetch/decode/execute cycle, it also enables the latter to query
 * the machine's state in between executing instructions, thus making it
 * particularly amenable for use as part of a debugger. More specifically,
 * given a {@link JvmFacade} {@code facade} used to instantiate an instance
 * {@code interpreter} of the current class, the following client code is
 * typical for driving execution:
 * <pre>
 * while (interpreter.hasNext()) {
 *     // Call debugging methods on interpreter.getFacade()
 *     interpreter.next();
 * }
 * </pre>
 *
 * @author Arno Bastenhof
 */
public interface JvmInterpreter {

	/**
	 * Executes the next instruction.
	 *
	 * @throws IllegalStateException if {@link #hasNext()} returns false
	 */
	void next();

	/**
	 * Returns true iff there is a next instruction to execute.
	 */
	boolean hasNext();

	/**
	 * Returns the {@link JvmFacade} used to initialize this instance. Intended
	 * for debugging purposes.
	 */
	JvmFacade getFacade();

}