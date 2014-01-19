package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import sound.Sound;

public class Init {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static Game game;
	
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("grOw -- developer edition");
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			System.err.println("Failed to intialize display. Exiting.");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Load sound files. We can move this later if it takes too long.
		Sound.init();
		
		game = new Game(WIDTH, HEIGHT);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		while (! Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			Display.sync(60);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			game.step();
			game.draw();
			
			Display.update();
		}
		
		Display.destroy();
		AL.destroy();
	}
}
