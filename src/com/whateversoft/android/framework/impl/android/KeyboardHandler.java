package com.whateversoft.android.framework.impl.android;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

import com.whateversoft.android.framework.Pool;
import com.whateversoft.android.framework.Input.KeyEvent;
import com.whateversoft.android.framework.Pool.PoolObjectFactory;

/** Copyright 2011 Robert Concepcion III */
public class KeyboardHandler implements OnKeyListener
{
	/** stores the current state of each key pressed */
	boolean[] pressedKeys = new boolean[128];
	
	/** to prevent the garbage handler from constantly interrupting the application */
	Pool<KeyEvent> keyEventPool;
	/** stores a list of key events not-yet given to the game */
	List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();
	
	List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
	
	public KeyboardHandler(View view)
	{
		PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>()
		{
			@Override
			public KeyEvent createObject()
			{
				return new KeyEvent();
			}
		};
		
		keyEventPool = new Pool<KeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}
	
	public boolean isKeyPressed(int keyCode) {
		if (keyCode < 0 || keyCode > 127)
			return false;
		return pressedKeys[keyCode];
		}
	
	@Override
	public boolean onKey(View v, int keyCode, android.view.KeyEvent e)
	{
		//ACTION_MULTIPLE events are not relevant to our application
		if(e.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
			return false;
		
		synchronized(this)
		{
			//fetch a key event from the key event pool or create a new one if necessary
			KeyEvent keyEvent = keyEventPool.newObject();
			//store keyCode/char pressed
			keyEvent.keyCode = keyCode;
			keyEvent.keyChar = (char) e.getUnicodeChar();
			
			//decode the type and change the state of the pressedKeys[] boolean appropriate, synchronization isn't necessary since we're working with primitives
			if(e.getAction() == android.view.KeyEvent.ACTION_UP)
			{
				keyEvent.type = KeyEvent.KEY_UP;
				if(keyEvent.keyCode > 0 && keyEvent.keyCode < 127)
					pressedKeys[keyCode] = false;
			}
			if(e.getAction() == android.view.KeyEvent.ACTION_DOWN)
			{
				keyEvent.type = KeyEvent.KEY_DOWN;
				if(keyEvent.keyCode > 0 && keyEvent.keyCode < 127)
					pressedKeys[keyCode] = true;
			}
			keyEventsBuffer.add(keyEvent);
			
			//DO NOT REGISTER BACK KEY... this allows us to override it if we choose
			if(keyCode == android.view.KeyEvent.KEYCODE_BACK)
				return true;
		}
		return false;
	}

	public List<KeyEvent> getKeyEvents()
	{
		synchronized(this)
		{
			int len = keyEvents.size();
			for(int i = 0; i < len; i++)
				keyEventPool.free(keyEvents.get(i));
			keyEvents.clear();
			keyEvents.addAll(keyEventsBuffer);
			return keyEvents;
		}
	}
}
