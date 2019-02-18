package tetris.code;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class ContentLoader {

	protected static void load_images() {
		// image files URLs
		URL url_tetris_welcome = Tetris.class.getResource("../images/tetris_welcome.png"); // gets the folder/file from
																							// runnable jar file
																							// location
		URL url_tetris_intro = Tetris.class.getResource("../images/tetris_intro.gif");
		URL url_tetris_bg = Tetris.class.getResource("../images/tetris_bg.png");
		URL url_tetris_instructions = Tetris.class.getResource("../images/tetris_instructions.png");

		// set image variables with content
		Tetris.tetrisVariables.set_view_welcome_image(new ImageIcon(url_tetris_welcome).getImage());
		Tetris.tetrisVariables.set_view_intro_image(new ImageIcon(url_tetris_intro).getImage());
		Tetris.tetrisVariables.set_view_bg_image(new ImageIcon(url_tetris_bg).getImage());
		Tetris.tetrisVariables.set_view_instructions_image(new ImageIcon(url_tetris_instructions).getImage());
	}

	protected static void load_sounds() {
		// sound files URLs
		URL url_tetris_audio_explosion = Tetris.class.getResource("../sounds/explosion.wav");
		URL url_tetris_audio_move = Tetris.class.getResource("../sounds/move.wav");
		URL url_tetris_audio_rotate = Tetris.class.getResource("../sounds/rotate.wav");
		URL url_tetris_audio_loading = Tetris.class.getResource("../sounds/loading.wav");
		URL url_tetris_audio_drop = Tetris.class.getResource("../sounds/drop.wav");
		URL url_tetris_audio_theme_music = Tetris.class.getResource("../sounds/theme_music.wav");

		// create then set audio inputs with content
		AudioInputStream audio_explosion = null;
		AudioInputStream audio_move = null;
		AudioInputStream audio_rotate = null;
		AudioInputStream audio_drop = null;
		AudioInputStream audio_loading = null;
		AudioInputStream audio_theme_music = null;
		try {
			audio_explosion = AudioSystem.getAudioInputStream(url_tetris_audio_explosion);
			audio_move = AudioSystem.getAudioInputStream(url_tetris_audio_move);
			audio_rotate = AudioSystem.getAudioInputStream(url_tetris_audio_rotate);
			audio_loading = AudioSystem.getAudioInputStream(url_tetris_audio_loading);
			audio_drop = AudioSystem.getAudioInputStream(url_tetris_audio_drop);
			audio_theme_music = AudioSystem.getAudioInputStream(url_tetris_audio_theme_music);
		} catch (UnsupportedAudioFileException e) {
			System.err.println("Audio files are not readable");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Audio files couldn't loaded");
			e.printStackTrace();
		}

		// set audio variables
		Tetris.tetrisVariables.set_audio_explosion(audio_explosion);
		Tetris.tetrisVariables.set_audio_move(audio_move);
		Tetris.tetrisVariables.set_audio_rotate(audio_rotate);
		Tetris.tetrisVariables.set_audio_loading(audio_loading);
		Tetris.tetrisVariables.set_audio_drop(audio_drop);
		Tetris.tetrisVariables.set_audio_theme_music(audio_theme_music);

		try {
			Tetris.tetrisVariables.set_clip_explosion(AudioSystem.getClip());
			Tetris.tetrisVariables.set_clip_move(AudioSystem.getClip());
			Tetris.tetrisVariables.set_clip_rotate(AudioSystem.getClip());
			Tetris.tetrisVariables.set_clip_loading(AudioSystem.getClip());
			Tetris.tetrisVariables.set_clip_drop(AudioSystem.getClip());
			Tetris.tetrisVariables.set_clip_theme_music(AudioSystem.getClip());
		} catch (LineUnavailableException e1) {
			System.err.println("ERROR : while setting the clips");
			e1.printStackTrace();
		}

		try {
			if (!Tetris.tetrisVariables.get_clip_explosion().isOpen())
				Tetris.tetrisVariables.get_clip_explosion().open(Tetris.tetrisVariables.get_audio_explosion());
			if (!Tetris.tetrisVariables.get_clip_move().isOpen())
				Tetris.tetrisVariables.get_clip_move().open(Tetris.tetrisVariables.get_audio_move());
			if (!Tetris.tetrisVariables.get_clip_rotate().isOpen())
				Tetris.tetrisVariables.get_clip_rotate().open(Tetris.tetrisVariables.get_audio_rotate());
			if (!Tetris.tetrisVariables.get_clip_loading().isOpen())
				Tetris.tetrisVariables.get_clip_loading().open(Tetris.tetrisVariables.get_audio_loading());
			if (!Tetris.tetrisVariables.get_clip_drop().isOpen())
				Tetris.tetrisVariables.get_clip_drop().open(Tetris.tetrisVariables.get_audio_drop());
			if (!Tetris.tetrisVariables.get_clip_theme_music().isOpen())
				Tetris.tetrisVariables.get_clip_theme_music().open(Tetris.tetrisVariables.get_audio_theme_music());
		} catch (LineUnavailableException | IOException e) {
			System.err.println("Rotate audio clip couldn't opened.");
			e.printStackTrace();
		}
	}
}
