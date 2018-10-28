package tetris.code;

import java.io.Serializable;
import java.util.TreeMap;

 public class highScoreObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	protected TreeMap<Integer, String> highScoreDataMap = new TreeMap<>();
	
	protected void set_highScoreDataMap(int p_point , String p_name) {
		highScoreDataMap.put(p_point, p_name);
	}
	protected TreeMap<Integer, String> get_highScoreDataMap() {
		return highScoreDataMap;
	}
}
