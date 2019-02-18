package tetris.code;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.util.Calendar;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

class GenericVariables {
	/* ### ALL INSTANCES and VARIABLES HAVE MANAGED HERE ### */
	// variables
	/*
	 * s_ : screen original width or height value. gw_ : game window assigned by
	 * myself.
	 */

	private JFrame frame;
	private DrawPanel drawPanel;
	private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

	private HighScore highscore;

	private Vector<Shape> all_shapes = new Vector<>(0); // this vector keeps the information of the all shapes hasdrawed
														// and also returning to draw function to draw it for allgame
														// time.
	private Vector<Shape> buffered_shapes = new Vector<>(3); // this vector keeps the new generated buffer than sends
																// the generated shapes to game area.

	private int[] explode_lines = new int[25];

	private final int s_WIDTH = gd.getDisplayMode().getWidth();
	private final int s_HEIGHT = gd.getDisplayMode().getHeight();
	private final int gw_WIDTH = 900;
	private final int gw_HEIGHT = 760;
	private final int sleep_time = 16;
	private final int TOP_BORDER = 1 * 25;
	private final int LEFT_BORDER = 1 * 25;
	private final int RIGHT_BORDER = 21 * 25;
	private final int BOTTOM_BORDER = 28 * 25;

	private int shape_cnt = 0;
	private int frameCounter_left = 0;
	private int frameCounter_right = 0;
	private int frameCounter_collision = 0;
	private int frameCounter_collision_bot = 0;
	private int frameCounter = 0;
	private int pause_selection = 0;
	private int score = 0;
	private int score_multiplier;
	private int score_add = 0;
	private int speed_game = 30; // lower value has more speed // 16 gameSpeed draws 60 fps
	private int speed_down = 30;
	private int tetrobox_size = 25;

	private boolean left;
	private boolean right;
	private boolean down;
	private boolean up;
	private boolean enter = false;
	private boolean esc = false;
	private boolean directDown = false;
	private boolean pause = false;
	private boolean pause_apply = false;
	private boolean started = false; // record game started or not
	private boolean endGame = false;
	private boolean restartGame = false;
	private boolean rotate_available = true;
	private boolean col_right_exists = false;
	private boolean col_left_exists = false;
	private boolean col_bot_exists = false;
	private boolean highscore_file_exists = true;
	private boolean record_done = false;
	private boolean call_new_shape = true;
	private boolean exit_game = false;

	private final String font_type = "Tahoma";
	private final String high_score_file_name = "tetris.highscoredata";
	private String user_name = "";
	private String game_state = "loading";
	private String[][] shape_codes = { /* 0. --> I */ { "AXXXAXXXAXXXAXXX", "AAAAXXXXXXXXXXXX" },
			/* 1. --> L */ { "AXXXAXXXAAXXXXXX", "AAAXAXXXXXXXXXXX", "AAXXXAXXXAXXXXXX", "XXAXAAAXXXXXXXXX" },
			/* 2. --> J */ { "XAXXXAXXAAXXXXXX", "AXXXAAAXXXXXXXXX", "AAXXAXXXAXXXXXXX", "AAAXXXAXXXXXXXXX" },
			/* 3. --> O */ { "AAXXAAXXXXXXXXXX" },
			/* 4. --> T */ { "XAXXAAAXXXXXXXXX", "AXXXAAXXAXXXXXXX", "AAAXXAXXXXXXXXXX", "XAXXAAXXXAXXXXXX" },
			/* 5. --> S */ { "XAAXAAXXXXXXXXXX", "AXXXAAXXXAXXXXXX" },
			/* 6. --> Z */ { "AAXXXAAXXXXXXXXX", "XAXXAAXXAXXXXXXX" } };

	private Image view_welcome_image;
	private Image view_intro_image;
	private Image view_bg_image;
	private Image view_instructions_image;

	private Calendar game_startedTimeStamp = Calendar.getInstance();
	private Calendar score_addTimeStamp;

	private Color bg_color = new Color(190, 247, 236);

	private AudioInputStream audio_explosion;
	private AudioInputStream audio_move;
	private AudioInputStream audio_rotate;
	private AudioInputStream audio_loading;
	private AudioInputStream audio_drop;
	private AudioInputStream audio_theme_music;

