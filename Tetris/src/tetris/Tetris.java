package tetris;
/*
 * Version No : 0.16
 * Version Notes : 
 *  @Author : Taner Esmeroğlu
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;





class Tetris extends genericVariables
{
    public static void main(String... args)
    {
    	new Tetris().go();
    }
    private void go()
    {
    	genericVariables.set_game_startedTimeStamp(Calendar.getInstance());
    	URL url_tetris_menu = Tetris.class.getResource("tetris_menu.png"); //gets the folder/file from runnable jar file location
		URL url_tetris_intro = Tetris.class.getResource("tetris_intro.gif"); 
		URL url_tetris_bg = Tetris.class.getResource("tetris_bg.png"); 

		genericVariables.set_view_welcome_image(new ImageIcon(url_tetris_menu).getImage());
		genericVariables.set_view_intro_image(new ImageIcon(url_tetris_intro).getImage());
		genericVariables.set_view_bg_image(new ImageIcon(url_tetris_bg).getImage());
		
		
    	//this block to configure window settings 
        genericVariables.set_frame(new JFrame("Tetris"));
        genericVariables.get_frame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        genericVariables.get_frame().addKeyListener(new ActionListener());
        genericVariables.set_drawPanel(new DrawPanel());
        

        genericVariables.get_frame().getContentPane().add(BorderLayout.CENTER, genericVariables.get_drawPanel());

        genericVariables.get_frame().setResizable(false);
        genericVariables.get_frame().setUndecorated(true);
        genericVariables.get_frame().setSize(400, 200);
        genericVariables.get_frame().setLocation(genericVariables.get_s_WIDTH()/ 3, genericVariables.get_s_HEIGHT()/3-50);
        genericVariables.get_frame().setVisible(true);
        moveIt();
    }
    
    private void moveIt()
    {
    	//start a game with generating a new shape
    	if(genericVariables.get_game_state() == "running" || genericVariables.get_game_state() == "loading")
    	gameComponents.swap_buffered_shape_to_game();
    	
    	//endless loop for game running.
        while (true)
        {
        	//restarting
        	if(genericVariables.get_restartGame()){
        		genericVariables.set_my_tetris(new gameComponents());
        		genericVariables.reset();
        		genericVariables.set_game_startedTimeStamp(Calendar.getInstance());
        		moveIt();
        	}else {

            	//game running state
            	if(!genericVariables.get_endGame() && !genericVariables.get_pause() && genericVariables.get_started()) { 
            		run_gameLoop();
                    
            	}else { //game paused state
            		if(gameComponents.get_exit_game()) {
            			break;
            		}
            	}
            	sleep(genericVariables.get_sleep_time());
            	
            	genericVariables.get_frame().repaint();
        	}
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
		if(genericVariables.get_my_tetris().get_all_shapes() != null) {
			if(genericVariables.get_down()) {
				genericVariables.set_speed_down(genericVariables.get_speed_game()/10);
			}
			
			shape active = genericVariables.get_my_tetris().get_all_shapes().lastElement();
			
			if(genericVariables.get_col_left_exists() || genericVariables.get_col_right_exists()) {
				genericVariables.set_frameCounter_collision(genericVariables.get_frameCounter_collision()+1);
			}else {
				genericVariables.set_frameCounter_collision(0);
			}

			if(active.get_shape_active()) {
				
				if(genericVariables.get_directDown()) {
					gameComponents.directDown_event();
				}else {
					gameComponents.checkcollisions(active);
					if(!genericVariables.get_col_left_exists())gameComponents.left_event();
			    	if(!genericVariables.get_col_right_exists())gameComponents.right_event();

					gameComponents.checkcollisions(active);
					gameComponents.down_event();
				}
				
		    		
			}
	    	
	    	//generate new shape
	    	if(!active.get_shape_active() && !genericVariables.get_endGame()) {
	    		gameComponents.swap_buffered_shape_to_game();
	    	}
			genericVariables.set_frameCounter(genericVariables.get_frameCounter()+1);
		}
	}
	
    @SuppressWarnings("serial")
	protected class DrawPanel extends JPanel 
    {
        	@SuppressWarnings("null")
        	@Override
			protected void paintComponent(Graphics g)
            {
        		draw_Grid(g);
        		draw_MainBorders(g);
        		
                //drawings for all frames.)
            	
            	draw_Shapes(g);
            	draw_Buffer(g);
            	draw_Score(g);

    			
            	
        		if(genericVariables.get_started() && (genericVariables.get_pause() || genericVariables.get_endGame()))pause_gameLoop(g);
        		if(!genericVariables.get_started() && genericVariables.get_game_state() == "loading"){
        	        genericVariables.get_frame().setSize(400, 200);
        	        genericVariables.get_frame().setLocation(genericVariables.get_s_WIDTH()/ 3-50, genericVariables.get_s_HEIGHT()/3-50);
        			Calendar introTimeStamp = Calendar.getInstance();
        			long millisGameStarted = game_startedTimeStamp.getTimeInMillis();
        			long millisIntro = introTimeStamp.getTimeInMillis();
        			int timeInterval = (int) ((millisIntro - millisGameStarted)/1000);
        			if(timeInterval < 2) {
        				g.drawImage(genericVariables.view_intro_image, 0, 0, genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT(), null, null);
        			}
        			else {
            	        genericVariables.get_frame().setSize(genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT());
            	        genericVariables.get_frame().setLocation(genericVariables.get_s_WIDTH()/ 4-50, 0);
        				genericVariables.set_game_state("welcome");
        			}
    			}
        		if(!genericVariables.get_started() && genericVariables.get_game_state() == "welcome") {
    				g.drawImage(genericVariables.get_view_welcome_image(), 0, 0, genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT(), Color.WHITE, null);
        		}
            }
            
        	private void draw_MainBorders(Graphics g) {
        		int size = 25;
        		Color color_back = Color.BLACK;
        		Color color_front = new Color(232, 104, 104);
        		
        		g.drawImage(genericVariables.get_view_bg_image(), 0, 0, genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT(), Color.WHITE, null);
            	
        		
        		
        		//draw container for score, buffer and game area
            	g.setColor(new Color(104, 155, 232));
            	g.fillRect(25-3, 3*25-3, 33*25+6, 26*25+6);
            	
            	//paint game area background
            	g.setColor(Color.DARK_GRAY);
            	g.fillRect(50, 3*25, 19*25, 25*25);
        		
        		//BORDER BLOCKS
                //Left & Right side blocks
                for(int i = 0 ; i < 26 ; i++) {
                	g.setColor(color_back);
                	g.fillRect(size, 3*size + (i*size), size, size);
                	g.fillRect(21*size, 3*size + (i*size), size, size);
                	
                	g.setColor(color_front);
                	g.fill3DRect(size+1, 3*size + (i*size)+1, size-2, size-2,true);
                	g.fill3DRect(21*size+1, 3*size + (i*size)+1, size-2, size-2,true);
                	
                	if(i < 20) {
                		g.setColor(color_back);
                		g.fillRect(2*size + (i*size), 28*size, size, size);
                		g.setColor(color_front);
                		g.fill3DRect(2*size + (i*size)+1, 28*size+1, size-2, size-2,true);
                	}
                }
                //game screen border deactivated.
               /* int size_small = 20;
            	int right_end = genericVariables.get_gw_WIDTH();
            	int bottom_end = genericVariables.get_gw_HEIGHT();
                
            	g.setColor(color_back);
            	//verticals
            	g.fillRect(0,0, size_small , bottom_end);
            	g.fillRect(right_end-size_small,0,size_small,bottom_end);
            	//horizontals
            	g.fillRect(size_small, 0, right_end-size_small, size_small);
            	g.fillRect(size_small, bottom_end-size_small, right_end-size_small, size_small);
            	
            	g.setColor(new Color(104, 232, 159));
                for(int i = 0 ; i < right_end/size_small; i++) {
                	//verticals
                	if(i < bottom_end/size_small) {
                		g.fill3DRect(0+1,(i*size_small)+1, size_small-2, size_small-2,true);
                    	g.fill3DRect(right_end-size_small+1,(i*size_small)+1, size_small-2, size_small-2,true);
                	}
                	//horizontals
                	g.fill3DRect((i*size_small)+1,0+1,size_small-2,size_small-2,true);
                	g.fill3DRect((i*size_small)+1,bottom_end-size_small+1, size_small-2, size_small-2,true);
        		}*/
			}
        	
        	int font_size = 0;
        	int max_font_size = 40;
			private void draw_Score(Graphics g) {
				int size = 25;
				Color color_back = Color.black;
				Color color_front = new Color(232, 232, 104);
				
    			g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 30));
    			
    			
            	for(int i = 1 ; i <= 8 ; i++) {
            		//score frame vertical edges
            		if(i <= 5) {
                    	g.setColor(color_back);
                    	g.fillRect(600, 575+(i*size), size, size);
                    	g.fillRect(825, 575+(i*size), size, size);

                    	g.setColor(color_front);
                    	g.fill3DRect(600+1, 575+(i*size)+1, size-2, size-2,true);
                    	g.fill3DRect(825+1, 575+(i*size)+1, size-2, size-2,true);
            		}
            		//score frame horizontal edges
            		g.setColor(color_back);
                	g.fillRect(600+(i*size), 600, size, size);
                	g.fillRect(600+(i*size), 700, size, size);
                	
                	g.setColor(color_front);
            		g.fill3DRect(600+(i*size)+1, 600+1, size-2, size-2,true);
            		g.fill3DRect(600+(i*size)+1, 700+1, size-2, size-2,true);
            	}
            	//score frame background
            	g.setColor(Color.DARK_GRAY);
            	g.fillRect(625, 625, 200, 75);
            	
            	//point
            	String score = Integer.toString(genericVariables.get_score());
            	g.setColor(Color.white);
            	g.drawString(score, 675, 670);
            	

				g.setColor(Color.GREEN);
            	g.setFont(new Font(genericVariables.get_font_type(),Font.BOLD,font_size));
    			if(genericVariables.get_score_add() > 0) {
    				Calendar addedScoreTimeStamp = Calendar.getInstance();
        			int timeInterval = (int) ((addedScoreTimeStamp.getTimeInMillis() - genericVariables.get_score_addTimeStamp().getTimeInMillis())/1000);
        			if(timeInterval < 2) {
        				g.drawString("+" +Integer.toString(genericVariables.get_score_add()), 675, (670 - (font_size*2)));
        				if(font_size < max_font_size) {
        					font_size++;
        				}
        			}else
        				font_size = 0;
    			}
            	
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
                    			g.setColor(Color.BLACK);
                    			g.fillRect(draw_x, draw_y, size, size);
                    			switch(my_shapes.get(k).get_shape_type()) {
                    				case "I": g.setColor(Color.GREEN);break;
        	            				case "L":g.setColor(Color.MAGENTA);break;
        		            				case "J":g.setColor(Color.GRAY);break;
        			            				case "O":g.setColor(Color.BLUE);break;
        				            				case "T":g.setColor(Color.YELLOW);break;
        					            				case "S":g.setColor(Color.CYAN);break;
        						            				case "Z":g.setColor(Color.ORANGE);break;
                    			}
                    			g.fill3DRect(draw_x+1, draw_y+1, size-2, size-2,true);
                    			//g.fillRect(draw_x+1, draw_y+1, size-2, size-2);
                				//g.drawString(Integer.toString(i), draw_x+3, draw_y+17); //draws box numbers.
            				}
                		}
            		}
            		
            	}            	
            }
            
            private void draw_Buffer(Graphics g) {
            	int size = 25;
            	int draw_x;
            	int draw_y;
            	
            	Color color_back = Color.black;
            	Color color_front = new Color(115, 232, 104);
            	for(int i = 1 ; i <= 18 ; i++) {
                	//buffer frame horizontal edges
            		if(i <= 8) {
                    	g.setColor(color_back);
                    	g.fillRect(600+(i*size), 75, size, size);
                    	g.fillRect(600+(i*size), 500, size, size);

                    	g.setColor(color_front);
                    	g.fill3DRect(600+(i*size)+1, 75+1, size-2, size-2,true);
                    	g.fill3DRect(600+(i*size)+1, 500+1, size-2, size-2,true);
            		}
                	//buffer frame vertical edges
                	g.setColor(color_back);
                	g.fillRect(600, 50+(i*size), size, size);
                	g.fillRect(825, 50+(i*size), size, size);

                	g.setColor(color_front);
                	g.fill3DRect(600+1, 50+(i*size)+1, size-2, size-2,true);
                	g.fill3DRect(825+1, 50+(i*size)+1, size-2, size-2,true);
            	}
            	//buffer frame background
            	g.setColor(Color.DARK_GRAY);
            	g.fillRect(625, 100, 200, 400);
            	
            	Vector<shape>my_shapes = gameComponents.get_buffered_shapes();
            	//draw each boxes.
            	for(int k = 0 ; k < my_shapes.size();k++) {
            		if(my_shapes.get(k) != null) {
            			for(int i = 0 ; i < 4 ; i++) {
            				if(my_shapes.get(k).sh_boxes.get(i) != null)
            				{
            					draw_x = my_shapes.get(k).sh_boxes.get(i).get_box_x();
                    			draw_y = my_shapes.get(k).sh_boxes.get(i).get_box_y();
                    			g.setColor(Color.BLACK);
                    			g.fillRect((draw_x + 420), (draw_y + k*125 +100), size, size);
                    			switch(my_shapes.get(k).get_shape_type()) {
                    				case "I": g.setColor(Color.GREEN);break;
        	            				case "L":g.setColor(Color.MAGENTA);break;
        		            				case "J":g.setColor(Color.GRAY);break;
        			            				case "O":g.setColor(Color.BLUE);break;
        				            				case "T":g.setColor(Color.YELLOW);break;
        					            				case "S":g.setColor(Color.CYAN);break;
        						            				case "Z":g.setColor(Color.ORANGE);break;
                    			}
                    			g.fill3DRect((draw_x + 420)+1, (draw_y + k*125 +100)+1, size-2, size-2,true);
                				//g.fill3DRect((draw_x + 420), (draw_y + k*125 +100), size, size,false);
                				//g.drawString(Integer.toString(i), draw_x+3, draw_y+17);
            				}
                		}
            		}
            		
            	}
            }
            
            private void draw_Grid(Graphics g) {
            	//Draw Grid with 25x25 blocks and shows line numbers
            	
                //paint grid lines
            	g.setColor(Color.BLACK);
            	int x = 0;
            	int y = 0;
            	for(int i = 0; i < genericVariables.get_gw_HEIGHT()/25 ; i++) {
            		g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 11));
            		//show line numbers
            		/*
            		if(i <= 25) {
            			g.drawString(Integer.toString(i), 5, y+90);
            			g.drawString(Integer.toString(i), 22*25+5, y+90);
            		}*/
            		//draw grid horizontal lines
                    if(i < 28 && i >= 4 ) g.drawLine(50, y,genericVariables.get_gw_WIDTH() -375,y);
                    //draw grid vertical lines
                    if(i < 21 && i >= 3) g.drawLine(x, 75,x ,genericVariables.get_gw_HEIGHT()-100);
                    //intervals
                    
                    y+=25;
                    x+=25;
            	}
            }
            
            @SuppressWarnings("null")
        	private void pause_gameLoop(Graphics g) {
            	Color color_back = Color.BLACK;
            	Color color_front = new Color(104, 106, 232);
            	Color color_selection = new Color(229, 232, 104);
            	
            	int size = 25;
        		int pause_x = 200;
        		int pause_y = 150;
        		int line_space = 47;
        		int tab_space = 47;
        		
        		g.setColor(new Color(163, 104, 232));
        		g.fillRect(pause_x, pause_y, 250, 300);
        		g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 25));
        		
        		//pause frame
        		for(int i = 0 ; i <= 11 ; i++) {
            		//pause frame horizontal edges
            		if(i <= 8) {
                    	g.setColor(color_back);
                    	g.fillRect(pause_x+(size*i)+25, pause_y, size, size );
                    	g.fillRect(pause_x+(size*i)+25, pause_y+275, size, size);

                    	g.setColor(color_front);
                    	g.fill3DRect(pause_x+(size*i)+1+25, pause_y+1, size-2, size-2 ,true);
                    	g.fill3DRect(pause_x+(size*i)+25+1, pause_y+275+1, size-2, size-2,true);
            		}

                	//pause frame vertical edges
                	g.setColor(color_back);
                	g.fillRect(pause_x, pause_y+(size*i), size, size );
                	g.fillRect(pause_x+250, pause_y+(size*i), size, size);

                	g.setColor(color_front);
                	g.fill3DRect(pause_x+1, pause_y+(size*i)+1, size-2, size-2,true);
                	g.fill3DRect(pause_x+250+1, pause_y+(size*i)+1, size-2, size-2,true);
        		}

    			g.setColor(new Color(219, 232, 104));
        		g.fillRect(pause_x+25, pause_y+line_space*2, 225, 5);
        		
        		switch(genericVariables.get_game_state()) {
        		case "paused" :

            		g.setColor(Color.WHITE);
        			g.drawString("! PAUSED !", pause_x+tab_space+27 , pause_y+line_space+20);
            		
            		
        			if(genericVariables.get_pause_selection() == 0){
            			g.setColor(color_selection);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*3-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("RESUME", pause_x+tab_space+40 , pause_y+line_space*3);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("RESUME", pause_x+tab_space+40 , pause_y+line_space*3);
            		}
        			
        			if(genericVariables.get_pause_selection() == 1) {
            			g.setColor(color_selection);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*4-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*4);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*4);
            		}
            		
        			if(genericVariables.get_pause_selection() == 2) {
            			g.setColor(color_selection);
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
        			g.drawString("! END !", pause_x+tab_space+52 , pause_y+line_space+20);
            		
        			if(genericVariables.get_pause_selection() == 0){
            			g.setColor(color_selection);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*3-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*3);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("RESTART", pause_x+tab_space+35 , pause_y+line_space*3);
            		}
            		
            		if(genericVariables.get_pause_selection() == 1) {
            			g.setColor(color_selection);
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