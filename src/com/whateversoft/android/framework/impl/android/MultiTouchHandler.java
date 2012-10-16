package com.whateversoft.android.framework.impl.android;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.whateversoft.android.framework.Pool;
import com.whateversoft.android.framework.Input.TouchEvent;
import com.whateversoft.android.framework.Pool.PoolObjectFactory;

/** Copyright 2011 Robert Concepcion III */
public class MultiTouchHandler implements TouchHandler
{
	boolean [] isTouched = new boolean[20];
	int[] touchX = new int[20];
	int[] touchY = new int[20];
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	public MultiTouchHandler(View view, float sX, float sY)
	{
		PoolObjectFactory factory = new PoolObjectFactory<TouchEvent>()
		{
			@Override
			public TouchEvent createObject()
			{
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool(factory, 100);
		view.setOnTouchListener(this);
		
		scaleX = sX;
		scaleY = sY;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent e)
	{
		synchronized(this)
		{
			int action = e.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (e.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerId = e.getPointerId(pointerIndex);
			TouchEvent touchEvent;
			
			switch(action)
			{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				touchEvent.pointer = pointerId;
				touchEvent.x = touchX[pointerId] = (int) (e.getX() * scaleX);
				touchEvent.y = touchY[pointerId] = (int) (e.getY() * scaleY);
				isTouched[pointerId] = true;
				touchEventsBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_MOVE:
				int pointerCount = e.getPointerCount();
				for(int i = 0; i < pointerCount; i++)
				{
					touchEvent = touchEventPool.newObject();
					pointerIndex = i;
					pointerId = e.getPointerId(pointerIndex);
					touchEvent.x = touchX[pointerId] = (int) (e.getX(pointerIndex) * scaleX);
					touchEvent.y = touchY[pointerId] = (int) (e.getY(pointerIndex) * scaleY);
					touchEventsBuffer.add(touchEvent);
				}
					break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_UP;
				touchEvent.pointer = pointerId;
				touchEvent.x = touchX[pointerId] = (int) (e.getX(pointerIndex) * scaleX);
				touchEvent.y = touchY[pointerId] = (int) (e.getY(pointerIndex) * scaleY);				
				touchEventsBuffer.add(touchEvent);
				isTouched[pointerId] = false;
				break;
			}
			return true;
		}
	}
	
	@Override
	public boolean isTouchDown(int pointer)
	{
		synchronized(this)
		{
			if(pointer < 0 || pointer > 20)
			{
				return false;
			}
			else
				return isTouched[pointer];
		}
	}
	
	@Override
	public int getTouchX(int pointer)
	{
		synchronized(this)
		{
			if(pointer < 0 || pointer > 20)
				return 0;
			else
			{
				return touchX[pointer];
			}
		}
	}
	
	@Override
	public int getTouchY(int pointer)
	{
		synchronized(this)
		{
			if(pointer < 0 || pointer > 20)
				return 0;
			else
			{
				return touchY[pointer];
			}
		}
	}
	
	@Override
	public List<TouchEvent> getTouchEvents()
	{
		synchronized(this)
		{
			int len = touchEvents.size();
			for(int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(i));
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
