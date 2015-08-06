package com.jvm.mini.util;

/**
 * Replicates the validation logic from Guava's {@code Preconditions} and
 * Apache Commons' {@code Validate}, obviating the need to include either as
 * dependencies to help keep down the size of the generated jar.
 *
 * @author Arno Bastenhof
 *
 */
public final class Validate {

	// Private constructor to prevent instantiation.
	private Validate() {
		throw new AssertionError();
	}

	/**
	 * Returns its argument if not null, otherwise throwing an exception.
	 *
	 * @param obj the object to be null-checked
	 * @throws NullPointerException if {@code obj == null}
	 */
	public static <T> T notNull(T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

	/**
	 *
	 * @param cond the condition to be validated
	 * @throws IllegalArgumentException if {@code cond} is false
	 */
	public static void argument(boolean cond) {
		exception(cond, IllegalArgumentException.class);
	}

	/**
	 *
	 * @param cond the condition to be validated
	 * @param msg the exception message
	 * @throws IllegalArgumentException if {@code cond} is false
	 */
	public static void argument(boolean cond, String msg) {
		exception(cond, IllegalArgumentException.class, msg);
	}

	/**
	 *
	 * @param cond the condition to be validated
	 * @throws IllegalStateException if {@code cond} is false
	 */
	public static void state(boolean cond) {
		exception(cond, IllegalStateException.class);
	}

	private static void exception(boolean cond,
			Class<? extends RuntimeException> clazz) {
		if (!cond) {
			try {
				throw clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void exception(boolean cond,
			Class<? extends RuntimeException> clazz, String msg) {
		if (!cond) {
			try {
				throw clazz.getDeclaredConstructor(String.class)
					.newInstance(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
