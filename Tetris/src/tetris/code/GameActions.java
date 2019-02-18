package tetris.code;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;
import tetris.externaltools.SerializationUtil;

//this class is the most important class.
class GameActions {
	
	GameActions() {
		for (int i = 0; i < 25; i++) {
			Tetris.tetrisVariables.get_explode_lines()[i] = 0;
		}
	}

	protected void check_audios() {
		Tetris.tetrisVariables.reset_sound(Tetris.tetrisVariables.get_clip_rotate());
		Tetris.tetrisVariables.reset_sound(Tetris.tetrisVariables.get_clip_explosion());
		Tetris.tetrisVariables.reset_sound(Tetris.tetrisVariables.get_clip_loading());
		Tetris.tetrisVariables.reset_sound(Tetris.tetrisVariables.get_clip_move());
		Tetris.tetrisVariables.reset_sound(Tetris.tetrisVariables.get_clip_drop());
		Tetris.tetrisVariables.reset_sound(Tetris.tetrisVariables.get_clip_theme_music());

		if (Tetris.tetrisVariables.get_game_state() == "running") {
			Tetris.tetrisVariables.get_clip_loading().stop();
			Tetris.tetrisVariables.get_clip_loading().setFramePosition(0);
		} else if (Tetris.tetrisVariables.get_game_state() == "paused") {
			System.out.println(Tetris.tetrisVariables.get_game_state());
			Tetris.tetrisVariables.get_clip_theme_music().stop();
		} else {
			Tetris.tetrisVariables.get_clip_theme_music().stop();
			Tetris.tetrisVariables.get_clip_theme_music().setFramePosition(0);
		}
	}

	protected void record_highScore() {
		try {
			if (!Tetris.tetrisVariables.get_highscore_file_exists()) {
				Tetris.tetrisVariables.set_highscore(new HighScore());
				System.out.println("new highscore object created.");
			}
			if (!Tetris.tetrisVariables.get_record_done()) {
				Tetris.tetrisVariables.get_highscore().get_highScoreDataMap().put(Tetris.tetrisVariables.get_score(),
						Tetris.tetrisVariables.get_user_name());
				System.out.println("new record succesfully added to datamap");
				SerializationUtil.serialize(Tetris.tetrisVariables.get_highscore(), Tetris.tetrisVariables.get_high_score_file_name());
				Tetris.tetrisVariables.set_record_done(true);
				;
			}

		} catch (IOException e) {
			System.err.println("Problem Occured While Saving Highscore.. in record_highScore");
			// e.printStackTrace();
		}
	}

	int ch_order = 0;
	char ch_temp = '/';
	String name = "";

	protected void high_score_name_writing() {
		if (Tetris.tetrisVariables.get_game_state() == "highscore") {
			if (ch_temp == '/')
				ch_temp = 'A';
			if (Tetris.tetrisVariables.get_up()) {
				ch_temp++;
				if (ch_temp > 'Z')
					ch_temp = '0';
				Tetris.tetrisVariables.set_up(false);
			}
			if (Tetris.tetrisVariables.get_down()) {
				ch_temp--;
				if (ch_temp < '0')
					ch_temp = 'Z';
				Tetris.tetrisVariables.set_down(false);
			}
			if (Tetris.tetrisVariables.get_enter()) {
				name += ch_temp;
				ch_order++;
				ch_temp = 'A';
				if (name != "")
					Tetris.tetrisVariables.set_user_name(name);
				Tetris.tetrisVariables.set_enter(false);
			}
		}
	}

