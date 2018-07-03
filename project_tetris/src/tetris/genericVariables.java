package tetris;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;


import tetris.Tetris.DrawPanel;


class genericVariables {
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
	private static boolean pause = false;
	private static boolean pause_apply = false;
	private static boolean started = false; //record game started or not
	private static boolean top = true;
	private static boolean rotate_available = true;
	private static boolean go_right = false;
	private static boolean col_right_exists = false;
	private static boolean col_left_exists = false;
	private static boolean col_bot_exists = false;
	
	private static final String font_type = "Tahoma";
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
	protected static boolean get_pause() { return pause;}
	protected static boolean get_pause_apply() { return pause_apply;}
	protected static boolean get_started() { return started;}
	protected static boolean get_top() { return top;}
	protected static boolean get_rotate_available() { return rotate_available;}
	protected static boolean get_go_right() { return go_right;}
	protected static boolean get_col_right_exists() { return col_right_exists;}
	protected static boolean get_col_left_exists() { return col_left_exists;}
	protected static boolean get_col_bot_exists() { return col_bot_exists;}
	
	protected static String get_font_type() { return font_type;}
	protected static String[][] get_shape_codes() { return shape_codes;}
	
	
	//setter methods
	protected static void set_frame(JFrame p_frame) { frame = p_frame;}
	protected static void set_drawPanel(DrawPanel p_drawPanel) { drawPanel = p_drawPanel;}
	
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
	protected static void set_pause(boolean p_pause) { pause = p_pause;}
	protected static void set_pause_apply(boolean p_pause_apply) { pause_apply = p_pause_apply;}
	protected static void set_started(boolean p_started) { started = p_started;}
	protected static void set_rotate_available(boolean p_rotate_available) { rotate_available = p_rotate_available;}
	protected static void set_col_right_exists(boolean p_col_right_exists) { col_right_exists = p_col_right_exists;}
	protected static void set_col_left_exists(boolean p_col_left_exists) { col_left_exists = p_col_left_exists;}
	protected static void set_col_bot_exists(boolean p_col_bot_exists) { col_bot_exists = p_col_bot_exists;}
	
    
    //check for exploding lines
    private static void check_exploding_line() {
    	my_tetris.reset_explode_lines();
		shape sh;
		box bx;
		int line_no;
		for(int i = 0; i < my_tetris.get_all_shapes().size() ; i++) {
			if(my_tetris.get_all_shapes().get(i) != null) {
				sh = my_tetris.get_all_shapes().get(i);
				for(int k = 0 ; k < 4 ; k++) {
					if(sh.sh_boxes.get(k) != null) {
						bx = sh.sh_boxes.get(k);
						line_no = (bx.get_box_y() / 25) - 3;
						my_tetris.get_explode_lines()[line_no]++;
						System.out.println("line["+(line_no+3)+"] : " + my_tetris.get_explode_lines()[line_no]);
						if(my_tetris.get_explode_lines()[line_no] == 19) {
							explode_line(line_no);
						}
					}
				}
			}
		}
    }
    
    //explode lines after checked
    private static void explode_line(int line_number) {
    	System.out.println("Process : Delete Line " + (line_number+3));
    	int row_y = (line_number+3) * 25;
		shape sh;
    	box bx;
    	int size = my_tetris.get_all_shapes().size();
    	
    	int counter = 0;
    	for(int i = 0 ; i < size ; i++) {
    		if(my_tetris.get_all_shapes().get(i) != null)
    		{
    			sh = my_tetris.get_all_shapes().get(i);
        		for(int k = 0 ; k < 4 ; k++) {
        			if(sh.sh_boxes.get(k) != null)
        			{
        				bx = sh.sh_boxes.get(k);
            			if(row_y == bx.get_box_y()) {
            				sh.sh_boxes.setElementAt(null, k);
            				counter++;
            			}
        			}
        		}
    		}
    		
    	}
    	System.out.println("counter = " + counter);
    	if(counter == 19) {
    		my_tetris.get_explode_lines()[line_number] = 0;
    		drop_upper_boxes(line_number);
    	}
    		
    }
    //drops bottom-free boxes on upper lines after explosion
    private static void drop_upper_boxes(int line_number) {
    	int drop_y = (line_number+3)*25;
    	shape sh;
    	box bx;
    	for(int n = 1 ; n < 25 ; n++) {
    		for(int i = 0 ; i < my_tetris.get_all_shapes().size();i++) {
        		if(my_tetris.get_all_shapes().get(i) != null) {
        			sh = my_tetris.get_all_shapes().get(i);
        			for(int k = 0; k < 4 ; k++) {
        				if(sh.sh_boxes.get(k) != null) {
        					bx = sh.sh_boxes.get(k);
        					if(bx.get_box_y() == drop_y-(n*25)) {
        						 if(check_box_bottom_free(bx)) {
        							 bx.set_box_y(bx.get_box_y()+25);
        							 k--;
        						 }
        					}
        				}
        			}
        		}
        	}
    	}
    }
    
