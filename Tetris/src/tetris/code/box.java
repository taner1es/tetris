package tetris.code;

class box{
	private int x;
	private int y;
	private int bottom_end;
	private int right_end;
	
	//Constructor
	protected box(int p_x, int p_y, int p_bottom_end , int p_right_end){
		this.x = p_x;
		this.y = p_y;
		this.bottom_end = p_bottom_end;
		this.right_end = p_right_end;
	}
	//getter methods
	protected int get_box_x() { return this.x;}
	protected int get_box_y() { return this.y;}
	protected int get_box_right_end() { return this.right_end; }
	protected int get_box_bottom_end() { return this.bottom_end; }
	
	//setter methods
	protected void set_box_x(int p_x) { this.x = p_x; this.right_end = p_x+25;}
	protected void set_box_y(int p_y) { this.y = p_y; this.bottom_end = p_y+25;}
}
