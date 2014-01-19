package main;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Player extends TexturedRect {

	public enum Animation {WALKING, FALLING, LANDING};
	// Lower is faster!
	private static final int WALK_CYCLE_SPEED = 5;

	private Texture[][] textures;

	private Animation animation;
	private int cycleIndex;
	private int cycleCounter;

	private boolean facingRight;

	public Player(Vec2 pos, Vec2 dim) {
		super(pos, dim);

		textures = new Texture[Animation.values().length][];
		textures[Animation.WALKING.ordinal()] = new Texture[8];
		for (int i = 0; i < textures[Animation.WALKING.ordinal()].length; i++) {
			try {
				textures[Animation.WALKING.ordinal()][i] = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream("assets/textures/playerWalk" + i + ".png"));
				textures[Animation.WALKING.ordinal()][i].bind();
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		textures[Animation.FALLING.ordinal()] = new Texture[4];
		for (int i = 0; i < textures[Animation.FALLING.ordinal()].length; i++) {
			try {
				textures[Animation.FALLING.ordinal()][i] = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream("assets/textures/playerFalling" + i + ".png"));
				textures[Animation.FALLING.ordinal()][i].bind();
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		textures[Animation.LANDING.ordinal()] = new Texture[5];
		for (int i = 0; i < textures[Animation.LANDING.ordinal()].length; i++) {
			try {
				textures[Animation.LANDING.ordinal()][i] = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream("assets/textures/playerLand" + i + ".png"));
				textures[Animation.LANDING.ordinal()][i].bind();
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		animation = Animation.WALKING;
		cycleIndex = 0;
		texture = textures[animation.ordinal()][0];
		facingRight = true;
		r = g = b = 1;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void step() {
		if (animation == Animation.WALKING || animation == Animation.FALLING) {
			cycleCounter++;
			if (cycleCounter > 5) {
				cycleIndex = (cycleIndex + 1) % textures[animation.ordinal()].length;
				texture = textures[animation.ordinal()][cycleIndex];
				cycleCounter = 0;
			}
		} else if (animation == Animation.LANDING) {
			cycleCounter++;
			if (cycleCounter > 3) {
				cycleIndex++;
				if (cycleIndex < textures[animation.ordinal()].length) {
					texture = textures[animation.ordinal()][cycleIndex];
				} else {
					animation = Animation.WALKING;
				}
				cycleCounter = 0;
			}
		}
	}

	public void standStill() {
		cycleIndex = 0;
		texture = textures[animation.ordinal()][0];
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public void draw() {
		if (facingRight) {
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
