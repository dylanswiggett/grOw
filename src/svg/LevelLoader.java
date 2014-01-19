package svg;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import main.Coin;
import main.Level;
import main.Platform;
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
					level.addCoin(new Coin(pos, dim.y));
					System.out.println("Coin:");
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
