package com.whateversoft.android.framework.impl.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

/** Copyright 2011 Robert Concepcion III */
public class AndroidFastRenderView extends SurfaceView implements Runnable
{
	CanvasGame game;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;
	long startTime;
	long deltaTime;
	
	long pauseStarted;
	long pauseTimeElapsed;
	boolean gameJustUnpaused = false;
	boolean gamePaused       = false;
	Paint	screenPainter = new Paint();
	
	
	/** keeps track of the game's context to load resources later from other classes */
	public static Context context; 
	
	public AndroidFastRenderView(CanvasGame game, Bitmap frameBuffer)
	{
		super(game);
		this.game = game;
		this.frameBuffer = frameBuffer;
		this.holder = getHolder();
		this.requestFocus();
	}
	
	public void resume()
	{
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}
	
	public void run()
	{
		Looper.prepare();
		Rect dstRect = new Rect();
		startTime = System.currentTimeMillis();
		while(running)
		{
			if(!holder.getSurface().isValid())
				continue;
			
			if(!gameJustUnpaused)
			{
				deltaTime = (System.currentTimeMillis() - startTime );	//get the difference in time in miliseconds
				startTime = System.currentTimeMillis();
			}
			else
			{
				pauseTimeElapsed = System.currentTimeMillis() - pauseStarted;
				deltaTime = (System.currentTimeMillis() - startTime - pauseTimeElapsed);	//get the difference in time in miliseconds
				startTime = System.currentTimeMillis();
				gameJustUnpaused = false;
			}
			
			if(game.getCurrentScreen() != null)
			{
				synchronized(this)
				{
					game.getCurrentScreen().update(deltaTime);
					game.getCurrentScreen().present();
				}
			}
			
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(frameBuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
			
			//wait before calling thread to update/re-render. saves resources
			try
			{
				synchronized(this)
				{
					wait(5);
				}
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void pause()
	{
		running = false;
		while(true)
		{
			try
			{
				renderThread.join();
				break;
			}
			catch(InterruptedException e)
			{
				//retry automatically in the while loop
			}
		}
	}
	
	public void setFrameBuffer(Bitmap bitmap)
	{
		if(frameBuffer != bitmap)	//if it isn't the original bitmap, set to new frameBuffer bitmap
		{
			frameBuffer = null;
			frameBuffer = bitmap;
		}
	}

	/** declares when an actual game has been paused so that we can properly get the update time between frames with no errors */
	public void pauseGameWhileRunning()
	{
		synchronized(this)
		{
			pauseStarted = System.currentTimeMillis();
			gamePaused = true;
		}
	}
	
	/** declares when an actual game has been resumed so that we can properly get the update time between frames with no errors*/
	public void resumeGameWhileRunning()
	{
		synchronized(this)
		{
			gameJustUnpaused = true;
		}
	}
}
