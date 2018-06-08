package tetris;

import java.util.Vector;

//this class is the most important class.
public class gameComponents extends genericVariables{ 
	//declare borders
	public final int top_border = 1*25;
	public final int left_border = 1*25;
	public final int right_border = 21*25;
	public final int bottom_border = 28*25;

	//Game info states
	Vector<shape> all_shapes = new Vector<shape>(0); //this vector keeps the information of the all shapes has drawed and also returning to draw function to draw it for all game time.
	public boolean call_new_shape = true;
	public String game_state = "running"; //game state created to understand for game stopped or running 
	//this method has really really critical role in the program it gets the new shape data and adding to all shapes list (vector) and goes on for everything.
	public void newShape(shape new_shape) {
		all_shapes.addElement(new_shape);
		shape_cnt++;
		gameLog.addElement("New Shape Added to Array list.");
	}
	public void set_call_new_shape (boolean b) {
		call_new_shape = b;
		System.out.println("!! --> size of v_all_shapes : "+ all_shapes.size() + "<-- call_new_shape setted up -->" + Boolean.toString(b));
	}
}