    private static boolean check_box_bottom_free(box checkboxbottomfree) {
    	shape sh;
    	box bx;
    	for(int i = 0 ; i < my_tetris.get_all_shapes().size();i++) {
    		if(my_tetris.get_all_shapes().get(i) != null) {
    			sh = my_tetris.get_all_shapes().get(i);
    			for(int k = 0; k < 4 ; k++) {
    				if(sh.sh_boxes.get(k) != null) {
    					bx = sh.sh_boxes.get(k);
    					if(checkboxbottomfree.get_box_x() == bx.get_box_x()) {
    						if(checkboxbottomfree.get_box_y()+25 != bx.get_box_y() && checkboxbottomfree.get_box_y()+25 != my_tetris.get_bottom_border()) {
								return true;
    						}
    					}
    				}
    			}
    		}
		}
    	return false;
    }
    
    
	protected static void generate_a_new_shape() {
		//this block written to generate a shape and add it to vector<shape> all_shapes
		if(my_tetris.get_all_shapes().size() > 0)check_exploding_line();
		String code = null;
		String s_type = null;
		int s_type_no;
		int rot_no = 0; //rotation no , initially 0
		Random rndm = new Random();
		int rndm_no = rndm.nextInt(7);
		s_type_no = rndm_no;
		code = shape_codes[rndm_no][rot_no];
		
		if(rndm_no == 0) s_type = "I";
			else if(rndm_no == 1) s_type = "L";
			else if(rndm_no == 2) s_type = "J";
			else if(rndm_no == 3) s_type = "O";
			else if(rndm_no == 4) s_type = "T";
			else if(rndm_no == 5) s_type = "S";
			else if(rndm_no == 6) s_type = "Z";
		else s_type = "X";
		

    	my_tetris.newShape(stringtoShape(code,s_type,s_type_no,rot_no,15*25,0));
    	my_tetris.set_call_new_shape(false);
    	
    }
	
	protected static shape stringtoShape(String code,String s_type,int s_type_no,int rot_no,int x,int y) {
		int draw_x = x; //initialvalue : 15*25;
    	int draw_y = y;	//initialvalue : 0;
    	
    	shape new_generated_shape = new shape(draw_x,draw_y,s_type,s_type_no,rot_no,code);
    	for(int i = 0 ; i < 16 ; i++) {
    		char ch_at = code.charAt(i);
    		if(i % 4 == 0) {
    			draw_y += 25;
    			draw_x -= 100;
    		}
    		if(ch_at == 'A') {
    			new_generated_shape.add_box(new box(draw_x,draw_y,draw_y+25,draw_x+25));
    			new_generated_shape.set_shape_loc_X(draw_x);
    			new_generated_shape.set_shape_loc_Y(draw_y);
			}
    		draw_x += 25;
    	}
    	new_generated_shape.set_shape_active(true);
    	return new_generated_shape;
	}
	
	
	protected static boolean checkcollisions(shape checkforshape) {
		top = true;
		col_right_exists = false;
		col_left_exists = false;
		col_bot_exists = false;
    	int active_bottom;
		int active_x;
		int active_y;
		int active_right_end;
		
    	//checks for bottom border.
    	for(int i = 0 ; i < 4 ; i++) {
    		if(checkforshape.sh_boxes.get(i).get_box_bottom_end() >= my_tetris.get_bottom_border()) {
    			if(checkforshape.get_shape_active()) {
        			col_bot_exists = true;
    				break;
    			}
    			else
    				return false;
    		}
    	}
    	
    	//checks for top border
    	for(int i = 0 ; i < 4 ; i++) {
    		if(checkforshape.sh_boxes.get(i).get_box_y() < my_tetris.get_top_border()) {
    			top = false;
    		}
    	}

    	//check right and main border with each boxes
    	if(checkforshape.get_shape_active()) {
    		for(int i = 0 ; i < 4 ; i++) {
        		//check for just horizontal matching according to left
            	active_x = checkforshape.sh_boxes.get(i).get_box_x();
        		if(active_x == my_tetris.get_left_border()+25) {
        			left = false;
        		}
        		//check for just horizontal matching according to right
        		active_right_end = checkforshape.sh_boxes.get(i).get_box_right_end();
        		if(active_right_end == my_tetris.get_right_border()) {
        			right = false;
        		}
        	}
    	}
    	
    	
    	//check right and left main borders with shape start & end locations for rotatable or not
		checkforshape.calc_shape_start_end_loc();
		if(checkforshape.get_shape_start_loc_X() <= my_tetris.get_left_border()) {
			col_left_exists = true;
		}
		if(checkforshape.get_shape_end_loc_X() >= my_tetris.get_right_border()) {
			col_right_exists = true;
		}
    	
		for(int i = 0 ; i < my_tetris.get_all_shapes().size()-1 ; i++) {
    		if(my_tetris.get_all_shapes().get(i) != null) {

        		for(int k = 0 ; k < 4 ; k++) {//this loop selects active shape boxes
        			active_bottom = checkforshape.sh_boxes.get(k).get_box_bottom_end();
        			active_x = checkforshape.sh_boxes.get(k).get_box_x();
        			active_y = checkforshape.sh_boxes.get(k).get_box_y();
        			active_right_end = checkforshape.sh_boxes.get(k).get_box_right_end();
        			
        			//this loop checking selected active shape box with boxes of all passive shapes
        			for(int t = 0 ; t < 4 ; t++) { 
        				if(my_tetris.get_all_shapes().get(i).sh_boxes.get(t) != null) {
            				int passive_x = my_tetris.get_all_shapes().get(i).sh_boxes.get(t).get_box_x();
            				int passive_right_end = my_tetris.get_all_shapes().get(i).sh_boxes.get(t).get_box_right_end();
            				int passive_y = my_tetris.get_all_shapes().get(i).sh_boxes.get(t).get_box_y();
            				@SuppressWarnings("unused")
							int passive_bottom = my_tetris.get_all_shapes().get(i).sh_boxes.get(t).get_box_bottom_end();
            				//check horizontal and vertical matching
            				if(active_bottom == passive_y && active_x == passive_x) {
                    			col_bot_exists = true;
            				}
            				//check for just horizontal matching according to right
            				if(right || !checkforshape.get_shape_active()) {
            					if(active_y == passive_y) {
            						if(active_right_end == passive_x) {
            							right = false;
                    					col_right_exists = true;
            						}
            					}else if(active_bottom == passive_y) {
            						if(active_right_end == passive_y) {
            							right = false;
                    					col_right_exists = true;
            						}
            					}
            				}
            				//check for just horizontal matching according to left
            				if(left || !checkforshape.get_shape_active()) {
            					if(active_y == passive_y) {
            						if(active_x == passive_right_end) {
            							left = false;
                    					col_left_exists = true;
            						}
            					}else if(active_bottom == passive_y) {
            						if(active_x == passive_right_end) {
            							left = false;
                    					col_left_exists = true;
            						}
            					}
            				}
            				//check if there no place for new shape and finish the game
            				if(active_bottom > passive_y && active_bottom < 4*25) {
            					top = false;
            				}
        				}
        			}
        		}
    		}
    	}

    	if(col_bot_exists) frameCounter_collision_bot++;
    	else frameCounter_collision_bot = 0;

    	if(!col_left_exists && !col_right_exists && !checkforshape.get_shape_active()) return true;
    	else return false;
    			
    }
	
