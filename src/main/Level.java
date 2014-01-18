package main;

import java.util.ArrayList;

public class Level implements Drawable{

	private ArrayList<Drawable> drawables;
	
	private ArrayList<Platform> platforms;
	
	public Level() {
		drawables = new ArrayList<Drawable>();
		platforms = new ArrayList<Platform>();
	}
	
	public void addPlatform(Platform p) {
		platforms.add(p);
		drawables.add(p);
	}
	
	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}
	
	@Override
	public void draw() {
		for (Drawable d : drawables) {
			d.draw();
		}
	}

}
