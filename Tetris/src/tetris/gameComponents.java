package tetris;

import java.util.Random;
import java.util.Vector;

//this class is the most important class.
class gameComponents extends genericVariables{ 
	
	private Vector<shape>  all_shapes = new Vector<>(0); //this vector keeps the information of the all shapes has drawed and also returning to draw function to draw it for all game time.
	private static final int TOP_BORDER = 1*25;
	private static final int LEFT_BORDER = 1*25;
	private static final int RIGHT_BORDER = 21*25;
	private static final int BOTTOM_BORDER = 28*25;
	private int[] explode_lines = new int[25];
	private boolean call_new_shape = true;
	private static boolean exit_game = false;
	
	
	//getter methods
	protected Vector<shape> get_all_shapes() { return this.all_shapes;}
	protected final int get_top_border() { return gameComponents.TOP_BORDER;}
	protected final int get_left_border() { return gameComponents.LEFT_BORDER;}
	protected final int get_right_border() { return gameComponents.RIGHT_BORDER;}
	protected final int get_bottom_border() { return gameComponents.BOTTOM_BORDER;}
	protected int[] get_explode_lines() { return this.explode_lines;}
	protected boolean get_call_new_shape() { return this.call_new_shape;}
	protected static boolean get_exit_game() { return gameComponents.exit_game;}
	
	//setter methods
	protected static void set_exit_game(boolean exit_game) { gameComponents.exit_game = exit_game;}
	
	
	//constructor
	gameComponents() {
		for(int i = 0 ; i < 25 ; i++) {
			explode_lines[i] = 0;
		}
	}
	
	//this method has really really critical role in the program it gets the new shape data and adding to all shapes list (vector) and goes on for everything.
	protected void newShape(shape new_shape) {
		all_shapes.addElement(new_shape);
		genericVariables.set_shape_cnt(genericVariables.get_shape_cnt()+1);
	}
	protected void set_call_new_shape (boolean b) {
		call_new_shape = b;
	}
	protected void rotate_shape() {
		int last_index = all_shapes.size()-1;
		int actual_x = all_shapes.lastElement().get_shape_start_loc_X()+100;		
		int actual_y = all_shapes.lastElement().get_shape_start_loc_Y();		
		String type = all_shapes.lastElement().get_shape_type();
		
		int type_no = all_shapes.lastElement().get_shape_type_no(); //shape_codes array first dimension number
		int rot_no = all_shapes.lastElement().get_shape_rotation_no(); // shape_codes array second dimension number
		String code = null;
		if(type_no != 3 ) {
			 // will be taken from array list with new rotation no
			System.out.println("type_no & rot_no : " + type_no + rot_no );
			if(genericVariables.get_shape_codes()[type_no].length-1 == rot_no) {
				rot_no = 0;
				code = genericVariables.get_shape_codes()[type_no][rot_no];
			}else {
				rot_no++;
				code = genericVariables.get_shape_codes()[type_no][rot_no];
			}
			shape rotatedshape;
			rotatedshape = stringtoShape(code,type,type_no,rot_no,actual_x,actual_y);
			rotatedshape.set_shape_active(false);
			if(checkcollisions(rotatedshape)) {
				rotatedshape.set_shape_active(true);
				all_shapes.setElementAt(rotatedshape, last_index);
				all_shapes.elementAt(last_index).set_shape_id(last_index);
				System.out.println("rotated_shape_id " +rotatedshape.get_shape_id());
			}
				
			else System.err.println("Collision exists while rotating.");
		}
		else {
			System.err.println("no rotation for this shape.");
		}
		
	}

	protected void reset_explode_lines() {
		for(int i = 0 ; i < 25 ; i++) {
			explode_lines[i] = 0;
		}
	}
	
