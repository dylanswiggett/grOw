package main;

public class Platform implements Drawable {

	private Vec2 pos, dim;
	
	public static Vec2 cameraPos = new Vec2(0, 0);
	
	private static final ColorRect rect =
			new ColorRect(new Vec2(0, 0), new Vec2(0, 0), 0, 1, 0);
	
	public static final Shader shader =
			new Shader("assets/shaders/basic.vert", "assets/shaders/basic.frag");
	
	public Platform(Vec2 pos, Vec2 dim) {
		this.pos = pos;
		this.dim = dim;
		System.out.println(dim);
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public Vec2 getDim() {
		return dim;
	}
	
	public void draw() {
		shader.enable();
		shader.Uniform2f("cameraPos", cameraPos);
		rect.setDrawPos(pos);
		rect.setDrawDim(dim);
		rect.draw();
		shader.disable();
	}
}