	// let user select one of the options on the view during pause menu
	protected void pause_menu_select_func() {
		switch (Tetris.tetrisVariables.get_game_state()) {
		case "welcome":
			if (Tetris.tetrisVariables.get_pause_apply()) {
				switch (Tetris.tetrisVariables.get_pause_selection()) {
				case 0: /* PLAY */
					Tetris.tetrisVariables.set_started(true);
					Tetris.tetrisVariables.set_game_state("running");
					break;
				case 1: /* INSTRUCTIONS */
					Tetris.tetrisVariables.set_game_state("instructions");
					break;
				case 2: /* EXIT */
					Tetris.tetrisVariables.set_exit_game(true);
					Tetris.tetrisVariables.set_game_state("exit");
					break;
				default:
					System.err.println("pause menu selection apply problem.");
					break;
				}
			}
			break;
		case "paused":
			if (Tetris.tetrisVariables.get_pause_apply()) {
				switch (Tetris.tetrisVariables.get_pause_selection()) {
				case 0:
					Tetris.tetrisVariables.set_pause(false);
					Tetris.tetrisVariables.set_game_state("running");
					break;
				case 1:
					Tetris.tetrisVariables.set_restartGame(true);
					break;
				case 2:
					Tetris.tetrisVariables.set_exit_game(true);
					Tetris.tetrisVariables.set_game_state("exit");
					break;
				default:
					System.err.println("pause menu selection apply problem.");
					break;
				}
			}
			break;
		case "end":
			if (Tetris.tetrisVariables.get_pause_apply()) {
				switch (Tetris.tetrisVariables.get_pause_selection()) {
				case 0:
					Tetris.tetrisVariables.set_game_state("highscore");
					break;
				case 1:
					Tetris.tetrisVariables.set_restartGame(true);
					break;
				case 2:
					Tetris.tetrisVariables.set_exit_game(true);
					Tetris.tetrisVariables.set_game_state("exit");
					break;
				default:
					System.err.println("pause menu selection apply problem.");
				}
			}
			break;
		default:

			break;
		}

		Tetris.tetrisVariables.set_pause_apply(false);
	}

	// this method has really really critical role in the program it gets the new
	// shape data and adding to all shapes list (vector) and goes on for everything.
	protected void newShape(Shape new_shape) {
		Tetris.tetrisVariables.get_all_shapes().addElement(new_shape);
		Tetris.tetrisVariables.set_shape_cnt(Tetris.tetrisVariables.get_shape_cnt() + 1);
	}

	protected void set_call_new_shape(boolean b) {
		Tetris.tetrisVariables.set_call_new_shape(b);
	}

	protected void rotate_shape(Vector<Shape> all_shapes) {
		if (Tetris.tetrisVariables.get_clip_rotate().getFramePosition() == 0) {
			int last_index = all_shapes.size() - 1;
			int actual_x = all_shapes.lastElement().get_shape_start_loc_X() + 100;
			int actual_y = all_shapes.lastElement().get_shape_start_loc_Y();
			String type = all_shapes.lastElement().get_shape_type();

			int type_no = all_shapes.lastElement().get_shape_type_no(); // shape_codes array first dimension number
			int rot_no = all_shapes.lastElement().get_shape_rotation_no(); // shape_codes array second dimension number
			String code = null;
			if (type_no != 3) {
				// will be taken from array list with new rotation no
				if (Tetris.tetrisVariables.get_shape_codes()[type_no].length - 1 == rot_no) {
					rot_no = 0;
					code = Tetris.tetrisVariables.get_shape_codes()[type_no][rot_no];
				} else {
					rot_no++;
					code = Tetris.tetrisVariables.get_shape_codes()[type_no][rot_no];
				}
				Shape rotatedshape;
				rotatedshape = stringtoShape(code, type, type_no, rot_no, actual_x, actual_y - 25);
				rotatedshape.set_shape_active(false);
				if (checkcollisions(rotatedshape)) {
					rotatedshape.set_shape_active(true);
					all_shapes.setElementAt(rotatedshape, last_index);
					all_shapes.elementAt(last_index).set_shape_id(last_index);
					Tetris.tetrisVariables.get_clip_rotate().start();
				} else
					System.err.println("Collision exists while rotating.");
			} else {
				System.err.println("no rotation for this shape.");
			}
		}
	}

	protected void reset_explode_lines() {
		for (int i = 0; i < 25; i++) {
			Tetris.tetrisVariables.get_explode_lines()[i] = 0;
		}
	}

