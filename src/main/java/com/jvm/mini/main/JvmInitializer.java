package com.jvm.mini.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.jvm.mini.api.JvmFacade;
import com.jvm.mini.impl.JvmFacadeImpl;
import com.jvm.mini.util.Validate;

/**
 * Class used for initializing a JVM instance based on an input class.
 *
 * @author Arno Bastenhof
 */
public enum JvmInitializer {

	/**
	 * The unique instance of this class
	 */
	INSTANCE;

	private static final String ERROR_MAIN_CLASS =
			"Could not find or load main class %s.";
	private static final String ERROR_MAIN_METHOD =
			"Could not find main method.";

	/**
	 * Returns an initialized {@link JvmFacade} for the specified class file.
	 *
	 * @param classFile the class file to be loaded
	 * @throws IOException
	 */
	public JvmFacade init(File classFile) throws IOException {
		// Validate preconditions
		Validate.notNull(classFile);

		// Load initial class
		ClassNode clazz;
		try (InputStream is = new FileInputStream(classFile)) {
			clazz = loadInitialClass(is);
		}
		catch (IOException e) {
			throw new IOException(
					String.format(ERROR_MAIN_CLASS, classFile.toString()), e);
		}

		// TODO Process static initializers (<clinit>)

		// Find main method
		MethodNode method = findMainMethod(clazz);
		Validate.argument(method != null, ERROR_MAIN_METHOD);

		// Create and initialize a facade for the VM's internals
		return initialize(method);
	}

	private ClassNode loadInitialClass(InputStream is)
			throws IOException {
		ClassNode result = new ClassNode();
		ClassReader cr = new ClassReader(is);
		cr.accept(result, 0);
		return result;
	}

	private MethodNode findMainMethod(ClassNode clazz) {
		Iterator<?> it = clazz.methods.iterator();
		while (it.hasNext()) {
			MethodNode method = (MethodNode)it.next();
			if (method.name.equals("main")
				&& hasModifier(method, Opcodes.ACC_PUBLIC)
				&& hasModifier(method, Opcodes.ACC_STATIC)) {
				return method;
			}
		}
		return null;
	}

	private boolean hasModifier(MethodNode method, int modifier) {
		return (method.access & modifier) != 0;
	}

	private JvmFacadeImpl initialize(MethodNode method) {
		JvmFacadeImpl facade = new JvmFacadeImpl();
		facade.pushFrame(method.maxStack, method.maxLocals, new Object[]{});
		facade.jump(method.instructions.getFirst());
		return facade;
	}

}
