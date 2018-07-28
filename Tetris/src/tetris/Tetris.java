package tetris;
/*
 * Version No : 0.13
 * Version Notes : 
 *  @Author : Taner Esmeroğlu
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;




class Tetris extends genericVariables
{
    public static void main(String... args)
    {
    	new Tetris().go();
    }
    private void go()
    {
    	//loading images before start game
    	try {
    		URL url_tetris_menu = Tetris.class.getResource("tetris_menu.png"); //gets the folder/file from runnable jar file location
    		genericVariables.set_view_welcome_image(ImageIO.read(url_tetris_menu));
    	} catch (IOException e) {	
			// TODO Auto-generated catch block
    		System.out.println("image loading problem.");
			e.printStackTrace();
		}
    	//this block to configure window settings 
        genericVariables.set_frame(new JFrame("Tetris v0.13"));
        genericVariables.get_frame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        genericVariables.get_frame().addKeyListener(new ActionListener());
        genericVariables.set_drawPanel(new DrawPanel());
        

        genericVariables.get_frame().getContentPane().add(BorderLayout.CENTER, genericVariables.get_drawPanel());

        genericVariables.get_frame().setResizable(false);
        genericVariables.get_frame().setUndecorated(false);
        genericVariables.get_frame().setSize(genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT());
        genericVariables.get_frame().setLocation(genericVariables.get_s_WIDTH()/ 4, 0);
        genericVariables.get_frame().setVisible(true);
        moveIt();
    }
    
    private void moveIt()
    {
    	//start a game with generating a new shape
    	if(genericVariables.get_game_state() == "running" || genericVariables.get_game_state() == "welcome")
    	gameComponents.swap_buffered_shape_to_game();
    	
    	//endless loop for game running.
        while (true)
        {
        	//game running state
        	if(!genericVariables.get_endGame() && !genericVariables.get_pause() && genericVariables.get_started()) { 
        		run_gameLoop();
                
        	}else { //game paused state
        		if(gameComponents.get_exit_game()) {
        			break;
        		}
        	}
        	sleep(genericVariables.get_gameSpeed());
        	
        	genericVariables.get_frame().repaint();
        }
        if(gameComponents.get_exit_game()) {
        	System.exit(0);
        }
    }
    
    private void sleep(int speed) {
    	try {
			Thread.sleep(speed);
		} catch (InterruptedException e) {
			System.err.println("ERROR : sleep");
			e.printStackTrace();
		}
    }
	private void run_gameLoop() {
    	shape active = genericVariables.get_my_tetris().get_all_shapes().lastElement();
		gameComponents.checkcollisions(active);
		
		if(genericVariables.get_col_left_exists() || genericVariables.get_col_right_exists()) {
			genericVariables.set_frameCounter_collision(genericVariables.get_frameCounter_collision()+1);
		}else {
			genericVariables.set_frameCounter_collision(0);
		}
    	gameComponents.left_event();
    	gameComponents.right_event();
    	gameComponents.down_event();
    	
    	//generate new shape
    	if(!active.get_shape_active() && !genericVariables.get_endGame()) {
    		gameComponents.swap_buffered_shape_to_game();
    	}
		genericVariables.set_frameCounter(genericVariables.get_frameCounter()+1);
	}
	
    @SuppressWarnings("serial")
	protected class DrawPanel extends JPanel 
    {
        	@SuppressWarnings("null")
        	@Override
			protected void paintComponent(Graphics g)
            {
        		//BORDER BLOCKS
            	//Left & Right side blocks
                g.setColor(Color.RED);
                for(int i = 0 ; i < 26 ; i++) {
                	g.fill3DRect(25, 3*25 + (i*25), 25, 25,false);
                	g.fill3DRect(21*25, 3*25 + (i*25), 25, 25,false);
                }
                //bottom side block
                for(int i = 0 ; i < 20 ; i++) {
                	g.fill3DRect(2*25 + (i*25), 28*25, 25, 25,false);
                }
                g.setColor(Color.BLACK);
                
                //drawings for all frames.
            	draw_Grid(g);
            	//draw_GameInfo(g);
            	draw_Shapes(g);
            	draw_Buffer(g);
        		if(genericVariables.get_started() && (genericVariables.get_pause() || genericVariables.get_endGame()))pause_gameLoop(g);
        		if(!genericVariables.get_started())g.drawImage(genericVariables.get_view_welcome_image(), 0, 0, genericVariables.get_gw_WIDTH()-325, genericVariables.get_gw_HEIGHT()-25, Color.WHITE, null);
            }
            
            private void draw_Shapes(Graphics g) {
            	//string are created with 16 characters for indexing think it 0-15 each 4 grouped character dedicated to a single line.think like 4x4 matrix compressed a string value to get it easier.
            	//drawing formula from string to graphic like : shape type "I": AXXX-AXXX-AXXX-AXXX
            	int draw_x;
            	int draw_y;
            	int size = 25;
            	
            	Vector<shape>my_shapes = genericVariables.get_my_tetris().get_all_shapes();
            	//draw each boxes.
            	for(int k = 0 ; k < my_shapes.size();k++) {
            		if(my_shapes.get(k) != null) {
            			for(int i = 0 ; i < 4 ; i++) {
            				if(my_shapes.get(k).sh_boxes.get(i) != null)
            				{
            					draw_x = my_shapes.get(k).sh_boxes.get(i).get_box_x();
                    			draw_y = my_shapes.get(k).sh_boxes.get(i).get_box_y();
                    			switch(my_shapes.get(k).get_shape_type()) {
                    				case "I": g.setColor(Color.GREEN);break;
        	            				case "L":g.setColor(Color.MAGENTA);break;
        		            				case "J":g.setColor(Color.GRAY);break;
        			            				case "O":g.setColor(Color.BLUE);break;
        				            				case "T":g.setColor(Color.YELLOW);break;
        					            				case "S":g.setColor(Color.CYAN);break;
        						            				case "Z":g.setColor(Color.ORANGE);break;
                    			}
                				g.fill3DRect(draw_x, draw_y, size, size,false);
                				//g.drawString(Integer.toString(i), draw_x+3, draw_y+17); //draws box numbers.
            				}
                		}
            		}
            		
            	}
            }
            
            private void draw_Buffer(Graphics g) {
            	//buffer frame vertical edges
            	g.setColor(Color.MAGENTA);
            	for(int i = 1 ; i <= 18 ; i++) {
            		g.fill3DRect(600, 50+(i*25), 25, 25,false);
            		g.fill3DRect(825, 50+(i*25), 25, 25,false);
            	}
            	//buffer frame horizontal edges
            	for(int i = 1 ; i <= 8 ; i++) {
            		g.fill3DRect(600+(i*25), 75, 25, 25,false);
            		g.fill3DRect(600+(i*25), 500, 25, 25,false);
            	}
            	//buffer frame background
            	g.setColor(Color.DARK_GRAY);
            	g.fill3DRect(625, 100, 200, 400,false);
            	/*g.fill3DRect(625, 50, 200, 25,false);
            	g.fill3DRect(825, 50, 25, 400,false);
            	g.fill3DRect(625, 425, 200, 25,false);
            	*/
            	int draw_x;
            	int draw_y;
            	int size = 25;
            	
            	Vector<shape>my_shapes = gameComponents.get_buffered_shapes();
            	//draw each boxes.
            	for(int k = 0 ; k < my_shapes.size();k++) {
            		if(my_shapes.get(k) != null) {
            			for(int i = 0 ; i < 4 ; i++) {
            				if(my_shapes.get(k).sh_boxes.get(i) != null)
            				{
            					draw_x = my_shapes.get(k).sh_boxes.get(i).get_box_x();
                    			draw_y = my_shapes.get(k).sh_boxes.get(i).get_box_y();
                    			switch(my_shapes.get(k).get_shape_type()) {
                    				case "I": g.setColor(Color.GREEN);break;
        	            				case "L":g.setColor(Color.MAGENTA);break;
        		            				case "J":g.setColor(Color.GRAY);break;
        			            				case "O":g.setColor(Color.BLUE);break;
        				            				case "T":g.setColor(Color.YELLOW);break;
        					            				case "S":g.setColor(Color.CYAN);break;
        						            				case "Z":g.setColor(Color.ORANGE);break;
                    			}
                				g.fill3DRect((draw_x + 420), (draw_y + k*125 +100), size, size,false);
                				//g.drawString(Integer.toString(i), draw_x+3, draw_y+17);
            				}
                		}
            		}
            		
            	}
            }
            
            private void draw_Grid(Graphics g) {
            	//Draw Grid with 25x25 blocks and shows line numbers
                g.setColor(Color.BLACK);
            	int x = 0;
            	int y = 0;
            	for(int i = 0; i < genericVariables.get_gw_HEIGHT()/25 ; i++) {
            		g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 11));
            		//show line numbers
            		if(i <= 25) {
            			g.drawString(Integer.toString(i), 5, y+90);
            			g.drawString(Integer.toString(i), 22*25+5, y+90);
            		}
            		//draw grid horizontal lines
                    if(i < 28 && i >= 4 ) g.drawLine(50, y,genericVariables.get_gw_WIDTH() -375,y);
                    //draw grid vertical lines
                    if(i < 21 && i >= 3) g.drawLine(x, 75,x ,genericVariables.get_gw_HEIGHT()-100);
                    //intervals
                    
                    y+=25;
                    x+=25;
            	}
            }
            private void draw_GameInfo(Graphics g) {
            	//this block written to check game mechanics are working well or not by seeing all the values about.
            	g.setColor(Color.BLACK);
            	int x = 23*25+3;
            	int y = 19;
            	//int size = genericVariables.get_my_tetris().get_all_shapes().size()-1;
        		g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 15));
        		

            	//temporary section
            	g.drawString("left : "+ genericVariables.get_left(), x, y);
            	g.drawString("right : "+ genericVariables.get_right(), x, y+=25);
            	g.drawString("down : "+ genericVariables.get_down(), x, y+=25);
            	g.drawString("rotate_available : " + genericVariables.get_rotate_available(), x, y+=25);
        		g.drawString("shape_active : " + gameComponents.get_my_tetris().get_all_shapes().lastElement().get_shape_active(), x, y+=25);
        		g.drawString("shape_type : " + gameComponents.get_my_tetris().get_all_shapes().lastElement().get_shape_type(), x, y+=25);
        		g.drawString("shape_type : " + gameComponents.get_my_tetris().get_all_shapes().lastElement().get_shape_type(), x, y+=25);
        		g.drawString("all_shape_cnt : " + genericVariables.get_shape_cnt(), x, y+=25);
        		g.drawString("shape_id : " + gameComponents.get_my_tetris().get_all_shapes().lastElement().get_shape_id(), x, y+=25);
        		g.drawString("frameCounter : " + genericVariables.get_frameCounter(), x, y+=25);
        		g.drawString("frameCounter_Left : " + genericVariables.get_frameCounter_left(), x, y+=25);
            	
            	
            	//select active shape
            	/*shape sh_last_index = null;
            	if(genericVariables.get_my_tetris().get_all_shapes().size() > 0 )
            		sh_last_index = genericVariables.get_my_tetris().get_all_shapes().lastElement();
        		//Draw active shape boxes informations
        		g.drawString("ACTIVE SHAPE  SHAPE NO --> "+ size, x, y);
        		y+=25;
        		//Draw active shape type
        		g.drawString("ACTIVE SHAPE  SHAPE TYPE --> "+ sh_last_index.get_shape_type(), x, y);
        		y+=25;
        		
        		for(int i = 0 ; i< 4 ; i++) {
        			g.drawString("shape["+size+"].(x,y) : (" +
        						Integer.toString(sh_last_index.get_shape_loc_X())
        						+","+
        						Integer.toString(sh_last_index.get_shape_loc_Y())+")", x, y);
        			y+=25;
        		}*/
        		//Draw passive boxes locations on right bar
        		/*g.drawString("PASSIVE SHAPE LIST ~ TOTAL --> "+ size, x, y);
        		int k;
        		if(genericVariables.get_my_tetris().get_all_shapes().size() >= 5 )
        			k = genericVariables.get_my_tetris().get_all_shapes().size()-5;
        		else 
        			k = 0;
        		for(; k< genericVariables.get_my_tetris().get_all_shapes().size() ; k++) {
        			y+=25;
        			if(genericVariables.get_my_tetris().get_all_shapes().get(k) != null)
        			{
            			for(int i = 0 ; i< 4 ; i++) {
            				if(genericVariables.get_my_tetris().get_all_shapes().get(k).sh_boxes.get(i) != null)
            				{
                				g.drawString("shape["+k+"]box["+i+"].(x,y) : (" +
                						Integer.toString(genericVariables.get_my_tetris().get_all_shapes().get(k).sh_boxes.get(i).get_box_x())
                						+","+
                						Integer.toString(genericVariables.get_my_tetris().get_all_shapes().get(k).sh_boxes.get(i).get_box_y())
                						+")", 
                						x, y);
                				y+=25;
            					
            				}
            			}
        				
        			}
        		}*/
        	}
            
            @SuppressWarnings("null")
        	private void pause_gameLoop(Graphics g) {
        		int pause_x = 200;
        		int pause_y = 150;
        		int line_space = 50;
        		int tab_space = 30;
        		String menu_title = null;

        		g.setColor(Color.GRAY);
        		g.fill3DRect(pause_x, pause_y, 250, 300, false);
        		g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 25));

        		
        		switch(genericVariables.get_game_state()) {
        		case "paused" :

            		g.setColor(Color.WHITE);
        			g.drawString("! PAUSED !", pause_x+tab_space+25 , pause_y+line_space);
            		
        			g.setColor(Color.YELLOW);
            		g.fill3DRect(pause_x, pause_y+line_space*2-25, 250, 5, false);
            		
        			if(genericVariables.get_pause_selection() == 0){
            			g.setColor(Color.YELLOW);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*3-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("RESUME", pause_x+tab_space+40 , pause_y+line_space*3);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("RESUME", pause_x+tab_space+40 , pause_y+line_space*3);
            		}
        			
        			if(genericVariables.get_pause_selection() == 1) {
            			g.setColor(Color.YELLOW);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*4-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*4);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*4);
            		}
            		
        			if(genericVariables.get_pause_selection() == 2) {
            			g.setColor(Color.YELLOW);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*5-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("EXIT", pause_x+tab_space+60 , pause_y+line_space*5);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("EXIT", pause_x+tab_space+60 , pause_y+line_space*5);
            		}
            		
            		gameComponents.pause_menu_select_func();
            		break;
            	
        		case "end":
            		g.setColor(Color.WHITE);
        			g.drawString("! END !", pause_x+tab_space+50 , pause_y+line_space);
            		
        			g.setColor(Color.YELLOW);
            		g.fill3DRect(pause_x, pause_y+line_space*2-25, 250, 5, false);
            		
        			if(genericVariables.get_pause_selection() == 0){
            			g.setColor(Color.YELLOW);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*3-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*3);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*3);
            		}
            		
            		if(genericVariables.get_pause_selection() == 1) {
            			g.setColor(Color.YELLOW);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*4-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("EXIT", pause_x+tab_space+60 , pause_y+line_space*4);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("EXIT", pause_x+tab_space+60 , pause_y+line_space*4);
            		}
            		
            		gameComponents.pause_menu_select_func();
        			break;
        		default :
        			System.err.println("ERROR : unkown game state");
        			System.err.println("genericVariables.get_game_state() called : " + genericVariables.get_game_state());
        			break;
        		}
        	}
    }
}