package com.whateversoft.android.framework.impl.android;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.whateversoft.android.framework.Pool;
import com.whateversoft.android.framework.Input.TouchEvent;
import com.whateversoft.android.framework.Pool.PoolObjectFactory;

/** Copyright 2011 Robert Concepcion III */
public class SingleTouchHandler implements TouchHandler
{
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer  = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	public SingleTouchHandler(View view, float sX, float sY)
	{
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>()
		{
			@Override
			public TouchEvent createObject()
			{
				return new TouchEvent();
			}
		};
		
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		scaleX = sX;
		scaleY = sY;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent e)
	{
		synchronized(this)
		{
			TouchEvent touchEvent = touchEventPool.newObject();	//create a new touchEvent to register in the buffer
			//register it's action
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			}
			
			//set its x relative to the scale and the coordinate touched
			touchEvent.x =(int)(e.getX() * scaleX);
			touchEvent.y =(int)(e.getY() * scaleY);
			
			//add the touchEvent created to the buffer of touch events
			touchEventsBuffer.add(touchEvent);
			//register this key as pressed
			return true;
		}
	}
	
	@Override
	public boolean isTouchDown(int pointer)
	{
		synchronized(this)
		{
			if(pointer == 0)	//only returns from pointer zero, as this is the first finger and it is single touch.
				return isTouched;
			else
				return false;
		}
	}
	
	@Override
	public int getTouchX(int pointer)
	{
		synchronized(this)
		{
			return touchX;
		}
	}
	
	@Override
	public int getTouchY(int pointer)
	{
		synchronized(this)
		{
			return touchY;
		}
	}
	
	/** shift all touchevents from the buffer to the touchevents list and then return the touchevents to the main thread. */
	@Override
	public List<TouchEvent> getTouchEvents()
	{
		synchronized(this)	
		{
			int len = touchEvents.size();
			for(int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(len));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

	@Override
	public void setScale(float scaleX, float scaleY)
	{
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
}
