package com.whateversoft.android.framework;

import java.io.Serializable;
import android.graphics.Rect;

/** Represents an image frame that can be used to display in an AnimEntity or ImageEntity
 * Copyright 2011 Robert Concepcion III */
@SuppressWarnings("serial")
public class ImageFrame implements Serializable
{
	protected Pixmap img;	//image for particular frame
	public 	  int  actionPointX, actionPointY;	//for drawing offset and rotation orientation
	
	public ImageFrame(Pixmap i, Object parent)
	{
		img = i;
	}
	
	//constructor for image frame, image loaded, the collision box, and optional action points(x, y),
	//the parent ref is needed to extract the resource location!(typically in a sprite)
	public ImageFrame(Pixmap i, int apX, int apY, Object parent)
	{
		this(i, parent);
		actionPointX = apX;
		actionPointY = apY;
	}
	
	/** method to return an image from the frame */
	public Pixmap getImg()
	{
		if(img != null) return img;	//prevent nPE exception
		else	return null;
	}
	
	/** return the outer bounds of an image for convenient calculations */
	public Rect getImgBounds()
	{
		if(img == null)
			return null;
		else
			return new Rect(-actionPointX, -actionPointY, 
							img.getWidth() - actionPointX, img.getHeight() - actionPointY);
	}
}
