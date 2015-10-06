package com.lum.scram.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lum.scram.Scram;
import java.lang.Thread.UncaughtExceptionHandler;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 540;
		//config.resizable = false;
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, final Throwable ex) {
			String error = "A critical system or hardware error has occurred: \n\n\t" + ex.getLocalizedMessage()
			+ "\n\t" + ex.getCause();

			System.err.println("Critical Failure: " + error);
			ex.printStackTrace();

			System.exit(0);
			}
		});
		
		new LwjglApplication(new Scram(), config);
	}
}
