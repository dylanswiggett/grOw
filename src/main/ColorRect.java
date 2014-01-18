package main;

import org.lwjgl.opengl.GL11;

public class ColorRect implements Drawable {

	// Position and dimension (dimension must be positive)
	private Vec2 pos, dim;
	private float r, g, b;
	
	public ColorRect(Vec2 pos, Vec2 dim, float r, float g, float b) {
		this.pos = pos;
		this.dim = dim;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	@Override
	public void draw(Vec2 cameraOffset) {
		GL11.glColor3f(r, g, b);
		Vec2 screenPos1 = pos.subtract(cameraOffset);
		Vec2 screenPos2 = screenPos1.add(dim);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2f(screenPos1.x, screenPos1.y);
			GL11.glVertex2f(screenPos2.x, screenPos1.y);
			GL11.glVertex2f(screenPos2.x, screenPos2.y);
			GL11.glVertex2f(screenPos1.x, screenPos2.y);
		}
		GL11.glEnd();
	}
}