package com.whateversoft.android.framework.impl.android;

import android.graphics.Bitmap;

import com.whateversoft.android.framework.Pixmap;
import com.whateversoft.android.framework.Graphics.PixmapFormat;

/** Copyright 2011 Robert Concepcion III */
public class AndroidPixmap implements Pixmap
{
	Bitmap bitmap;
	PixmapFormat format;
	
	public AndroidPixmap(Bitmap b, PixmapFormat f)
	{
		bitmap = b;
		format = f;
	}
	
	public Bitmap getBmp()
	{
		return bitmap;
	}
	
	@Override
	public PixmapFormat getFormat()
	{
		return format;
	}
	@Override
	public int getWidth()
	{
		return bitmap.getWidth();
	}
	
	@Override
	public int getHeight()
	{
		return bitmap.getHeight();
	}
	
	@Override
	public void dispose()
	{
		bitmap.recycle();
	}
}
