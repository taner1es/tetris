package tetris.code;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		switch (Tetris.tetrisVariables.get_game_state()) {
		case "instructions":
			if (!Tetris.tetrisVariables.get_esc()) {
				if (key == KeyEvent.VK_ESCAPE) {
					Tetris.tetrisVariables.set_esc(true);
				}
			}
			break;
		case "highscore":
			if (!Tetris.tetrisVariables.get_up()) {
				if (key == KeyEvent.VK_UP) {
					Tetris.tetrisVariables.set_up(true);
				}
			}
			if (!Tetris.tetrisVariables.get_down()) {
				if (key == KeyEvent.VK_DOWN) {
					Tetris.tetrisVariables.set_down(true);
				}
			}
			if (!Tetris.tetrisVariables.get_enter()) {
				if (key == KeyEvent.VK_ENTER) {
					Tetris.tetrisVariables.set_enter(true);
				}
			}
			if (!Tetris.tetrisVariables.get_esc()) {
				if (key == KeyEvent.VK_ESCAPE) {
					Tetris.tetrisVariables.set_esc(true);
				}
			}
			break;
		case "loading":

			break;
		case "welcome":
			Tetris.tetrisComponents.control_pause_menu_3_selection(key);
			break;
		case "paused":
			Tetris.tetrisComponents.control_pause_menu_3_selection(key);
			break;
		case "end":
			Tetris.tetrisComponents.control_pause_menu_3_selection(key);
			break;
		case "running":
			// pause game
			if (key == KeyEvent.VK_P) {
				Tetris.tetrisVariables.set_pause(true);
				Tetris.tetrisVariables.set_game_state("paused");
			}
			// rotate shape
			if (key == KeyEvent.VK_UP && Tetris.tetrisVariables.get_rotate_available()) {
				Tetris.tetrisComponents.rotate_shape(Tetris.tetrisVariables.get_all_shapes());
				Tetris.tetrisVariables.set_rotate_available(false);
			}
			if (key == KeyEvent.VK_DOWN) {
				Tetris.tetrisVariables.set_down(true);
			}
			// move shape
			if (key == KeyEvent.VK_RIGHT) {
				Tetris.tetrisVariables.set_left(false);
				Tetris.tetrisVariables.set_right(true);
			} else if (key == KeyEvent.VK_LEFT) {
				Tetris.tetrisVariables.set_right(false);
				Tetris.tetrisVariables.set_left(true);
			}
			if (key == KeyEvent.VK_SPACE) {
				Tetris.tetrisVariables.set_directDown(true);
			}
			break;
		default:
			System.err.println("!! ERROR : GAME STATE @KeyInput.KeyPressed");
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		switch (Tetris.tetrisVariables.get_game_state()) {
		case "instructions":
			if (Tetris.tetrisVariables.get_esc()) {
				if (key == KeyEvent.VK_ESCAPE) {
					Tetris.tetrisVariables.set_esc(false);
				}
			}
			break;
		case "highscore":
			if (Tetris.tetrisVariables.get_up()) {
				if (key == KeyEvent.VK_UP) {
					Tetris.tetrisVariables.set_up(false);
				}
			}
			if (Tetris.tetrisVariables.get_down()) {
				if (key == KeyEvent.VK_DOWN) {
					Tetris.tetrisVariables.set_down(false);
				}
			}
			if (Tetris.tetrisVariables.get_enter()) {
				if (key == KeyEvent.VK_ENTER) {
					Tetris.tetrisVariables.set_enter(false);
				}
			}
			if (Tetris.tetrisVariables.get_esc()) {
				if (key == KeyEvent.VK_ESCAPE) {
					Tetris.tetrisVariables.set_esc(false);
				}
			}
			break;
		case "loading":

			break;
		case "welcome":

			break;
		case "paused":

			break;
		case "end":

			break;
		case "running":
			if (key == KeyEvent.VK_RIGHT && Tetris.tetrisVariables.get_right()) {
				Tetris.tetrisVariables.set_frameCounter_right(0);
				Tetris.tetrisVariables.set_right(false);
			} else if (key == KeyEvent.VK_LEFT && Tetris.tetrisVariables.get_left()) {
				Tetris.tetrisVariables.set_frameCounter_left(0);
				Tetris.tetrisVariables.set_left(false);
			}
			if (key == KeyEvent.VK_DOWN) {
				Tetris.tetrisVariables.set_down(false);
				Tetris.tetrisVariables.set_speed_down(Tetris.tetrisVariables.get_speed_game());
			}
			if (key == KeyEvent.VK_SPACE) {
				Tetris.tetrisVariables.set_directDown(false);
			}
			if (key == KeyEvent.VK_UP && !Tetris.tetrisVariables.get_rotate_available()) {
				Tetris.tetrisVariables.set_rotate_available(true);
			}
			break;
		default:
			System.err.println("!! ERROR : GAME STATE @KeyInput.KeyReleased");
			break;
		}
	}
}