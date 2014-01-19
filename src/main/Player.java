package main;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Player extends TexturedRect {

	// Lower is faster!
	private static final int WALK_CYCLE_SPEED = 5;
	
	private Texture[] textures;
	private int walkCycleIndex;
	private int walkCycleCounter;
	private boolean movingRight;
	
	public Player(Vec2 pos, Vec2 dim) {
		super(pos, dim);
		
		textures = new Texture[8];
		for (int i = 0; i < textures.length; i++) {
			try {
				textures[i] = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream("assets/textures/playerWalk" + i + ".png"));
				textures[i].bind();
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			} catch (IOException e) {
				e.printStackTrace();
			}
			walkCycleIndex = 0;
			step();
			movingRight = true;
			r = 1;
			g = 1;
			b = 1;
		}
	}
	
	public void step() {
		walkCycleCounter++;
		if (walkCycleCounter > 5) {
			walkCycleIndex = (walkCycleIndex + 1) % textures.length;
			texture = textures[walkCycleIndex];
			walkCycleCounter = 0;
		}
	}
	
	public void standStill() {
		walkCycleIndex = 0;
		texture = textures[0];
	}
	
	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}
	
	public void draw() {
		if (movingRight) {
			super.draw();
		} else {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			new Color(r, g, b).bind();
			texture.bind();
			
			GL11.glPushMatrix();
			GL11.glTranslatef(getPos().x, getPos().y, 0);
			GL11.glScalef(getDim().x, getDim().y, 0);
			
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(1, 1);
				GL11.glTexCoord2f(0, texture.getHeight());
				GL11.glVertex2f(1, 0);
				GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
				
				GL11.glVertex2f(0, 0);
				GL11.glTexCoord2f(texture.getWidth(), 0);
				
				GL11.glVertex2f(0, 1);
				
			}
			GL11.glEnd();
			
			GL11.glPopMatrix();
		}
	}

}
