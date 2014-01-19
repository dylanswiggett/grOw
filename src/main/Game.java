package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import sound.Sound;
import svg.LevelLoader;

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
	private static Shader bgShader = new Shader("assets/shaders/basic.vert", "assets/shaders/background.frag");
	private static float bgColor = .1f;
	private static float bgVel = 0f;

	private int w, h;
	
	private Vec2 respawn;

	private Vec2 playerPos, playerVel;
	private float playerSize;
	private float playerGrowthSpeed = 0;
	private float playerShrinkSpeed = 0;
	private float originalPlayerSize;
	private float goalPlayerSize;
	private Player playerSprite;

	private Level curLvl;

	private boolean onPlatform = false;
	private int jumpTimeout = 1;

	private int counter = 1;
	
	private boolean playerDead = false;
	private int playerDeadWait = 0;
	private static final int DEAD_WAIT = 50;

	// If the player is saying something, it will be said for this many more frames.
	private int sayCounter;
	private int sayWait = 0;
	private String saying;
	
	private Queue<SayBubble> toSay = new LinkedList<SayBubble>();

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
		respawn = playerPos;
		playerVel = new Vec2(0, 0);
		playerSize = curLvl.getPlayerInitialDimensions().y;
		goalPlayerSize = playerSize;
	}

	public void step() {
		
		if (playerDead) {
			playerDeadWait--;
			if (playerDeadWait == 0) {
				playerPos = respawn;
				playerDead = false;
				int r = (int) (Math.random() * 10);
				switch (r) {
				case 0:
					saying = "Ouch!";
					break;
				case 1:
					saying = "Ow!";
					break;
				case 2:
					saying = "I endure.";
					break;
				case 3:
					saying = "Continue? Yes.";
					break;
				case 4:
					saying = "Suffering.";
					break;
				case 5:
					saying = "What is death, anyways?";
					break;
				case 6:
					saying = "No respite.";
					break;
				case 7:
					saying = "Is there no end?";
					break;
				case 8:
					saying = "Is this the afterlife?";
					break;
				case 9:
					saying = "What happened?";
					break;
				default:
					saying = "Try again.";
				}
				sayCounter = 100;
			}
		} else {
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
	
			// Always fall if you're going down.
			if (playerVel.y < -0.01 * playerSize) {
				if (playerSprite.getAnimation() != Player.Animation.FALLING) {
					playerSprite.setAnimation(Player.Animation.FALLING);
				}
			}
			// Always jump if you're going up.
			else if (playerVel.y > 0.01 * playerSize) {
				if (playerSprite.getAnimation() != Player.Animation.JUMPING) {
					playerSprite.setAnimation(Player.Animation.JUMPING);
				}
			}
			// Land if you just stopped falling.
			else if(playerSprite.getAnimation() == Player.Animation.FALLING) {
				playerSprite.setAnimation(Player.Animation.LANDING);
			}
			// Walk if you are landing and trying to move.
			else if (playerSprite.getAnimation() == Player.Animation.LANDING && (Keyboard.isKeyDown(Keyboard.KEY_LEFT) ^ Keyboard.isKeyDown(Keyboard.KEY_RIGHT))) {
				playerSprite.setAnimation(Player.Animation.WALKING);
			}
			
			playerSprite.step();

			if (playerSprite.getAnimation() == Player.Animation.WALKING && !(Keyboard.isKeyDown(Keyboard.KEY_LEFT) ^ Keyboard.isKeyDown(Keyboard.KEY_RIGHT))) {
				playerSprite.standStill();
			}
		}

		// Growing/Shrinking
		if (Math.abs(playerSize - goalPlayerSize) <= playerGrowthSpeed / 2) {
			System.out.println("Grow stop");
			playerSize = goalPlayerSize;
			playerGrowthSpeed = 0;
			Sound.stop(Sound.WHOOSH);
		} else if (playerSize < goalPlayerSize) {
			if (playerSize > (originalPlayerSize + goalPlayerSize) / 2 && playerGrowthSpeed > GROWTH_RATE)
				playerGrowthSpeed -= GROWTH_RATE;
			else
				playerGrowthSpeed += GROWTH_RATE;
			playerSize += playerGrowthSpeed;
			Sound.setVolume(Sound.WHOOSH, playerGrowthSpeed);
		}
		
		if (Math.abs(playerSize - goalPlayerSize) <= playerShrinkSpeed / 2) {
			System.out.println("Shrink stop");
			playerSize = goalPlayerSize;
			playerShrinkSpeed = 0;
			Sound.stop(Sound.WHOOSH);
		} else if (playerSize > goalPlayerSize) {
			if (playerSize < (originalPlayerSize + goalPlayerSize) / 2 && playerShrinkSpeed < -GROWTH_RATE)
				playerShrinkSpeed += GROWTH_RATE;
			else
				playerShrinkSpeed -= GROWTH_RATE;
			playerSize += playerShrinkSpeed;
			Sound.setVolume(Sound.WHOOSH, -playerShrinkSpeed);
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
				respawn = c.getPos();
				if (c.isAnti()) {
					Sound.play(Sound.ANTI_COIN);
					goalPlayerSize = c.getValue();
					originalPlayerSize = playerSize;
					playerShrinkSpeed = -GROWTH_RATE;
					playerGrowthSpeed = 0;
				}else {
					Sound.play(Sound.COIN);
					goalPlayerSize += c.getValue() * GROWTH_SCALE;
					originalPlayerSize = playerSize;
					playerGrowthSpeed = GROWTH_RATE;
					playerShrinkSpeed = 0;
				}
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
				toSay.add(b);
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
		
		ArrayList<Spike> spikes = curLvl.getSpikes();
		
		
		for (int i = 0; i < spikes.size(); i++) {
			Spike s = spikes.get(i);
			if ((playerPos.x + playerSize) > s.getPos().x 		&&
					playerPos.x < (s.getPos().x + s.getDim().x) 	&& 
					(playerPos.y + playerSize) > s.getPos().y 		&&
					playerPos.y < (s.getPos().y + s.getDim().y)) {
				s.bloody();
				playerDead = true;
			}
		}
		
		if (playerDead && playerDeadWait == 0) {
			playerDeadWait = DEAD_WAIT;
			playerDead = true;
		}
		
		// Keep streaming audio working.
		Sound.poll();
	}

	public void draw() {
		bgShader.enable();
		bgShader.Uniform1f("time", counter++);
		bgShader.Uniform1f("bgColor", bgColor);
		if (bgColor < 0)
			bgVel += .0003;
		else if (bgColor > .15)
			bgVel -= .0003;
		else bgVel += (Math.random() - .5) * .0005;
		bgVel *= .95;
		bgColor += bgVel;
		counter = (int) (1000 * Math.random());
		background.draw();
		bgShader.disable();

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
		if (sayWait > 0)
			sayWait--;
		else if (sayCounter > 0) {
			new TextBubble(playerPos.add(new Vec2(playerSize * scale, playerSize * scale)),
					saying).draw();
			sayCounter--;
		}
		if (!toSay.isEmpty()){
			SayBubble minDelayB = null;
			int minDelay = 100000;
			for (int i = 0; i < toSay.size(); i++) {
				SayBubble b = toSay.peek();
				if (b.getDelay() < minDelay) {
					minDelayB = b;
					minDelay = b.getDelay();
				}
				if (b.getDelay() == 0)
					break;
				toSay.add(toSay.remove());
				
			}
			if ((!(minDelayB.getDelay() > 0)) || sayCounter == 0) {
				toSay.remove();
				saying = minDelayB.getMessage();
				sayCounter = minDelayB.getDur();
				sayWait = minDelayB.getDelay();
			} else {
				toSay.add(toSay.remove());
			}
		}
		GL11.glPopMatrix();
	}
}
