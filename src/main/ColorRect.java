package main;

import org.lwjgl.opengl.GL11;

public class ColorRect implements Drawable {

	// Position and dimension (dimension must be positive)
	private Vec2 colorRectPos, colorRectDim;
	private float r, g, b;
	
	public ColorRect(Vec2 pos, Vec2 dim, float r, float g, float b) {
		this.colorRectPos = pos;
		this.colorRectDim = dim;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void setDrawPos(Vec2 pos) {
		colorRectPos = pos;
	}
	
	public void setDrawDim(Vec2 dim) {
		colorRectDim = dim;
	}
	
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	@Override
	public void draw() {
		GL11.glColor3f(r, g, b);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(colorRectPos.x, colorRectPos.y, 0);
		GL11.glScalef(colorRectDim.x, colorRectDim.y, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(1, 0);
			GL11.glVertex2f(1, 1);
			GL11.glVertex2f(0, 1);
		}
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
}