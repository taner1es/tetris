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
	public int gameSpeed = 16; //lower value has more speed // 16 gameSpeed draws 60 fps 
	boolean pause = false;
	public int pause_selection = 0;
	public boolean pause_apply = false;
	boolean started = false; //record game started or not
	public int frameCounter = 0;
	public int frameCounter_left = 0;
	public int frameCounter_right = 0;
	public int frameCounter_collision = 0;
	public static int frameCounter_collision_bot = 0;

	static boolean top = true;
	static boolean rotate_available = true;
	boolean go_right = false;
	
	static boolean col_right_exists = false;
	static boolean col_left_exists = false;
	static boolean col_bot_exists = false;
	
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
    
    //check for exploding lines
    public static void check_exploding_line() {
    	my_tetris.reset_explode_lines();
		shape sh;
		box bx;
		int line_no;
		for(int i = 0; i < my_tetris.all_shapes.size() ; i++) {
			if(my_tetris.all_shapes.get(i) != null) {
				sh = my_tetris.all_shapes.get(i);
				for(int k = 0 ; k < 4 ; k++) {
					if(sh.sh_boxes.get(k) != null) {
						bx = sh.sh_boxes.get(k);
						line_no = (bx.get_box_y() / 25) - 3;
						my_tetris.explode_lines[line_no]++;
						System.out.println("line["+(line_no+3)+"] : " + my_tetris.explode_lines[line_no]);
						if(my_tetris.explode_lines[line_no] == 19) {
							explode_line(line_no);
						}
					}
				}
			}
		}
    	
    	
    	//old algorithm i didn't delete because, i may be return it, after tested carefully
    	/*System.out.println("================================");
		//my_tetris.reset_explode_lines();
    	int row_y;
    	shape sh;
    	box bx;
    	//int size = my_tetris.all_shapes.size();
		//select shape
		sh = my_tetris.all_shapes.lastElement();
		//System.out.println("active " + sh.get_shape_active());
		//System.out.println("selected shape.type,orderno : " + sh.shape_type + "," + size);
		for(int k = 0 ; k < 4 ;k++) {
			//select box
			bx = sh.sh_boxes.get(k);
			//System.out.println("\tselected box.y : " + bx.y);
			for(int t = 0 ; t < my_tetris.explode_lines.length ; t++) {
				row_y = (t+3) * 25;
				if(row_y == bx.y) {
					my_tetris.explode_lines[t]++;
					//System.out.println("upadate : explode_lines["+t+"] : " + 
					//my_tetris.explode_lines[t]);
					if(my_tetris.explode_lines[t] == 19) 
						explode_line(t);
					else
						System.out.println("line " + (t+3) +" : not filled : " + my_tetris.explode_lines[t]);
						
				}
			}
		}*/
    }
    //explode lines after checked
    public static void explode_line(int line_number) {
    	System.out.println("Process : Delete Line " + (line_number+3));
    	int row_y = (line_number+3) * 25;
		shape sh;
    	box bx;
    	int size = my_tetris.all_shapes.size();
    	
    	int counter = 0;
    	for(int i = 0 ; i < size ; i++) {
    		if(my_tetris.all_shapes.get(i) != null)
    		{
    			sh = my_tetris.all_shapes.get(i);
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
    		my_tetris.explode_lines[line_number] = 0;
    		drop_upper_boxes(line_number);
    	}
    		
    }
    //drops bottom-free boxes on upper lines after explosion
    public static void drop_upper_boxes(int line_number) {
    	int drop_y = (line_number+3)*25;
    	shape sh;
    	box bx;
    	for(int n = 1 ; n < 25 ; n++) {
    		for(int i = 0 ; i < my_tetris.all_shapes.size();i++) {
        		if(my_tetris.all_shapes.get(i) != null) {
        			sh = my_tetris.all_shapes.get(i);
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
    
    public static boolean check_box_bottom_free(box checkboxbottomfree) {
    	shape sh;
    	box bx;
    	for(int i = 0 ; i < my_tetris.all_shapes.size();i++) {
    		if(my_tetris.all_shapes.get(i) != null) {
    			sh = my_tetris.all_shapes.get(i);
    			for(int k = 0; k < 4 ; k++) {
    				if(sh.sh_boxes.get(k) != null) {
    					bx = sh.sh_boxes.get(k);
    					if(checkboxbottomfree.get_box_x() == bx.get_box_x()) {
    						if(checkboxbottomfree.get_box_y()+25 != bx.get_box_y() && checkboxbottomfree.get_box_y()+25 != my_tetris.bottom_border) {
								return true;
    						}
    					}
    				}
    			}
    		}
		}
    	return false;
    }
    
    
	public static void generate_a_new_shape() {
		//this block written to generate a shape and add it to vector<shape> all_shapes
		if(my_tetris.all_shapes.size() > 0)check_exploding_line();
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
		col_right_exists = false;
		col_left_exists = false;
		col_bot_exists = false;
    	int active_bottom;
		int active_x;
		int active_y;
		int active_right_end;
		
    	//shape active = my_tetris.all_shapes.lastElement();
    	//checks for bottom border.
    	for(int i = 0 ; i < 4 ; i++) {
    		if(checkforshape.sh_boxes.get(i).get_box_bottom_end() >= my_tetris.bottom_border) {
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
    		if(checkforshape.sh_boxes.get(i).get_box_y() < my_tetris.top_border) {
    			top = false;
    		}
    	}

    	//check right and main border with each boxes
    	if(checkforshape.get_shape_active()) {
    		for(int i = 0 ; i < 4 ; i++) {
        		//check for just horizontal matching according to left
            	active_x = checkforshape.sh_boxes.get(i).get_box_x();
        		if(active_x == my_tetris.left_border+25) {
        			left = false;
        		}
        		//check for just horizontal matching according to right
        		active_right_end = checkforshape.sh_boxes.get(i).get_box_right_end();
        		if(active_right_end == my_tetris.right_border) {
        			right = false;
        		}
        	}
    	}
    	
    	
    	//System.out.println("active.get_shape_active()  : " + active.get_shape_active() );
    	//check right and left main borders with shape start & end locations for rotatable or not
		checkforshape.calc_shape_start_end_loc();
		if(checkforshape.get_shape_start_loc_X() <= my_tetris.left_border) {
			col_left_exists = true;
		}
		if(checkforshape.get_shape_end_loc_X() >= my_tetris.right_border) {
			col_right_exists = true;
		}
    	
		//System.out.println("size " + my_tetris.all_shapes.size());
    	for(int i = 0 ; i < my_tetris.all_shapes.size()-1 ; i++) {
    		if(my_tetris.all_shapes.get(i) != null) {

        		for(int k = 0 ; k < 4 ; k++) {//this loop selects active shape boxes
        			active_bottom = checkforshape.sh_boxes.get(k).get_box_bottom_end();
        			active_x = checkforshape.sh_boxes.get(k).get_box_x();
        			active_y = checkforshape.sh_boxes.get(k).get_box_y();
        			active_right_end = checkforshape.sh_boxes.get(k).get_box_right_end();
        			
        			//this loop checking selected active shape box with boxes of all passive shapes
        			for(int t = 0 ; t < 4 ; t++) { 
        				if(my_tetris.all_shapes.get(i).sh_boxes.get(t) != null) {
            				int passive_x = my_tetris.all_shapes.get(i).sh_boxes.get(t).get_box_x();
            				int passive_right_end = my_tetris.all_shapes.get(i).sh_boxes.get(t).get_box_right_end();
            				int passive_y = my_tetris.all_shapes.get(i).sh_boxes.get(t).get_box_y();
            				int passive_bottom = my_tetris.all_shapes.get(i).sh_boxes.get(t).get_box_bottom_end();
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

    	//System.out.println("col_left_exists , col_right_exists , active.get_shape_active()  : " +col_left_exists + col_right_exists + active.get_shape_active());
    	if(!col_left_exists && !col_right_exists && !checkforshape.get_shape_active()) return true;
    	else return false;
    			
    }
	
}
