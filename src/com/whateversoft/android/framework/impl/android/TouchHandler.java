package com.whateversoft.android.framework.impl.android;

import java.util.List;

import android.view.View.OnTouchListener;

import com.whateversoft.android.framework.Input.TouchEvent;

/** Copyright 2011 Robert Concepcion III */
public interface TouchHandler extends OnTouchListener
{
	public boolean isTouchDown(int pointerIndex);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<TouchEvent> getTouchEvents();
	
	public void setScale(float scaleX, float scaleY);
}
