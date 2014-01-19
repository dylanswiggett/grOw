package svg;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import main.Coin;
import main.Level;
import main.Platform;
import main.SayBubble;
import main.Spike;
import main.Text;
import main.Vec2;

public class LevelLoader {

	public static Level load(String filename) throws IOException {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(filename));

			doc.getDocumentElement().normalize();

			Level level = new Level();

			NodeList listOfRectangles = doc.getElementsByTagName("rect");
			// NodeList is not iterable, so we have to loop manually.
			for (int i = 0; i < listOfRectangles.getLength(); i++) {
				Element rectangle = (Element) listOfRectangles.item(i);

				float width = Float.parseFloat(rectangle.getAttribute("width"));
				float height = Float.parseFloat(rectangle.getAttribute("height"));
				Vec2 dim = new Vec2(width, height);
				float x = Float.parseFloat(rectangle.getAttribute("x"));
				float y = Float.parseFloat(rectangle.getAttribute("y"));
				// Adjust coordinates to "flip" the level.
				y = -1f * y - height;
				Vec2 pos = new Vec2(x, y);

				String style = rectangle.getAttribute("style");
				if (style.equals("fill:#ff00ff")) {
					level.setPlayerInitialPosition(pos);
					level.setPlayerInitialDimensions(dim);
					System.out.println("Player:");
					System.out.println("\twidth: " + width);
					System.out.println("\theight: " + height);
					System.out.println("\tx: " + x);
					System.out.println("\ty: " + y);
				} else if (style.equals("fill:#ffff00")) {
					level.addCoin(new Coin(pos, dim.y, false));
					System.out.println("Coin:");
					System.out.println("\twidth: " + width);
					System.out.println("\theight: " + height);
					System.out.println("\tx: " + x);
					System.out.println("\ty: " + y);
				} else if (style.equals("fill:#0000ff")) {
					level.addCoin(new Coin(pos, dim.y, true));
					System.out.println("Anti Coin:");
					System.out.println("\twidth: " + width);
					System.out.println("\theight: " + height);
					System.out.println("\tx: " + x);
					System.out.println("\ty: " + y);
				} else if (style.equals("fill:#00ff00")) {
					level.addSpike(new Spike(pos, dim));
					System.out.println("Spike:");
					System.out.println("\twidth: " + width);
					System.out.println("\theight: " + height);
					System.out.println("\tx: " + x);
					System.out.println("\ty: " + y);
				} else {
					level.addPlatform(new Platform(pos, dim));
					System.out.println("Platform:");
					System.out.println("\twidth: " + width);
					System.out.println("\theight: " + height);
					System.out.println("\tx: " + x);
					System.out.println("\ty: " + y);
				}
			}
			
			NodeList listOfTexts = doc.getElementsByTagName("text");
			for (int i = 0; i < listOfTexts.getLength(); i++) {
				Element text = (Element) listOfTexts.item(i);
				
				String string = text.getElementsByTagName("tspan").item(0).getTextContent();
//				String style = text.getAttribute("style").split(";")[0];
//				// 10 is the length of "font-size:" and 2 is the length of "px"
//				System.out.println(style);
//				style = style.substring(10, style.length() - 2);
//				float size = Float.parseFloat(style);
				float x = Float.parseFloat(text.getAttribute("x"));
				float y = Float.parseFloat(text.getAttribute("y"));
				// Adjust coordinates to "flip" the level.
//				y = -1f * y + size;
				Vec2 pos = new Vec2(x, y);
				
				String[] data = string.split(" \\|\\| ");
				
				if (data[0].compareTo("text") == 0) {
					int px = Integer.parseInt(data[1]);
					level.addText(new Text(data[2], pos, px));
				} else if (data[0].compareTo("say") == 0 || data[0].compareTo("sayP") == 0
						|| data[0].compareTo("sayD") == 0) {
					int duration = Integer.parseInt(data[1]);
					if ( data[0].compareTo("sayD") == 0) {
						int width = Integer.parseInt(data[3]);
						int height = Integer.parseInt(data[4]);
						Vec2 dim = new Vec2(width, height);
						System.out.println(pos);
						pos = new Vec2(pos.x, -pos.y);
						level.addBubble(new SayBubble(pos.subtract(dim.mult(.5f)),
								dim, duration, data[5], false, Integer.parseInt(data[2])));
					}
					int width = Integer.parseInt(data[2]);
					int height = Integer.parseInt(data[3]);
					Vec2 dim = new Vec2(width, height);
					System.out.println(pos);
					pos = new Vec2(pos.x, -pos.y);
					level.addBubble(new SayBubble(pos.subtract(dim.mult(.5f)),
							dim, duration, data[4], data[0].compareTo("sayP") == 0, 0));
				}
				
				System.out.println("Text:");
				System.out.println("\tstring: " + string);
				System.out.println("\tx: " + x);
				System.out.println("\ty: " + y);
			}

			return level;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		// This only happens if there is a parsing error.
		return null;
	}

}
