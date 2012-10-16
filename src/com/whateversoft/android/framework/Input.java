package com.whateversoft.android.framework;

import java.util.List;

/** Copyright 2011 Robert Concepcion III */
public interface Input
{
	public static class KeyEvent
	{
		public final static int KEY_DOWN = 0;
		public final static int KEY_UP = 1;
		
		public int type;
		public int keyCode;
		public int keyChar;
	}
	
	public static class TouchEvent
	{
		public static final int TOUCH_DOWN = 0;
		public static final int TOUCH_UP = 1;
		public static final int TOUCH_DRAGGED = 2;
		
		public int type;
		public int x, y;
		public int pointer;
	}
	
	public boolean isKeyPressed(int keyCode);
	
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public float getAccelX();
	public float getAccelY();
	public float getAccelZ();
	
	public List<KeyEvent> getKeyEvents();
	
	public List<TouchEvent> getTouchEvents();
	
	public void setTouchScale(float scaleX, float scaleY);
}
