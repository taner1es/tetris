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
	public boolean left;
	public boolean right;
	public int gameSpeed = 50; //lower value has more speed
	boolean pause = false;
	public int pause_selection = 0;
	public boolean pause_apply = false;
	boolean started = false; //record game started or not
	
    //Key Events
    String key_pressed = null;
	
	public static void generate_a_new_shape() {
		//this block written to generate a shape and add it to vector<shape> all_shapes
		//TODO : All shapes exists with first position. Add rotated shapes.
		String[][] shape_codes = 
			{
				/* 0. --> I*/		{"AXXXAXXXAXXXAXXX","AAAAXXXXXXXXXXXX"},  
				/* 1. --> L*/		{"AXXXAXXXAAXXXXXX","AAAXAXXXXXXXXXXX","AAXXXAXXXAXXXXXX","XXAXAAAXXXXXXXXX"},
				/* 2. --> J*/		{"XAXXXAXXAAXXXXXX","AXXXAAAXXXXXXXXX","AAXXAXXXAXXXXXXX","AAAXXXAXXXXXXXXX"},
				/* 3. --> O*/		{"AAXXAAXXXXXXXXXX"},
				/* 4. --> T*/		{"XAXXAAAXXXXXXXXX","AXXXAAXXAXXXXXXX","AAAXXAXXXXXXXXXX","XAXXAAXXXAXXXXXX"},
				/* 5. --> S*/		{"XAAXAAXXXXXXXXXX","AXXXAAXXXAXXXXXX"},
				/* 6. --> Z*/		{"AAXXXAAXXXXXXXXX","XAXXAAXXAXXXXXXX"}
			};
		String code = null;
		String s_type = null;
		Random rndm = new Random();
		int rndm_no = rndm.nextInt(7);
		code = shape_codes[rndm_no][0];
		
		if(rndm_no == 0) s_type = "I";
			else if(rndm_no == 1) s_type = "L";
			else if(rndm_no == 2) s_type = "J";
			else if(rndm_no == 3) s_type = "O";
			else if(rndm_no == 4) s_type = "T";
			else if(rndm_no == 5) s_type = "S";
			else if(rndm_no == 6) s_type = "Z";
		else s_type = "X";
		
		
		
    	int draw_x = 15*25;
    	int draw_y = 0*25;
    	
    	shape new_generated_shape = new shape(draw_x,draw_y,s_type,code);
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
    	my_tetris.newShape(new_generated_shape);
    	my_tetris.set_call_new_shape(false);
    	System.out.println("!! --> New Shape Generated");
    }
	
	
	
	
}
