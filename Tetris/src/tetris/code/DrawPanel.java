package tetris.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		// game states : welcome, running, paused , end, exit , loading , highscore
		switch (Tetris.tetrisVariables.get_game_state()) {
		case "instructions":
			g.drawImage(Tetris.tetrisVariables.get_view_instructions_image(), 0, 0,
					Tetris.tetrisVariables.get_gw_WIDTH(), Tetris.tetrisVariables.get_gw_HEIGHT(), null);
			break;
		case "welcome":
			if (!Tetris.tetrisVariables.get_started()) {
				draw_WelcomePage(g);
				// draw_highScorePage(g);
			}
			break;
		case "running":
			if (Tetris.tetrisVariables.get_pause())
				Tetris.tetrisVariables.set_game_state("paused");
			else if (Tetris.tetrisVariables.get_endGame())
				Tetris.tetrisVariables.set_game_state("end");
			else {
				draw_Grid(g);
				draw_MainBorders(g);
				draw_Shapes(g);
				draw_Buffer(g);
				draw_Score(g);
			}
			break;
		case "paused":
			pause_gameLoop(g);
			break;
		case "end":
			pause_gameLoop(g);
			break;
		case "exit":

			break;
		case "loading":
			if (!Tetris.tetrisVariables.get_started()) {
				Tetris.tetrisComponents.read_highscore_file();
				Tetris.tetrisVariables.get_frame().setSize(400, 200);
				Tetris.tetrisVariables.get_frame().setLocation(Tetris.tetrisVariables.get_s_WIDTH() / 3 - 50,
						Tetris.tetrisVariables.get_s_HEIGHT() / 3 - 50);
				Calendar introTimeStamp = Calendar.getInstance();
				long millisGameStarted = Tetris.tetrisVariables.get_game_startedTimeStamp().getTimeInMillis();
				long millisIntro = introTimeStamp.getTimeInMillis();
				int timeInterval = (int) ((millisIntro - millisGameStarted) / 1000);
				if (timeInterval < 2) {
					g.drawImage(Tetris.tetrisVariables.get_view_intro_image(), 0, 0, 400, 200, null, null);
				} else {
					Tetris.tetrisVariables.get_frame().setSize(Tetris.tetrisVariables.get_gw_WIDTH(),
							Tetris.tetrisVariables.get_gw_HEIGHT());
					Tetris.tetrisVariables.get_frame().setLocation(Tetris.tetrisVariables.get_s_WIDTH() / 4 - 50, 0);
					Tetris.tetrisVariables.set_game_state("welcome");
				}
			}
			break;
		case "highscore":
			draw_highScorePage(g);
			break;
		}
	}

	private void draw_WelcomePage(Graphics g) {
		g.drawImage(Tetris.tetrisVariables.get_view_welcome_image(), 0, 0, Tetris.tetrisVariables.get_gw_WIDTH(),
				Tetris.tetrisVariables.get_gw_HEIGHT(), Color.WHITE, null);

		int size = 10;
		int pos_x = 250;
		int pos_y = 350;
		Color color_back = new Color(255, 161, 0);
		Color color_front = Color.WHITE;
		Color color_highlighted = new Color(146, 63, 255);
		int interval = 100;
		for (int i = 0; i < 3; i++) {
			if (Tetris.tetrisVariables.get_pause_selection() == i) {
				g.setColor(color_back);
				color_front = color_highlighted;
			} else {
				g.setColor(Color.BLACK);
				color_front = Color.WHITE;
			}

			g.fillRect(pos_x + 10, pos_y + 20 + (interval * i), 290, 50);
			draw_tetroline(g, pos_x, pos_y + (interval * i), 7, true, Color.BLACK, color_front, size);
			draw_tetroline(g, pos_x + 300, pos_y + (interval * i), 7, true, Color.BLACK, color_front, size);
			draw_tetroline(g, pos_x, pos_y + (interval * i), 29, false, Color.BLACK, color_front, size);
			draw_tetroline(g, pos_x, pos_y + 60 + (interval * i), 29, false, Color.BLACK, color_front, size);
		}

		g.setFont(new Font(Tetris.tetrisVariables.get_font_type(), Font.BOLD, 30));
		if (Tetris.tetrisVariables.get_pause_selection() == 0) {
			g.setColor(color_highlighted);
			g.drawString("PLAY", pos_x + 115, pos_y + 55);
			g.setColor(new Color(84, 255, 0));
			g.drawString("INSTRUCTIONS", pos_x + 40, pos_y + 155);
			g.drawString("EXIT", pos_x + 115, pos_y + 255);
		} else if (Tetris.tetrisVariables.get_pause_selection() == 1) {
			g.setColor(color_highlighted);
			g.drawString("INSTRUCTIONS", pos_x + 40, pos_y + 155);
			g.setColor(new Color(84, 255, 0));
			g.drawString("PLAY", pos_x + 115, pos_y + 55);
			g.drawString("EXIT", pos_x + 115, pos_y + 255);
		} else if (Tetris.tetrisVariables.get_pause_selection() == 2) {
			g.setColor(color_highlighted);
			g.drawString("EXIT", pos_x + 115, pos_y + 255);
			g.setColor(new Color(84, 255, 0));
			g.drawString("PLAY", pos_x + 115, pos_y + 55);
			g.drawString("INSTRUCTIONS", pos_x + 40, pos_y + 155);
		}

	}

	private void draw_highScorePage(Graphics g) {
		g.drawImage(Tetris.tetrisVariables.get_view_bg_image(), 0, 0, Tetris.tetrisVariables.get_gw_WIDTH(),
				Tetris.tetrisVariables.get_gw_HEIGHT(), null, null);
		g.setFont(new Font(Tetris.tetrisVariables.get_font_type(), Font.BOLD, 30));
		int pos_x = 250;
		int pos_y = 250;
		int width = 60;
		int height = 50;
		int interval = 80;
		int char_distance = 15;
		String temp = "";
		int score = Tetris.tetrisVariables.get_score();
		Color color_back;
		Color color_front;
		Color highlighted_username_color = new Color(255, 50, 74);
		// title section
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 50, Tetris.tetrisVariables.get_gw_WIDTH() - 1, 170);
		g.setColor(new Color(65, 244, 121));
		g.drawString("Your Score : " + Integer.toString(score), 300, 115);
		// title section tetroborders
		color_back = Color.BLACK;
		color_front = new Color(150, 45, 81);
		draw_tetroline(g, 0, 25, 7, true, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, Tetris.tetrisVariables.get_gw_WIDTH() - 25, 25, 7, true, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, 0, 25, Tetris.tetrisVariables.get_gw_WIDTH() / 25 - 2, false, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, 0, 175, Tetris.tetrisVariables.get_gw_WIDTH() / 25 - 2, false, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());

		color_back = Color.BLACK;
		color_front = new Color(97, 13, 193);

		// background of highscore table
		g.setColor(Color.DARK_GRAY);
		g.fillRect(pos_x - 150, pos_y + 100, 700, 375);
		g.setColor(Color.BLACK);
		g.drawString("{ no }           { name }               { score }", pos_x - 100, pos_y + 135);
		g.fillRect(pos_x - 150, pos_y + 155, 700, 4);

		draw_tetroline(g, pos_x - 175, pos_y + 50, 17, true, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, pos_x - 175 + 700, pos_y + 50, 17, true, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, pos_x - 175, pos_y + 50, 27, false, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, pos_x - 175, pos_y + 450, 27, false, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());

		if (Tetris.tetrisComponents.ch_order < 5) {
			for (int i = 0; i < 5; i++) {
				// draw small tetroborders
				if (i == Tetris.tetrisComponents.ch_order)
					color_front = Color.RED;
				else
					color_front = new Color(97, 13, 193);
				draw_tetroline(g, pos_x - 10 + (i * interval), pos_y - 20, 7, true, color_back, color_front, 10);
				draw_tetroline(g, pos_x + width + (i * interval), pos_y - 20, 7, true, color_back, color_front, 10);
				draw_tetroline(g, pos_x - 10 + (i * interval), pos_y - 20, 6, false, color_back, color_front, 10);
				draw_tetroline(g, pos_x - 10 + (i * interval), pos_y + height - 10, 6, false, color_back, color_front,
						10);

				g.setColor(Color.GREEN);
				g.fill3DRect(pos_x + (interval * i), pos_y, width, height, true);
				g.setColor(Color.BLACK);
				for (int k = 0; k < Tetris.tetrisVariables.get_user_name().length(); k++) {
					temp += Tetris.tetrisVariables.get_user_name().charAt(k);
					g.drawString(temp, pos_x + (interval * k) + char_distance, pos_y + 35);
					temp = "";
				}
			}
			if (Tetris.tetrisVariables.get_up())
				g.setColor(Color.RED);
			else
				g.setColor(Color.ORANGE);
			g.fill3DRect(pos_x + (interval * Tetris.tetrisComponents.ch_order), pos_y, width, height, true);
			g.setColor(Color.BLACK);
			temp += Tetris.tetrisComponents.ch_temp;
			g.drawString(temp, pos_x + (Tetris.tetrisComponents.ch_order * interval) + char_distance, pos_y + 35);

			Tetris.tetrisComponents.high_score_name_writing();
		} else {
			Tetris.tetrisVariables.set_up(false);
			Tetris.tetrisVariables.set_enter(false);

			g.setColor(highlighted_username_color);
			g.drawString(Tetris.tetrisVariables.get_user_name(), 350, 160);

			if (!Tetris.tetrisVariables.get_record_done()) {
				Tetris.tetrisComponents.record_highScore();
				Tetris.tetrisComponents.read_highscore_file();
			}
		}

		if (Tetris.tetrisVariables.get_highscore_file_exists()) {
			HighScore hs = Tetris.tetrisVariables.get_highscore();
			NavigableMap<Integer, String> map = hs.get_highScoreDataMap().descendingMap();
			Set<Entry<Integer, String>> set = map.entrySet();
			Iterator<Entry<Integer, String>> itr = set.iterator();
			int cnt = 0;
			while (itr.hasNext()) {
				if (cnt < 10) {
					Map.Entry<Integer, String> entry = itr.next();
					if (entry.getValue().equals(Tetris.tetrisVariables.get_user_name())) {
						g.setColor(highlighted_username_color);
					} else {
						g.setColor(Color.GRAY);
					}
					g.drawString("{ " + (cnt + 1) + " }", pos_x - 100, pos_y + 190 + (cnt * 30));
					g.drawString(entry.getValue(), pos_x + 90, pos_y + 190 + (cnt * 30));
					g.drawString(Integer.toString(entry.getKey()), pos_x + 370, pos_y + 190 + (cnt * 30));

					cnt++;
				} else {
					break;
				}
			}
		} else {
			System.out.println("get_high_score_file = " + Tetris.tetrisVariables.get_highscore_file_exists());
			System.out.println("get_record_done= " + Tetris.tetrisVariables.get_record_done());
		}

		if (Tetris.tetrisVariables.get_record_done()) {
			g.setColor(Color.DARK_GRAY);

			g.fillRect(pos_x - 20, pos_y - 10, 420, 60);

			draw_tetroline(g, pos_x - 30, pos_y - 20, 7, true, color_back, color_front, 10);
			draw_tetroline(g, pos_x + 400, pos_y - 20, 7, true, color_back, color_front, 10);
			draw_tetroline(g, pos_x - 30, pos_y - 20, 43, false, color_back, color_front, 10);
			draw_tetroline(g, pos_x - 30, pos_y + height - 10, 43, false, color_back, color_front, 10);

			g.setColor(Color.WHITE);
			g.drawString("PRESS \"ESC\" FOR MENU", pos_x, pos_y + 35);

			if (Tetris.tetrisVariables.get_esc()) {
				Tetris.tetrisVariables.set_game_state("end");
				Tetris.tetrisVariables.set_esc(false);
			}
		}
	}

	private void draw_MainBorders(Graphics g) {
		int size = 25;
		Color color_back = Color.BLACK;
		Color color_front = new Color(232, 104, 104);

		g.drawImage(Tetris.tetrisVariables.get_view_bg_image(), 0, 0, Tetris.tetrisVariables.get_gw_WIDTH(),
				Tetris.tetrisVariables.get_gw_HEIGHT(), Color.WHITE, null);

		// paint game area background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(50, 3 * 25, 19 * 25, 25 * 25);

		// verticals main border blocks
		draw_tetroline(g, 1 * size, 2 * size, 26, true, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size()); // left
		draw_tetroline(g, 21 * size, 2 * size, 26, true, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size()); // right
		// horizontal bottom main border
		draw_tetroline(g, size, 27 * size, 20, false, color_back, color_front,
				Tetris.tetrisVariables.get_tetrobox_size());
	}

	int font_size = 0;
	int max_font_size = 40;

	private void draw_Score(Graphics g) {
		Color color_back = Color.black;
		Color color_front = new Color(232, 232, 104);

		g.setFont(new Font(Tetris.tetrisVariables.get_font_type(), Font.BOLD, 30));

		// score frame background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(625, 625, 200, 75);

		// vertical tetrolines
		draw_tetroline(g, 600, 575, 5, true, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, 825, 575, 5, true, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());
		// horizontal tetrolines
		draw_tetroline(g, 600, 575, 8, false, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, 600, 675, 8, false, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());

		// point
		String score = Integer.toString(Tetris.tetrisVariables.get_score());
		g.setColor(Color.white);
		g.drawString(score, 675, 670);

		g.setColor(Color.GREEN);
		g.setFont(new Font(Tetris.tetrisVariables.get_font_type(), Font.BOLD, font_size));
		if (Tetris.tetrisVariables.get_score_add() > 0) {
			Calendar addedScoreTimeStamp = Calendar.getInstance();
			int timeInterval = (int) ((addedScoreTimeStamp.getTimeInMillis()
					- Tetris.tetrisVariables.get_score_addTimeStamp().getTimeInMillis()) / 1000);
			if (timeInterval < 2) {
				g.drawString("+" + Integer.toString(Tetris.tetrisVariables.get_score_add()), 675,
						(670 - (font_size * 2)));
				if (font_size < max_font_size) {
					font_size++;
				}
			} else
				font_size = 0;
		}

	}

	private void draw_Shapes(Graphics g) {
		// string are created with 16 characters for indexing think it 0-15 each 4
		// grouped character dedicated to a single line.think like 4x4 matrix compressed
		// a string value to get it easier.
		// drawing formula from string to graphic like : shape type "I":
		// AXXX-AXXX-AXXX-AXXX
		int draw_x;
		int draw_y;
		int size = 25;

		Vector<Shape> my_shapes = Tetris.tetrisVariables.get_all_shapes();
		// draw each boxes.
		for (int k = 0; k < my_shapes.size(); k++) {
			if (my_shapes.get(k) != null) {
				for (int i = 0; i < 4; i++) {
					if (my_shapes.get(k).sh_boxes.get(i) != null) {
						draw_x = my_shapes.get(k).sh_boxes.get(i).get_box_x();
						draw_y = my_shapes.get(k).sh_boxes.get(i).get_box_y();
						g.setColor(Color.BLACK);
						g.fillRect(draw_x, draw_y, size, size);
						switch (my_shapes.get(k).get_shape_type()) {
						case "I":
							g.setColor(Color.GREEN);
							break;
						case "L":
							g.setColor(Color.MAGENTA);
							break;
						case "J":
							g.setColor(Color.GRAY);
							break;
						case "O":
							g.setColor(Color.BLUE);
							break;
						case "T":
							g.setColor(Color.YELLOW);
							break;
						case "S":
							g.setColor(Color.CYAN);
							break;
						case "Z":
							g.setColor(Color.ORANGE);
							break;
						}
						g.fill3DRect(draw_x + 1, draw_y + 1, size - 2, size - 2, true);
					}
				}
			}

		}
	}

	private void draw_Buffer(Graphics g) {
		int size = 25;
		int draw_x = 600;
		int draw_y = 50;

		Color color_back = Color.black;
		Color color_front = new Color(115, 232, 104);

		// buffer frame background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(625, 100, 200, 400);

		draw_tetroline(g, 600, 50, 18, true, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, 825, 50, 18, true, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, 600, 50, 8, false, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());
		draw_tetroline(g, 600, 475, 8, false, color_back, color_front, Tetris.tetrisVariables.get_tetrobox_size());

		Vector<Shape> my_shapes = Tetris.tetrisVariables.get_buffered_shapes();
		// draw each boxes.
		for (int k = 0; k < my_shapes.size(); k++) {
			if (my_shapes.get(k) != null) {
				for (int i = 0; i < 4; i++) {
					if (my_shapes.get(k).sh_boxes.get(i) != null) {
						draw_x = my_shapes.get(k).sh_boxes.get(i).get_box_x();
						draw_y = my_shapes.get(k).sh_boxes.get(i).get_box_y();
						g.setColor(Color.BLACK);
						g.fillRect((draw_x + 420), (draw_y + k * 125 + 100), size, size);
						switch (my_shapes.get(k).get_shape_type()) {
						case "I":
							g.setColor(Color.GREEN);
							break;
						case "L":
							g.setColor(Color.MAGENTA);
							break;
						case "J":
							g.setColor(Color.GRAY);
							break;
						case "O":
							g.setColor(Color.BLUE);
							break;
						case "T":
							g.setColor(Color.YELLOW);
							break;
						case "S":
							g.setColor(Color.CYAN);
							break;
						case "Z":
							g.setColor(Color.ORANGE);
							break;
						}
						g.fill3DRect((draw_x + 420) + 1, (draw_y + k * 125 + 100) + 1, size - 2, size - 2, true);
					}
				}
			}

		}
	}

	private void draw_Grid(Graphics g) {
		// Draw Grid with 25x25 blocks and shows line numbers

		// paint grid lines
		g.setColor(Color.BLACK);
		int x = 0;
		int y = 0;
		for (int i = 0; i < Tetris.tetrisVariables.get_gw_HEIGHT() / 25; i++) {
			g.setFont(new Font(Tetris.tetrisVariables.get_font_type(), Font.BOLD, 11));
			// show line numbers
			/*
			 * if(i <= 25) { g.drawString(Integer.toString(i), 5, y+90);
			 * g.drawString(Integer.toString(i), 22*25+5, y+90); }
			 */
			// draw grid horizontal lines
			if (i < 28 && i >= 4)
				g.drawLine(50, y, Tetris.tetrisVariables.get_gw_WIDTH() - 375, y);
			// draw grid vertical lines
			if (i < 21 && i >= 3)
				g.drawLine(x, 75, x, Tetris.tetrisVariables.get_gw_HEIGHT() - 100);
			// intervals

			y += 25;
			x += 25;
		}
	}

	@SuppressWarnings("null")
	private void pause_gameLoop(Graphics g) {
		g.drawImage(Tetris.tetrisVariables.get_view_bg_image(), 0, 0, Tetris.tetrisVariables.get_gw_WIDTH(),
				Tetris.tetrisVariables.get_gw_HEIGHT(), null, null);
		Color color_back = Color.BLACK;
		Color color_front = new Color(104, 106, 232);
		Color color_selection = new Color(229, 232, 104);

		int size = 25;
		int pause_x = 300;
		int pause_y = 150;
		int line_space = 47;
		int tab_space = 47;

		g.setColor(new Color(163, 104, 232));
		g.fillRect(pause_x, pause_y, 250, 300);
		g.setFont(new Font(Tetris.tetrisVariables.get_font_type(), Font.BOLD, 25));

		// pause frame
		for (int i = 0; i <= 11; i++) {
			// pause frame horizontal edges
			if (i <= 8) {
				g.setColor(color_back);
				g.fillRect(pause_x + (size * i) + 25, pause_y, size, size);
				g.fillRect(pause_x + (size * i) + 25, pause_y + 275, size, size);

				g.setColor(color_front);
				g.fill3DRect(pause_x + (size * i) + 1 + 25, pause_y + 1, size - 2, size - 2, true);
				g.fill3DRect(pause_x + (size * i) + 25 + 1, pause_y + 275 + 1, size - 2, size - 2, true);
			}

			// pause frame vertical edges
			g.setColor(color_back);
			g.fillRect(pause_x, pause_y + (size * i), size, size);
			g.fillRect(pause_x + 250, pause_y + (size * i), size, size);

			g.setColor(color_front);
			g.fill3DRect(pause_x + 1, pause_y + (size * i) + 1, size - 2, size - 2, true);
			g.fill3DRect(pause_x + 250 + 1, pause_y + (size * i) + 1, size - 2, size - 2, true);
		}

		g.setColor(new Color(219, 232, 104));
		g.fillRect(pause_x + 25, pause_y + line_space * 2, 225, 5);

		switch (Tetris.tetrisVariables.get_game_state()) {
		case "paused":

			g.setColor(Color.WHITE);
			g.drawString("! PAUSED !", pause_x + tab_space + 27, pause_y + line_space + 20);

			if (Tetris.tetrisVariables.get_pause_selection() == 0) {
				g.setColor(color_selection);
				g.fill3DRect(pause_x + tab_space, pause_y + line_space * 3 - 25, 180, 35, true);
				g.setColor(Color.BLACK);
				g.drawString("RESUME", pause_x + tab_space + 40, pause_y + line_space * 3);
			} else {
				g.setColor(Color.WHITE);
				g.drawString("RESUME", pause_x + tab_space + 40, pause_y + line_space * 3);
			}

			if (Tetris.tetrisVariables.get_pause_selection() == 1) {
				g.setColor(color_selection);
				g.fill3DRect(pause_x + tab_space, pause_y + line_space * 4 - 25, 180, 35, true);
				g.setColor(Color.BLACK);
				g.drawString("RESTART", pause_x + tab_space + 35, pause_y + line_space * 4);
			} else {
				g.setColor(Color.WHITE);
				g.drawString("RESTART", pause_x + tab_space + 35, pause_y + line_space * 4);
			}

			if (Tetris.tetrisVariables.get_pause_selection() == 2) {
				g.setColor(color_selection);
				g.fill3DRect(pause_x + tab_space, pause_y + line_space * 5 - 25, 180, 35, true);
				g.setColor(Color.BLACK);
				g.drawString("EXIT", pause_x + tab_space + 60, pause_y + line_space * 5);
			} else {
				g.setColor(Color.WHITE);
				g.drawString("EXIT", pause_x + tab_space + 60, pause_y + line_space * 5);
			}

			break;

		case "end":
			g.setColor(Color.WHITE);
			g.drawString("! END !", pause_x + tab_space + 52, pause_y + line_space + 20);

			if (Tetris.tetrisVariables.get_pause_selection() == 0) {
				g.setColor(color_selection);
				g.fill3DRect(pause_x + tab_space, pause_y + line_space * 3 - 25, 180, 35, true);
				g.setColor(Color.BLACK);
				g.drawString("RECORD", pause_x + tab_space + 35, pause_y + line_space * 3);
			} else {
				g.setColor(Color.WHITE);
				g.drawString("RECORD", pause_x + tab_space + 35, pause_y + line_space * 3);
			}

			if (Tetris.tetrisVariables.get_pause_selection() == 1) {
				g.setColor(color_selection);
				g.fill3DRect(pause_x + tab_space, pause_y + line_space * 4 - 25, 180, 35, true);
				g.setColor(Color.BLACK);
				g.drawString("RESTART", pause_x + tab_space + 35, pause_y + line_space * 4);
			} else {
				g.setColor(Color.WHITE);
				g.drawString("RESTART", pause_x + tab_space + 35, pause_y + line_space * 4);
			}

			if (Tetris.tetrisVariables.get_pause_selection() == 2) {
				g.setColor(color_selection);
				g.fill3DRect(pause_x + tab_space, pause_y + line_space * 5 - 25, 180, 35, true);
				g.setColor(Color.BLACK);
				g.drawString("EXIT", pause_x + tab_space + 60, pause_y + line_space * 5);
			} else {
				g.setColor(Color.WHITE);
				g.drawString("EXIT", pause_x + tab_space + 60, pause_y + line_space * 5);
			}

			break;
		case "highscore":
			// TODO:
			break;
		default:
			System.err.println("ERROR : unkown game state");
			System.err.println(
					"Tetris.tetrisVariables.get_game_state() called : " + Tetris.tetrisVariables.get_game_state());
			break;
		}
	}

	private void draw_tetroline(Graphics g, int pos_x, int pos_y, int length, boolean vertical, Color color_back,
			Color color_front, int size) {
		if (vertical) {
			for (int i = 1; i <= length; i++) {

				g.setColor(color_back);
				g.fillRect(pos_x, pos_y + (i * size), size, size);

				g.setColor(color_front);
				g.fill3DRect(pos_x + 1, pos_y + (i * size) + 1, size - 2, size - 2, true);
			}
		} else {
			for (int i = 1; i <= length; i++) {
				g.setColor(color_back);
				g.fillRect(pos_x + (i * size), pos_y + size, size, size);

				g.setColor(color_front);
				g.fill3DRect(pos_x + (i * size) + 1, pos_y + size + 1, size - 2, size - 2, true);
			}
		}

	}

	@SuppressWarnings("unused")
	private void draw_tetrosquare(Graphics g, int pos_x, int pos_y, int x_length, int y_length, Color color_back,
			Color color_front, Color fill_rect_at_back, int size) {
		g.setColor(fill_rect_at_back);
		g.fillRect(pos_x + size, pos_y + size, x_length - size, y_length - size);

		draw_tetroline(g, pos_x, pos_y, (y_length / size), true, color_back, color_front, size);
		draw_tetroline(g, (pos_x + x_length), pos_y, (y_length / size), true, color_back, color_front, size);

		draw_tetroline(g, pos_x, pos_y, (x_length / size), false, color_back, color_front, size);
		draw_tetroline(g, pos_x, pos_y + y_length - size, (x_length / size), false, color_back, color_front, size);
	}
}
