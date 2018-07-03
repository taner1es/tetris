package tetris;

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
			rotatedshape = genericVariables.stringtoShape(code,type,type_no,rot_no,actual_x,actual_y);
			rotatedshape.set_shape_active(false);
			if(genericVariables.checkcollisions(rotatedshape)) {
				rotatedshape.set_shape_active(true);
				all_shapes.setElementAt(rotatedshape, last_index);
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
}
