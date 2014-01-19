package main;

public class Coin implements Drawable{
	private static final ColorRect rect =
			new ColorRect(new Vec2(0, 0), new Vec2(0, 0), 1, 1, 0);
	
	private float value;
	private Vec2 pos;
	private Vec2 dim;
	
	public Coin(Vec2 pos, float value) {
		this.pos = pos.subtract(new Vec2(value / 2, value / 2));
		dim = new Vec2(value, value);
		this.value = value;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public Vec2 getDim() {
		return dim;
	}
	
	public void draw() {
		rect.setDrawPos(pos);
		rect.setDrawDim(dim);
		rect.draw();
	}
}
