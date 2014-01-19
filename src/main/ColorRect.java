package main;

import org.lwjgl.opengl.GL11;

public class ColorRect implements Drawable {
	
	private static final TexturedRect rec =
			new TexturedRect(new Vec2(0, 0), new Vec2(0, 0), "assets/textures/whitePixel.png", 1, 1, 1);

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
		rec.setPos(colorRectPos);
		rec.setDim(colorRectDim);
		rec.setColor(r, g, b);
		rec.draw();
	}
}