package main;

public class Platform implements Drawable {

	private Vec2 pos, dim;
	
	private static final ColorRect rect =
			new ColorRect(new Vec2(0, 0), new Vec2(0, 0), 0, 1, 0);
	
	public Platform(Vec2 pos, Vec2 dim) {
		this.pos = pos;
		this.dim = dim;
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
