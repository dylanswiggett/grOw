package main;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import text.Fonts;

public class Text implements Drawable {

	String string;
	private Vec2 pos;
	private float size;
	
	public Text(String string, Vec2 pos, float size) {
		this.string = string;
		this.pos = pos;
		this.size = size;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public float getSize() {
		return size;
	}

	@Override
	public void draw() {
		Fonts.draw(Fonts.ABSENDER, (int) size, pos, string, Color.white);
	}
	
}