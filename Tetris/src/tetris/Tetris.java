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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
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
    	URL url_tetris_menu = Tetris.class.getResource("tetris_welcome.png"); //gets the folder/file from runnable jar file location
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
        /*genericVariables.get_frame().setSize(400, 200);
        genericVariables.get_frame().setLocation(genericVariables.get_s_WIDTH()/ 3, genericVariables.get_s_HEIGHT()/3-50);*/
        genericVariables.get_frame().setSize(900, 800);
        genericVariables.get_frame().setLocation(400, 0);
        genericVariables.get_frame().setVisible(true);
        moveIt();
    }
    
    private void moveIt()
    {
    	//genericVariables.set_game_state("highscore");
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
            		gameComponents.pause_menu_select_func();
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
            	//game states : welcome, running, paused , end, exit , loading , highscore
            	switch(genericVariables.get_game_state()) {
            		case "welcome":
            			if(!genericVariables.get_started()) {
            				draw_WelcomePage(g);
                    		//draw_highScorePage(g);
                		}
            			break;
            		case "running":
            			if(genericVariables.get_pause())
            				genericVariables.set_game_state("paused");
            			else if(genericVariables.get_endGame())
            				genericVariables.set_game_state("end");
            			else {
                    		draw_Grid(g);
                    		draw_MainBorders(g);
                        	draw_Shapes(g);
                        	draw_Buffer(g);
                        	draw_Score(g);
            			}
            			break;
            		case "paused":
            				pause_gameLoop(g);
            			break;
            		case "end":
            				pause_gameLoop(g);
            			break;
            		case "exit":
            			
            			break;
            		case "loading":
            			if(!genericVariables.get_started()){
							gameComponents.read_highscore_file();
                	        genericVariables.get_frame().setSize(400, 200);
                	        genericVariables.get_frame().setLocation(genericVariables.get_s_WIDTH()/ 3-50, genericVariables.get_s_HEIGHT()/3-50);
                			Calendar introTimeStamp = Calendar.getInstance();
                			long millisGameStarted = genericVariables.get_game_startedTimeStamp().getTimeInMillis();
                			long millisIntro = introTimeStamp.getTimeInMillis();
                			int timeInterval = (int) ((millisIntro - millisGameStarted)/1000);
                			if(timeInterval < 2) {
                				g.drawImage(genericVariables.get_view_intro_image(), 0, 0, 400, 200, null, null);
                			}
                			else {
                    	        genericVariables.get_frame().setSize(genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT());
                    	        genericVariables.get_frame().setLocation(genericVariables.get_s_WIDTH()/ 4-50, 0);
                				genericVariables.set_game_state("welcome");
                			}
            			}
            			break;
            		case "highscore":
            				draw_highScorePage(g);
            			break;
            	}
            }
        	
        	private void draw_WelcomePage(Graphics g) {
				g.drawImage(genericVariables.get_view_welcome_image(), 0, 0, genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT(), Color.WHITE, null);
				
				int size = 10;
        		int pos_x = 250;
        		int pos_y = 350;
        		Color color_back = new Color(255, 161, 0);
        		Color color_front = Color.WHITE;
        		Color color_highlighted = new Color(146, 63, 255);
        		int interval = 100;
        		for(int i = 0; i < 3 ; i++) {
        			if(genericVariables.get_pause_selection() == i) {
        				g.setColor(color_back);
        				color_front = color_highlighted;
        			}else {
        				g.setColor(Color.BLACK);
        				color_front = Color.WHITE;
        			}
        				
        			g.fillRect(pos_x+10, pos_y+20+(interval*i), 290, 50);
    				draw_tetroline(g, pos_x , pos_y +(interval*i), 7, true, Color.BLACK, color_front, size);
    				draw_tetroline(g, pos_x+300, pos_y+(interval*i), 7, true, Color.BLACK, color_front, size);
    				draw_tetroline(g, pos_x, pos_y+(interval*i), 29, false, Color.BLACK, color_front, size);
    				draw_tetroline(g, pos_x, pos_y+60+(interval*i), 29, false, Color.BLACK, color_front, size);
        		}

    			g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 30));
        		if(genericVariables.get_pause_selection() == 0) {
        			g.setColor(color_highlighted);
            		g.drawString("PLAY", pos_x+115, pos_y+55);
            		g.setColor(new Color(84, 255, 0));
            		g.drawString("INSTRUCTIONS", pos_x+40, pos_y+155);
            		g.drawString("EXIT", pos_x+115, pos_y+255);
        		}else if(genericVariables.get_pause_selection() == 1) {
        			g.setColor(color_highlighted);
        			g.drawString("INSTRUCTIONS", pos_x+40, pos_y+155);
            		g.setColor(new Color(84, 255, 0));
            		g.drawString("PLAY", pos_x+115, pos_y+55);
            		g.drawString("EXIT", pos_x+115, pos_y+255);
        		}else if(genericVariables.get_pause_selection() == 2) {
        			g.setColor(color_highlighted);
        			g.drawString("EXIT", pos_x+115, pos_y+255);
        			g.setColor(new Color(84, 255, 0));
            		g.drawString("PLAY", pos_x+115, pos_y+55);
            		g.drawString("INSTRUCTIONS", pos_x+40, pos_y+155);
        		}
        			
        	}
        	
			private void draw_highScorePage(Graphics g) {
				g.drawImage(genericVariables.get_view_bg_image(), 0, 0, genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT(), null, null);
    			g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 30));
				int pos_x = 250;
				int pos_y = 250;
				int width = 60;
				int height = 50;
				int interval = 80;
				int char_distance = 15;
				String temp = "";
				int score = genericVariables.get_score();
				Color color_back;
				Color color_front;
				Color highlighted_username_color = new Color(255, 50, 74);
				//title section
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 50 , genericVariables.get_gw_WIDTH()-1, 170);
				g.setColor(new Color(65, 244, 121));
				g.drawString("Your Score : " + Integer.toString(score), 300, 115);
				//title section tetroborders
				color_back = Color.BLACK;
				color_front = new Color(150, 45, 81);
				draw_tetroline(g, 0, 25, 7, true, color_back, color_front, genericVariables.get_tetrobox_size());
				draw_tetroline(g, genericVariables.get_gw_WIDTH()-25, 25, 7, true,color_back , color_front, genericVariables.get_tetrobox_size());
				draw_tetroline(g, 0, 25, genericVariables.get_gw_WIDTH()/25-2, false, color_back, color_front, genericVariables.get_tetrobox_size());
				draw_tetroline(g, 0, 175, genericVariables.get_gw_WIDTH()/25-2, false, color_back, color_front, genericVariables.get_tetrobox_size());
				
				color_back = Color.BLACK;
				color_front = new Color(97, 13, 193);

				//background of highscore table
				g.setColor(Color.DARK_GRAY);
				g.fillRect(pos_x-150, pos_y+100, 700, 375);
				g.setColor(Color.BLACK);
				g.drawString("{ no }           { name }               { score }", pos_x-100, pos_y+135);
				g.fillRect(pos_x-150, pos_y+155, 700, 4);
				
				draw_tetroline(g, pos_x-175, pos_y+50, 17, true, color_back, color_front, genericVariables.get_tetrobox_size());
				draw_tetroline(g, pos_x-175+700, pos_y+50, 17, true, color_back, color_front, genericVariables.get_tetrobox_size());
				draw_tetroline(g, pos_x-175, pos_y+50, 27, false, color_back, color_front, genericVariables.get_tetrobox_size());
				draw_tetroline(g, pos_x-175, pos_y+450, 27, false, color_back, color_front, genericVariables.get_tetrobox_size());
				
    			if(gameComponents.ch_order < 5) {
    				for(int i = 0 ; i < 5 ; i++) {
    					//draw small tetroborders
    					if(i == gameComponents.ch_order) color_front = Color.RED;
    					else color_front = new Color(97, 13, 193);
    					draw_tetroline(g, pos_x-10 +(i*interval), pos_y-20, 7, true, color_back, color_front, 10);
    					draw_tetroline(g, pos_x+width +(i*interval), pos_y-20, 7, true, color_back, color_front, 10);
    					draw_tetroline(g, pos_x-10 + (i*interval), pos_y-20, 6, false, color_back, color_front, 10);
    					draw_tetroline(g, pos_x-10 + (i*interval), pos_y+height-10, 6, false, color_back, color_front, 10);
    					
    					
    					g.setColor(Color.GREEN);
    					g.fill3DRect(pos_x + (interval*i), pos_y, width ,height ,true);
    					g.setColor(Color.BLACK);
    					for(int k = 0 ; k < genericVariables.get_user_name().length() ; k++) {
    						temp += genericVariables.get_user_name().charAt(k);
    						g.drawString(temp, pos_x+(interval*k)+char_distance, pos_y+35);
    						temp = "";
    					}
    				}
    				if(genericVariables.get_up()) g.setColor(Color.RED);
    				else g.setColor(Color.ORANGE);
    				g.fill3DRect(pos_x + (interval*gameComponents.ch_order), pos_y, width ,height ,true);
    				g.setColor(Color.BLACK);
    				temp += gameComponents.ch_temp;
    				g.drawString(temp,pos_x+(gameComponents.ch_order*interval)+char_distance, pos_y+35);
    				
    				gameComponents.high_score_name_writing();
    			}else{
    				genericVariables.set_up(false);
    				genericVariables.set_enter(false);
    				
    				g.setColor(highlighted_username_color);
    				g.drawString(genericVariables.get_user_name(), 350, 160);
    				
    				
    				if(!genericVariables.get_record_done()) {
    					gameComponents.record_highScore();
    					gameComponents.read_highscore_file();
    				}
    			}
    			
    			if(genericVariables.get_highscore_file_exists()) {
					highScoreObject hs = gameComponents.get_highscore();
					NavigableMap<Integer, String> map = hs.get_highScoreDataMap().descendingMap();
    				Set<Entry<Integer, String>> set = map.entrySet();
					Iterator<Entry<Integer, String>> itr = set.iterator();
    				int cnt = 0;
    				while(itr.hasNext()) {
    					if(cnt < 10) {
    						Map.Entry<Integer, String> entry = itr.next();
    						if(entry.getValue().equals(genericVariables.get_user_name())) {
    							g.setColor(highlighted_username_color);
    						}else {
    							g.setColor(Color.GRAY);
    						}
        					g.drawString("{ "+(cnt+1) +" }", pos_x-100, pos_y+190+(cnt*30));
        					g.drawString(entry.getValue(), pos_x+90, pos_y+190+(cnt*30));
        					g.drawString(Integer.toString(entry.getKey()), pos_x+370, pos_y+190+(cnt*30));
        					
        					cnt++;	
    					}else {
    						break;
    					}
    				}	
				}else {
					System.out.println("get_high_score_file = " + genericVariables.get_highscore_file_exists());
					System.out.println("get_record_done= " + genericVariables.get_record_done());
				}
    			
    			if(genericVariables.get_record_done()) {
					g.setColor(Color.DARK_GRAY);
					
					g.fillRect(pos_x-20, pos_y-10,420 ,60 );

					draw_tetroline(g, pos_x-30, pos_y-20, 7, true, color_back, color_front, 10);
					draw_tetroline(g, pos_x+400, pos_y-20, 7, true, color_back, color_front, 10);
					draw_tetroline(g, pos_x-30, pos_y-20, 43, false, color_back, color_front, 10);
					draw_tetroline(g, pos_x-30, pos_y+height-10, 43, false, color_back, color_front, 10);

					g.setColor(Color.WHITE);
					g.drawString("PRESS \"ESC\" FOR MENU", pos_x, pos_y+35);
					
    				if(genericVariables.get_esc()) {
    					genericVariables.set_game_state("end");
    					genericVariables.set_esc(false);
    				}
    			}				
			}




			private void draw_MainBorders(Graphics g) {
        		int size = 25;
        		Color color_back = Color.BLACK;
        		Color color_front = new Color(232, 104, 104);
        		
        		g.drawImage(genericVariables.get_view_bg_image(), 0, 0, genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT(), Color.WHITE, null);
            	
            	//paint game area background
            	g.setColor(Color.DARK_GRAY);
            	g.fillRect(50, 3*25, 19*25, 25*25);
        		
            	//verticals main border blocks
            	draw_tetroline(g, 1*size, 2*size, 26, true, color_back, color_front, genericVariables.get_tetrobox_size()); //left
            	draw_tetroline(g, 21*size, 2*size, 26, true, color_back, color_front, genericVariables.get_tetrobox_size()); //right
            	//horizontal bottom main border
            	draw_tetroline(g, size, 27*size, 20, false, color_back, color_front, genericVariables.get_tetrobox_size());
			}
        	
        	int font_size = 0;
        	int max_font_size = 40;
			private void draw_Score(Graphics g) {
				Color color_back = Color.black;
				Color color_front = new Color(232, 232, 104);
				
    			g.setFont(new Font(genericVariables.get_font_type(), Font.BOLD, 30));
    			

            	//score frame background
            	g.setColor(Color.DARK_GRAY);
            	g.fillRect(625, 625, 200, 75);
            	
    			//vertical tetrolines
    			draw_tetroline(g, 600, 575, 5, true, color_back, color_front, genericVariables.get_tetrobox_size());
    			draw_tetroline(g, 825, 575, 5, true, color_back, color_front, genericVariables.get_tetrobox_size());
    			//horizontal tetrolines
    			draw_tetroline(g, 600, 575, 8, false, color_back, color_front, genericVariables.get_tetrobox_size());
    			draw_tetroline(g, 600, 675, 8, false, color_back, color_front, genericVariables.get_tetrobox_size());
    			
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
            				}
                		}
            		}
            		
            	}            	
            }
            
            private void draw_Buffer(Graphics g) {
            	int size = 25;
            	int draw_x = 600;
            	int draw_y = 50;
            	
            	Color color_back = Color.black;
            	Color color_front = new Color(115, 232, 104);

            	//buffer frame background
            	g.setColor(Color.DARK_GRAY);
            	g.fillRect(625, 100, 200, 400);
            	
            	draw_tetroline(g, 600, 50, 18, true, color_back, color_front, genericVariables.get_tetrobox_size());
            	draw_tetroline(g, 825, 50, 18, true, color_back, color_front, genericVariables.get_tetrobox_size());
            	draw_tetroline(g, 600, 50, 8, false, color_back, color_front, genericVariables.get_tetrobox_size());
            	draw_tetroline(g, 600, 475, 8, false, color_back, color_front, genericVariables.get_tetrobox_size());
            	
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
            	g.drawImage(genericVariables.get_view_bg_image(), 0, 0, genericVariables.get_gw_WIDTH(), genericVariables.get_gw_HEIGHT(), null, null);
            	Color color_back = Color.BLACK;
            	Color color_front = new Color(104, 106, 232);
            	Color color_selection = new Color(229, 232, 104);
            	
            	int size = 25;
        		int pause_x = 300;
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
            		
            		break;
            	
        		case "end":
            		g.setColor(Color.WHITE);
        			g.drawString("! END !", pause_x+tab_space+52 , pause_y+line_space+20);
            		
        			if(genericVariables.get_pause_selection() == 0){
            			g.setColor(color_selection);
            			g.fill3DRect(pause_x+tab_space, pause_y+line_space*3-25 , 180, 35, true);
            			g.setColor(Color.BLACK);
            			g.drawString("RECORD", pause_x+tab_space+35 , pause_y+line_space*3);
            		}
            		else {
            			g.setColor(Color.WHITE);
            			g.drawString("RECORD", pause_x+tab_space+35 , pause_y+line_space*3);
            		}
        			
        			if(genericVariables.get_pause_selection() == 1){
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
            		
        			break;
        		case "highscore":
        			//TODO:
        			break;
        		default :
        			System.err.println("ERROR : unkown game state");
        			System.err.println("genericVariables.get_game_state() called : " + genericVariables.get_game_state());
        			break;
        		}
        	}
            

            private void draw_tetroline(Graphics g,int pos_x,int pos_y,int length, boolean vertical,Color color_back, Color color_front,int size) {
            	if(vertical) {
            		for(int i = 1 ; i <= length ; i++) {
                		
                    	g.setColor(color_back);
                    	g.fillRect(pos_x, pos_y+(i*size), size, size);

                    	g.setColor(color_front);
                    	g.fill3DRect(pos_x+1, pos_y+(i*size)+1, size-2, size-2,true);
                	}
            	}else {
            		for(int i = 1 ; i <= length ; i++) {
                    	g.setColor(color_back);
                    	g.fillRect(pos_x+(i*size), pos_y+size, size, size);

                    	g.setColor(color_front);
                    	g.fill3DRect(pos_x+(i*size)+1, pos_y+size+1, size-2, size-2,true);
            		}
            	}
            	
            }
    }
}