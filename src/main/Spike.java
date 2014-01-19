package main;

public class Spike extends Platform {

	public static TexturedRect textureClean =
			new TexturedRect("assets/textures/spike.png", false, true);
	public static TexturedRect textureBloody =
			new TexturedRect("assets/textures/spikeDirty.png", false, true);
	
	private boolean bloody;
	
	public Spike(Vec2 pos, Vec2 dim) {
		super(pos, dim);
		bloody = false;
	}
	
	public void bloody() {
		bloody = true;
	}

	@Override
	public void draw() {
		TexturedRect texture = bloody ? textureBloody : textureClean;
		texture.setPos(getPos());
		texture.setDim(getDim());
		texture.drawWrapped();
	}
	
}
