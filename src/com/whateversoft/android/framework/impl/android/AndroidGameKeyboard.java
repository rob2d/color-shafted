package com.whateversoft.android.framework.impl.android;

import java.util.HashMap;
import java.util.Map;

import com.whateversoft.android.framework.Game;

public class AndroidGameKeyboard
{
	public int[] keysMonitored;
	
	Map<Integer, Integer> keyStatus;
	
	Game game;
	
	
	public AndroidGameKeyboard(Game g, int... keys)
	{
		game = g;
		keysMonitored = keys;
		keyStatus = new HashMap<Integer, Integer>();
		
		//insert keys into the key map
		for(int k : keys)
			keyStatus.put(k, 0);
	}
	
	/** check for key updates/changes */
	public void update()
	{
		for(Map.Entry<Integer, Integer> entry : keyStatus.entrySet())
		{
			if(game.getInput().isKeyPressed(entry.getKey()))
				entry.setValue(entry.getValue() + 1);
			else
				entry.setValue(0);
		}
	}
	
	/** if a key's status is equal to 1, then the key was just pressed. Anything larger than 0 means it is being held. 
	 * If it is 0 the key is not pressed or held */
	public int getKeyStatus(int keyCode)
	{
		return keyStatus.get(keyCode);
	}
}
