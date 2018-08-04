package tetris;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.util.Calendar;

import javax.swing.JFrame;


import tetris.Tetris.DrawPanel;


class genericVariables extends KeyInput{
	//variables
    /*
     * s_ : screen original width or height value.
     * gw_ : game window assigned by myself.
     */
	private static JFrame frame;
    private static DrawPanel drawPanel;
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    
	private static gameComponents my_tetris = new gameComponents();
	
    private static final  int s_WIDTH = gd.getDisplayMode().getWidth();
    private static final  int s_HEIGHT = gd.getDisplayMode().getHeight();
    private static final  int gw_WIDTH = 900; 
    private static final  int gw_HEIGHT = 760;
    private static final  int sleep_time = 16;
    
	private static int shape_cnt = 0;
	private static int frameCounter_left = 0;
	private static int frameCounter_right = 0;
	private static int frameCounter_collision = 0;
	private static int frameCounter_collision_bot = 0;
	private static int frameCounter = 0;
	private static int pause_selection = 0;
	private static int score = 0;
	private static int score_multiplier;
	private static int score_add = 0;
	private static int speed_game = 30; //lower value has more speed // 16 gameSpeed draws 60 fps
	private static int speed_down = 30;
	
	
	private static boolean left;
	private static boolean right;
	private static boolean down;
	private static boolean up;
	private static boolean enter = false;
	private static boolean directDown = false;
	private static boolean pause = false;
	private static boolean pause_apply = false;
	private static boolean started = false; //record game started or not
	private static boolean endGame = false;
	private static boolean restartGame = false;
	private static boolean rotate_available = true;
	private static boolean col_right_exists = false;
	private static boolean col_left_exists = false;
	private static boolean col_bot_exists = false;
	private static boolean highscore_file_exists = true;
	private static boolean record_done = false;
	
	private static final String font_type = "Tahoma";
	private static final String high_score_file_name = "tetris.highscoredata";
	private static String user_name = "";
	private static String game_state = "loading";
    private static String[][] shape_codes = 
		{
			/* 0. --> I*/		{"AXXXAXXXAXXXAXXX","AAAAXXXXXXXXXXXX"},  
			/* 1. --> L*/		{"AXXXAXXXAAXXXXXX","AAAXAXXXXXXXXXXX","AAXXXAXXXAXXXXXX","XXAXAAAXXXXXXXXX"},
			/* 2. --> J*/		{"XAXXXAXXAAXXXXXX","AXXXAAAXXXXXXXXX","AAXXAXXXAXXXXXXX","AAAXXXAXXXXXXXXX"},
			/* 3. --> O*/		{"AAXXAAXXXXXXXXXX"},
			/* 4. --> T*/		{"XAXXAAAXXXXXXXXX","AXXXAAXXAXXXXXXX","AAAXXAXXXXXXXXXX","XAXXAAXXXAXXXXXX"},
			/* 5. --> S*/		{"XAAXAAXXXXXXXXXX","AXXXAAXXXAXXXXXX"},
			/* 6. --> Z*/		{"AAXXXAAXXXXXXXXX","XAXXAAXXAXXXXXXX"}
		};
	private static Image view_welcome_image;
	private static Image view_intro_image;
	private static Image view_bg_image;
	
	private static Calendar game_startedTimeStamp = Calendar.getInstance();
	private static Calendar score_addTimeStamp;
	
	static Color bg_color = new Color(190, 247, 236);
	
	//getter methods
	protected static JFrame get_frame() { return frame;}
	protected static DrawPanel get_drawPanel() { return drawPanel;}
	protected static gameComponents get_my_tetris() { return my_tetris;}
	
	protected static final int get_s_WIDTH() { return s_WIDTH;}
	protected static final int get_s_HEIGHT() { return s_HEIGHT;}
	protected static final int get_gw_WIDTH() { return gw_WIDTH;}
	protected static final int get_gw_HEIGHT() { return gw_HEIGHT;}
	protected static final int get_sleep_time() { return sleep_time;}
	
	protected static int get_shape_cnt() { return shape_cnt;}
	protected static int get_frameCounter() { return frameCounter;}
	protected static int get_frameCounter_left() { return frameCounter_left;}
	protected static int get_frameCounter_right() { return frameCounter_right;}
	protected static int get_frameCounter_collision() { return frameCounter_collision;}
	protected static int get_frameCounter_collision_bot() { return frameCounter_collision_bot;}
	protected static int get_pause_selection() { return pause_selection;}
	protected static int get_score() { return score;}
	protected static int get_score_multiplier() { return score_multiplier;}
	protected static int get_score_add() { return score_add;}
	protected static int get_speed_down() { return speed_down;}
	protected static int get_speed_game() { return speed_game;}
	
	protected static boolean get_left() { return left;}
	protected static boolean get_right() { return right;}
	protected static boolean get_down() { return down;}
	protected static boolean get_up() { return up;}	
	protected static boolean get_enter() { return enter;}	
	protected static boolean get_directDown() { return directDown;}
	protected static boolean get_pause() { return pause;}
	protected static boolean get_pause_apply() { return pause_apply;}
	protected static boolean get_started() { return started;}
	protected static boolean get_endGame() { return endGame;}
	protected static boolean get_restartGame() { return restartGame;}
	protected static boolean get_rotate_available() { return rotate_available;}
	protected static boolean get_col_right_exists() { return col_right_exists;}
	protected static boolean get_col_left_exists() { return col_left_exists;}
	protected static boolean get_col_bot_exists() { return col_bot_exists;}
	protected static boolean get_highscore_file_exists() { return highscore_file_exists;}
	protected static boolean get_record_done() { return record_done;}
	
