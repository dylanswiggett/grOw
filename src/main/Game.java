package main;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import sound.Sound;
import svg.LevelLoader;

public class Game {
	// All of these values are scaled by the player's current size.
	private static final Vec2 GRAVITY = new Vec2(0, -.05);
	private static final Vec2 JUMP 	  = new Vec2(0, 1);
	private static final float MOVE_SPEED = .1f;
	private static final float VERT_DAMPING = .97f;
	private static final float HORZ_DAMPING = .8f;
	
	private static final int JUMP_TIMEOUT = 30;
	
	private int w, h;
	
	private Vec2 playerPos, playerVel;
	private float playerSize;
	private ColorRect playerSprite;
	
	private Level curLvl;
	
	private boolean onPlatform = false;
	private int jumpTimeout = 1;
	
	public Game(int w, int h) {
		this.w = w;
		this.h = h;
		
		playerSprite = new ColorRect(playerPos, new Vec2(playerSize, playerSize), .8f, .8f, 1);
		
		try {
			curLvl = LevelLoader.load("test2.svg");
		} catch (IOException e) {
			System.err.println("Failed to load level.");
			e.printStackTrace();
			System.exit(1);
		}
		
		playerPos = curLvl.getPlayerInitialPosition();
		playerVel = new Vec2(0, 0);
		playerSize = curLvl.getPlayerInitialDimensions().y;
		System.out.println(playerPos.x);
	}
	
	public void step() {
		/*
		 * Handle controls
		 */
		if (onPlatform && (jumpTimeout == 1) && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			playerVel = playerVel.add(JUMP.mult(playerSize));
			jumpTimeout = JUMP_TIMEOUT;
			Sound.JUMP.playAsSoundEffect(1f, 1f, false);
		}
		if (jumpTimeout > 1)
			jumpTimeout--;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			playerVel = playerVel.add(new Vec2(-MOVE_SPEED * playerSize, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			playerVel = playerVel.add(new Vec2(MOVE_SPEED * playerSize, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			playerVel.y = playerSize / 2;
		}
		
		// Gravity
		playerVel = playerVel.add(GRAVITY.mult(playerSize));
		playerVel.y *= VERT_DAMPING;
		playerVel.x *= HORZ_DAMPING;
		playerPos = playerPos.add(playerVel);
		
		Vec2 nextPlayerVel = new Vec2(playerVel);
		
		/*
		 * Handle collisions with platforms
		 */
		onPlatform = false;
		for (Platform plat : curLvl.getPlatforms()) {
			if ((playerPos.x + playerSize) > plat.getPos().x 		&&
				playerPos.x < (plat.getPos().x + plat.getDim().x) 	&& 
				(playerPos.y + playerSize) > plat.getPos().y 		&&
				playerPos.y < (plat.getPos().y + plat.getDim().y)) {
				
				float left   = (playerPos.x + playerSize - plat.getPos().x);
				float right  = (playerPos.x - plat.getPos().x - plat.getDim().x);
				float bottom = (playerPos.y + playerSize - plat.getPos().y);
				float top    = (playerPos.y - plat.getPos().y - plat.getDim().y);
				
				if (playerVel.x != 0) {
					left   /= playerVel.x;
					right  /= playerVel.x;
				}
				if (playerVel.y != 0) {
					bottom /= playerVel.y;
					top    /= playerVel.y;
				}
				
				if (left > 0 && (left < right  || right  < 0) &&
								(left < top    || top    < 0) &&
								(left < bottom || bottom < 0)) {
					nextPlayerVel.x = 0;
					playerPos.x = plat.getPos().x - playerSize - .5f;
				} else if (right > 0 && (right < top    || top < 0) &&
										(right < bottom || bottom < 0)) {
					nextPlayerVel.x = 0;
					playerPos.x = plat.getPos().x + plat.getDim().x + .5f;
				} else if (top > 0 && (top < bottom || bottom < 0)) {
					nextPlayerVel.y = 0;
					playerPos.y = plat.getPos().y + plat.getDim().y;
					onPlatform = true;
				} else {
					nextPlayerVel.y = 0;
					playerPos.y = plat.getPos().y - playerSize;
				}
			}
		}
		
		playerVel = nextPlayerVel;
	}
	
	public void draw() {
		
		GL11.glPushMatrix();
		
		float scale = 20 / playerSize;
		
		GL11.glTranslatef(-playerPos.x * scale + w / 2,
						  -playerPos.y * scale + h / 2, 0);
		GL11.glScalef(scale, scale, 0);
		
		playerSprite.setDrawPos(playerPos);
		playerSprite.setDrawDim(new Vec2(playerSize, playerSize));
		playerSprite.draw();
		
		curLvl.draw();
		
		GL11.glPopMatrix();
	}
}