	protected static void left_event() {
		//left event
		shape active = get_my_tetris().get_all_shapes().lastElement();
    	if(genericVariables.get_left() && active.get_shape_active()) {
    		if(!genericVariables.get_down()) {
				System.out.println("asd");
				if(genericVariables.get_frameCounter_left() == 1 ) {
	    			active.go_left();
	        		genericVariables.set_frameCounter_left(genericVariables.get_frameCounter_left()+1);
	    		}else if(genericVariables.get_frameCounter_left() >= 10) {
	    			active.go_left();
	    			genericVariables.set_frameCounter_left(2);
	    		}else {
	    			genericVariables.set_frameCounter_left(genericVariables.get_frameCounter_left()+1);
	    		}
			}
			else {
				if(genericVariables.get_frameCounter_left() == 1 ) {
	    			active.go_left();
	        		genericVariables.set_frameCounter_left(genericVariables.get_frameCounter_left()+1);
	    		}else if(genericVariables.get_frameCounter_left() >= 30) {
	    			active.go_left();
	    			genericVariables.set_frameCounter_left(2);
	    		}else {
	    			genericVariables.set_frameCounter_left(genericVariables.get_frameCounter_left()+1);
	    		}
			}
    	}
	}
	
	protected static void right_event() {
		//right event
		shape active = get_my_tetris().get_all_shapes().lastElement();
		if(genericVariables.get_right() && active.get_shape_active()) {
			if(!genericVariables.get_down()) {
				System.out.println("asd");
				if(genericVariables.get_frameCounter_right() == 1 ) {
	    			active.go_right();
	        		genericVariables.set_frameCounter_right(genericVariables.get_frameCounter_right()+1);
	    		}else if(genericVariables.get_frameCounter_right() >= 10) {
	    			active.go_right();
	    			genericVariables.set_frameCounter_right(2);
	    		}else {
	    			genericVariables.set_frameCounter_right(genericVariables.get_frameCounter_right()+1);
	    		}
			}
			else {
				if(genericVariables.get_frameCounter_right() == 1 ) {
	    			active.go_right();
	        		genericVariables.set_frameCounter_right(genericVariables.get_frameCounter_right()+1);
	    		}else if(genericVariables.get_frameCounter_right() >= 50) {
	    			active.go_right();
	    			genericVariables.set_frameCounter_right(2);
	    		}else {
	    			genericVariables.set_frameCounter_right(genericVariables.get_frameCounter_right()+1);
	    		}
			}
		}
	}
	
	protected static void down_event() {
		//down event
		shape active = get_my_tetris().get_all_shapes().lastElement();
    	if(active.get_shape_active() && genericVariables.get_frameCounter() >= 30) {
    		if(genericVariables.get_col_bot_exists())
    		{
    			if(genericVariables.get_frameCounter_collision_bot() >= 60) {
    				checkcollisions(active);
    				if(genericVariables.get_col_bot_exists()){
    					active.set_shape_active(false);
    				}else{
    	    			genericVariables.set_frameCounter(0);
    	        		active.go_down();
    				}
    			}	
    		}else {
    			genericVariables.set_frameCounter(0);
        		active.go_down();
    		}
    			
		}
	}
	
