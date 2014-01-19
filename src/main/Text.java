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
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glPushMatrix();
		//GL11.glTranslatef(-pos.x + Init.WIDTH / 2, -pos.y + Init.HEIGHT / 2, 0);
		//Fonts.getFont(Fonts.ABSENDER, size).drawString(pos.x, pos.y, string, Color.white);
		//GL11.glPopMatrix();
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		Fonts.draw(Fonts.ABSENDER, (int) size, pos, string, Color.white);
	}
	
}
