package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Init {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("grOw -- developer edition");
			Display.create();
		} catch (LWJGLException e) {
			System.err.println("Failed to intialize display. Exiting.");
			e.printStackTrace();
			System.exit(1);
		}
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		ColorRect testRect = new ColorRect(new Vec2(100, 100), new Vec2(300, 400), 1, 0, 0);
		
		while (! Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			testRect.draw(new Vec2(0, 0));
			
			Display.update();
		}
		
		Display.destroy();
	}
}