	private Clip clip_explosion;
	private Clip clip_move;
	private Clip clip_rotate;
	private Clip clip_loading;
	private Clip clip_drop;
	private Clip clip_theme_music;

	// getter methods
	protected JFrame get_frame() {
		return frame;
	}

	protected DrawPanel get_drawPanel() {
		return drawPanel;
	}

	protected HighScore get_highscore() {
		return highscore;
	}
	
	protected Vector<Shape> get_all_shapes() {
		return this.all_shapes;
	}

	protected Vector<Shape> get_buffered_shapes() {
		return buffered_shapes;
	}

	protected int[] get_explode_lines() {
		return this.explode_lines;
	}
	
	protected final int get_top_border() {
		return TOP_BORDER;
	}

	protected final int get_left_border() {
		return LEFT_BORDER;
	}

	protected final int get_right_border() {
		return RIGHT_BORDER;
	}

	protected final int get_bottom_border() {
		return BOTTOM_BORDER;
	}
	
	protected final int get_s_WIDTH() {
		return s_WIDTH;
	}

	protected final int get_s_HEIGHT() {
		return s_HEIGHT;
	}

	protected final int get_gw_WIDTH() {
		return gw_WIDTH;
	}

	protected final int get_gw_HEIGHT() {
		return gw_HEIGHT;
	}

	protected final int get_sleep_time() {
		return sleep_time;
	}

	protected int get_shape_cnt() {
		return shape_cnt;
	}

	protected int get_frameCounter() {
		return frameCounter;
	}

	protected int get_frameCounter_left() {
		return frameCounter_left;
	}

	protected int get_frameCounter_right() {
		return frameCounter_right;
	}

	protected int get_frameCounter_collision() {
		return frameCounter_collision;
	}

	protected int get_frameCounter_collision_bot() {
		return frameCounter_collision_bot;
	}

	protected int get_pause_selection() {
		return pause_selection;
	}

	protected int get_score() {
		return score;
	}

	protected int get_score_multiplier() {
		return score_multiplier;
	}

	protected int get_score_add() {
		return score_add;
	}

	protected int get_speed_down() {
		return speed_down;
	}

	protected int get_speed_game() {
		return speed_game;
	}

	protected int get_tetrobox_size() {
		return tetrobox_size;
	}

	protected boolean get_left() {
		return left;
	}

	protected boolean get_right() {
		return right;
	}

	protected boolean get_down() {
		return down;
	}

	protected boolean get_up() {
		return up;
	}

	protected boolean get_enter() {
		return enter;
	}

	protected boolean get_esc() {
		return esc;
	}

	protected boolean get_directDown() {
		return directDown;
	}

	protected boolean get_pause() {
		return pause;
	}

	protected boolean get_pause_apply() {
		return pause_apply;
	}

	protected boolean get_started() {
		return started;
	}

	protected boolean get_endGame() {
		return endGame;
	}

	protected boolean get_restartGame() {
		return restartGame;
	}

	protected boolean get_rotate_available() {
		return rotate_available;
	}

	protected boolean get_col_right_exists() {
		return col_right_exists;
	}

	protected boolean get_col_left_exists() {
		return col_left_exists;
	}

	protected boolean get_col_bot_exists() {
		return col_bot_exists;
	}

	protected boolean get_highscore_file_exists() {
		return highscore_file_exists;
	}

	protected boolean get_record_done() {
		return record_done;
	}

	protected boolean get_call_new_shape() {
		return this.call_new_shape;
	}

	protected boolean get_exit_game() {
		return exit_game;
	}
	
	protected String get_font_type() {
		return font_type;
	}

	protected String get_user_name() {
		return user_name;
	}

	protected String get_game_state() {
		return game_state;
	}

	protected String get_high_score_file_name() {
		return high_score_file_name;
	}

	protected String[][] get_shape_codes() {
		return shape_codes;
	}

	protected Image get_view_welcome_image() {
		return view_welcome_image;
	}

	protected Image get_view_intro_image() {
		return view_intro_image;
	}

	protected Image get_view_bg_image() {
		return view_bg_image;
	}

	protected Image get_view_instructions_image() {
		return view_instructions_image;
	}

	protected Calendar get_game_startedTimeStamp() {
		return game_startedTimeStamp;
	}

