package tetris;

import java.util.Vector;
import tetris.box;

class shape extends genericVariables{
	Vector<box> sh_boxes = new Vector<box>(4);
	private String shape_type;
	private int shape_type_no;
	private int shape_rotation_no;
	private String shape_code;
	private long shape_id;
	private boolean active;
	private int loc_x; //trace with initial location data
	private int loc_y; //trace with initial location data
	private int start_loc_x; //trace with derived location data
	private int start_loc_y; //trace with derived location data
	private int end_loc_x; //trace with derived location data
	private int end_loc_y; //trace with derived location data
	
	
	//constructor
	protected shape(int x , int y ,String type,int type_no,int rot_no, String code) {
		this.shape_id = genericVariables.get_shape_cnt();
		this.shape_type = type;
		this.shape_type_no = type_no;
		this.shape_rotation_no = rot_no;
		this.shape_code = code;
		this.active = true;
		this.loc_x = x;
		this.loc_y = y;
	}
	protected void add_box(box newbox) {
		sh_boxes.addElement(newbox);
	}
	
	//getter methods for shape objects
	protected String get_shape_type() { return this.shape_type; }
	protected int get_shape_type_no() { return this.shape_type_no; }
	protected int get_shape_rotation_no() { return this.shape_rotation_no; }
	protected String get_shape_code() { return this.shape_code; }
	protected long get_shape_id() { return this.shape_id; }
	protected boolean get_shape_active() { return this.active; }
	protected int get_shape_loc_X() { return this.loc_x; }
	protected int get_shape_loc_Y() { return this.loc_y; }
	protected int get_shape_start_loc_X() { return this.start_loc_x;}
	protected int get_shape_start_loc_Y() { return this.start_loc_y;}
	protected int get_shape_end_loc_X() { return this.end_loc_x;}
	protected int get_shape_end_loc_Y() { return this.end_loc_y;}
	
	//setter methods for shape objects
	protected void set_shape_type(String type) { this.shape_type = type; }
	protected void set_shape_code(String code) { this.shape_code = code; }
	protected void set_shape_id(long id) { this.shape_id = id; }
	protected void set_shape_active(boolean act) { this.active = act; }
	protected void set_shape_loc_X(int x) { this.loc_x = x; }
	protected void set_shape_loc_Y(int y) { this.loc_y = y; }
	protected void set_shape_start_loc_X(int x) { this.start_loc_x = x; }
	protected void set_shape_start_loc_Y(int y) { this.start_loc_y = y; }
	protected void set_shape_end_loc_X(int x) { this.end_loc_x = x; }
	protected void set_shape_end_loc_Y(int y) { this.end_loc_y = y; }
	
	//go down
	protected void go_down() {
		this.loc_y+=25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).set_box_y(sh_boxes.get(i).get_box_y()+25);
		}
	}
	//go up
	protected void go_up() {
		this.loc_y-=25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).set_box_y(sh_boxes.get(i).get_box_y()-25);
		}
	}
	//go left
	protected void go_left() {
		this.loc_x-=25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).set_box_x(sh_boxes.get(i).get_box_x()-25);
		}
	}
	//go right
	protected void go_right() {
		this.loc_x += 25;
		for(int i= 0 ; i< 4 ; i++) {
			sh_boxes.get(i).set_box_x(sh_boxes.get(i).get_box_x()+25);
		}
	}
	//calculate shape start location and end location to clarify collision checker
	protected void calc_shape_start_end_loc(){
		int min_x = 5000, min_y = 5000,max_x = 0,max_y = 0;
		box fetch;
		for(int i = 0 ; i < 4 ; i++) {
			fetch = this.sh_boxes.get(i);
			if(fetch.get_box_x() < min_x) { //find start loc x
				min_x = fetch.get_box_x();
			}
			if(fetch.get_box_y() < min_y) { //find start loc y
				min_y = fetch.get_box_y();
			}
			if(fetch.get_box_x() > max_x) { //find end loc x
				max_x = fetch.get_box_x();
			}
			if(fetch.get_box_y() > max_y) { //find end loc y
				max_y = fetch.get_box_y();
			}
		}
		
		this.start_loc_x = min_x;
		this.start_loc_y = min_y;
		this.end_loc_x = max_x;
		this.end_loc_y = max_y;
		
	}
	

}
