package tetris;

import java.util.Vector;
import tetris.box;

public class shape extends genericVariables{
	Vector<box> sh_boxes = new Vector<box>(4);
	String shape_type;
	int shape_type_no;
	int shape_rotation_no;
	String shape_code;
	long shape_id;
	boolean active;
	int loc_x;
	int loc_y;
	int end_loc_x;
	int end_loc_y;
	int start_loc_x;
	int start_loc_y;
	
	
	//constructor
	public shape(int x , int y ,String type,int type_no,int rot_no, String code) {
		shape_id = shape_cnt;
		shape_type = type;
		shape_type_no = type_no;
		shape_rotation_no = rot_no;
		shape_code = code;
		active = true;
		loc_x = x;
		loc_y = y;
		gameLog.addElement("shape constructed");
	}
	public void add_box(box newbox) {
		sh_boxes.addElement(newbox);
	}
	//getter methods for shape objects
	public String get_shape_type() { return shape_type; }
	public String get_shape_code() { return shape_code; }
	public long get_shape_id() { return shape_id; }
	public boolean get_shape_active() { return active; }
	public int get_shape_loc_X() { return loc_x; }
	public int get_shape_loc_Y() { return loc_y; }
	//setter methods for shape objects
	public void set_shape_type(String type) { shape_type = type; }
	public void set_shape_code(String code) { shape_code = code; }
	public void set_shape_id(long id) { shape_id = id; }
	public void set_shape_active(boolean act) { active = act; }
	public void set_shape_loc_X(int x) { loc_x = x; }
	public void set_shape_loc_Y(int y) { loc_y = y; }
	
	//go down
	public void go_down() {
		loc_y+=25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).y +=25;
			sh_boxes.get(i).bottom_end+=25;
		}
	}
	//go up
	public void go_up() {
		loc_y-=25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).y -=25;
			sh_boxes.get(i).bottom_end-=25;
		}
	}
	//go left
	public void go_left() {
		loc_x-=25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).x -=25;
			sh_boxes.get(i).right_end -= 25;
		}
	}
	//go right
	public void go_right() {
		loc_x += 25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).x +=25;
			sh_boxes.get(i).right_end += 25;
		}
	}
	//calculate shape start location and end location to clarify collision checker
	public void calc_shape_start_end_loc(){
		int min_x = 5000, min_y = 5000,max_x = 0,max_y = 0;
		box fetch;
		for(int i = 0 ; i < 4 ; i++) {
			fetch = this.sh_boxes.get(i);
			if(fetch.x < min_x) { //find start loc x
				min_x = fetch.x;
			}
			if(fetch.y < min_y) { //find start loc y
				min_y = fetch.y;
			}
			if(fetch.x > max_x) { //find end loc x
				max_x = fetch.x;
			}
			if(fetch.y > max_y) { //find end loc y
				max_y = fetch.y;
			}
		}
		
		this.start_loc_x = min_x;
		this.start_loc_y = min_y;
		this.end_loc_x = max_x;
		this.end_loc_y = max_y;
		
	}
	

}