	protected Calendar get_score_addTimeStamp() {
		return score_addTimeStamp;
	}

	protected Color get_bg_color() {
		return bg_color;
	}

	protected AudioInputStream get_audio_explosion() {
		return audio_explosion;
	}

	protected AudioInputStream get_audio_move() {
		return audio_move;
	}

	protected AudioInputStream get_audio_rotate() {
		return audio_rotate;
	}

	protected AudioInputStream get_audio_loading() {
		return audio_loading;
	}

	protected AudioInputStream get_audio_drop() {
		return audio_drop;
	}

	protected AudioInputStream get_audio_theme_music() {
		return audio_theme_music;
	}

	protected Clip get_clip_explosion() {
		return clip_explosion;
	}

	protected Clip get_clip_move() {
		return clip_move;
	}

	protected Clip get_clip_rotate() {
		return clip_rotate;
	}

	protected Clip get_clip_loading() {
		return clip_loading;
	}

	protected Clip get_clip_drop() {
		return clip_drop;
	}

	protected Clip get_clip_theme_music() {
		return clip_theme_music;
	}

	protected Shape get_active_shape() {
		return get_all_shapes().lastElement();
	}

	// setter methods
	protected void set_frame(JFrame p_frame) {
		frame = p_frame;
	}

	protected void set_drawPanel(DrawPanel p_drawPanel) {
		drawPanel = p_drawPanel;
	}

	protected void set_shape_cnt(int p_shape_cnt) {
		shape_cnt = p_shape_cnt;
	}

	protected void set_pause_selection(int p_pause_selection) {
		pause_selection = p_pause_selection;
	}

	protected void set_frameCounter(int p_frameCounter) {
		frameCounter = p_frameCounter;
	}

	protected void set_frameCounter_left(int p_frameCounter_left) {
		frameCounter_left = p_frameCounter_left;
	}

	protected void set_frameCounter_right(int p_frameCounter_right) {
		frameCounter_right = p_frameCounter_right;
	}

	protected void set_frameCounter_collision(int p_frameCounter_collision) {
		frameCounter_collision = p_frameCounter_collision;
	}

	protected void set_frameCounter_collision_bot(int p_frameCounter_collision_bot) {
		frameCounter_collision_bot = p_frameCounter_collision_bot;
	}

	protected void set_score(int p_score) {
		score = p_score;
	}

	protected void set_score_multiplier(int p_score_multiplier) {
		score_multiplier = p_score_multiplier;
	}

	protected void set_score_add(int p_score_add) {
		score_add = p_score_add;
	}

	protected void set_speed_game(int p_speed_game) {
		speed_game = p_speed_game;
	}

	protected void set_speed_down(int p_speed_down) {
		speed_down = p_speed_down;
	}

	protected void set_tetrobox_size(int p_tetrobox_size) {
		tetrobox_size = p_tetrobox_size;
	}

	protected void set_left(boolean p_left) {
		left = p_left;
	}

	protected void set_right(boolean p_right) {
		right = p_right;
	}

	protected void set_down(boolean p_down) {
		down = p_down;
	}

	protected void set_up(boolean p_up) {
		up = p_up;
	}

	protected void set_enter(boolean p_enter) {
		enter = p_enter;
	}

	protected void set_esc(boolean p_esc) {
		esc = p_esc;
	}

	protected void set_directDown(boolean p_directDown) {
		directDown = p_directDown;
	}

	protected void set_pause(boolean p_pause) {
		pause = p_pause;
	}

	protected void set_pause_apply(boolean p_pause_apply) {
		pause_apply = p_pause_apply;
	}

	protected void set_started(boolean p_started) {
		started = p_started;
	}

	protected void set_endGame(boolean p_endGame) {
		endGame = p_endGame;
	}

	protected void set_restartGame(boolean p_restartGame) {
		restartGame = p_restartGame;
	}

	protected void set_rotate_available(boolean p_rotate_available) {
		rotate_available = p_rotate_available;
	}

	protected void set_col_right_exists(boolean p_col_right_exists) {
		col_right_exists = p_col_right_exists;
	}

	protected void set_col_left_exists(boolean p_col_left_exists) {
		col_left_exists = p_col_left_exists;
	}

	protected void set_col_bot_exists(boolean p_col_bot_exists) {
		col_bot_exists = p_col_bot_exists;
	}