	protected void left_event() {
		// left event
		if (Tetris.tetrisVariables.get_clip_move().getFramePosition() == 0) {
			Shape active = Tetris.tetrisVariables.get_all_shapes().lastElement();
			if (Tetris.tetrisVariables.get_left() && active.get_shape_active()) {
				if (!Tetris.tetrisVariables.get_down()) {
					if (Tetris.tetrisVariables.get_frameCounter_left() == 1) {
						active.go_left();
						Tetris.tetrisVariables
								.set_frameCounter_left(Tetris.tetrisVariables.get_frameCounter_left() + 1);
					} else if (Tetris.tetrisVariables.get_frameCounter_left() >= Tetris.tetrisVariables.get_speed_game()
							/ 2) {
						active.go_left();
						Tetris.tetrisVariables.set_frameCounter_left(2);
					} else {
						Tetris.tetrisVariables
								.set_frameCounter_left(Tetris.tetrisVariables.get_frameCounter_left() + 1);
					}
				} else {
					if (Tetris.tetrisVariables.get_frameCounter_left() == 1) {
						active.go_left();
						Tetris.tetrisVariables
								.set_frameCounter_left(Tetris.tetrisVariables.get_frameCounter_left() + 1);
					} else if (Tetris.tetrisVariables.get_frameCounter_left() >= Tetris.tetrisVariables.get_speed_game()
							/ 7) {
						active.go_left();
						Tetris.tetrisVariables.set_frameCounter_left(2);
					} else {
						Tetris.tetrisVariables
								.set_frameCounter_left(Tetris.tetrisVariables.get_frameCounter_left() + 1);
					}
				}
			}
		}
	}

	protected void right_event() {
		// right event
		Shape active = Tetris.tetrisVariables.get_all_shapes().lastElement();
		if (Tetris.tetrisVariables.get_right() && active.get_shape_active()) {
			if (!Tetris.tetrisVariables.get_down()) {
				if (Tetris.tetrisVariables.get_frameCounter_right() == 1) {
					active.go_right();
					Tetris.tetrisVariables.set_frameCounter_right(Tetris.tetrisVariables.get_frameCounter_right() + 1);
				} else if (Tetris.tetrisVariables.get_frameCounter_right() >= Tetris.tetrisVariables.get_speed_game()
						/ 2) {
					active.go_right();
					Tetris.tetrisVariables.set_frameCounter_right(2);
				} else {
					Tetris.tetrisVariables.set_frameCounter_right(Tetris.tetrisVariables.get_frameCounter_right() + 1);
				}
			} else {
				if (Tetris.tetrisVariables.get_frameCounter_right() == 1) {
					active.go_right();
					Tetris.tetrisVariables.set_frameCounter_right(Tetris.tetrisVariables.get_frameCounter_right() + 1);
				} else if (Tetris.tetrisVariables.get_frameCounter_right() >= Tetris.tetrisVariables.get_speed_down()) {
					active.go_right();
					Tetris.tetrisVariables.set_frameCounter_right(2);
				} else {
					Tetris.tetrisVariables.set_frameCounter_right(Tetris.tetrisVariables.get_frameCounter_right() + 1);
				}
			}
		}
	}

	protected void down_event() {
		// down event
		Shape active = Tetris.tetrisVariables.get_all_shapes().lastElement();
		if (active.get_shape_active()
				&& Tetris.tetrisVariables.get_frameCounter() >= Tetris.tetrisVariables.get_speed_down()) {
			if (Tetris.tetrisVariables.get_col_bot_exists()) {
				if (Tetris.tetrisVariables.get_frameCounter_collision_bot() >= 60) {
					if (Tetris.tetrisVariables.get_col_bot_exists()) {
						active.set_shape_active(false);
					} else {
						Tetris.tetrisVariables.set_frameCounter(0);
						active.go_down();
					}
				}
			} else {
				Tetris.tetrisVariables.set_frameCounter(0);
				active.go_down();
			}
		}
	}

	protected void directDown_event() {
		int last_index = Tetris.tetrisVariables.get_all_shapes().size() - 1;
		Shape active = Tetris.tetrisVariables.get_all_shapes().lastElement();
		Shape aspect = active;
		aspect.set_shape_active(false);
		while (checkcollisions(aspect)) {
			aspect.go_down();
		}
		aspect.set_shape_active(true);
		Tetris.tetrisVariables.get_all_shapes().setElementAt(aspect, last_index);
		Tetris.tetrisVariables.get_all_shapes().elementAt(last_index).set_shape_id(last_index);
		Tetris.tetrisVariables.set_directDown(false);
		Tetris.tetrisVariables.get_clip_drop().start();
	}

	// manage gamespeed
	protected void manage_game_speed() {
		int score = Tetris.tetrisVariables.get_score();
		if (score >= 0 && score < 1000)
			Tetris.tetrisVariables.set_speed_game(30);
		else if (score >= 1000 && score < 3000)
			Tetris.tetrisVariables.set_speed_game(20);
		else if (score >= 3000 && score < 5000)
			Tetris.tetrisVariables.set_speed_game(10);
		else
			Tetris.tetrisVariables.set_speed_game(5);

		Tetris.tetrisVariables.set_speed_down(Tetris.tetrisVariables.get_speed_game());
	}

