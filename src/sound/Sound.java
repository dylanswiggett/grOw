package sound;

import java.io.BufferedInputStream;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.WaveData;

public class Sound {

	/** Position of the source sound. */
	private static final FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Velocity of the source sound. */
	private static final FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Position of the listener. */
	private static final FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Velocity of the listener. */
	private static final FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	private static final FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
	
	public static int JUMP;
	public static int BUMP;
	public static int LAND;
	public static int WHOOSH;
	
	public static void init() {
		try{
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		AL10.alGetError();
		
		AL10.alListener(AL10.AL_POSITION,    listenerPos);
		AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
		
		
		JUMP = loadSource("assets/sound/Jump.wav", 1, false)[0];
		BUMP = loadSource("assets/sound/Bump.wav", 1, false)[0];
		LAND = loadSource("assets/sound/Land.wav", 1, false)[0];
		WHOOSH = loadSource("assets/sound/Wush.wav", 1, false)[0];
	}
	
	public static int[] loadSource(String filepath, int maxSources, boolean looping) {
		// Load .wav data into a buffer.
		int buffer = AL10.alGenBuffers();

		int error = AL10.alGetError();
		if(error != AL10.AL_NO_ERROR) {
			System.err.println("Encountered AL error code " + error + "!");
			System.exit(-1);
		}

		//Load .wav file
		java.io.FileInputStream fin = null;
		try {
			fin = new java.io.FileInputStream(filepath);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		WaveData waveFile = WaveData.create(new BufferedInputStream(fin));
		try {
			fin.close();
		} catch (java.io.IOException ex) {}
		
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();

		int[] sources = new int[maxSources];
		
		for (int i = 0; i < maxSources; i++) {
		
		// Bind the buffer with the source.
		sources[i] = AL10.alGenSources();

		error = AL10.alGetError();
		if(error != AL10.AL_NO_ERROR) {
			System.err.println("Encountered AL error code " + error + "!");
			System.exit(-1);
		}

		AL10.alSourcei(sources[i], AL10.AL_BUFFER,   buffer   );
		AL10.alSourcef(sources[i], AL10.AL_PITCH,    1.0f     );
		AL10.alSourcef(sources[i], AL10.AL_GAIN,     1.0f     );
		AL10.alSource (sources[i], AL10.AL_POSITION, sourcePos);
		AL10.alSource (sources[i], AL10.AL_VELOCITY, sourceVel);
		if (looping)
			AL10.alSourcei(sources[i], AL10.AL_LOOPING,  AL10.AL_TRUE );

		}
		
		// Do another error check and return.
		error = AL10.alGetError();
		if(error != AL10.AL_NO_ERROR) {
			System.err.println("Encountered AL error code " + error + "!");
			System.exit(-1);
		}

		return sources;
	}
	
	public static void play(int sound) {
		AL10.alSourcePlay(sound);
	}
	
	public static void stop(int sound) {
		AL10.alSourceStop(sound);
	}
	
	public static void setVolume(int sound, float gain) {
		AL10.alSourcef(sound, AL10.AL_GAIN, gain);
	}
	
}