	protected void set_highscore_file_exists(boolean p_highscore_file_exists) {
		highscore_file_exists = p_highscore_file_exists;
	}

	protected void set_record_done(boolean p_record_done) {
		record_done = p_record_done;
	}
	
	protected void set_call_new_shape(boolean p_bNew) {
		call_new_shape = p_bNew;
	}
	
	protected void set_exit_game(boolean p_bExit) {
		exit_game = p_bExit;
	}
	
	protected boolean set_highscore(HighScore p_highscore) {
		highscore = p_highscore;
		return true;
	}

	protected void set_game_state(String p_state) {
		game_state = p_state;
	}

	protected void set_user_name(String p_user_name) {
		user_name = p_user_name;
	}

	/**
	 * Game States : welcome, running, paused , end, exit , loading , highscore ,
	 * instructions
	 */
	protected void set_view_welcome_image(Image p_image) {
		view_welcome_image = p_image;
	}

	protected void set_view_intro_image(Image p_image) {
		view_intro_image = p_image;
	}

	protected void set_view_bg_image(Image p_image) {
		view_bg_image = p_image;
	}

	protected void set_view_instructions_image(Image p_image) {
		view_instructions_image = p_image;
	}

	protected void set_game_startedTimeStamp(Calendar p_Date) {
		game_startedTimeStamp = p_Date;
	}

	protected void set_score_addTimeStamp(Calendar p_Date) {
		score_addTimeStamp = p_Date;
	}

	protected void set_audio_explosion(AudioInputStream p_audio) {
		audio_explosion = p_audio;
	}

	protected void set_audio_move(AudioInputStream p_audio) {
		audio_move = p_audio;
	}

	protected void set_audio_rotate(AudioInputStream p_audio) {
		audio_rotate = p_audio;
	}

	protected void set_audio_loading(AudioInputStream p_audio) {
		audio_loading = p_audio;
	}

	protected void set_audio_drop(AudioInputStream p_audio) {
		audio_drop = p_audio;
	}

	protected void set_audio_theme_music(AudioInputStream p_audio) {
		audio_theme_music = p_audio;
	}

	protected void set_clip_explosion(Clip p_clip) {
		clip_explosion = p_clip;
	}

	protected void set_clip_move(Clip p_clip) {
		clip_move = p_clip;
	}

	protected void set_clip_rotate(Clip p_clip) {
		clip_rotate = p_clip;
	}

	protected void set_clip_loading(Clip p_clip) {
		clip_loading = p_clip;
	}

	protected void set_clip_drop(Clip p_clip) {
		clip_drop = p_clip;
	}

	protected void set_clip_theme_music(Clip p_clip) {
		clip_theme_music = p_clip;
	}

	// increase methods
	protected void inc_frameCounter_collision_bot() {
		frameCounter_collision_bot++;
	}

	protected void reset() {
		shape_cnt = 0;
		frameCounter = 0;
		frameCounter_left = 0;
		frameCounter_right = 0;
		frameCounter_collision = 0;
		frameCounter_collision_bot = 0;
		pause_selection = 0;
		score = 0;
		score_multiplier = 0;
		score_add = 0;
		speed_down = 30;
		speed_game = 30; // lower value has more speed // 16 gameSpeed draws 60 fps
		tetrobox_size = 25;

		left = false;
		right = false;
		down = false;
		up = false;
		enter = false;
		esc = false;
		directDown = false;
		pause = false;
		pause_apply = false;
		started = false;
		endGame = false;
		restartGame = false;
		rotate_available = true;
		col_right_exists = false;
		col_left_exists = false;
		col_bot_exists = false;
		highscore_file_exists = false;
		record_done = false;

		user_name = "";
		game_state = "loading";
		Tetris.tetrisComponents.ch_order = 0;
		Tetris.tetrisComponents.ch_temp = '/';
		Tetris.tetrisComponents.name = "";
		
		get_all_shapes().removeAllElements();
		get_buffered_shapes().removeAllElements();

		game_startedTimeStamp = Calendar.getInstance();
	}

	protected void reset_sound(Clip clip) {
		if (clip.getFrameLength() <= clip.getFramePosition()) {
			clip.stop();
			clip.setFramePosition(0);
		}
	}
}
