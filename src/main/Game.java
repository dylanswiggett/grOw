package main;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import sound.Sound;
import svg.LevelLoader;
import text.Fonts;

public class Game {
	// All of these values are scaled by the player's current size.
	private static final Vec2 GRAVITY = new Vec2(0, -.03);
	private static final Vec2 JUMP 	  = new Vec2(0, .5);
	private static final float MOVE_SPEED = .1f;
	private static final float VERT_DAMPING = .97f;
	private static final float HORZ_DAMPING = .7f;
	private static final int VISIBLE_PLAYER_HEIGHT = 40;
	private static final int JUMP_TIMEOUT = 30;
	private static final float GROWTH_RATE = .001f;
	private static final float GROWTH_SCALE = .2f;
	
	private static TexturedRect background;
	
	private int w, h;
	
	private Vec2 playerPos, playerVel;
	private float playerSize;
	private float playerGrowthSpeed = 0;
	private float originalPlayerSize;
	private float goalPlayerSize;
	private Player playerSprite;
	
	private Level curLvl;
	
	private boolean onPlatform = false;
	private int jumpTimeout = 1;
	
	private int counter = 1;
	
	// If the player is saying something, it will be said for this many more frames.
	private int sayCounter;
	private String saying;
	
	public Game(int w, int h) {
		this.w = w;
		this.h = h;
		
		playerSprite = //new ColorRect(playerPos, new Vec2(playerSize, playerSize), .8f, .8f, 1);
				new Player(playerPos, new Vec2(playerSize, playerSize));
				
		background =
				new TexturedRect(new Vec2(0, 0), new Vec2(w, h), "assets/textures/background.png", 1, 1, 1);
		
		try {
			curLvl = LevelLoader.load("another.svg");
		} catch (IOException e) {
			System.err.println("Failed to load level.");
			e.printStackTrace();
			System.exit(1);
		}
		
		playerPos = curLvl.getPlayerInitialPosition();
		playerVel = new Vec2(0, 0);
		playerSize = curLvl.getPlayerInitialDimensions().y;
		goalPlayerSize = playerSize;
		System.out.println(playerPos.x);
	}
	
	public void step() {
		/*
		 * Handle controls
		 */
		if (onPlatform && (jumpTimeout == 1) && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			playerVel = playerVel.add(JUMP.mult(playerSize));
			jumpTimeout = JUMP_TIMEOUT;
			Sound.play(Sound.JUMP);
		}
		if (jumpTimeout > 1)
			jumpTimeout--;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			playerVel = playerVel.add(new Vec2(-MOVE_SPEED * playerSize, 0));
			playerSprite.setFacingRight(false);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			playerVel = playerVel.add(new Vec2(MOVE_SPEED * playerSize, 0));
			playerSprite.setFacingRight(true);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			playerVel.y = playerSize / 2;
		}
		
		// Animation
		
		if (playerVel.y < -0.01 * playerSize) {
			playerSprite.setAnimation(Player.Animation.FALLING);
		} else if(playerSprite.getAnimation() == Player.Animation.FALLING) {
			playerSprite.setAnimation(Player.Animation.LANDING);
		}
		
		playerSprite.step();
		
		if (playerSprite.getAnimation() == Player.Animation.WALKING && !(Keyboard.isKeyDown(Keyboard.KEY_LEFT) ^ Keyboard.isKeyDown(Keyboard.KEY_RIGHT))) {
			playerSprite.standStill();
		}
		
		// Growing
		
		if (playerSize >= goalPlayerSize) {
			playerSize = goalPlayerSize;
			playerGrowthSpeed = 0;
			// TODO: Stop whooshing sound
			Sound.stop(Sound.WHOOSH);
		} else if (playerSize < goalPlayerSize) {
			if (playerSize > (originalPlayerSize + goalPlayerSize) / 2 && playerGrowthSpeed > GROWTH_RATE)
				playerGrowthSpeed -= GROWTH_RATE;
			else
				playerGrowthSpeed += GROWTH_RATE;
			playerSize += playerGrowthSpeed;
			// TODO: Scale whooshing sound relative to playerGrowthSpeed.
			Sound.setVolume(Sound.WHOOSH, playerGrowthSpeed);
		}
		
