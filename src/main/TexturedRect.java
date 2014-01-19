package main;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TexturedRect implements Drawable{
	private Vec2 pos, dim;
	private Texture texture;
	private float r, g, b;
	
	public TexturedRect(Vec2 pos, Vec2 dim, String filePath, float r, float g, float b) {
		this.pos = pos;
		this.dim = dim;
		this.r = r;
		this.g = g;
		this.b = b;
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(filePath));
			texture.bind();
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
	                 GL11.GL_LINEAR_MIPMAP_NEAREST );
			// when texture area is large, bilinear filter the original
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public Vec2 getDim() {
		return dim;
	}
	
	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
	
	public void setDim(Vec2 dim) {
		this.dim = dim;
	}
	
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void draw() {
		GL11.glColor3f(r, g, b);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		texture.bind();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glScalef(dim.x, dim.y, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(1, 0);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(1, 1);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(0, 1);
			GL11.glTexCoord2f(0, 1);
		}
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
}
