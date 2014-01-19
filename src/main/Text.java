package main;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import text.Fonts;

public class Text implements Drawable {

	String string;
	private Vec2 pos;
	private float size;
	private float scale;
	
	public Text(String string, Vec2 pos, float size) {
		this(string, pos, size, 1.0f);
	}
	
	public Text(String string, Vec2 pos, float size, float scale) {
		this.string = string;
		this.pos = pos;
		this.size = size;
		this.scale = scale;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public float getSize() {
		return size;
	}

	@Override
	public void draw() {
		GL11.glPushMatrix();
//		GL11.glScalef(.25f, .25f, 0);
		Fonts.draw(Fonts.ABSENDER, size, pos, string, Color.white, .25f * scale);
		GL11.glPopMatrix();
	}
	
}
