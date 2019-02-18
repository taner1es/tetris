package tetris.code;
/*
 *  @Author : Taner Esmeroglu
 */

import java.awt.BorderLayout;
import java.util.Calendar;
import javax.swing.JFrame;

class Tetris {
	static Tetris myTetris = new Tetris();
	static GenericVariables tetrisVariables = new GenericVariables();
	static GameActions tetrisComponents = new GameActions();
	static KeyInput tetrisKeyInputs = new KeyInput();

	public static void main(String... args) {
		myTetris.go();
	}

	private void go() {
		tetrisVariables.set_game_startedTimeStamp(Calendar.getInstance());

		ContentLoader.load_sounds();
		ContentLoader.load_images();

		// this block to configure window settings
		tetrisVariables.set_frame(new JFrame("Tetris"));
		tetrisVariables.get_frame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tetrisVariables.get_frame().addKeyListener(tetrisKeyInputs);
		tetrisVariables.set_drawPanel(new DrawPanel());

		tetrisVariables.get_frame().getContentPane().add(BorderLayout.CENTER, tetrisVariables.get_drawPanel());

		tetrisVariables.get_frame().setResizable(false);
		tetrisVariables.get_frame().setUndecorated(true);
		tetrisVariables.get_frame().setSize(900, 800);
		tetrisVariables.get_frame().setLocation(400, 0);
		tetrisVariables.get_frame().setVisible(true);
		moveIt();
	}

	private void moveIt() {
		// start a game with generating a new shape
		if (tetrisVariables.get_game_state() == "running" || tetrisVariables.get_game_state() == "loading") {
			tetrisComponents.swap_buffered_shape_to_game();
		}

		// endless loop for game running.
		while (true) {
			System.out.println(tetrisVariables.get_game_state());
			tetrisComponents.check_audios();
			if (tetrisVariables.get_game_state() == "exit") {
				System.exit(0);
				break;
			}
			// restarting
			if (tetrisVariables.get_restartGame()) {
				tetrisComponents = new GameActions();
				tetrisVariables.reset();
				tetrisVariables.set_game_startedTimeStamp(Calendar.getInstance());
				moveIt();
			} else {/**
					 * Game States : welcome, running, paused , end, exit , loading , highscore ,
					 * instructions
					 */
				switch (tetrisVariables.get_game_state()) {
				case "welcome":
					tetrisComponents.pause_menu_select_func();
					break;
				case "running":
					tetrisComponents.run_gameLoop();
					break;
				case "paused":
					tetrisComponents.pause_menu_select_func();
					break;
				case "end":
					tetrisComponents.pause_menu_select_func();
					break;
				case "loading":
					if (tetrisVariables.get_clip_loading().isOpen()
							&& !tetrisVariables.get_clip_loading().isRunning()) {
						tetrisVariables.get_clip_loading().start();
					}
					break;
				case "highscore":
					break;
				case "instructions":
					if (tetrisVariables.get_esc()) {
						tetrisVariables.set_game_state("welcome");
						tetrisVariables.set_esc(false);
					}
					break;
				default:
					System.out.println("no game state found");
					break;
				}

				tetrisComponents.sleep(tetrisVariables.get_sleep_time());
				tetrisVariables.get_frame().repaint();
			}
		}
	}
}