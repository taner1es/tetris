package tetris;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;


import tetris.Tetris.DrawPanel;


public class genericVariables {
	JFrame frame;
    DrawPanel drawPanel;
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    
    /*
     * s_ : screen original width or height value.
     * gw_ : game window assigned by myself.
     */
    final int s_WIDTH = gd.getDisplayMode().getWidth();
    final int s_HEIGHT = gd.getDisplayMode().getHeight();
    final int gw_WIDTH = 900; 
    final int gw_HEIGHT = 800;
	static int shape_cnt = 0;
	static Vector<String> gameLog = new Vector<String>(0);
	static gameComponents my_tetris = new gameComponents();
	public static boolean left;
	public static boolean right;
	public int gameSpeed = 500; //lower value has more speed
	boolean pause = false;
	public int pause_selection = 0;
	public boolean pause_apply = false;
	boolean started = false; //record game started or not

	static boolean top = true;
	boolean rotate_available = true;
	
    //Key Events
    String key_pressed = null;
	
    //shapes
    static String[][] shape_codes = 
		{
			/* 0. --> I*/		{"AXXXAXXXAXXXAXXX","AAAAXXXXXXXXXXXX"},  
			/* 1. --> L*/		{"AXXXAXXXAAXXXXXX","AAAXAXXXXXXXXXXX","AAXXXAXXXAXXXXXX","XXAXAAAXXXXXXXXX"},
			/* 2. --> J*/		{"XAXXXAXXAAXXXXXX","AXXXAAAXXXXXXXXX","AAXXAXXXAXXXXXXX","AAAXXXAXXXXXXXXX"},
			/* 3. --> O*/		{"AAXXAAXXXXXXXXXX"},
			/* 4. --> T*/		{"XAXXAAAXXXXXXXXX","AXXXAAXXAXXXXXXX","AAAXXAXXXXXXXXXX","XAXXAAXXXAXXXXXX"},
			/* 5. --> S*/		{"XAAXAAXXXXXXXXXX","AXXXAAXXXAXXXXXX"},
			/* 6. --> Z*/		{"AAXXXAAXXXXXXXXX","XAXXAAXXAXXXXXXX"}
		};
    
	public static void generate_a_new_shape() {
		//this block written to generate a shape and add it to vector<shape> all_shapes
	
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
	
	public static shape stringtoShape(String code,String s_type,int s_type_no,int rot_no,int x,int y) {
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
	
	
	public static boolean checkcollisions(shape checkforshape) {
		top = true;
		boolean col_right_exists = false;
		boolean col_left_exists = false;
    	int active_bottom;
		int active_x;
		int active_y;
		int active_right_end;
		
    	shape active = my_tetris.all_shapes.lastElement();
    	//checks for bottom border.
    	for(int i = 0 ; i < 4 ; i++) {
    		active_bottom = active.sh_boxes.get(i).bottom_end;
    		if(active_bottom >= my_tetris.bottom_border) {
    			active.set_shape_active(false);
    		}
    	}
    	
    	//checks for top border
    	for(int i = 0 ; i < 4 ; i++) {
    		int active_top = active.sh_boxes.get(i).y;
    		if(active_top < my_tetris.top_border) {
    			top = false;
    		}
    	}

		//check for just horizontal matching according to left
    	active_x = active.loc_x;
		if(active_x == my_tetris.right_border-25) {
			right = false;
		}
		//check for just horizontal matching according to left
		active_right_end = active.loc_x;
		if(active_right_end == my_tetris.left_border+25) {
			left = false;
		}
		//System.out.println("size " + my_tetris.all_shapes.size());
    	for(int i = 0 ; i < my_tetris.all_shapes.size()-1 ; i++) {
    		for(int k = 0 ; k < 4 ; k++) {//this loop checks for active to game borders
    			active_bottom = active.sh_boxes.get(k).bottom_end;
    			active_x = active.sh_boxes.get(k).x;
    			active_y = active.sh_boxes.get(k).y;
    			active_right_end = active.sh_boxes.get(k).right_end;
    			for(int t = 0 ; t < 4 ; t++) { //this loop checking with passive shapes
    				int passive_top = my_tetris.all_shapes.get(i).sh_boxes.get(t).y;
    				int passive_x = my_tetris.all_shapes.get(i).sh_boxes.get(t).x;
    				int passive_right_end = my_tetris.all_shapes.get(i).sh_boxes.get(t).right_end;
    				int passive_y = my_tetris.all_shapes.get(i).sh_boxes.get(t).y;
    				//check horizontal and vertical matching
    				if(active_bottom == passive_top && active_x == passive_x) {
    					active.set_shape_active(false);
    				}
    				//check for just horizontal matching according to right
    				if( (active_right_end == passive_x && active_y == passive_y) || (active_right_end == passive_x && active_y+25 == passive_y)) {
    					right = false;
    					col_right_exists = true;
    				}
    				//check for just horizontal matching according to left
    				if( (active_x == passive_right_end && active_y == passive_y) || (active_x == passive_right_end && active_y+25 == passive_y)) {
    					left = false;
    					col_left_exists = true;
    				}
    				//check if there no place for new shape and finish the game
    				if(active_bottom > passive_top && active_bottom < 4*25) {
    					//top = false;
    				}
    			}
    		}
    	}
    	System.out.println("col_left_exists , col_right_exists , active.active  : " +col_left_exists + col_right_exists + active.active);
    	if(!col_left_exists && !col_right_exists && active.active) return true;
    	else return false;
    			
    }
	
}
