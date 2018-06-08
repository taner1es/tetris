package tetris;
/*
 * Version No : 0.06
 * Version Notes : 
 * 	.Collision problems around %95 solved. ( tested with shape "I" and "L" works fine for now)
 *  .Left-Right keyboard arrows handlers added (tested - works fine)
 *  @Author : Taner Esmeroğlu
 */
/*
 * TODO list for next versions: 
 * 		0) Separate the script pages to read the code easier. (Completed @v0.06)
 * 		1) Add all shapes as a string.(Completed @v0.06)
 * 		2) Make randomly the shape generate function
 * 		3) Assign random color to generated shapes,( color range will be max 10 )
 * 		4) Design a game menu
 * 		5) Add a pause button to game
 * 		6) Implement a score system
 * 		7) Design a highscore page 
 * 		
 * 		!!)and don't forget for the comment lines to understand the code when looked again.
 * 		!!)Don't forget to add version notes after worked.
 * 
 * 
 * ----WHAT I DID CURRENT SESSION----
 * -->RIGHT INFORMATION BAR UPDATED 
 * 	    -added (x,y) location drawing for active and passive shapes
 *  	-total passive shape number,active shape number
 *  	-listing passive 5 shapes (start from size-5)
 * -->SEPERATE THE CLASS SCRIPTS
 * 		-box,gameComponents,genericVariables,shape classes are distincted from the tetris.java file
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

final public class Tetris extends genericVariables
{
	
	
    public static void main(String... args)
    {
        new Tetris().go();
    }
    
    //gets keyboard input
    class ActionListener extends KeyAdapter{
    	
        
    	public void keyPressed(KeyEvent e){
    		int key = e.getKeyCode();		
        	if(key == KeyEvent.VK_RIGHT) {
        		left = false;
        		right = true;
        	}
        	else if(key == KeyEvent.VK_LEFT) {
        		right = false;
        		left = true;
        	}
        	else {
        		right = false;
        		left = false;
        	}
        }
    	public void keyReleased(KeyEvent e){
    		int key = e.getKeyCode();
    		if(key == KeyEvent.VK_RIGHT) {
    			right = false;
    		}
    		else if(key == KeyEvent.VK_LEFT) {
    			left = false;
    		}
    	}
    	
    	
    }
    
    public void moveIt(DrawPanel drawPanel)
    {
    	
    	boolean bottom = true;
    	boolean top = true;
    	int active_bottom;
		int active_x;
		int active_y;
		int active_right_end;
    	//start a game with generating a new shape
    	generate_a_new_shape();
    	
    	//endless loop for game running.
        while (true)
        {
        	shape active = my_tetris.all_shapes.lastElement();
        	//checks for bottom border.
        	for(int i = 0 ; i < 4 ; i++) {
        		active_bottom = active.sh_boxes.get(i).bottom_end;
        		if(active_bottom >= my_tetris.bottom_border) {
        			active.set_shape_active(false);
        		}
        	}
        	
        	//checks for top border
        	for(int i = 0 ; i < 4 ; i++) {
        		int active_top = active.sh_boxes.get(i).y;
        		if(active_top < my_tetris.top_border) {
        			top = false;
        		}
        	}

			//check for just horizontal matching according to left
        	active_x = active.loc_x;
			if(active_x == my_tetris.right_border-25) {
				right = false;
			}
			//check for just horizontal matching according to left
			active_right_end = active.loc_x;
			if(active_right_end == my_tetris.left_border+25) {
				left = false;
			}
    		System.out.println("size " + my_tetris.all_shapes.size());
        	for(int i = 0 ; i < my_tetris.all_shapes.size()-1 ; i++) {
        		for(int k = 0 ; k < 4 ; k++) {//this loop checks for active to game borders
        			active_bottom = active.sh_boxes.get(k).bottom_end;
        			active_x = active.sh_boxes.get(k).x;
        			active_y = active.sh_boxes.get(k).y;
        			active_right_end = active.sh_boxes.get(k).right_end;
        			for(int t = 0 ; t < 4 ; t++) { //this loop checking with passive shapes
        				int passive_top = my_tetris.all_shapes.get(i).sh_boxes.get(t).y;
        				int passive_x = my_tetris.all_shapes.get(i).sh_boxes.get(t).x;
        				int passive_right_end = my_tetris.all_shapes.get(i).sh_boxes.get(t).right_end;
        				int passive_y = my_tetris.all_shapes.get(i).sh_boxes.get(t).y;
        				//check horizontal and vertical matching
        				if(active_bottom == passive_top && active_x == passive_x) {
        					active.set_shape_active(false);
        				}
        				//check for just horizontal matching according to right
        				if( (active_right_end == passive_x && active_y == passive_y) || (active_right_end == passive_x && active_y+25 == passive_y)) {
        					right = false;
        				}
        				//check for just horizontal matching according to left
        				if( (active_x == passive_right_end && active_y == passive_y) || (active_x == passive_right_end && active_y+25 == passive_y)) {
        					left = false;
        				}
        				//check if there no place for new shape and finish the game
        				if(active_bottom > passive_top && active_bottom < 4*25) {
        					//top = false;
        				}
        			}
        		}
        	}
        	
        	//left event
        	if(left && active.active) {
        		active.go_left();
        	}
        	//right event
        	if(right && active.active) {
        		active.go_right();
        	}
        	//down event
        	if(bottom && active.active) {
        		active.go_down();
        	}
        	//generate new shape
        	if(!active.active && top) {
        		generate_a_new_shape();
        	}
        	//check game has finished or not
        	if(!top) {
        		JOptionPane.showConfirmDialog(null, "You LOST .. :(");
        		System.exit(1);
        	}
        	
        	
        	
            try
            { 
                Thread.sleep(gameSpeed);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            frame.repaint();
        }
    }
    public void go()
    {
    	//this block to configure window settings 
        frame = new JFrame("Tetris v0.06");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new ActionListener());
        drawPanel = new DrawPanel();

        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);

        frame.setResizable(false);
        frame.setUndecorated(false);
        frame.setSize(gw_WIDTH, gw_HEIGHT);
        frame.setLocation(s_WIDTH / 4, 0);
        frame.setVisible(true);
        moveIt(drawPanel);
    }
    @SuppressWarnings("serial")
	public class DrawPanel extends JPanel 
    {
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
            	int size = my_tetris.all_shapes.size()-1;
        		g.setFont(new Font("Tahoma", Font.BOLD, 15));
        		
        		

            	//temporary section
            	g.drawString("left : "+ left, 51, 47);
            	g.drawString("right : "+ right, 51, 67);
        		
            	
            	
            	//select active shape
        		shape sh_last_index = my_tetris.all_shapes.lastElement();
        		//Draw active shape boxes informations
        		g.drawString("ACTIVE SHAPE  SHAPE NO --> "+ size, x, y);
        		y+=25;
        		for(int i = 0 ; i< 4 ; i++) {
        			g.drawString("shape["+size+"].(x,y) : (" +
        						Integer.toString(sh_last_index.loc_x)
        						+","+
        						Integer.toString(sh_last_index.loc_y), x, y);
        			y+=25;
        		}
        		//Draw passive boxes locations on right bar
        		g.drawString("PASSIVE SHAPE LIST ~ TOTAL --> "+ size, x, y);
        		int k;
        		if(my_tetris.all_shapes.size() >= 5 )
        			k = my_tetris.all_shapes.size()-5;
        		else 
        			k = 0;
        		for(; k< my_tetris.all_shapes.size() ; k++) {
        			y+=25;
        			for(int i = 0 ; i< 4 ; i++) {
        				g.drawString("shape["+k+"]box["+i+"].(x,y) : (" +
        						Integer.toString(my_tetris.all_shapes.get(k).sh_boxes.get(i).x)
        						+","+
        						Integer.toString(my_tetris.all_shapes.get(k).sh_boxes.get(i).y)
        						+")", 
        						x, y);
        				y+=25;
        			}
        		}
        		/*g.drawString("right_end : ", x, y);
        		y+=25;
        		for(int i = 0 ; i< 4 ; i++) {
        			g.drawString("active.box["+i+"].right_end : " +Integer.toString(sh_last_index.sh_boxes.get(i).right_end), x, y);
        			y+=25;
        		}*/
        		
        		/*g.drawString("all_shapes.size : " +my_tetris.all_shapes.size(), x, y);
        		y+=25;
        		g.drawString("call_new_shape : " +Boolean.toString(my_tetris.call_new_shape), x, y);
        		y+=25;
        		g.drawString("game_state : " + my_tetris.game_state, x, y);
        		y+=25;
        		
        		g.drawString("active_shape_loc_(X,Y):(" + sh_last_index.loc_x + "," + sh_last_index.loc_y + ")", x, y);
        		y+=25;*/
        		/*for(int i = 0 ; i < 4  ; i++) {
        			g.drawString("Shape No: " + shape_cnt + " - Box No: " + Integer.toString(i), x, y);
        			y+=25;
        			g.drawString("active_box["+i+"]_(X,Y):(" + sh_last_index.sh_boxes.get(i).x+ "," + sh_last_index.sh_boxes.get(i).y + ")", x, y);
        			y+=25;
        			g.drawString("active_box["+i+"]_(e_bot,e_r):(" + sh_last_index.sh_boxes.get(i).bottom_end+ "," + sh_last_index.sh_boxes.get(i).right_end + ")", x, y);
        			y+=25;
        		}*/

        		/*for(int k = 0 ; k < my_tetris.all_shapes.size();k++) {
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
        		}*/
        	}
    }
}