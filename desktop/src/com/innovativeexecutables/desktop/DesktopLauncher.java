package com.innovativeexecutables.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.innovativeexecutables.Gridlock;

import static com.innovativeexecutables.Gridlock.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = VIEWPORT_WIDTH;
		config.height = VIEWPORT_HEIGHT;
		new LwjglApplication(new Gridlock(), config);
	}
}
