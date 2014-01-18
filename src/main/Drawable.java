package main;

public interface Drawable {
	/**
	 * Draw with GL11 -- run only in main thread!
	 * 
	 * Passes in the amount the camera has moved from the origin, so that all
	 * objects can be moved accordingly.;
	 */
	public void draw();
}
