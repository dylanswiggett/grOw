package main;

public class Vec2 {
	public float x, y;
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(Vec2 v) {
		this(v.x, v.y);
	}
	
	public Vec2(double x, double y) {
		this((float) x, (float) y);
	}
	
	public float dot(Vec2 v) {
		return x * v.x + y * v.y;
	}
	
	public float crossZ(Vec2 v) {
		return x * v.y - y * v.x;
	}
	
	public float norm() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public Vec2 mult(float s) {
		return new Vec2(s * x, s * y);
	}
	
	public Vec2 add(Vec2 v) {
		return new Vec2(x + v.x, y + v.y);
	}
	
	public Vec2 subtract(Vec2 v) {
		return new Vec2(x - v.x, y - v.y);
	}
	
	public Vec2 normalize() {
		return mult(1 / norm());
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
