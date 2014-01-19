package main;

public class SayBubble implements Drawable{
	
	private static final ColorRect rect =
			new ColorRect(new Vec2(0, 0), new Vec2(0, 0), 0, 0, 1);
	
	Vec2 pos, dim;
	int dur;
	String msg;
	boolean isPerm;
	
	public SayBubble(Vec2 pos, Vec2 dim, int duration, String msg, boolean isPerm) {
		this.pos = pos;
		this.dim = dim;
		this.dur = duration;
		this.msg = msg;
		this.isPerm = isPerm;
	}
	
	public void draw() {
		rect.setDrawPos(pos);
		rect.setDrawDim(dim);
		rect.draw();
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public Vec2 getDim() {
		return dim;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public int getDur() {
		return dur;
	}
	
	public boolean isPerm() {
		return isPerm;
	}
}