		// Gravity
		playerVel = playerVel.add(GRAVITY.mult(playerSize));
		playerVel.y *= VERT_DAMPING;
		playerVel.x *= HORZ_DAMPING;
		playerPos = playerPos.add(playerVel);
		
		Vec2 nextPlayerVel = new Vec2(playerVel);
		
		ArrayList<Coin> coins = curLvl.getCoins();
		
		for (int i = 0; i < coins.size(); i++) {
			Coin c = coins.get(i);
			if ((playerPos.x + playerSize) > c.getPos().x 		&&
				playerPos.x < (c.getPos().x + c.getDim().x) 	&& 
				(playerPos.y + playerSize) > c.getPos().y 		&&
				playerPos.y < (c.getPos().y + c.getDim().y)) {
				System.out.println("COIN GET");
				curLvl.removeCoin(c);
				goalPlayerSize += c.getValue() * GROWTH_SCALE;
				originalPlayerSize = playerSize;
				playerGrowthSpeed = GROWTH_RATE;
				// TODO: Start whooshing sound
				Sound.play(Sound.WHOOSH);
			}
		}
		
		ArrayList<SayBubble> bubbles = curLvl.getBubbles();
		
		for (int i = 0; i < bubbles.size(); i++) {
			SayBubble b = bubbles.get(i);
			if ((playerPos.x + playerSize) > b.getPos().x 		&&
				playerPos.x < (b.getPos().x + b.getDim().x) 	&& 
				(playerPos.y + playerSize) > b.getPos().y 		&&
				playerPos.y < (b.getPos().y + b.getDim().y)) {
				if (!b.isPerm())
					curLvl.removeBubble(b);
				saying = b.getMessage();
				sayCounter = b.getDur();
			}
		}
		
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
					playerPos.x = plat.getPos().x - playerSize * 1f;
				} else if (right > 0 && (right < top    || top < 0) &&
										(right < bottom || bottom < 0)) {
					nextPlayerVel.x = 0;
					playerPos.x = plat.getPos().x + plat.getDim().x * 1.00f;
				} else if (top > 0 && (top < bottom || bottom < 0)) {
					if (playerVel.y < 2 * GRAVITY.y * playerSize)
						Sound.play(Sound.LAND);
					nextPlayerVel.y = 0;
					playerPos.y = plat.getPos().y + plat.getDim().y;
					onPlatform = true;
				} else {
					Sound.play(Sound.BUMP);
					nextPlayerVel.y = 0;
					playerPos.y = plat.getPos().y - playerSize;
				}
			}
		}
		
		playerVel = nextPlayerVel;
	}
	
	public void draw() {
		background.draw();
		
		float scale = VISIBLE_PLAYER_HEIGHT / playerSize;
		
		GL11.glPushMatrix();
		
		Vec2 cameraOffset = new Vec2(-playerPos.x * scale + w / 2,
				  -playerPos.y * scale + h / 2);
		
		GL11.glTranslatef(cameraOffset.x, cameraOffset.y, 0);
		GL11.glScalef(scale, scale, 0);
		
		// Used for shader
		Platform.cameraPos = cameraOffset;
		
		curLvl.draw();
		
		playerSprite.setPos(playerPos);
		playerSprite.setDim(new Vec2(playerSize, playerSize));
		playerSprite.draw();
				
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		
		GL11.glTranslatef(-playerPos.x + w / 2,
						  -playerPos.y + h / 2, 0);
		if (sayCounter > 0) {
			new TextBubble(playerPos.add(new Vec2(playerSize * scale, playerSize * scale)),
					saying).draw();
			sayCounter--;
		}
		GL11.glPopMatrix();
	}
}
