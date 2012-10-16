package com.whateversoft.colorshafted.game;

import android.graphics.Rect;


/** A collidable(bounding boxed) instance entity
 *  Copyright 2011 Robert Concepcion III */
public interface Collidable
{
	/** retrieve a collidable shape */
	public Rect getCollidable();	
	/** return whether or not this object has collided with another collidable object */
	public boolean collidesWith(Collidable col);
}
