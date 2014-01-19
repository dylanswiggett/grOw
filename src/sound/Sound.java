package sound;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sound {

	public static Audio JUMP;
	
	static {
		try {
			JUMP = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("jump.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