	//check for exploding lines
    private static void check_exploding_line() {
    	genericVariables.get_my_tetris().reset_explode_lines();
		shape sh;
		box bx;
		int line_no;
		for(int i = 0; i < genericVariables.get_my_tetris().get_all_shapes().size() ; i++) {
			if(genericVariables.get_my_tetris().get_all_shapes().get(i) != null) {
				sh = genericVariables.get_my_tetris().get_all_shapes().get(i);
				for(int k = 0 ; k < 4 ; k++) {
					if(sh.sh_boxes.get(k) != null) {
						bx = sh.sh_boxes.get(k);
						line_no = (bx.get_box_y() / 25) - 3;
						genericVariables.get_my_tetris().get_explode_lines()[line_no]++;
						System.out.println("line["+(line_no+3)+"] : " + genericVariables.get_my_tetris().get_explode_lines()[line_no]);
						if(genericVariables.get_my_tetris().get_explode_lines()[line_no] == 19) {
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
    	int size = genericVariables.get_my_tetris().get_all_shapes().size();
    	
    	int counter = 0;
    	for(int i = 0 ; i < size ; i++) {
    		if(genericVariables.get_my_tetris().get_all_shapes().get(i) != null)
    		{
    			sh = genericVariables.get_my_tetris().get_all_shapes().get(i);
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
    		genericVariables.get_my_tetris().get_explode_lines()[line_number] = 0;
    		drop_upper_boxes(line_number);
    	}
    		
    }
    
  //drops bottom-free boxes on upper lines after explosion
    private static void drop_upper_boxes(int line_number) {
    	int drop_y = (line_number+3)*25;
    	shape sh;
    	box bx;
    	for(int n = 1 ; n < 25 ; n++) {
    		for(int i = 0 ; i < genericVariables.get_my_tetris().get_all_shapes().size();i++) {
        		if(genericVariables.get_my_tetris().get_all_shapes().get(i) != null) {
        			sh = genericVariables.get_my_tetris().get_all_shapes().get(i);
        			for(int k = 0; k < 4 ; k++) {
        				if(sh.sh_boxes.get(k) != null) {
        					bx = sh.sh_boxes.get(k);
        					if(bx.get_box_y() == drop_y-(n*25) && check_box_bottom_free(bx)) {
        							 bx.set_box_y(bx.get_box_y()+25);
        							 k--;
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
    	for(int i = 0 ; i < genericVariables.get_my_tetris().get_all_shapes().size();i++) {
    		if(genericVariables.get_my_tetris().get_all_shapes().get(i) != null) {
    			sh = genericVariables.get_my_tetris().get_all_shapes().get(i);
    			for(int k = 0; k < 4 ; k++) {
    				if(sh.sh_boxes.get(k) != null) {
    					bx = sh.sh_boxes.get(k);
    					if(checkboxbottomfree.get_box_x() == bx.get_box_x() && (checkboxbottomfree.get_box_y()+25 != bx.get_box_y() && checkboxbottomfree.get_box_y()+25 != genericVariables.get_my_tetris().get_bottom_border() )) {
								return true;
    					}
    				}
    			}
    		}
		}
    	return false;
    }
    
    protected static void generate_a_new_shape() {
		//this block written to generate a shape and add it to vector<shape> all_shapes
		if(!genericVariables.get_my_tetris().get_all_shapes().isEmpty())
			check_exploding_line();
		String code = null;
		String s_type = null;
		int s_type_no;
		int rot_no = 0; //rotation no , initially 0
		Random rndm = new Random();
		int rndm_no = rndm.nextInt(7);
		s_type_no = rndm_no;
		code = genericVariables.get_shape_codes()[rndm_no][rot_no];
		
		if(rndm_no == 0) s_type = "I";
			else if(rndm_no == 1) s_type = "L";
			else if(rndm_no == 2) s_type = "J";
			else if(rndm_no == 3) s_type = "O";
			else if(rndm_no == 4) s_type = "T";
			else if(rndm_no == 5) s_type = "S";
			else if(rndm_no == 6) s_type = "Z";
		else s_type = "X";
		

		genericVariables.get_my_tetris().newShape(stringtoShape(code,s_type,s_type_no,rot_no,15*25,0));
		genericVariables.get_my_tetris().set_call_new_shape(false);
    	
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
		genericVariables.set_top(true);
		genericVariables.set_col_right_exists(false);
		genericVariables.set_col_left_exists(false);
		genericVariables.set_col_bot_exists(false);
    	int active_bottom;
		int active_x;
		int active_y;
		int active_right_end;
		
    	//checks for bottom border.
    	for(int i = 0 ; i < 4 ; i++) {
    		if(checkforshape.sh_boxes.get(i).get_box_bottom_end() >= genericVariables.get_my_tetris().get_bottom_border()) {
    			if(checkforshape.get_shape_active()) {
    				genericVariables.set_col_bot_exists(true);
    				break;
    			}
    			else
    				return false;
    		}
    	}
    	
    	//checks for top border
    	for(int i = 0 ; i < 4 ; i++) {
    		if(checkforshape.sh_boxes.get(i).get_box_y() < genericVariables.get_my_tetris().get_top_border()) {
    			genericVariables.set_top(false);
    		}
    	}

    	//check right and main border with each boxes
    	if(checkforshape.get_shape_active()) {
    		for(int i = 0 ; i < 4 ; i++) {
        		//check for just horizontal matching according to left
            	active_x = checkforshape.sh_boxes.get(i).get_box_x();
        		if(active_x == genericVariables.get_my_tetris().get_left_border()+25) {
        			genericVariables.set_left(false);
        		}
        		//check for just horizontal matching according to right
        		active_right_end = checkforshape.sh_boxes.get(i).get_box_right_end();
        		if(active_right_end == genericVariables.get_my_tetris().get_right_border()) {
        			genericVariables.set_right(false);
        		}
        	}
    	}
    	
    	
    	//check right and left main borders with shape start & end locations for rotatable or not
		checkforshape.calc_shape_start_end_loc();
		if(checkforshape.get_shape_start_loc_X() <= genericVariables.get_my_tetris().get_left_border()) {
			genericVariables.set_col_left_exists(true);
		}
		if(checkforshape.get_shape_end_loc_X() >= genericVariables.get_my_tetris().get_right_border()) {
			genericVariables.set_col_right_exists(true);
		}
    	
		for(int i = 0 ; i < genericVariables.get_my_tetris().get_all_shapes().size()-1 ; i++) {
    		if(genericVariables.get_my_tetris().get_all_shapes().get(i) != null) {

        		for(int k = 0 ; k < 4 ; k++) {//this loop selects active shape boxes
        			active_bottom = checkforshape.sh_boxes.get(k).get_box_bottom_end();
        			active_x = checkforshape.sh_boxes.get(k).get_box_x();
        			active_y = checkforshape.sh_boxes.get(k).get_box_y();
        			active_right_end = checkforshape.sh_boxes.get(k).get_box_right_end();
        			
        			//this loop checking selected active shape box with boxes of all passive shapes
        			for(int t = 0 ; t < 4 ; t++) { 
        				if(genericVariables.get_my_tetris().get_all_shapes().get(i).sh_boxes.get(t) != null) {
            				int passive_x = genericVariables.get_my_tetris().get_all_shapes().get(i).sh_boxes.get(t).get_box_x();
            				int passive_right_end = genericVariables.get_my_tetris().get_all_shapes().get(i).sh_boxes.get(t).get_box_right_end();
            				int passive_y = genericVariables.get_my_tetris().get_all_shapes().get(i).sh_boxes.get(t).get_box_y();
            				@SuppressWarnings("unused")
							int passive_bottom = genericVariables.get_my_tetris().get_all_shapes().get(i).sh_boxes.get(t).get_box_bottom_end();
            				//check horizontal and vertical matching
            				if(active_bottom == passive_y && active_x == passive_x) {
                    			genericVariables.set_col_bot_exists(true);
            				}
            				//check for just horizontal matching according to right
            				if( genericVariables.get_right() || !checkforshape.get_shape_active()) {
            					if(active_y == passive_y) {
            						if(active_right_end == passive_x) {
            							genericVariables.set_right(false);
                    					genericVariables.set_col_right_exists(true);
            						}
            					}
            				}
            				//check for just horizontal matching according to left
            				if( genericVariables.get_left() || !checkforshape.get_shape_active()) {
            					if(active_y == passive_y) {
            						if(active_x == passive_right_end) {
            							genericVariables.set_left(false);
                    					genericVariables.set_col_left_exists(true);
            						}
            					}
            				}
            				
            				//check if there no place for new shape and finish the game
            				if(active_bottom > passive_y && active_bottom < 4*25) {
            					genericVariables.set_top(false);
            				}
        				}
        			}
        		}
    		}
    	}

    	if(genericVariables.get_col_bot_exists()) genericVariables.inc_frameCounter_collision_bot();
    	else genericVariables.set_frameCounter_collision_bot(0);

    	if(!genericVariables.get_col_left_exists() && !genericVariables.get_col_right_exists() && !checkforshape.get_shape_active()) return true;
    	else return false;
    			
    }
}
