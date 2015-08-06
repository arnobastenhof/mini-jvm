package com.jvm.mini.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class.
 *
 * @author Arno Bastenhof
 */
public final class Operands {

	// Private constructor to prevent instantiation
	private Operands() {
		throw new AssertionError();
	}

	private static final Map<Class<?>, FieldDescr> FIELD_DESCRIPTORS;

	static {
		FIELD_DESCRIPTORS = new HashMap<>();
		for (FieldDescr descr : FieldDescr.values()) {
			FIELD_DESCRIPTORS.put(descr.clazz, descr);
		}
	}

	/**
	 * Returns the field descriptor for the specified {@code clazz}.
	 *
	 * @param clazz a Java type
	 * @throws IllegalArgumentException if {@code clazz} is unsupported
	 */
	public static String getFieldDescriptor(Class<?> clazz) {
		FieldDescr descr = FIELD_DESCRIPTORS.get(clazz);
		Validate.argument(descr != null);
		return descr.value;
	}

	private enum FieldDescr {
		INTEGER(Integer.class, "I"),
		LONG(Long.class, "J");

		private final Class<?> clazz;
		private final String value;

		private FieldDescr(Class<?> clazz, String descr) {
			this.clazz = clazz;
			this.value = descr;
		}
	}

}
