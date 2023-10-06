import javax.sound.sampled.AudioInputStream;		// for playing sound clips
import javax.sound.sampled.*;
import java.io.*;

import java.util.HashMap;				// for storing sound clips


public class SoundManager {				// a Singleton class
	HashMap<String, Clip> clips;

	private static SoundManager instance = null;	// keeps track of Singleton instance

	private SoundManager () {
		clips = new HashMap<>();

		//Level 1 Music
		Clip clip = loadClip("sounds/level1.wav");
		clips.put("level1", clip);

		//Level 2 Music
		clip = loadClip("sounds/level2.wav");
		clips.put("level2", clip);

		//Menu Music
		clip = loadClip("sounds/menu.wav");
		clips.put("menu", clip);

		//Win Music
		clip = loadClip("sounds/win.wav");
		clips.put("win", clip);

		//Lose Music
		clip = loadClip("sounds/lose.wav");
		clips.put("lose", clip);

		//Attack Sound
		clip = loadClip("sounds/attack.wav");
		clips.put("attack", clip);

		//Jump Sound
		clip = loadClip("sounds/jump.wav");
		clips.put("jump", clip);

		//Kill Enemy Sound
		clip = loadClip("sounds/kill.wav");
		clips.put("kill", clip);

		//Detected by Enemy Sound
		clip = loadClip("sounds/detected.wav");
		clips.put("detected", clip);

		//Hit Enemy Sound
		clip = loadClip("sounds/hit.wav");
		clips.put("hit", clip);

		//Walk Sound
		clip = loadClip("sounds/walk.wav");
		clips.put("walk", clip);

		//Enemy Attacks sound
		clip = loadClip("sounds/enemyAttack.wav");
		clips.put("enemyAttack", clip);

		//Respawn Sound
		clip = loadClip("sounds/respawn.wav");
		clips.put("respawn", clip);

		//Achievement Unlocked Sound
		clip = loadClip("sounds/achievement.wav");
		clips.put("achievement", clip);
	}


	public static SoundManager getInstance() {	// class method to get Singleton instance
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		


	public Clip getClip (String title) {

		return clips.get(title);		// gets a sound by supplying key
	}


    	public Clip loadClip (String fileName) {	// gets clip from the specified file
 		AudioInputStream audioIn;
		Clip clip = null;

		try {
    			File file = new File(fileName);
    			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
    			clip = AudioSystem.getClip();
    			clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("Error opening sound files: " + e);
		}
    		return clip;
    	}


    	public void playSound(String title, Boolean looping) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.setFramePosition(0);
			if (looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();
		}
    	}


    	public void stopSound(String title) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.stop();
		}
    	}

		public void stopAllSounds(){
			clips.forEach((str, clp) -> this.stopSound(str));
		}

}