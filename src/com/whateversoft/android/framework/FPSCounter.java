package com.whateversoft.android.framework;

import android.util.Log;

/**
 * class used to calculate framerates during game rendering
 * @author rob2D
 */
public class FPSCounter
{
	long startTime = System.nanoTime();
	int frames     = 0;
	
	public void logFrame()
	{
		frames++;
		if(System.nanoTime() - startTime >= 1000000000)
		{
			Log.d("FPSCounter", "fps: " + frames);
			frames = 0;
			startTime = System.nanoTime();
		}
	}
}
