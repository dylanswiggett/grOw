package main;

import java.awt.Font;

import org.newdawn.slick.Color;

import text.Fonts;

public class TextBubble implements Drawable{
	
	private static final Font FONT = Fonts.MONOFONTO;
	private static final float WIDTH = 0.5f;
	private static final int TEXT_DIM = 15;
	private static final int BUBBLE_DIM = 48;
	private static final int FROM_SIDES = 15;
	private static final int FROM_BOTTOM = 39;

	private static final TexturedRect LEFT = 
			new TexturedRect(new Vec2(0, 0), new Vec2(BUBBLE_DIM, BUBBLE_DIM),
			"assets/textures/textBubbleLeft.png", 1, 1, 1);
	private static final TexturedRect RIGHT = 
			new TexturedRect(new Vec2(0, 0), new Vec2(BUBBLE_DIM, BUBBLE_DIM),
			"assets/textures/textBubbleRight.png", 1, 1, 1);
	private static final TexturedRect CENTER = 
			new TexturedRect(new Vec2(0, 0), new Vec2(0, BUBBLE_DIM),
			"assets/textures/textBubbleCenter.png", 1, 1, 1);
	
	private Vec2 pos;
	private String text;
	private float width;
	private float disp;
	
	public TextBubble(Vec2 pos, String text) {
		this.pos = pos;
		this.text = text;
		width = WIDTH * TEXT_DIM * text.length();
		disp = Math.max(width + FROM_SIDES * 2 - BUBBLE_DIM, FROM_SIDES);
		
//		System.out.println(text);
	}
	
	@Override
	public void draw() {
		LEFT.setPos(pos);
		RIGHT.setPos(pos.add(new Vec2(disp, 0)));
		CENTER.setPos(pos.add(new Vec2(BUBBLE_DIM, 0)));
		CENTER.setDim(new Vec2(Math.max(0, disp - BUBBLE_DIM), BUBBLE_DIM));
		CENTER.draw();
		RIGHT.draw();
		LEFT.draw();
		Fonts.draw(FONT, TEXT_DIM, new Vec2(FROM_SIDES + pos.x, - FROM_BOTTOM - pos.y),
				text, Color.black, 4);
	}

}
