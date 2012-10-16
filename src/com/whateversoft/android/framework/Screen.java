package com.whateversoft.android.framework;

import java.util.ArrayList;

import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.KeyEvent;

import com.whateversoft.android.framework.impl.android.AndroidGraphics;
import com.whateversoft.android.framework.impl.android.CanvasGame;

/** represents a basic Screen where entities can exist and logic is conveniently encapsulated
 *  Copyright 2011 Robert Concepcion III*/
public abstract class Screen 
{
	public final int FPS = 50;
	public final int TIME_INTERVAL = 1000/FPS;
	
	public ScreenAssets assets;
	public int fadeInTimer = 0;
	public float gameTimer = 0;
	public int fadeOutTimer = 0;
	Screen nextScreen = null;
	public int layerCount = 6;	//DEFAULT OF 6 LAYERS TO DRAW ON
	public long lastLogicTick;
	boolean enableZooming = false;
	public FPSCounter fpsCounter = new FPSCounter();
	
	int debugRotateMatrix = 0;
	
	//TOUCH DATA
	/* how long a user has been touching the screen(2 fingers allowed; one for each index) */
	public int [] touchTimer = {0, 0};
	/* how long the last screen touch was on the screen after having been released on the screen
	 * (2 fingers allowed; one for each index) */
	public int [] touchLength = {0, 0};
	/** the number of touch events saved in touchPoints */
	public final int POINTER_1_OFFSET = 15;
	/** points where the user has touched the screen(index 0-11 are first finger and index 12-23 are second) */
	Point[] touchPoints = new Point[30];
	
	public Game game;
	public ArrayList<ScreenEntity>[] entities;	//list of entities within our screen
	
	public int backPressed = 0;
	
	public boolean fadingIn = true,
			   	   fadingOut= false;
	

	public Screen(Game game, ScreenAssets sAM, int lC) 
	{
		this.game = game;
		layerCount = lC;
		
		//initialize graphical/audio assets
		if(sAM != null)
		{
			assets = sAM;
			sAM.obtainAssets();
		}
		
		//initialize the touch points to be recorded for gestures
		for(int i = 0; i < 30; i++)
			touchPoints[i] = new Point();
		
		//initialize the layers for entity drawing
		if(layerCount != -1)
		{
			entities = new ArrayList[layerCount];
			for(int i = 0; i < layerCount; i++)
			{
				entities[i] = new ArrayList<ScreenEntity>();
				entities[i].clear();
			}
		}
		 lastLogicTick = (System.currentTimeMillis() + TIME_INTERVAL);	//set as if the last logic tick happened right before the app started
		 fadeInTimer = 0;
	}

	public void update(float deltaTime)
	{
		game.getInput().getKeyEvents();	//flush key events to clear ram :b
		
		if(game.getInput().isKeyPressed(KeyEvent.KEYCODE_BACK) && backPressed < 10)
			backPressed += 1;
		else if(!game.getInput().isKeyPressed(KeyEvent.KEYCODE_BACK))
			backPressed = 0;
		
		//run the "backPressed()" method which is overloaded by subclasses of the Screen object!
		if(backPressed == 1 && (fadeInTimer + gameTimer > 5))
			backPressed();
		
		//PRECURSOR TO TIMEDLOGIC()
		long timePassed = System.currentTimeMillis() - lastLogicTick;
		if(timePassed > TIME_INTERVAL || fadeInTimer == 0)
		{				
			//get the game keys press lengths
			if(game instanceof CanvasGame)
				((CanvasGame)game).getGameKeyboard().update();
			
			//GESTURE STUFF
			//test touch events being timed for each finger(0, 1)
			for(int i = 0; i < 2; i++)
			{
				touchLength[i] = 0;
				//update the state of the touch Sense during logic Timing Events
				if(game.getInput().isTouchDown(i) && touchTimer[i] < TIME_INTERVAL)
					touchTimer[i] += 1;
				else if(!game.getInput().isTouchDown(i))
				{
					touchLength[i] = touchTimer[i];
					touchTimer[i] = 0;
				}
			}
			
			synchronized(this)
			{
			if(!fadingIn && !fadingOut)	//if in game and no fadeIn or fadeOut sequence happenig...
			{
				//UPDATE ENTITIES/RUN THEIR LOGIC
				for(int i = 0; i < entities.length; i++)
					for(int j = 0; j < entities[i].size(); j++)
					{
						ScreenEntity e = entities[i].get(j) != null ? entities[i].get(j) : null;
						if(e != null && !e.destroyed)
						{
							e.update(deltaTime);
							if(e.touchable == true)
							{
								if(game.getInput().isTouchDown(0))
								{
									if(touchTimer[0] == 1)					//if tapping the screen, set the entity as touched if it happens to be within the entities' bounds
										e.touched(e.getBounds().contains(game.getInput().getTouchX(0), game.getInput().getTouchY(0)));
									else if(e.touched == true)				//otherwise only allow touched to stay true if we're still touching it...
										e.touched(e.getBounds().contains(game.getInput().getTouchX(0), game.getInput().getTouchY(0)));
								}
								else if(!game.getInput().isTouchDown(0) && e.touched)
								{
																	//lastly, if we're not touching the screen and the entity is listed as touched
										e.touched(false);			// then set it not being touched anymore
								}
							}
						}
					}
				
					timedLogic();	//run the current screen's logic event
					gameTimer+= deltaTime / 20.0f;	//increment gaming timer
				
				//DETECT SCREEN TAPS!
				//if the screen is touched with index 0(first finger), jump to the menu!
				if(this.touchLength[0] > 0 && touchLength[0] < 10 && touchTimer[0] == 0)
					screenTapped();	
			}
			else if(fadingIn)
			{
				fadeInLogic(deltaTime);
				fadeInTimer+= deltaTime / 20.0f;
			}
			else if(fadingOut)	//keep track of fade out timer
			{
				fadeOutLogic(deltaTime);
				fadeOutTimer+= deltaTime / 20.0f;
			}
			lastLogicTick = System.currentTimeMillis() - (System.currentTimeMillis() % TIME_INTERVAL);
		}
		}
		fpsCounter.logFrame();
	}
	
