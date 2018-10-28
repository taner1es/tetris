package tetris.code;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput {
	//gets keyboard input
    class ActionListener extends KeyAdapter{
    	
        @Override
    	public void keyPressed(KeyEvent e){
    		int key = e.getKeyCode();
    		
    		switch(genericVariables.get_game_state()) {
				case "instructions":
					if(!genericVariables.get_esc()) {
    					if(key == KeyEvent.VK_ESCAPE) {
    						genericVariables.set_esc(true);
    					}
    				}
					break;
    			case "highscore":
    				if(!genericVariables.get_up()) {
    					if(key == KeyEvent.VK_UP) {
    						genericVariables.set_up(true);
    					}
    				}
    				if(!genericVariables.get_down()) {
    					if(key == KeyEvent.VK_DOWN) {
    						genericVariables.set_down(true);
    					}
    				}
    				if(!genericVariables.get_enter()) {
    					if(key == KeyEvent.VK_ENTER) {
    						genericVariables.set_enter(true);
    					}
    				}
    				if(!genericVariables.get_esc()) {
    					if(key == KeyEvent.VK_ESCAPE) {
    						genericVariables.set_esc(true);
    					}
    				}
    				break;
    			case "loading":
    				
    				break;
    			case "welcome":
    				control_pause_menu_3_selection(key);
    				break;
    			case "paused":
    				control_pause_menu_3_selection(key);
    				break;
    			case "end":
    				control_pause_menu_3_selection(key);
    				break;
    			case "running":
    					//pause game
	            		if(key == KeyEvent.VK_P) {
	            				genericVariables.set_pause(true);
	            				genericVariables.set_game_state("paused");
	            		}
	            		//rotate shape
	            		if(key == KeyEvent.VK_UP && genericVariables.get_rotate_available()) {
	            			genericVariables.get_my_tetris().rotate_shape();
	            			genericVariables.set_rotate_available(false);
	            		}
	            		if(key == KeyEvent.VK_DOWN) {
	            			genericVariables.set_down(true);
	            		}
	            		//move shape
	                	if(key == KeyEvent.VK_RIGHT) {
	                		genericVariables.set_left(false);
	                		genericVariables.set_right(true);
	                	}
	                	else if(key == KeyEvent.VK_LEFT) {
	                		genericVariables.set_right(false);
	                		genericVariables.set_left(true);
	                	}
	                	if(key == KeyEvent.VK_SPACE) {
	                		genericVariables.set_directDown(true);
	                	}
    				break;
    			default :
    				System.err.println("!! ERROR : GAME STATE @KeyInput.KeyPressed");
    				break;
    		}
        }
        @Override
    	public void keyReleased(KeyEvent e){
    		int key = e.getKeyCode();
    		
    		switch(genericVariables.get_game_state()) {
    			case "instructions":
    				if(genericVariables.get_esc()) {
    					if(key == KeyEvent.VK_ESCAPE) {
    						genericVariables.set_esc(false);
    					}
    				}
				break;
    			case "highscore":
    				if(genericVariables.get_up()) {
    					if(key == KeyEvent.VK_UP) {
    						genericVariables.set_up(false);
    					}
    				}
    				if(genericVariables.get_down()) {
    					if(key == KeyEvent.VK_DOWN) {
    						genericVariables.set_down(false);
    					}
    				}
    				if(genericVariables.get_enter()) {
    					if(key == KeyEvent.VK_ENTER) {
    						genericVariables.set_enter(false);
    					}
    				}
    				if(genericVariables.get_esc()) {
    					if(key == KeyEvent.VK_ESCAPE) {
    						genericVariables.set_esc(false);
    					}
    				}
				break;
    			case "loading":
				
				break;
    			case "welcome":
    				
    				break;
    			case "paused":
    				
    				break;
    			case "end":
    				
    				break;
    			case "running":
    				if(key == KeyEvent.VK_RIGHT && genericVariables.get_right()) {
    	    			genericVariables.set_frameCounter_right(0);
                		genericVariables.set_right(false);
    	    		}
    	    		else if(key == KeyEvent.VK_LEFT && genericVariables.get_left()) {
    	    			genericVariables.set_frameCounter_left(0);
    	    			genericVariables.set_left(false);
    	    		}
            		if(key == KeyEvent.VK_DOWN) {
            			genericVariables.set_down(false);
            			genericVariables.set_speed_down(genericVariables.get_speed_game());
            		}
            		if(key == KeyEvent.VK_SPACE) {
                		genericVariables.set_directDown(false);
                	}
            		if(key == KeyEvent.VK_UP && !genericVariables.get_rotate_available()) {
            			genericVariables.set_rotate_available(true);
            		}
    				break;   
    			default:
    				System.err.println("!! ERROR : GAME STATE @KeyInput.KeyReleased");
    				break;
    		}
    	}
    }
    
    private static void control_pause_menu_3_selection(int key) {

		if(key == KeyEvent.VK_DOWN && !genericVariables.get_clip_move().isRunning()) {
			genericVariables.get_clip_move().start();
			if(genericVariables.get_pause_selection() == 0) genericVariables.set_pause_selection(1);
			else if(genericVariables.get_pause_selection() == 1) genericVariables.set_pause_selection(2);
			else if(genericVariables.get_pause_selection() == 2) genericVariables.set_pause_selection(0);
		}else if(key == KeyEvent.VK_UP && !genericVariables.get_clip_move().isRunning()) {
			genericVariables.get_clip_move().start();
			if(genericVariables.get_pause_selection() == 0) genericVariables.set_pause_selection(2);
			else if(genericVariables.get_pause_selection() == 2) genericVariables.set_pause_selection(1);
			else if(genericVariables.get_pause_selection() == 1) genericVariables.set_pause_selection(0);
		}
		if(key == KeyEvent.VK_ENTER && !genericVariables.get_clip_explosion().isRunning()) {
			genericVariables.get_clip_explosion().start();
			genericVariables.set_pause_apply(true);
		}
			
    }
}
