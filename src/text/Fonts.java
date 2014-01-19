package text;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class Fonts {
	public static final Font ABSENDER = loadFont("assets/fonts/absender1.ttf");
	
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
		TrueTypeFont newFont = new TrueTypeFont(f.deriveFont(size), false);
		sizes.put(size, newFont);
		return newFont;
	}
}
