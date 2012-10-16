package com.whateversoft.android.framework;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;

/** Copyright 2011 Robert Concepcion III */
public class ShapeEntity extends ScreenEntity
{
	public ShapeDrawable shape;	//shape to render
	public int color;	//color to render
	
	public ShapeEntity(float x, float y, ShapeDrawable s, int c, int layer, Screen scr)
	{
		super(layer, scr);
		shape = s;
		color = c;
	}

	@Override
	public Rect getBounds()
	{
		return shape.getBounds();
	}
}