	/** necessary method for defining the behavior of a screen */
	public abstract void timedLogic();
	
	public void present()
	{
		Graphics g = game.getGraphics();
		drawEntities(g);
		//clear the input buffers :b
		game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
	}
	
	public void pause()
	{}
	public void resume()
	{}
	
	public void dispose()
	{
		for(int i = 0 ; i < entities.length; i++)
		{
			entities[i].clear();
			entities[i] = null;
		}
		entities = null;	
		game = null;
	}
	/* optional method which is only overriden to detect if a loading dialog which was cancelable was exited */
	public void onLoadingDialogCanceled()
	{}
	/** OPTIONAL. called when the user presses the back key */
	public void backPressed()
	{}
	
	/** OPTIONAL. just makes it easier to detect a simple screen tap from the user */
	public void screenTapped()
	{}
	
	/** method to draw all entities on the screen*/
	public void drawEntities(Graphics g)
	{
		for(int i = 0; i < entities.length; i++)
			for(int j = 0; j < entities[i].size(); j++)
			{
				ScreenEntity e = entities[i].get(j) != null ? entities[i].get(j) : null;
				if(e != null && !e.destroyed)
				{
					//if it is animatable, complete its logic
					if(e instanceof AnimEntity && e.visible)
					{
						AnimEntity a = (AnimEntity)e;
						if(a.imgFrame != null)
							g.drawPixmap(a.imgFrame.getImg(), (int)e.x, (int)e.y, a.imgFrame.actionPointX, a.imgFrame.actionPointY);
					}
				
					//and then display its given image
					else if(e instanceof ImageEntity && e.visible)
					{
						//simplify our calculations by doing a few ahead of time
						ImageEntity sprite = (ImageEntity)e;
						if(sprite.imgFrame != null)
						{
							int xDrawn = Math.round(sprite.x);	//position to draw in x coordinates
							int yDrawn = Math.round(sprite.y); // position to draw in y coordinates
							int actionPointX = sprite.imgFrame.actionPointX;
							int actionPointY = sprite.imgFrame.actionPointY;
							//draw the sprites
							if(sprite.rotation == 0)
							{
								if(!sprite.semiTrans)
								{
									if(sprite.zoomPercentage == 100)
										g.drawPixmap(sprite.imgFrame.getImg(), xDrawn, yDrawn, actionPointX, actionPointY);
									else
										g.drawPixmapScaled(sprite.imgFrame.getImg(), xDrawn, yDrawn, actionPointX, actionPointY, sprite.zoomPercentage / 100.0F);
								}
									//g.drawPixmap(sprite.imgFrame.getImg(), xDrawn, yDrawn, actionPointX, actionPointY);
								else
									g.drawPixmapAlpha(sprite.imgFrame.getImg(), xDrawn, yDrawn, actionPointX, actionPointY, sprite.alpha);
							}
							else
							{
								if(!sprite.semiTrans)
									g.drawPixmapRotated(sprite.imgFrame.getImg(), xDrawn, yDrawn, 
											sprite.imgFrame.actionPointX, sprite.imgFrame.actionPointY, (360 - (int)Math.round(sprite.rotation)) % 360);
								else
									g.drawPixmapRotatedAlpha(sprite.imgFrame.getImg(), xDrawn, yDrawn, 
											sprite.imgFrame.actionPointX, sprite.imgFrame.actionPointY, (360 - (int)Math.round(sprite.rotation)) % 360, sprite.alpha);
							}
						}
					}
					
					else if(e instanceof TextEntity && e.visible)
					{
						TextEntity t = (TextEntity)e;
						//if the bounds of the string drawn are horizontally within bounds of screen...
						g.drawText(t.string, (int)e.x, (int)e.y, t.color, t.size, t.font, t.align);
					}			
					else if(e instanceof RectEntity && e.visible)
					{
						RectEntity r = (RectEntity)e;
						g.drawRect(r.rect.left + (int)e.x, 
								   r.rect.top + (int)e.y, 
								   r.rect.right + 1- r.rect.left , 
								   r.rect.bottom + 1 - r.rect.top, 
								   r.color, r.fill);
					}
				}
			}
		}
	
	/** starts at the beginning of every screen. override to create specific fade in! 
	 * @param deltaTime */
	public void fadeInLogic(float deltaTime)
	{fadingIn = false;}
	
	/** initialize the fade out sequence, store next screen to go to when its complete.
	 * If it's not implemented, the default behavior is to go straight to the next screen! */
	public void goToScreen(Screen nScreen)
	{
		if(fadingIn)			
			fadingIn = false;
		if(!fadingOut)
		{
			nextScreen = nScreen;
			fadingOut = true;
		}
	}
	
	public void completeFadeOut()
	{
		game.setScreen(nextScreen);
	}
	
	/** runs at the end of every screen. 
	 * If it's not overridden, default behavior is to go straight to next screen! 
	 * @param deltaTime */
	public void fadeOutLogic(float deltaTime)
	{
		completeFadeOut();
	}
	
	/** Returns the screen code number so that we can call native android UI widgets
	 *  within the context of the current screen running.<br>
	 *  We store the constants for an application within <b>[appName]ScreenCodes.class</b>
	 * */
	public abstract int getScreenCode();
}