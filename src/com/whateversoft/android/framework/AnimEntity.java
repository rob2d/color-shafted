package com.whateversoft.android.framework;

import java.util.ArrayList;

/** An animatable entity class 
 * Copyright 2011 Robert Concepcion III */
public class AnimEntity extends ImageEntity
{
	/** our animation array */
	public ArrayList<Anim> anims = new ArrayList<Anim>();		
	/** quick reference to one of the animations in our anim array */
	protected int animIndex = 0;						
	/** current frame of animation */
	protected int animFrame = 0;						
	/** last animation tick */
	protected long animLastTick = System.currentTimeMillis();
	/** whether anim is paused */
	protected boolean isAnimPaused = false;		
	/** whether anim has finished(for related logic events) */
	protected boolean isAnimFinished = false;
	
	public AnimEntity(int layer, Screen screen) 
	{
		super(layer, screen);
	}
	
	public AnimEntity(float x, float y, int layer, Screen screen, Anim... animsAdded)
	{
		super(layer, screen);
		this.x = x;
		this.y = y;
		
		for(Anim a : animsAdded)
			anims.add(a);
		
		update(0f);
	}
	
	/** game logic event */
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		animate(System.currentTimeMillis());
	}
	
	/** animate an entity during a game logic tick */
	public void animate(long time)
	{
		isAnimFinished = false;
		
		//update anim loop when necessary events occur
		while(((time - animLastTick)) >  (100 - anims.get(animIndex).frameSpeed) * 10)
		{
			animLastTick = (long)(time - 									//update last animTick time
						   (time % ((100 - anims.get(animIndex).frameSpeed)) * 10));	
			animFrame += 1;
			if(animFrame >= anims.get(animIndex).frameCount)
			{
				animFrame = anims.get(animIndex).frameCycleTo;
				isAnimFinished = true;
			}
			
			imgFrame = anims.get(animIndex).frames[animFrame];
		}
		
		//if the image is not yet shown, we update it!
		if(imgFrame == null)
			imgFrame = anims.get(animIndex).frames[animFrame];
	}
	
	/** reset animation so it can be played back again */
	public void resetAnim()
	{
		animFrame = 0;
		animLastTick = System.currentTimeMillis();
		isAnimFinished = false;
		imgFrame = anims.get(animIndex).frames[animFrame];
	}
}
