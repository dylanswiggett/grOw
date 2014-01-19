package text;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import main.Init;
import main.Vec2;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class Fonts {
	public static final Font ABSENDER = loadFont("assets/fonts/absender1.ttf");
	public static final Font MONOFONTO = loadFont("assets/fonts/monofonto.ttf");
	
	private static HashMap<Font, HashMap<Float, TrueTypeFont>> fontSizes =
			new HashMap<>();
	
	private static Font loadFont(String path) {
		InputStream fontStream = ResourceLoader.getResourceAsStream(path);
		
		try {
			return Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static TrueTypeFont getFont(Font f, float size) {
		HashMap<Float, TrueTypeFont> sizes = fontSizes.get(f);
		if (sizes != null) {
			TrueTypeFont font = sizes.get(size);
			if (font != null)
				return font;
		} else {
			sizes = new HashMap<Float, TrueTypeFont>();
			fontSizes.put(f, sizes);
		}
		TrueTypeFont newFont = new TrueTypeFont(f.deriveFont(size), true);
		sizes.put(size, newFont);
		return newFont;
	}
	
	public static void draw(Font font, int size, Vec2 pos, String text, Color color) {
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, -pos.y, 0);
		GL11.glScalef(.5f, -.5f, 0);
		Fonts.getFont(font, size).drawString(0, 0, text, color);
		GL11.glPopMatrix();
	}
}
