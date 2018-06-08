package tetris;

/*
 * TODO list : 
 * 		create a new class for each produced 3dfilled rectangle or each shape(think about it more) -if necessary- and keep x1,x2,y1,y2 coordinates for each box to check the collisions -be careful about this, save a backup of project-
 * 		keyboard key handlers will be added
 * 		produce randomly color for each shape
 * 		and don't forget for the comment lines to understand the code when looked again.
 *		also add a key to pause game or resume.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

final public class Tetris
{
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
	
    public static void main(String... args)
    {
        new Tetris().go();
    }
    
    private void go()
    {
    	//this block to configure window settings 
        frame = new JFrame("Tetris v0.02");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawPanel = new DrawPanel();

        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);

        frame.setResizable(false);
        frame.setUndecorated(false);
        frame.setSize(gw_WIDTH, gw_HEIGHT);
        frame.setLocation(s_WIDTH / 4, 0);
        frame.setVisible(true);
        moveIt(drawPanel);
    }
    
    static class box{
    	private int x;
    	private int y;
    	private int bottom_end;
    	private int right_end;
    	
    	//Constructor
    	box(int p_x, int p_y, int p_bottom_end , int p_right_end){
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
	static class shape{
		private Vector<box> sh_boxes = new Vector<box>(4);
		private String shape_type;
		private String shape_code;
		private long shape_id;
		private boolean active;
		private int loc_x;
		private int loc_y;
		
		//constructor
		public shape(int x , int y ,String type, String code) {
			shape_id = shape_cnt;
			shape_type = type;
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
	}
	
	//this class is the most important class.
	public static class gameComponents{
		//declare borders
		public final int top_border = 1*25;
		public final int left_border = 1*25;
		public final int right_border = 21*25;
		public final int bottom_border = 28*25;

		//Game info states
		Vector<shape> all_shapes = new Vector<shape>(0); //this vector keeps the information of the all shapes has drawed and also returning to draw function to draw it for all game time.
		private boolean call_new_shape = true;
		private String game_state = "running"; //game state created to understand for game stopped or running 
		//this method has really really critical role in the program it gets the new shape data and adding to all shapes list (vector) and goes on for everything.
		public void newShape(shape new_shape) {
			all_shapes.addElement(new_shape);
			gameLog.addElement("New Shape Added to Array list.");
		}
		public void set_call_new_shape (boolean b) {
			call_new_shape = b;
			System.out.println("!! --> size of v_all_shapes : "+ all_shapes.size() + "<-- call_new_shape setted up -->" + Boolean.toString(b));
		}
	}
    
    class DrawPanel extends JPanel
    {
        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics g)
        {
        	//Left side block
            g.setColor(Color.DARK_GRAY);
            g.fillRect(1*25, 3*25,25 , 26*25);
            //bottom side block
            g.setColor(Color.RED);
            g.fillRect(2*25, 28*25,19*25, 25);
            //right side block
            g.setColor(Color.CYAN);
            g.fillRect(21*25, 3*25, 25, 26*25);
            g.setColor(Color.BLACK);
            
            //drawings for all frames.
        	draw_Grid(g);
        	draw_GameInfo(g);
        	draw_Shapes(g);
        }
    }

    private void moveIt(DrawPanel drawPanel)
    {
    	//endless loop for game running.
    	boolean left = true;
    	boolean right = true;
    	boolean bottom = true;
    	boolean top = true;
    	generate_a_new_shape("L");
        while (true)
        {
        	shape active = my_tetris.all_shapes.lastElement();
        	/*if(active.sh_boxes.lastElement().bottom_end >= my_tetris.bottom_border) 
        		active.set_shape_active(false);*/
        	for(int i = 0 ; i < 4 ; i++) {
        		int active_bottom = active.sh_boxes.get(i).bottom_end;
        		if(active_bottom >= my_tetris.bottom_border) {
        			active.set_shape_active(false);
        		}
        	}
        	
        	for(int i = 0 ; i < 4 ; i++) {
        		int active_top = active.sh_boxes.get(i).y;
        		if(active_top < my_tetris.top_border) {
        			top = false;
        		}
        	}
        	System.out.println(my_tetris.all_shapes.size());
        	//check for top side of all passive shapes
        	for(int i = 0 ; i < my_tetris.all_shapes.size()-1 ; i++) {
        		for(int k = 0 ; k < 4 ; k++) {
        			for(int t = 0 ; t < 4 ; t++) {
        				int active_bottom = active.sh_boxes.get(k).bottom_end;
        				int passive_top = my_tetris.all_shapes.get(i).sh_boxes.get(t).y;
        				if(active_bottom == passive_top) {
        					active.set_shape_active(false);
        				}
        				if(active_bottom > passive_top) {
        					top = false;
        				}
        			}
        		}
        	}
        	
        	if(bottom && active.active) {
        		active.go_down();
        	}
        	
        	if(!active.active && top) {
        		generate_a_new_shape("I");
        	}
        	if(!top) {
        		JOptionPane.showConfirmDialog(null, "You LOST .. :(");
        		System.exit(1);
        	}
        	
        	
        	
            try
            { 
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            frame.repaint();
        }
    }
    
    private void draw_Grid(Graphics g) {
    	//Draw Grid with 25x25 blocks and shows line numbers
        g.setColor(Color.BLACK);
    	int x = 0;
    	int y = 0;
    	for(int i = 0; i < gw_HEIGHT/25 ; i++) {
    		g.setFont(new Font("Tahoma", Font.BOLD, 11));
    		//show line numbers
    		if(i <= 29)g.drawString(Integer.toString(i), 5, y+16);
    		if(i != 0 && i <= 29)g.drawString(Integer.toString(i), 22*25+5, y+16);
    		if(i != 0 && i < 23)g.drawString(Integer.toString(i), x+5, 16);
    		if(i != 0 && i < 22) g.drawString(Integer.toString(i), x+5, 29*25+16);
    		//draw grid horizontal lines
            if(i < 31) g.drawLine(0, y,gw_WIDTH -25,y);
            //draw grid vertical lines
            if(i < 24) g.drawLine(x, 0,x ,gw_HEIGHT-50);
            //intervals
            y+=25;
            x+=25;
    	}
		gameLog.addElement("Grid Successfully Drawed.");
    }
    private void draw_GameInfo(Graphics g) {
    	//this block written to check game mechanics are working well or not by seeing all the values about.
    	g.setColor(Color.BLACK);
    	int x = 23*25+3;
    	int y = 19;
		g.setFont(new Font("Tahoma", Font.BOLD, 15));
		g.drawString("call_new_shape : " +Boolean.toString(my_tetris.call_new_shape), x, y);
		y+=25;
		g.drawString("game_state : " + my_tetris.game_state, x, y);
		y+=25;
		shape sh_last_index = my_tetris.all_shapes.lastElement();
		g.drawString("active_shape_loc_(X,Y):(" + sh_last_index.loc_x + "," + sh_last_index.loc_y + ")", x, y);
		y+=25;
		/*for(int i = 0 ; i < 4  ; i++) {
			g.drawString("Shape No: " + shape_cnt + " - Box No: " + Integer.toString(i), x, y);
			y+=25;
			g.drawString("active_box["+i+"]_(X,Y):(" + sh_last_index.sh_boxes.get(i).x+ "," + sh_last_index.sh_boxes.get(i).y + ")", x, y);
			y+=25;
			g.drawString("active_box["+i+"]_(e_bot,e_r):(" + sh_last_index.sh_boxes.get(i).bottom_end+ "," + sh_last_index.sh_boxes.get(i).right_end + ")", x, y);
			y+=25;
		}*/

		for(int k = 0 ; k < my_tetris.all_shapes.size();k++) {
			for(int i = 0 ; i < 4  ; i++) {
				g.drawString("Shape No: " + k + " - Box No: " + Integer.toString(i), x, y);
				y+=25;
				String act;
				if(my_tetris.all_shapes.get(k).active) act = "active";
				else act = "passive";
				g.drawString(act+"_box["+i+"]_(X,Y):(" + my_tetris.all_shapes.get(k).sh_boxes.get(i).x+ "," + my_tetris.all_shapes.get(k).sh_boxes.get(i).y + ")", x, y);
				y+=25;
				g.drawString(act+"_box["+i+"]_(e_bot,e_r):(" + my_tetris.all_shapes.get(k).sh_boxes.get(i).bottom_end+ "," + my_tetris.all_shapes.get(k).sh_boxes.get(i).right_end + ")", x, y);
				y+=25;
			}
		}
		for(int i = 0 ; i < my_tetris.all_shapes.size();i++) {
			g.drawString("Shape["+Integer.toString(i)+"]_active: " + Boolean.toString(my_tetris.all_shapes.get(i).active), x, y);
			y+=25;
		}
	}
    
	private static void generate_a_new_shape(String p_type) {
		//this block written to generate a shape and add it to vector<shape> all_shapes
		//TODO : random generation will be added with including all shapes and all positions for now just two types of shape added shape "I" and "L" first positions
		String code = null;
		if(p_type == "I")
	    	code = "AXXXAXXXAXXXAXXX";
		else if(p_type == "L")
			code = "AXXXAXXXAAXXXXXX";
    	int draw_x = 15*25;
    	int draw_y = 0*25;

    	shape new_generated_shape = new shape(draw_x,draw_y,p_type,code);
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
    private void draw_Shapes(Graphics g) {
    	//string are created with 16 characters for indexing think it 0-15 each 4 grouped character dedicated to a single line.think like 4x4 matrix compressed a string value to get it easier.
    	//drawing formula from string to graphic like : shape type "I": AXXX-AXXX-AXXX-AXXX
    	int draw_x,draw_y;
    	int size = 25;
    	
    	Vector<shape>my_shapes = my_tetris.all_shapes;
    	//draw each boxes.
    	for(int k = 0 ; k < my_shapes.size();k++) {
    		for(int i = 0 ; i < 4 ; i++) {
    			draw_x = my_shapes.get(k).sh_boxes.get(i).x;
    			draw_y = my_shapes.get(k).sh_boxes.get(i).y;
    			g.setColor(Color.GREEN);
				g.fill3DRect(draw_x, draw_y, size, size,false);
				g.drawString(Integer.toString(i), draw_x+3, draw_y+17);
    		}
    	}
    }
}