package tetris;
/*
 * Version No : 0.09
 * Version Notes : 
 *  @Author : Taner Esmeroğlu
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


final public class Tetris extends genericVariables
{
	static BufferedImage image;
	
    public static void main(String... args)
    {
        new Tetris().go();
    }
    
    //gets keyboard input
    class ActionListener extends KeyAdapter{
    	
        
    	public void keyPressed(KeyEvent e){
    		int key = e.getKeyCode();
    		
    		if(!started) {
    			if(key == KeyEvent.VK_ENTER)
    				started = true;
    		}
    		
    		if(pause) {
    			if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP) {
        			if(pause_selection == 0) pause_selection++;
        			else pause_selection--;
        		}
    			if(key == KeyEvent.VK_ENTER)
    				pause_apply = true;
    		}
    		//started and continues game input
    		if(started) {
    			if(!pause) {
    				//pause game
            		if(key == KeyEvent.VK_P) {
            			if(pause) pause = false;
            			else pause = true;
            		}
            		//rotate shape
            		if(key == KeyEvent.VK_SPACE && rotate_available) {
            			my_tetris.rotate_shape();
            			rotate_available = false;
            		}
            		if(key == KeyEvent.VK_DOWN) {
            			gameSpeed = 50;
            		}
    			}
    		}
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
    		//started and continues game input
    		if(started) {
    			if(!pause) {
    	    		if(key == KeyEvent.VK_RIGHT) {
    	    			right = false;
    	    		}
    	    		else if(key == KeyEvent.VK_LEFT) {
    	    			left = false;
    	    		}
            		if(key == KeyEvent.VK_DOWN) {
            			gameSpeed = 250;
            		}
            		if(key == KeyEvent.VK_SPACE && !rotate_available) {
            			rotate_available = true;
            		}
    			}
    		}
    	}
    }
    
    public void moveIt(DrawPanel drawPanel)
    {
    	
    	
    	//start a game with generating a new shape
    	generate_a_new_shape();
    	
    	//endless loop for game running.
        while (true)
        {
        	if(!pause && started) { //game running state
        		run_gameLoop();
                
        	}else { //game paused state
        		
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
    
    
	private void run_gameLoop() {
		shape active = my_tetris.all_shapes.lastElement();

		checkcollisions(active);
    	//left event
    	if(left && active.active) {
    		active.go_left();
    	}
    	//right event
    	if(right && active.active) {
    		active.go_right();
    	}
    	//down event
    	if(active.active) {
    		active.go_down();
    	}
    	//generate new shape
    	if(!active.active && top) {
    		generate_a_new_shape();
    	}
    	//check game has finished or not
    	if(!top) {
    		//temporary popup , i will change later
    		JOptionPane.showConfirmDialog(null, "You LOST .. :(");
    		System.exit(1);
    	}
	}
	public void go()
    {
		//try to load image
    	try {
			image = ImageIO.read(new File("C:\\Users\\taner\\Documents\\GitHub\\Tetris\\project_tetris\\src\\tetris\\tetris_menu.png"));
		} catch (IOException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//this block to configure window settings 
        frame = new JFrame("Tetris v0.07");
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
	

	@SuppressWarnings("null")
	private void pause_gameLoop(Graphics g) {
		int pause_x = 200;
		int pause_y = 150;
		int line_space = 50;
		int tab_space = 30;
		
		g.setColor(Color.GRAY);
		g.fill3DRect(pause_x, pause_y, 250, 300, false);
		g.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		
		g.setColor(Color.WHITE);
		g.drawString("! GAME PAUSED !", pause_x+tab_space-10 , pause_y+line_space);
		g.fill3DRect(pause_x, pause_y+line_space*2-25, 250, 5, true);
		
		if(pause_selection == 0){
			g.setColor(Color.YELLOW);
			g.fill3DRect(pause_x+tab_space, pause_y+line_space*3-25 , 180, 35, true);
			g.setColor(Color.BLACK);
			g.drawString("RESUME", pause_x+tab_space+35 , pause_y+line_space*3);
		}
		else {
			g.setColor(Color.WHITE);
			g.drawString("RESUME", pause_x+tab_space+35 , pause_y+line_space*3);
		}
		
		
		if(pause_selection == 1) {
			g.setColor(Color.YELLOW);
			g.fill3DRect(pause_x+tab_space, pause_y+line_space*4-25 , 180, 35, true);
			g.setColor(Color.BLACK);
			g.drawString("EXIT", pause_x+tab_space+55 , pause_y+line_space*4);
		}
		else {
			g.setColor(Color.WHITE);
			g.drawString("EXIT", pause_x+tab_space+55 , pause_y+line_space*4);
		}
		
		
		if(pause_apply) {
			switch(pause_selection) {
				case 0:
					pause = false;
					pause_apply = false;
					break;
				case 1:
					System.exit(0);
					break;
			}
		}
	}
	
	
    @SuppressWarnings("serial")
	public class DrawPanel extends JPanel 
    {
        	@SuppressWarnings("null")
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
        		if(started)if(pause)pause_gameLoop(g);
        		//System.out.println("started : " + started);
        		if(!started)g.drawImage(image, 0, 0, gw_WIDTH-325, gw_HEIGHT-25, Color.WHITE, null);
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
            			switch(my_shapes.get(k).shape_type) {
            				case "I": g.setColor(Color.GREEN);break;
	            				case "L":g.setColor(Color.RED);break;
		            				case "J":g.setColor(Color.GRAY);break;
			            				case "O":g.setColor(Color.BLUE);break;
				            				case "T":g.setColor(Color.YELLOW);break;
					            				case "S":g.setColor(Color.CYAN);break;
						            				case "Z":g.setColor(Color.ORANGE);break;
            			}
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
        		//Draw active shape type
        		g.drawString("ACTIVE SHAPE  SHAPE TYPE --> "+ sh_last_index.shape_type, x, y);
        		y+=25;
        		
        		for(int i = 0 ; i< 4 ; i++) {
        			g.drawString("shape["+size+"].(x,y) : (" +
        						Integer.toString(sh_last_index.loc_x)
        						+","+
        						Integer.toString(sh_last_index.loc_y)+")", x, y);
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