	// check for exploding lines
	private void check_exploding_line() {
		Tetris.tetrisComponents.reset_explode_lines();
		Tetris.tetrisVariables.set_score_multiplier(0);
		Tetris.tetrisVariables.set_score_add(0);
		Tetris.tetrisVariables.set_score_addTimeStamp(Calendar.getInstance());
		Shape sh;
		Box bx;
		int line_no;
		int exp_line_counter = 0;
		for (int i = 0; i < Tetris.tetrisVariables.get_all_shapes().size(); i++) {
			if (Tetris.tetrisVariables.get_game_state() == "running") {
				if (Tetris.tetrisVariables.get_all_shapes().get(i) != null) {
					sh = Tetris.tetrisVariables.get_all_shapes().get(i);
					for (int k = 0; k < 4; k++) {
						if (sh.sh_boxes.get(k) != null) {
							bx = sh.sh_boxes.get(k);
							line_no = (bx.get_box_y() / 25) - 3;
							if (line_no <= 0) {
								gameOver();
							} else if (Tetris.tetrisVariables.get_game_state() == "running") {
								Tetris.tetrisVariables.get_explode_lines()[line_no]++;
								if (Tetris.tetrisVariables.get_explode_lines()[line_no] == 19) {
									explode_line(line_no);
									exp_line_counter++;
								}
							} else {
								break;
							}
						}
					}
				}
			} else {
				break;
			}
		}
		if (exp_line_counter == 0) {
			Tetris.tetrisVariables.set_score_multiplier(0);
		} else if (exp_line_counter == 1) {
			Tetris.tetrisVariables.set_score_multiplier(1);
		} else if (exp_line_counter == 2) {
			Tetris.tetrisVariables.set_score_multiplier(8);
		} else if (exp_line_counter == 3) {
			Tetris.tetrisVariables.set_score_multiplier(12);
		} else if (exp_line_counter == 4) {
			Tetris.tetrisVariables.set_score_multiplier(16);
		} else
			System.err.println("ERROR : UNKOWN exp_line_counter Value @gameComponents.check_exploding_line()");

		Tetris.tetrisVariables.set_score_add(250 * Tetris.tetrisVariables.get_score_multiplier());
		Tetris.tetrisVariables.set_score(Tetris.tetrisVariables.get_score() + Tetris.tetrisVariables.get_score_add());
		manage_game_speed();
	}

	private void gameOver() {
		Tetris.tetrisVariables.set_endGame(true);
		Tetris.tetrisVariables.set_game_state("end");
	}

	// explode lines after checked
	private void explode_line(int line_number) {
		int row_y = (line_number + 3) * 25;
		Shape sh;
		Box bx;
		int size = Tetris.tetrisVariables.get_all_shapes().size();

		int counter = 0;
		for (int i = 0; i < size; i++) {
			if (Tetris.tetrisVariables.get_all_shapes().get(i) != null) {
				sh = Tetris.tetrisVariables.get_all_shapes().get(i);
				for (int k = 0; k < 4; k++) {
					if (sh.sh_boxes.get(k) != null) {
						bx = sh.sh_boxes.get(k);
						if (row_y == bx.get_box_y()) {
							sh.sh_boxes.setElementAt(null, k);
							counter++;
						}
					}
				}
			}

		}
		if (counter == 19) {
			Tetris.tetrisVariables.get_explode_lines()[line_number] = 0;
			Tetris.tetrisVariables.get_clip_explosion().start();
			drop_upper_boxes(line_number);
		}

	}

	// drops bottom-free boxes on upper lines after explosion
	private void drop_upper_boxes(int line_number) {
		int drop_y = (line_number + 3) * 25;
		Shape sh;
		Box bx;
		for (int n = 1; n < 25; n++) {
			for (int i = 0; i < Tetris.tetrisVariables.get_all_shapes().size(); i++) {
				if (Tetris.tetrisVariables.get_all_shapes().get(i) != null) {
					sh = Tetris.tetrisVariables.get_all_shapes().get(i);
					for (int k = 0; k < 4; k++) {
						if (sh.sh_boxes.get(k) != null) {
							bx = sh.sh_boxes.get(k);
							if (bx.get_box_y() == drop_y - (n * 25) && check_box_bottom_free(bx)) {
								bx.set_box_y(bx.get_box_y() + 25);
								k--;
							}
						}
					}
				}
			}
		}
	}

