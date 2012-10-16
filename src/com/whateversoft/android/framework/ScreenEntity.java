package com.whateversoft.android.framework;

import android.graphics.Rect;

/** An entity on screen which can move around and be instantiated to have a specific visual representation 
 *  Copyright 2011 Robert Concepcion III */
public abstract class ScreenEntity
{
	public float   x, y, dx, dy;
	public int 	   layer;
	public Screen  screen;
	public boolean destroyed = false;
	public boolean visible = true;
	public boolean semiTrans = false;
	/** if this variable is set to false, an entity will not update dx/dy, animate, or check for being touched */
	public boolean updatingEntity = true;
	public int	   alpha = 255;
	
	public boolean touchable = false;
	boolean touched = false;
	public boolean touchJustReleased = false;
	public int touchTimer = 0;
	
	public ScreenEntity(int l, Screen scr)
	{
		//-------------------------------------------------------//
		//	SAVE DEFAULT CONSTRUCTOR VALUES(layer, parent screen)
		//-------------------------------------------------------//
		layer = l;
		screen = scr;
		scr.entities[layer].add(this);
	}
	
	public void touched(boolean t)
	{
		touched = t;
	}
	
	/** called before every logic event happens during the game(every x miliseconds) so that the entities can have their unique behaviors. This includes
	 *  moving the coordinates relative to dx/dy. 
	 * @param deltaTime */	
	public synchronized void update(float deltaTime)
	{
		//-----------------------------------------//
		//	SHIFT COORDINATES ACCORDING TO DX/DY
		//-----------------------------------------//
		x += dx * deltaTime/20f;
		y += dy * deltaTime/20f;
		
		if(touchable)
		{
		
			touchJustReleased = false;	
			if(touched)
				touchTimer += 1;
			else
			{
				if(touchTimer > 0)
					touchJustReleased = true;
				touchTimer = 0;
			}
		}
	}

	public abstract Rect getBounds();
	/** helper method which allows entities to be destroyed from the screen without causing a concurrent modification error. This is done by using an arraylist
	 *  on the screen which destroys objects outside of any for loops after the entity's destroy flag has been set.*/
	public synchronized void destroy()
	{
		//-----------------------------------------------------------------------------//
		//	SET DESTROYED FLAG, ADD TO DESTROYED ENTITIES ARRAYLIST
		//-----------------------------------------------------------------------------//
		if(!destroyed)
		{
			x = -1000;
			y = -1000;
			visible = false;
			if(screen != null)
				screen.entities[layer].remove(screen.entities[layer].indexOf(this));
			destroyed = true;
		}
	}
}