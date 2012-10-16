package com.whateversoft.android.framework;

import android.graphics.Color;
import android.graphics.Rect;

/** Copyright 2011 Robert Concepcion III */
public class RectEntity extends ScreenEntity
{
	public Rect rect;
	public int color;
	public boolean fill = false;
	public RectEntity(int layer, Screen s) 
	{
		super(layer, s);
	}
	
	public RectEntity(Rect r, int c, int layer, Screen s)
	{
		super(layer, s);
		
		rect = r;
		color = c;
	}

	@Override
	public Rect getBounds()
	{
		return null;
	}
}
