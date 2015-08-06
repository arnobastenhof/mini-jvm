package com.jvm.mini.data;

import org.objectweb.asm.Label;

import com.jvm.mini.data.ClassBuilder.Comparison;

/**
 * Object Mother producing class files that can be used for testing.
 *
 * @author Arno Bastenhof
 */
public final class ClassMother {

	// Private constructor to prevent instantiation
	private ClassMother() {
		throw new AssertionError();
	}

	// Class names
	public static final String ARITHMETIC = "Arithmetic";
	public static final String LOAD_STORE = "LoadStore";
	public static final String IINC = "Iinc";
	public static final String IF_THEN_ELSE = "IfThenElse";
	public static final String LOOP = "Loop";

	public static byte[] arithmetic() {
		return ClassBuilder.forClass(ARITHMETIC)
				.push(Integer.class, Integer.valueOf(2))   // bipush 2
				.push(Integer.class, Integer.valueOf(3))   // bipush 3
				.multiply(Integer.class)                   // imul
				.push(Integer.class, Integer.valueOf(6))   // bipush 6
				.negate(Integer.class)                     // ineg
				.push(Integer.class, Integer.valueOf(128)) // sipush 128
				.subtract(Integer.class)                   // isub
				.add(Integer.class)                        // iadd
				.build();                                  // return
	}

	public static byte[] loadStore() {
		return ClassBuilder.forClass(LOAD_STORE)
				.push(Integer.class, Integer.valueOf(21))  // bipush 21
				.store(Integer.class, 0)                   // istore 0
				.load(Integer.class, 0)                    // iload 0
				.push(Integer.class, Integer.valueOf(2))   // bipush 2
				.multiply(Integer.class)                   // imul
				.cast(Integer.class, Short.class)          // i2s
				.store(Integer.class, 0)                   // istore 0
				.build();                                  // return
	}

	public static byte[] iinc() {
		return ClassBuilder.forClass(IINC)
				.push(Integer.class, 0xCAFEBABD)           // ldc -889275715
				.store(Integer.class, 0)                   // istore 0
				.increment(0, 1)                           // iinc 0 1
				.build();                                  // return
	}

	public static byte[] ifThenElse() {
		Label thenLabel = new Label();
		Label endLabel = new Label();
		return ClassBuilder.forClass(IF_THEN_ELSE)
				.push(Long.class, Long.valueOf(0))         // ldc 0L
				.push(Long.class, Long.valueOf(1))         // ldc 1L
				.ifCmpThen(Long.class, Comparison.EQ, thenLabel) // lcmp, ifeq Then
				// Else code
				.goTo(endLabel)                            // goto endLabel
				.label(thenLabel)                          // Then:
				// Then code
				.label(endLabel)                           // End:
				.build();                                  // return
	}

	public static byte[] loop() {
		Label bodyLabel = new Label();
		Label conditionLabel = new Label();
		return ClassBuilder.forClass(LOOP)
				.push(Integer.class, Integer.valueOf(0))   // bipush 0
				.store(Integer.class, 1)                   // istore 1
				.goTo(conditionLabel)                      // goto Condition
				.label(bodyLabel)                          // Body:
				.increment(1, 1)                           // iinc 1 1
				.label(conditionLabel)                     // Condition:
				.load(Integer.class, 1)                    // iload 1
				.push(Integer.class, Integer.valueOf(3)) // bipush 100
				.ifCmpThen(Integer.class, Comparison.LT, bodyLabel) // if_icmplt Body
				.build();                                  // return
	}

}
