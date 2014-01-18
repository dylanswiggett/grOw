package main;

public class Game {
	private int w, h;
	
	private Vec2 playerPos;
	private float playerSize;
	private ColorRect playerSprite;
	
	private Level curLvl;
	
	public Game(int w, int h) {
		this.w = w;
		this.h = h;
		
		/*
		 * PRETTY MUCH EVERYTHING AFTER HERE SHOULD BE REPLACED WITH LEVEL LOADING CODE.
		 */
		
		playerPos = new Vec2(0, 0);
		playerSize = 20;
		playerSprite = new ColorRect(playerPos, new Vec2(playerSize, playerSize), 1, 0, 0);
		
		curLvl = new Level();
		curLvl.addPlatform(new Platform(new Vec2(30, 30), new Vec2(100, 10)));
	}
	
	public void step() {
		
	}
	
	public void draw() {
		Vec2 cameraOffset = new Vec2(playerSize / 2 - w / 2, playerSize / 2 - h / 2);
		
		playerSprite.setDrawPos(playerPos);
		playerSprite.setDrawDim(new Vec2(playerSize, playerSize));
		playerSprite.draw(cameraOffset);
		
		curLvl.draw(cameraOffset);
	}
}
