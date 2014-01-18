package main;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import svg.LevelLoader;

public class Game {
	// All of these values are scaled by the player's current size.
	private static final Vec2 GRAVITY = new Vec2(0, -.05);
	private static final Vec2 JUMP 	  = new Vec2(0, 1);
	private static final float MOVE_SPEED = .1f;
	private static final float VERT_DAMPING = .97f;
	private static final float HORZ_DAMPING = .9f;
	
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
		
		
		playerPos = new Vec2(300, 1000);
		playerVel = new Vec2(0, 0);
		playerSize = 20;
		playerSprite = new ColorRect(playerPos, new Vec2(playerSize, playerSize), .8f, .8f, 1);
		
		try {
			curLvl = LevelLoader.load("level_test.svg");
		} catch (IOException e) {
			System.err.println("Failed to load level.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void step() {
		/*
		 * Handle controls
		 */
		if (onPlatform && (jumpTimeout == 1) && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			playerVel = playerVel.add(JUMP.mult(playerSize));
			jumpTimeout = JUMP_TIMEOUT;
		}
		if (jumpTimeout > 1)
			jumpTimeout--;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			playerVel = playerVel.add(new Vec2(-MOVE_SPEED * playerSize, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			playerVel = playerVel.add(new Vec2(MOVE_SPEED * playerSize, 0));
		}
		
		// Gravity
		playerVel = playerVel.add(GRAVITY.mult(playerSize));
		playerVel.y *= VERT_DAMPING;
		playerVel.x *= HORZ_DAMPING;
		playerPos = playerPos.add(playerVel);
		
		/*
		 * Handle collisions with platforms
		 */
		onPlatform = false;
		for (Platform plat : curLvl.getPlatforms()) {
			if ((playerPos.x + playerSize) > plat.getPos().x 		&&
				playerPos.x < (plat.getPos().x + plat.getDim().x) 	&& 
				(playerPos.y + playerSize) > plat.getPos().y 		&&
				playerPos.y < (plat.getPos().y + plat.getDim().y)) {
				
				if (playerVel.y < 0) {
					playerPos.y = plat.getPos().y + plat.getDim().y;
					playerVel.y = 0;
					onPlatform = true;
				} else if (playerVel.y > 0) {
					playerPos.y = plat.getPos().y - playerSize;
					playerVel.y = -playerVel.y;
				}
			}
		}
	}
	
	public void draw() {
		Vec2 cameraOffset = playerPos.add(
				new Vec2(playerSize / 2 - w / 2, playerSize / 2 - h / 2));
		
		playerSprite.setDrawPos(playerPos);
		playerSprite.setDrawDim(new Vec2(playerSize, playerSize));
		playerSprite.draw(cameraOffset);
		
		curLvl.draw(cameraOffset);
	}
}
