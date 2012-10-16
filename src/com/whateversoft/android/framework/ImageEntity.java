package com.whateversoft.android.framework;
 
import android.graphics.Rect;

/** Copyright 2011 Robert Concepcion III */
/** An entity with an associated image */
public class ImageEntity extends ScreenEntity
{
	/** the image being displayed */
	public ImageFrame imgFrame 		= null;
	public int rotation 			= 0;
	public int zoomPercentage		= 100;
	public boolean desaturatable    = false;
	public float saturationValue    = 1.0f;
	
	public ImageEntity(int l, Screen s)
	{
		super(l, s);
	}
	
	public ImageEntity(float x, float y, ImageFrame i, int l, Screen s)
	{
		super(l, s);
		this.x = x;
		this.y = y;
		imgFrame = i;
	}
	
	@Override
	public void destroy()
	{
		imgFrame = null;
		super.destroy();
	}

	@Override
	public Rect getBounds()
	{
		Rect imageBounds = imgFrame.getImgBounds();
		return new Rect(imageBounds.left + (int)x, imageBounds.top + (int)y, 
					    imageBounds.right + (int)x, imageBounds.bottom + (int)y);
	}
}