	//gets keyboard input
    class ActionListener extends KeyAdapter{
    	
        
    	public void keyPressed(KeyEvent e){
    		int key = e.getKeyCode();
    		
    		if(!genericVariables.get_started()) {
    			if(key == KeyEvent.VK_ENTER)
    				genericVariables.set_started(true);
    		}
    		
    		if(genericVariables.get_pause()) {
    			if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP) {
        			if(genericVariables.get_pause_selection() == 0) genericVariables.set_pause_selection(genericVariables.get_pause_selection()+1);
        			else genericVariables.set_pause_selection(genericVariables.get_pause_selection()-1);
        		}
    			if(key == KeyEvent.VK_ENTER)
    				genericVariables.set_pause_apply(true);
    		}
    		//started and continues game input
    		if(genericVariables.get_started()) {
    			if(!genericVariables.get_pause()) {
    				//pause game
            		if(key == KeyEvent.VK_P) {
            			if(genericVariables.get_pause()) genericVariables.set_pause(false);
            			else genericVariables.set_pause(true);
            		}
            		//rotate shape
            		if(key == KeyEvent.VK_SPACE && genericVariables.get_rotate_available()) {
            			genericVariables.get_my_tetris().rotate_shape();
            			genericVariables.set_rotate_available(false);
            		}
            		if(key == KeyEvent.VK_DOWN) {
            			genericVariables.set_gameSpeed(1);
            		}
            		//move shape
                	if(key == KeyEvent.VK_RIGHT) {
                		genericVariables.set_frameCounter_right(genericVariables.get_frameCounter_right()+1);
                		genericVariables.set_left(false);
                		genericVariables.set_right(true);
                	}
                	else if(key == KeyEvent.VK_LEFT) {
                		genericVariables.set_frameCounter_left(genericVariables.get_frameCounter_left()+1);
                		genericVariables.set_right(false);
                		genericVariables.set_left(true);
                	}
                	else {
                		genericVariables.set_right(false);
                		genericVariables.set_left(false);
                	}
    			}
    		}
        }
    	public void keyReleased(KeyEvent e){
    		int key = e.getKeyCode();
    		//started and continues game input
    		if(genericVariables.get_started()) {
    			if(!genericVariables.get_pause()) {
    	    		if(key == KeyEvent.VK_RIGHT && genericVariables.get_right()) {
    	    			genericVariables.set_frameCounter_right(0);
                		genericVariables.set_right(false);
    	    		}
    	    		else if(key == KeyEvent.VK_LEFT && genericVariables.get_left()) {
    	    			genericVariables.set_frameCounter_left(0);
    	    			genericVariables.set_left(false);
    	    		}
            		if(key == KeyEvent.VK_DOWN) {
            			genericVariables.set_gameSpeed(16);
            		}
            		if(key == KeyEvent.VK_SPACE && !genericVariables.get_rotate_available()) {
            			genericVariables.set_rotate_available(true);
            		}
    			}
    		}
    	}
    }
}
