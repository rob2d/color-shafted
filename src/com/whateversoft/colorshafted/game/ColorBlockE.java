package com.whateversoft.colorshafted.game;

import android.util.Log;

import static com.whateversoft.colorshafted.screens.GameScr.*;

import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.colorshafted.screens.GameScr;
import com.whateversoft.colorshafted.screens.GameScrAssets;

/** Enemy instantiation of a ColorBlock on the game scren
 *  Copyright 2011 Robert Concepcion III  */
public class ColorBlockE extends ColorBlock
{
	/** whether the block goes past the center play field and the player does not hit it */
	public boolean hasBeenEvaded = false;
	public float   speed; 
	/** distance from the center of the screen */
	int	   distFromCenter = 100000;
	
	long   lastTimeCreatedShadow = System.currentTimeMillis();
	
	/** default constructor. if the initial direction and initial shaft are specified as -1, we do not yet
	 *  call instantiate as enemy */
	public ColorBlockE(int c, int initDir, int initShaft, int xInit, int yInit, int layer, GameScr s)
	{
		super(c, xInit, yInit, layer, s);
		
		if(initDir != -1 && initShaft != -1)
			instantiateAsEnemy(initDir, initShaft, c);
	}
	
	public ColorBlockE(int c,int xInit, int yInit, int layer, GameScr s)
	{
		super(c, xInit, yInit, layer, s);
		int initDir = 0, initShaft = 0;
		instantiateAsEnemy(initDir, initShaft, c);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		//check if the block escapes the game's canvas. if so, destroy it to free up memory
		if(dx > 0)	   //moving right
		{
			if(!hasBeenEvaded)
			{
				if(x > gameScreen.gameCanvasX(GameScr.GRID_RIGHT + 55))
				{
					hasBeenEvaded = true;
					GameStats.enemiesEvaded++;
				}
			}
			else
			{
				if(x - 16 > gameScreen.gameCanvasX(GameScr.GAME_CANVAS_WIDTH))
					gameScreen.eBlockFactory.throwInPool(this);
			}
		}
		else if(dx < 0)	//moving left
		{
			if(!hasBeenEvaded)
			{
				if(x < gameScreen.gameCanvasX(GameScr.GRID_LEFT - 55))
				{
					hasBeenEvaded = true;
					GameStats.enemiesEvaded++;
				}
			}
			else
			{
				if(x + 16 < gameScreen.gameCanvasX(0)) 
					gameScreen.eBlockFactory.throwInPool(this);
			}
		}
		if(dy > 0)		//moving down
		{
			if(!hasBeenEvaded)
			{
				if(y > gameScreen.gameCanvasY(GameScr.GRID_BOTTOM + 55))
				{
					hasBeenEvaded = true;
					GameStats.enemiesEvaded++;
				}
			}
			else
			{
				if(y - 16 > gameScreen.gameCanvasY(GameScr.GAME_CANVAS_HEIGHT))
					gameScreen.eBlockFactory.throwInPool(this);
			}
		}
		else if(dy < 0)	//moving up
		{
			if(!hasBeenEvaded)
			{
				if(y < GameScr.GRID_TOP - 55)
				{
					hasBeenEvaded = true;
					GameStats.enemiesEvaded++;
				}
			}
			else
			{
				if(y < -16) 
					gameScreen.eBlockFactory.throwInPool(this);
			}
		}
	}
	
	public void instantiateAsEnemy(int initDir, int initShaft, int color)
	{	
		//-----------------------------------------------//
		//	INITIALIZE THE STARTING MOVEMENT AND POSITIONS
		//-----------------------------------------------//
		speed = (1.8f + GameStats.difficulty * 0.18f);

		switch(initDir)
		{
		case 0:	//going down
		{
			x =  gameScreen.gameCanvasX(GameScr.GRID_LEFT + (GameScr.GRID_SQ_SIZE / 2) + (int)(GameScr.GRID_SQ_SIZE * initShaft));
			y =  0;
			dx = 0;
			dy = (float)speed;
		}
			break;
		case 1:	//going up
			x = gameScreen.gameCanvasX(GameScr.GRID_LEFT + (GameScr.GRID_SQ_SIZE / 2) + (int)(GameScr.GRID_SQ_SIZE * initShaft));
			y = 480;
			dx = 0;
			dy = (float)-speed;
			break;
		case 2:	//going right
			x = gameScreen.gameCanvasX(0);
			y = GameScr.GRID_TOP + (GameScr.GRID_SQ_SIZE / 2) + GameScr.GRID_SQ_SIZE * initShaft;
			dx = (float)speed;
			dy = 0;
			break;
		case 3:	//going left
			x = gameScreen.gameCanvasX(GameScr.GAME_CANVAS_WIDTH);
			y = GameScr.GRID_TOP + (GameScr.GRID_SQ_SIZE / 2) + GameScr.GRID_SQ_SIZE * initShaft;
			dx = (float)-speed;
			dy = 0;
			break;
		default:
			break;
		}
		//-----------------------------------------------//
		//	ADD THIS COLORBLOCK TO THE LIST OF ENEMIES	 
		//-----------------------------------------------//
		synchronized(gameScreen.eBlocks)
		{
			gameScreen.eBlocks.add(this);
		}
	}
	
	/** set the initial enemy parameters using its incoming shaft ID(enum taken from the Object Spawner class) */
	public void instantiateAsEnemy(int shaft, int c)
	{
		int initDir   = -1;
		int initShaft = -1;
		
		switch(shaft)
		{
			case SHAFT_DOWN_LEFT:    initDir   = 0; initShaft = 0;      break;
			case SHAFT_DOWN_CENTER:  initDir   = 0; initShaft = 1;      break;
			case SHAFT_DOWN_RIGHT:   initDir   = 0; initShaft = 2;      break;
			case SHAFT_UP_LEFT:      initDir   = 1; initShaft = 0;      break;
			case SHAFT_UP_CENTER:    initDir   = 1; initShaft = 1;      break;
			case SHAFT_UP_RIGHT:     initDir   = 1; initShaft = 2;      break;
			case SHAFT_RIGHT_TOP:    initDir   = 2; initShaft = 0;      break;
			case SHAFT_RIGHT_CENTER: initDir   = 2; initShaft = 1;      break;
			case SHAFT_RIGHT_BOTTOM: initDir   = 2; initShaft = 2;      break;
			case SHAFT_LEFT_TOP:     initDir   = 3; initShaft = 0;      break;
			case SHAFT_LEFT_CENTER:  initDir   = 3; initShaft = 1;      break;
			case SHAFT_LEFT_BOTTOM:  initDir   = 3; initShaft = 2;      break;
		}
		instantiateAsEnemy(initDir, initShaft, c);
	}
	
	/** calculate the distance from center of screen in order to use for certain effects
	 *  such as rotating or desaturating in Psych Out mode
	 */
	public void calcDistFromCenter()
	{
		float distCenterX = (x - ScreenInfo.virtualWidth/2)  * (x - ScreenInfo.virtualWidth/2);
		float distCenterY = (y - ScreenInfo.virtualHeight/2) * (y - ScreenInfo.virtualHeight/2);
		distFromCenter  =  (int)Math.sqrt(distCenterX + distCenterY);
	}

	public void desaturate()
	{
		
	}
}