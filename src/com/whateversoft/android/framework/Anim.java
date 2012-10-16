package com.whateversoft.android.framework;

/** Copyright 2011 Robert Concepcion III */
public class Anim
{
	/** number of frames in the animation */
	public int frameCount;		  //number of frames in the animation
	/** where to cycle the frames; if an animation does not loop then simply set it to the last frame */
	public int frameCycleTo;	  
	/** speed in miliseconds */
	public int frameSpeed;	
	/** array of image frames which contain the images and action points */
	public ImageFrame[] frames;
	
	public Anim(int fSpeed, int fCycleTo, ImageFrame... fSources)
	{
		frameSpeed = fSpeed;
		frameCycleTo = fCycleTo;
		//store the animations
		frames = new ImageFrame[fSources.length];	//initialize the array
		for(int i = 0; i < fSources.length; i++ )
			frames[i] = fSources[i];				//assign image frame members
		frameCount = fSources.length;	
	}
}
