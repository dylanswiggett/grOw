package main;

public class Coin implements Drawable{
	private static final TexturedRect rect =
			new TexturedRect(new Vec2(0, 0), new Vec2(0, 0), "assets/textures/coin.png", 1, 1, 1);
	private static final TexturedRect anti = 
			new TexturedRect(new Vec2(0, 0), new Vec2(0, 0), "assets/textures/antiCoin.png", 1, 1, 1);
	
	private float value;
	private Vec2 pos;
	private Vec2 dim;
	private boolean isAnti;
	
	public Coin(Vec2 pos, float value, boolean anti) {
		this.pos = pos.subtract(new Vec2(value / 2, value / 2));
		dim = new Vec2(value, value);
		this.value = value;
		this.isAnti = anti;
	}
	
	public float getValue() {
		return value;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public boolean isAnti() {
		return isAnti;
	}
	
	public Vec2 getDim() {
		return dim;
	}
	
	public void draw() {
		if (isAnti) {
			anti.setPos(pos);
			anti.setDim(dim);
			anti.draw();
		} else {
			rect.setPos(pos);
			rect.setDim(dim);
			rect.draw();
		}
	}
}
