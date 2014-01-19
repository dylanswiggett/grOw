package main;

import java.util.ArrayList;

public class Level implements Drawable{

	private ArrayList<Drawable> drawables;
	
	private ArrayList<Platform> platforms;
	private ArrayList<Coin> coins;
	
	private Vec2 playerInitialPosition;
	private Vec2 playerInitialDimensions;
	
	public Level() {
		drawables = new ArrayList<Drawable>();
		platforms = new ArrayList<Platform>();
		coins = new ArrayList<Coin>();
	}
	
	public void addPlatform(Platform p) {
		platforms.add(p);
		drawables.add(p);
	}
	
	public void addCoin(Coin c) {
		coins.add(c);
		drawables.add(c);
	}
	
	public void removeCoin(Coin c) {
		coins.remove(c);
		drawables.remove(c);
	}
	
	public void addText(Text text) {
		drawables.add(text);
	}
	
	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}
	
	public ArrayList<Coin> getCoins() {
		return coins;
	}
	
	public Vec2 getPlayerInitialPosition() {
		return playerInitialPosition;
	}
	
	public void setPlayerInitialPosition(Vec2 playerInitialPosition) {
		this.playerInitialPosition = playerInitialPosition;
	}
	
	public Vec2 getPlayerInitialDimensions() {
		return playerInitialDimensions;
	}
	
	public void setPlayerInitialDimensions(Vec2 playerInitialDimensions) {
		this.playerInitialDimensions = playerInitialDimensions;
	}
	
	@Override
	public void draw() {
		for (Drawable d : drawables) {
			d.draw();
		}
	}

}
