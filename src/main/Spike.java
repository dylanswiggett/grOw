package main;

public class Spike extends Platform {

	public static TexturedRect texture =
			new TexturedRect("assets/textures/spike.png", false, true);
	
	public Spike(Vec2 pos, Vec2 dim) {
		super(pos, dim);
	}

	@Override
	public void draw() {
		texture.setPos(getPos());
		texture.setDim(getDim());
		texture.draw();
	}
	
}
