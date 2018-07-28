package tetris;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

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
    private static final  int gw_HEIGHT = 800;
    
	private static int shape_cnt = 0;
	private static int frameCounter_left = 0;
	private static int frameCounter_right = 0;
	private static int frameCounter_collision = 0;
	private static int frameCounter_collision_bot = 0;
	private static int gameSpeed = 16; //lower value has more speed // 16 gameSpeed draws 60 fps
	private static int frameCounter = 0;
	private static int pause_selection = 0;
	
	private static boolean left;
	private static boolean right;
	private static boolean down;
	private static boolean pause = false;
	private static boolean pause_apply = false;
	private static boolean started = false; //record game started or not
	private static boolean endGame = false;
	private static boolean restartGame = false;
	private static boolean rotate_available = true;
	private static boolean col_right_exists = false;
	private static boolean col_left_exists = false;
	private static boolean col_bot_exists = false;
	
	private static final String font_type = "Tahoma";
	private static String game_state = "welcome";
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

	static BufferedImage view_welcome_image;
	
	//getter methods
	protected static JFrame get_frame() { return frame;}
	protected static DrawPanel get_drawPanel() { return drawPanel;}
	protected static gameComponents get_my_tetris() { return my_tetris;}
	
	protected static final int get_s_WIDTH() { return s_WIDTH;}
	protected static final int get_s_HEIGHT() { return s_HEIGHT;}
	protected static final int get_gw_WIDTH() { return gw_WIDTH;}
	protected static final int get_gw_HEIGHT() { return gw_HEIGHT;}
	
	protected static int get_shape_cnt() { return shape_cnt;}
	protected static int get_frameCounter() { return frameCounter;}
	protected static int get_frameCounter_left() { return frameCounter_left;}
	protected static int get_frameCounter_right() { return frameCounter_right;}
	protected static int get_frameCounter_collision() { return frameCounter_collision;}
	protected static int get_frameCounter_collision_bot() { return frameCounter_collision_bot;}
	protected static int get_gameSpeed() { return gameSpeed;}
	protected static int get_pause_selection() { return pause_selection;}
	
	protected static boolean get_left() { return left;}
	protected static boolean get_right() { return right;}
	protected static boolean get_down() { return down;}
	protected static boolean get_pause() { return pause;}
	protected static boolean get_pause_apply() { return pause_apply;}
	protected static boolean get_started() { return started;}
	protected static boolean get_endGame() { return endGame;}
	protected static boolean get_restartGame() { return restartGame;}
	protected static boolean get_rotate_available() { return rotate_available;}
	protected static boolean get_col_right_exists() { return col_right_exists;}
	protected static boolean get_col_left_exists() { return col_left_exists;}
	protected static boolean get_col_bot_exists() { return col_bot_exists;}
	
	protected static String get_font_type() { return font_type;}
	protected static String get_game_state() { return game_state;}
	protected static String[][] get_shape_codes() { return shape_codes;}
	
	protected static BufferedImage get_view_welcome_image() { return view_welcome_image;}
	
	
	//setter methods
	protected static void set_frame(JFrame p_frame) { frame = p_frame;}
	protected static void set_drawPanel(DrawPanel p_drawPanel) { drawPanel = p_drawPanel;}
	protected static void set_my_tetris(gameComponents p_gameComponents) { my_tetris = p_gameComponents;}
	
	protected static void set_shape_cnt(int p_shape_cnt) {shape_cnt = p_shape_cnt;}
	protected static void set_gameSpeed(int p_gameSpeed) { gameSpeed = p_gameSpeed;}
	protected static void set_pause_selection(int p_pause_selection) { pause_selection = p_pause_selection;}
	protected static void set_frameCounter(int p_frameCounter) { frameCounter = p_frameCounter;}
	protected static void set_frameCounter_left(int p_frameCounter_left) { frameCounter_left = p_frameCounter_left;}
	protected static void set_frameCounter_right(int p_frameCounter_right) { frameCounter_right = p_frameCounter_right;}
	protected static void set_frameCounter_collision(int p_frameCounter_collision) { frameCounter_collision = p_frameCounter_collision;}
	protected static void set_frameCounter_collision_bot(int p_frameCounter_collision_bot) { frameCounter_collision_bot = p_frameCounter_collision_bot;}
	
	protected static void set_left(boolean p_left) { left = p_left;}
	protected static void set_right(boolean p_right) { right = p_right;}
	protected static void set_down(boolean p_down) { down = p_down;}
	protected static void set_pause(boolean p_pause) { pause = p_pause;}
	protected static void set_pause_apply(boolean p_pause_apply) { pause_apply = p_pause_apply;}
	protected static void set_started(boolean p_started) { started = p_started;}
	protected static void set_endGame(boolean p_endGame) { endGame = p_endGame;}
	protected static void set_restartGame(boolean p_restartGame) { restartGame = p_restartGame;}
	protected static void set_rotate_available(boolean p_rotate_available) { rotate_available = p_rotate_available;}
	protected static void set_col_right_exists(boolean p_col_right_exists) { col_right_exists = p_col_right_exists;}
	protected static void set_col_left_exists(boolean p_col_left_exists) { col_left_exists = p_col_left_exists;}
	protected static void set_col_bot_exists(boolean p_col_bot_exists) { col_bot_exists = p_col_bot_exists;}
	
	protected static void set_game_state(String p_state) { game_state = p_state; }
	/**
	 * Game States : welcome, running, paused , end, exit
	 */
    protected static void set_view_welcome_image(BufferedImage p_image) { view_welcome_image = p_image;}
	
    //increase methods
    protected static void inc_frameCounter_collision_bot() { frameCounter_collision_bot++; }
    
    
    
    protected static void reset() {
    	shape_cnt = 0;
    	frameCounter_left = 0;
    	frameCounter_right = 0;
    	frameCounter_collision = 0;
    	frameCounter_collision_bot = 0;
    	gameSpeed = 16; //lower value has more speed // 16 gameSpeed draws 60 fps
    	frameCounter = 0;
    	pause_selection = 0;
    	
    	left = false;
    	right = false;
    	down = false;
    	pause = false;
    	pause_apply = false;
    	started = false;
    	endGame = false;
    	restartGame = false;
    	rotate_available = true;
    	col_right_exists = false;
    	col_left_exists = false;
    	col_bot_exists = false;

    	game_state = "welcome";
    }
    
    
    
    
    
	
	
	
	
	
	
	
	
}
