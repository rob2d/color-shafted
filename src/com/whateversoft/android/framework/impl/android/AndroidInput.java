package com.whateversoft.android.framework.impl.android;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;

import com.whateversoft.android.framework.Input;

/** Copyright 2011 Robert Concepcion III */
/** Class which handles the input abstraction on Android Devices */
public class AndroidInput implements Input
{
	AccelerometerHandler accelHandler;
	KeyboardHandler keyHandler;
	TouchHandler touchHandler;
	View		 parentView;
	
	public AndroidInput(Context context, View view, float scaleX, float scaleY)
	{
		parentView = view;
		accelHandler = new AccelerometerHandler(context);
		keyHandler = new KeyboardHandler(view);
		if(Integer.parseInt(VERSION.SDK) < 5)
			touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		else
			touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
	}
	
	@Override
	public boolean isKeyPressed(int keyCode)
	{
		return keyHandler.isKeyPressed(keyCode);
	}
	
	@Override
	public boolean isTouchDown(int pointer)
	{
		return touchHandler.isTouchDown(pointer);
	}
	
	@Override
	public int getTouchX(int pointer)
	{
		return touchHandler.getTouchX(pointer);
	}
	
	@Override
	public int getTouchY(int pointer)
	{
		return touchHandler.getTouchY(pointer);
	}
	
	@Override
	public float getAccelX()
	{
		return accelHandler.getAccelX();
	}
	
	@Override
	public float getAccelY()
	{
		return accelHandler.getAccelY();
	}
	
	@Override
	public float getAccelZ()
	{
		return accelHandler.getAccelZ();
	}
	
	@Override
	public List<KeyEvent> getKeyEvents()
	{
		return keyHandler.getKeyEvents();
	}
	
	@Override
	public List<TouchEvent> getTouchEvents()
	{
		return touchHandler.getTouchEvents();
	}

	@Override
	public void setTouchScale(float scaleX, float scaleY)
	{
		touchHandler.setScale(scaleX, scaleY);
	}
}
