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
	
	@Override
	public void draw(Vec2 cameraOffset) {
		for (Drawable d : drawables) {
			d.draw(cameraOffset);
		}
	}

}
