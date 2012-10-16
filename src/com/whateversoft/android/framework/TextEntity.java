package com.whateversoft.android.framework;

import com.whateversoft.android.framework.impl.android.AndroidGraphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/** Copyright 2011 Robert Concepcion III */
public class TextEntity extends ScreenEntity
{
	public StringBuffer string;
	public int color;
	public int size;
	public int strLength;
	public Typeface font;
	/** width of text; set to -1 if we never called getWidth() */
	public int width = -1;
	public Paint.Align align;
	
	public TextEntity(float x, float y, StringBuffer s, int c, Typeface f, int si, Paint.Align a, int layer, Screen scr)
	{
		super(layer, scr);
		this.x = x;
		this.y = y;
		align = a;
		string = s;
		strLength = s.length();
		color = c;
		font = f;
		size = si;
	}

	public int getWidth()
	{
		Paint painter = ((AndroidGraphics)(this.screen.game.getGraphics())).getPainter();
		painter.setTypeface(font);
		painter.setTextSize(size);
		//if width was never retrieved before or the length of the string changed, measure text
		if(string != null)
		{
			if(width == -1 || string.length() != strLength)
			{
				strLength = string.length();
				width = (int)painter.measureText(string.toString());
				return width;
			}
			else
				return width;
		}
		else
			return -1;
	}

	/** getBounds() isn't explicitly defined for text entities at this point in time in the API */
	@Override
	public Rect getBounds()
	{
		return null;
	}
}
