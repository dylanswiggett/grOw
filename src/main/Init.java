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
		
		while (! Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			GL11.glColor3f(1, 0, 0);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2f(100, 100);
				GL11.glVertex2f(400, 100);
				GL11.glVertex2f(400, 400);
				GL11.glVertex2f(100, 400);
			}
			GL11.glEnd();
			
			Display.update();
		}
		
		Display.destroy();
	}
}