	private boolean check_box_bottom_free(Box checkboxbottomfree) {
		Shape sh;
		Box bx;
		for (int i = 0; i < Tetris.tetrisVariables.get_all_shapes().size(); i++) {
			if (Tetris.tetrisVariables.get_all_shapes().get(i) != null) {
				sh = Tetris.tetrisVariables.get_all_shapes().get(i);
				for (int k = 0; k < 4; k++) {
					if (sh.sh_boxes.get(k) != null) {
						bx = sh.sh_boxes.get(k);
						if (checkboxbottomfree.get_box_x() == bx.get_box_x()
								&& (checkboxbottomfree.get_box_y() + 25 != bx.get_box_y()
										&& checkboxbottomfree.get_box_y() + 25 != Tetris.tetrisVariables
												.get_bottom_border())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected Shape generate_a_new_shape() {
		// this block written to generate a shape and add it to vector<shape>
		// buffered_shapes
		if (!Tetris.tetrisVariables.get_all_shapes().isEmpty())
			check_exploding_line();
		String code = null;
		String s_type = null;
		int s_type_no;
		int rot_no = 0; // rotation no , initially 0
		Random rndm = new Random();
		int rndm_no = rndm.nextInt(7);
		s_type_no = rndm_no;
		code = Tetris.tetrisVariables.get_shape_codes()[rndm_no][rot_no];

		if (rndm_no == 0)
			s_type = "I";
		else if (rndm_no == 1)
			s_type = "L";
		else if (rndm_no == 2)
			s_type = "J";
		else if (rndm_no == 3)
			s_type = "O";
		else if (rndm_no == 4)
			s_type = "T";
		else if (rndm_no == 5)
			s_type = "S";
		else if (rndm_no == 6)
			s_type = "Z";
		else
			s_type = "X";

		return stringtoShape(code, s_type, s_type_no, rot_no, 15 * 25, 0);
	}

	// fills shape buffer queue.
	protected void fill_buffer() {
		if (Tetris.tetrisVariables.get_buffered_shapes().isEmpty()) {
			Tetris.tetrisVariables.get_buffered_shapes().setSize(3);
			for (int i = 0; i < Tetris.tetrisVariables.get_buffered_shapes().size(); i++) {
				Tetris.tetrisVariables.get_buffered_shapes().setElementAt(generate_a_new_shape(), i);
			}
		} else {
			Tetris.tetrisVariables.get_buffered_shapes().setElementAt(generate_a_new_shape(), 2);
		}
	}

	// swapping shapes priority at queue
	protected Shape swap_buffered_shape_order() {
		Shape shape_toSend;
		if (Tetris.tetrisVariables.get_buffered_shapes().isEmpty()) {
			fill_buffer();
		}
		shape_toSend = Tetris.tetrisVariables.get_buffered_shapes().elementAt(0);
		Tetris.tetrisVariables.get_buffered_shapes().setElementAt(Tetris.tetrisVariables.get_buffered_shapes().get(1), 0);
		Tetris.tetrisVariables.get_buffered_shapes().setElementAt(Tetris.tetrisVariables.get_buffered_shapes().get(2), 1);
		Tetris.tetrisVariables.get_buffered_shapes().setElementAt(generate_a_new_shape(), 2);
		return shape_toSend;
	}

	// sends buffered shape to game
	protected void swap_buffered_shape_to_game() {
		Tetris.tetrisComponents.newShape(swap_buffered_shape_order());
		Tetris.tetrisVariables.set_call_new_shape(false);
	}

	protected Shape stringtoShape(String code, String s_type, int s_type_no, int rot_no, int x, int y) {
		int draw_x = x; // initialvalue : 15*25;
		int draw_y = y; // initialvalue : 0;

		Shape new_generated_shape = new Shape(draw_x, draw_y, s_type, s_type_no, rot_no, code);
		for (int i = 0; i < 16; i++) {
			char ch_at = code.charAt(i);
			if (i % 4 == 0) {
				draw_y += 25;
				draw_x -= 100;
			}
			if (ch_at == 'A') {
				new_generated_shape.add_box(new Box(draw_x, draw_y, draw_y + 25, draw_x + 25));
				new_generated_shape.set_shape_loc_X(draw_x);
				new_generated_shape.set_shape_loc_Y(draw_y);
			}
			draw_x += 25;
		}
		new_generated_shape.set_shape_active(true);
		return new_generated_shape;
	}

	protected boolean checkcollisions(Shape checkforshape) {
		Tetris.tetrisVariables.set_endGame(false);
		Tetris.tetrisVariables.set_col_right_exists(false);
		Tetris.tetrisVariables.set_col_left_exists(false);
		Tetris.tetrisVariables.set_col_bot_exists(false);
		int active_bottom;
		int active_x;
		int active_y;
		int active_right_end;

		// checks for bottom border.
		for (int i = 0; i < 4; i++) {
			if (checkforshape.sh_boxes.get(i).get_box_bottom_end() >= Tetris.tetrisVariables
					.get_bottom_border()) {
				if (checkforshape.get_shape_active()) {
					Tetris.tetrisVariables.set_col_bot_exists(true);
					break;
				} else
					return false;
			}
		}

		// check right and main border with each boxes
		if (checkforshape.get_shape_active()) {
			for (int i = 0; i < 4; i++) {
				// check for just horizontal matching according to left
				active_x = checkforshape.sh_boxes.get(i).get_box_x();
				if (active_x == Tetris.tetrisVariables.get_left_border() + 25) {
					Tetris.tetrisVariables.set_col_left_exists(true);
					;
				}
				// check for just horizontal matching according to right
				active_right_end = checkforshape.sh_boxes.get(i).get_box_right_end();
				if (active_right_end == Tetris.tetrisVariables.get_right_border()) {
					Tetris.tetrisVariables.set_col_right_exists(true);
				}
			}
		}

		// check right and left main borders with shape start & end locations for
		// rotatable or not
		checkforshape.calc_shape_start_end_loc();
		if (checkforshape.get_shape_start_loc_X() <= Tetris.tetrisVariables.get_left_border()) {
			Tetris.tetrisVariables.set_col_left_exists(true);
		}
		if (checkforshape.get_shape_end_loc_X() >= Tetris.tetrisVariables.get_right_border()) {
			Tetris.tetrisVariables.set_col_right_exists(true);
		}

		for (int i = 0; i < Tetris.tetrisVariables.get_all_shapes().size() - 1; i++) {
			if (Tetris.tetrisVariables.get_all_shapes().get(i) != null) {

				for (int k = 0; k < 4; k++) {// this loop selects active shape boxes
					active_bottom = checkforshape.sh_boxes.get(k).get_box_bottom_end();
					active_x = checkforshape.sh_boxes.get(k).get_box_x();
					active_y = checkforshape.sh_boxes.get(k).get_box_y();
					active_right_end = checkforshape.sh_boxes.get(k).get_box_right_end();

					// this loop checking selected active shape box with boxes of all passive shapes
					for (int t = 0; t < 4; t++) {
						if (Tetris.tetrisVariables.get_all_shapes().get(i).sh_boxes.get(t) != null) {
							int passive_x = Tetris.tetrisVariables.get_all_shapes().get(i).sh_boxes
									.get(t).get_box_x();
							int passive_right_end = Tetris.tetrisVariables.get_all_shapes()
									.get(i).sh_boxes.get(t).get_box_right_end();
							int passive_y = Tetris.tetrisVariables.get_all_shapes().get(i).sh_boxes
									.get(t).get_box_y();
							@SuppressWarnings("unused")
							int passive_bottom = Tetris.tetrisVariables.get_all_shapes().get(i).sh_boxes
									.get(t).get_box_bottom_end();

							// check horizontal and vertical matching
							if (active_bottom == passive_y && active_x == passive_x) {
								if (checkforshape.get_shape_active())
									Tetris.tetrisVariables.set_col_bot_exists(true);
								else
									return false;
							}

							if (Tetris.tetrisVariables.get_right()) {
								if (active_y == passive_y && active_bottom == passive_bottom
										&& active_right_end == passive_x) {
									if (checkforshape.get_shape_active())
										Tetris.tetrisVariables.set_col_right_exists(true);
									else
										return false;
								}
							}

							if (Tetris.tetrisVariables.get_left()) {
								if (active_y == passive_y && active_bottom == passive_bottom
										&& active_x == passive_right_end) {
									if (checkforshape.get_shape_active())
										Tetris.tetrisVariables.set_col_left_exists(true);
									else
										return false;
								}
							}

							if (active_bottom > passive_y && active_bottom < 4 * 25) {
								Tetris.tetrisVariables.set_endGame(false);
							}
						}
					}
				}
			}
		}

		if (Tetris.tetrisVariables.get_col_bot_exists())
			Tetris.tetrisVariables.inc_frameCounter_collision_bot();
		else
			Tetris.tetrisVariables.set_frameCounter_collision_bot(0);

		if (!Tetris.tetrisVariables.get_col_left_exists() && !Tetris.tetrisVariables.get_col_right_exists()
				&& !Tetris.tetrisVariables.get_col_bot_exists() && !checkforshape.get_shape_active())
			return true;
		else
			return false;

	}

	protected void read_highscore_file() {
		try {
			if (Tetris.tetrisVariables.set_highscore(SerializationUtil.deserialize(Tetris.tetrisVariables.get_high_score_file_name()))) {
				Tetris.tetrisVariables.set_highscore_file_exists(true);
			}
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("tetris.highscoredata file not found");
			Tetris.tetrisVariables.set_highscore_file_exists(false);
			// e.printStackTrace();
		}
	}

	protected void sleep(int speed) {
		try {
			Thread.sleep(speed);
		} catch (InterruptedException e) {
			System.err.println("ERROR : sleep");
			e.printStackTrace();
		}
	}

	protected void run_gameLoop() {
		Tetris.tetrisVariables.get_clip_theme_music().start();
		if (Tetris.tetrisVariables.get_all_shapes() != null) {
			if (Tetris.tetrisVariables.get_down()) {
				Tetris.tetrisVariables.set_speed_down(Tetris.tetrisVariables.get_speed_game() / 10);
			}

			Shape active = Tetris.tetrisVariables.get_active_shape();

			if (Tetris.tetrisVariables.get_col_left_exists() || Tetris.tetrisVariables.get_col_right_exists()) {
				Tetris.tetrisVariables
						.set_frameCounter_collision(Tetris.tetrisVariables.get_frameCounter_collision() + 1);
			} else {
				Tetris.tetrisVariables.set_frameCounter_collision(0);
			}

			if (active.get_shape_active()) {
				if (Tetris.tetrisVariables.get_directDown() && !Tetris.tetrisVariables.get_clip_drop().isRunning()) {
					directDown_event();
				} else {
					checkcollisions(active);
					if (!Tetris.tetrisVariables.get_col_left_exists())
						left_event();
					if (!Tetris.tetrisVariables.get_col_right_exists())
						right_event();

					checkcollisions(active);
					down_event();
				}

			}

			// generate new shape
			if (!active.get_shape_active() && !Tetris.tetrisVariables.get_endGame()) {
				swap_buffered_shape_to_game();
			}
			Tetris.tetrisVariables.set_frameCounter(Tetris.tetrisVariables.get_frameCounter() + 1);
		}
	}

	public void control_pause_menu_3_selection(int key) {

		if (key == KeyEvent.VK_DOWN && !Tetris.tetrisVariables.get_clip_move().isRunning()) {
			Tetris.tetrisVariables.get_clip_move().start();
			if (Tetris.tetrisVariables.get_pause_selection() == 0)
				Tetris.tetrisVariables.set_pause_selection(1);
			else if (Tetris.tetrisVariables.get_pause_selection() == 1)
				Tetris.tetrisVariables.set_pause_selection(2);
			else if (Tetris.tetrisVariables.get_pause_selection() == 2)
				Tetris.tetrisVariables.set_pause_selection(0);
		} else if (key == KeyEvent.VK_UP && !Tetris.tetrisVariables.get_clip_move().isRunning()) {
			Tetris.tetrisVariables.get_clip_move().start();
			if (Tetris.tetrisVariables.get_pause_selection() == 0)
				Tetris.tetrisVariables.set_pause_selection(2);
			else if (Tetris.tetrisVariables.get_pause_selection() == 2)
				Tetris.tetrisVariables.set_pause_selection(1);
			else if (Tetris.tetrisVariables.get_pause_selection() == 1)
				Tetris.tetrisVariables.set_pause_selection(0);
		}
		if (key == KeyEvent.VK_ENTER && !Tetris.tetrisVariables.get_clip_explosion().isRunning()) {
			Tetris.tetrisVariables.get_clip_explosion().start();
			Tetris.tetrisVariables.set_pause_apply(true);
		}

	}
}
