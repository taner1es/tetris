package tetris;

public class box{
	int x;
	int y;
	int bottom_end;
	int right_end;
	
	//Constructor
	public box(int p_x, int p_y, int p_bottom_end , int p_right_end){
		x = p_x;
		y = p_y;
		bottom_end = p_bottom_end;
		right_end = p_right_end;
	}
	//getter methods
	public int get_box_x() { return x;}
	public int get_box_y() { return y;}
	
	//setter methods
	public void set_box_x(int p_x) { x = p_x;}
	public void set_box_y(int p_y) { y = p_y;}
}
