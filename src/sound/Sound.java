package sound;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sound {

	public static Audio JUMP;
	public static Audio BUMP;
	public static Audio LAND;
	public static Audio WHOOSH;
	
	public static void init() {
		try {
			JUMP = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sound/Jump.wav"));
			BUMP = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sound/Bump.wav"));
			LAND = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sound/Land.wav"));
			WHOOSH = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sound/Wush.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