	protected static String get_font_type() { return font_type;}
	protected static String get_user_name() { return user_name;}
	protected static String get_game_state() { return game_state;}
	protected static String get_high_score_file_name() { return high_score_file_name;}
	protected static String[][] get_shape_codes() { return shape_codes;}
	
	protected static Image get_view_welcome_image() { return view_welcome_image;}
	protected static Image get_view_intro_image() { return view_intro_image;}
	protected static Image get_view_bg_image() { return view_bg_image;}
	
	protected static Calendar get_game_startedTimeStamp() { return game_startedTimeStamp;}
	protected static Calendar get_score_addTimeStamp() { return score_addTimeStamp;}
	
	protected static Color get_bg_color() { return bg_color; }
	//setter methods
	protected static void set_frame(JFrame p_frame) { frame = p_frame;}
	protected static void set_drawPanel(DrawPanel p_drawPanel) { drawPanel = p_drawPanel;}
	protected static void set_my_tetris(gameComponents p_gameComponents) { my_tetris = p_gameComponents;}
	
	protected static void set_shape_cnt(int p_shape_cnt) {shape_cnt = p_shape_cnt;}
	protected static void set_pause_selection(int p_pause_selection) { pause_selection = p_pause_selection;}
	protected static void set_frameCounter(int p_frameCounter) { frameCounter = p_frameCounter;}
	protected static void set_frameCounter_left(int p_frameCounter_left) { frameCounter_left = p_frameCounter_left;}
	protected static void set_frameCounter_right(int p_frameCounter_right) { frameCounter_right = p_frameCounter_right;}
	protected static void set_frameCounter_collision(int p_frameCounter_collision) { frameCounter_collision = p_frameCounter_collision;}
	protected static void set_frameCounter_collision_bot(int p_frameCounter_collision_bot) { frameCounter_collision_bot = p_frameCounter_collision_bot;}
	protected static void set_score(int p_score) { score = p_score;}
	protected static void set_score_multiplier(int p_score_multiplier) { score_multiplier = p_score_multiplier;}
	protected static void set_score_add(int p_score_add) { score_add = p_score_add;}
	protected static void set_speed_game(int p_speed_game) { speed_game = p_speed_game;}
	protected static void set_speed_down(int p_speed_down) { speed_down = p_speed_down;}
	
	protected static void set_left(boolean p_left) { left = p_left;}
	protected static void set_right(boolean p_right) { right = p_right;}
	protected static void set_down(boolean p_down) { down = p_down;}
	protected static void set_up(boolean p_up) { up = p_up;}
	protected static void set_enter(boolean p_enter) { enter = p_enter;}
	protected static void set_directDown(boolean p_directDown) { directDown = p_directDown;}
	protected static void set_pause(boolean p_pause) { pause = p_pause;}
	protected static void set_pause_apply(boolean p_pause_apply) { pause_apply = p_pause_apply;}
	protected static void set_started(boolean p_started) { started = p_started;}
	protected static void set_endGame(boolean p_endGame) { endGame = p_endGame;}
	protected static void set_restartGame(boolean p_restartGame) { restartGame = p_restartGame;}
	protected static void set_rotate_available(boolean p_rotate_available) { rotate_available = p_rotate_available;}
	protected static void set_col_right_exists(boolean p_col_right_exists) { col_right_exists = p_col_right_exists;}
	protected static void set_col_left_exists(boolean p_col_left_exists) { col_left_exists = p_col_left_exists;}
	protected static void set_col_bot_exists(boolean p_col_bot_exists) { col_bot_exists = p_col_bot_exists;}
	protected static void set_highscore_file_exists(boolean p_highscore_file_exists) { highscore_file_exists = p_highscore_file_exists;}
	protected static void set_record_done(boolean p_record_done) { record_done = p_record_done;}
	
	protected static void set_game_state(String p_state) { game_state = p_state; }
	protected static void set_user_name(String p_user_name) { user_name = p_user_name; }
	/**
	 * Game States : welcome, running, paused , end, exit , loading , highscore
	 */
    protected static void set_view_welcome_image(Image p_image) { view_welcome_image = p_image;}
    protected static void set_view_intro_image(Image p_image) { view_intro_image = p_image;}
    protected static void set_view_bg_image(Image p_image) { view_bg_image = p_image;}
    
    protected static void set_game_startedTimeStamp(Calendar p_Date) { game_startedTimeStamp = p_Date;}
    protected static void set_score_addTimeStamp(Calendar p_Date) { score_addTimeStamp = p_Date;}
	
    //increase methods
    protected static void inc_frameCounter_collision_bot() { frameCounter_collision_bot++; }
    
    
    
    protected static void reset() {
    	shape_cnt = 0;
    	frameCounter_left = 0;
    	frameCounter_right = 0;
    	frameCounter_collision = 0;
    	frameCounter_collision_bot = 0;
    	frameCounter = 0;
    	pause_selection = 0;
    	
    	left = false;
    	right = false;
    	down = false;
    	up = false;
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

    	game_state = "loading";
    	score = 0;
    	speed_game = 30; //lower value has more speed // 16 gameSpeed draws 60 fps
    	speed_down = 30;
    }
    
    
    
    
    
	
	
	
	
	
	
	
	
}
