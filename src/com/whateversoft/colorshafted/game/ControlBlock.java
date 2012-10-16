package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_CB_ARMS;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_CB_CENTER;
import static com.whateversoft.colorshafted.screens.GameScrAssets.SND_MOVE;
import static com.whateversoft.colorshafted.screens.GameScrAssets.SND_ROTATE;

import java.util.Random;

import android.graphics.Rect;
import android.util.Log;

import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.Input;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.android.framework.ScreenEntity;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.screens.GameScr;

/** represents the central control block on a game screen in Color Shafted 
 *  Copyright 2011 Robert Concepcion III */
public class ControlBlock extends ScreenEntity implements Collidable
{
	//----------------------------------//
	//		  CONSTANTS 				//
	//----------------------------------//
	public static final int INVINCIBILITY_TIMER = 75;
	
	//----------------------------------//
	//		  REFERENCES				//
	//----------------------------------//
	Input input;
	GameScr gameScreen;				//for quick access to our color shafted specific game screen
	ScreenAssets assets;
	boolean initialized = false;
	
	
	//		  DATA FIELDS				//
	//----------------------------------//
	public static final boolean TURN_LEFT = true;
	public static final boolean TURN_RIGHT = false;
	
	public static final int		MOVE_UP	   	    = 0,
								MOVE_DOWN  	    = 1,
								MOVE_LEFT  	    = 2,
								MOVE_RIGHT 	    = 3,
								MOVE_UP_RIGHT   = 4,
								MOVE_RIGHT_DOWN = 5,
								MOVE_DOWN_LEFT  = 6,
								MOVE_LEFT_UP     = 7;
	
	private float saturationFlux = 0;
	/** whether saturation shift is increasing or decreasing<br><t>(<b>false</b> - decreasing, <b>true</b> - increasing) */
	public  boolean saturationDirection = false;
	
	/** square position count on the grid */
	public int gridPosX, gridPosY;	
	/** the coordinates that the control block are positioned at(the dx/dy in this case are used for the shadow colors :b)*/
	public int truePosX, truePosY;
	/** rotation of the control block measured between 0-360 degrees */
	public double rotationCBArms = 0;	
	/** basic rotated position -- can represent 0, 90, 180, 270 */
	public int rotationPos;
	public boolean rotateDir = false;
	/** whether you have invincibility or not */
	public int invincibleTimer = 0;
	
	/** if shaking on the accelerometer's X axis or not*/
	public boolean shakeOnX   = false;
	public int	   shakeTimer = 0;
	
	
	/** distance from the control block to each of its color blocks */
	public final static int CB_DISTANCE = 36;
	
	public boolean hasInitialized = false;
	
	
	//		  GAME ENTITIES				//
	//----------------------------------//
	public ColorBlockP [] colorBlocks = new ColorBlockP[4];
	ImageEntity controlBlockCenter;
	ImageEntity controlBlockArms;
	
	//		  HELPER OBJECTS			//
	//----------------------------------//
	Random randomizer = new Random();
	
	public ControlBlock(int layer, GameScr s)
	{	
		super(layer, s);
		assets = s.assets;
		gameScreen = s;
		
		truePosX = GameScr.GAME_CANVAS_LEFT - 1000;	//set initial position!
		truePosY = GameScr.GAME_CANVAS_TOP  - 1000;
		gridPosX = 1;
		gridPosY = 1;
		input = screen.game.getInput();
		
		controlBlockCenter  = new ImageEntity(-1000, -1000, assets.getImage(IMG_CB_CENTER), GameScr.LAYER_ENTITIES_FRONT, screen);
		controlBlockArms	= new ImageEntity(-1000, -1000, assets.getImage(IMG_CB_ARMS),   GameScr.LAYER_ENTITIES, screen);
		
		colorBlocks[   ColorBlock.RED] = new ColorBlockP(   ColorBlock.RED, -1000, -1000, GameScr.LAYER_ENTITIES, gameScreen);
		colorBlocks[ ColorBlock.GREEN] = new ColorBlockP( ColorBlock.GREEN, -1000, -1000, GameScr.LAYER_ENTITIES, gameScreen);
		colorBlocks[ColorBlock.YELLOW] = new ColorBlockP(ColorBlock.YELLOW, -1000, -1000, GameScr.LAYER_ENTITIES, gameScreen);
		colorBlocks[  ColorBlock.BLUE] = new ColorBlockP(  ColorBlock.BLUE, -1000, -1000, GameScr.LAYER_ENTITIES, gameScreen);
	}
	
	/** position the blocks around the control block and calls the control block to shift its position */
	public void position()
	{
		//update where the new target destination
		if(hasInitialized)
		{
			truePosX = gameScreen.gameCanvasX(GameScr.GRID_LEFT + gridPosX * GameScr.GRID_SQ_SIZE + (GameScr.GRID_SQ_SIZE /2));
			truePosY = gameScreen.gameCanvasY(GameScr.GRID_TOP  + gridPosY * GameScr.GRID_SQ_SIZE + (GameScr.GRID_SQ_SIZE /2));
		}
		else
		{
			int destinationX = gameScreen.gameCanvasX(GameScr.GRID_LEFT + gridPosX * GameScr.GRID_SQ_SIZE + (GameScr.GRID_SQ_SIZE /2));
			int destinationY = gameScreen.gameCanvasY(GameScr.GRID_TOP  + gridPosY * GameScr.GRID_SQ_SIZE + (GameScr.GRID_SQ_SIZE /2));
			if(((destinationX - truePosX) / 5) > 0.2)
				truePosX += (destinationX - truePosX)/5;
			else
				truePosX = destinationX;
			if(((destinationY - truePosY) / 5) > 0.2)
				truePosY += (destinationY - truePosY)/5;
			else
				truePosY = destinationY;
			
			if(truePosX == destinationX && truePosY == destinationY)
				hasInitialized = true;
		}
		shiftToDest(); 		//shift the control block to where it's new position should be
		
		if(shakeTimer > 0)
		{
			if(shakeOnX)
				truePosX += shakeTimer - (randomizer.nextInt((shakeTimer * 2)));
			else
				truePosY += shakeTimer - (randomizer.nextInt((shakeTimer) * 2));
			shakeTimer--;
		}
		
		//move associated entities to the control block's position
		controlBlockCenter.x = truePosX;
		controlBlockCenter.y = truePosY;
		controlBlockArms.x 	 = truePosX;
		controlBlockArms.y 	 = truePosY;
		
		controlBlockArms.rotation = (int)Math.round(rotationCBArms);
		
		//position the color blocks relative to the control block's current angle
		colorBlocks[   ColorBlock.RED].x = (float)(truePosX + CB_DISTANCE * Math.cos(Math.toRadians(rotationPos)));
		colorBlocks[   ColorBlock.RED].y = (float)(truePosY - CB_DISTANCE * Math.sin(Math.toRadians(rotationPos))); 
		colorBlocks[  ColorBlock.BLUE].x = (float)(truePosX + CB_DISTANCE * Math.cos(Math.toRadians(rotationPos + 90)));
		colorBlocks[  ColorBlock.BLUE].y = (float)(truePosY - CB_DISTANCE * Math.sin(Math.toRadians(rotationPos + 90)));
		colorBlocks[ColorBlock.YELLOW].x = (float)(truePosX + CB_DISTANCE * Math.cos(Math.toRadians(rotationPos + 180)));
		colorBlocks[ColorBlock.YELLOW].y = (float)(truePosY - CB_DISTANCE * Math.sin(Math.toRadians(rotationPos + 180)));
		colorBlocks[ ColorBlock.GREEN].x = (float)(truePosX + CB_DISTANCE * Math.cos(Math.toRadians(rotationPos + 270)));
		colorBlocks[ ColorBlock.GREEN].y = (float)(truePosY - CB_DISTANCE * Math.sin(Math.toRadians(rotationPos + 270))); 
		
		initialized = true;	//after positioning the first time, we can safely say the control block was initialized
	}
	
	public void shiftToDest()
	{	
		boolean moved = false;
		//set speed of movement and if it is less than minimum step(0.25) stop movement
		if(x != truePosX) 
		{
			dx = (truePosX - x) / 5;
		}
		if(Math.abs(dx) <= 0.21) 
			dx = 0;
		if(y != truePosY) 
		{
			dy = (truePosY - y) / 5;
		}
		if(Math.abs(dy) <= 0.21) 
			dy = 0;
		//create motion blur if applicable
		if((dx != 0 || dy != 0) && hasInitialized && 
		   gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_GFX_MOTIONBLUR, true))
		{
			for(int color = 0; color < 4; color++)
			{
				ShadowColor blurColor = ((GameScr)screen).sColorFactory.getFactoryObject(color, x, y, 
													rotationPos, 0, rotateDir, false);
			}
		}
	}
	
	public void rotate(boolean direction)
	{
		//sfx
		if(gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
			assets.getSound(SND_ROTATE).play(1f);
		rotateDir = direction;	//set the direction rotating
		if(direction == TURN_LEFT)					
		{
			rotationPos = (rotationPos + 90) % 360;
		}	
		else if(direction == TURN_RIGHT)
		{
			rotationPos -= 90;					//rotate right
			if(rotationPos < 0)
				rotationPos = rotationPos += 360;
		}
		
		//create shadows if rotating
		if(gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_GFX_MOTIONBLUR, true))
		if(gameScreen.gameTimer > 5)	//prevents concurrent mod error!
		{
			for(int incVal = 0; incVal < 5; incVal++)
				for(int color = 0; color < 4; color++)
					((GameScr)screen).sColorFactory.getFactoryObject(color, x, y, rotationPos, 90/5 * incVal, rotateDir, true);
		}
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);

		//position the control block
		position();
		
		//set control blocks semitransparent during 
		if(invincibleTimer > 0)
		{
			for(ColorBlock c : colorBlocks)
			{
				if(invincibleTimer > 1)
				{
					c.semiTrans = true;
					c.alpha = 255 - (int)(invincibleTimer * 255.0/(INVINCIBILITY_TIMER * 1.5));
				}
				else
				{
					c.semiTrans = false;
					c.alpha = 255;
				}
			}
			invincibleTimer--;
		}
		
		//rotate the blocks if called for to suitable position
		if(rotationCBArms != rotationPos)
		{
			if(rotateDir)
				rotationCBArms = (rotationCBArms + 30) % 360;
			else
			{
				rotationCBArms = (rotationCBArms - 30);
				if(rotationCBArms < 0)
					rotationCBArms += 360;
			}
		}
		
		//if we're low on lives in the current game config, fluctuate the attached color blocks' saturation
		if(GameStats.lives <= 1)
			fluctuateSaturation(deltaTime);
	}
	
	/** move the control block in a specific position */
	public void move(final int direction)
	{
		boolean moved = false;
		switch(direction)
		{
			case MOVE_UP:	
				if(gridPosY > 0) { gridPosY--; moved = true; }
				break;
			case MOVE_UP_RIGHT:
				if(gridPosX < 2) { gridPosX++; moved = true; }
				if(gridPosY > 0) { gridPosY--; moved = true; }
				break;
			case MOVE_RIGHT:
				if(gridPosX < 2) { gridPosX++; moved = true; }
				break;
			case MOVE_RIGHT_DOWN:
				if(gridPosX < 2) { gridPosX++; moved = true; }
				if(gridPosY < 2) { gridPosY++; moved = true; }
				break;
			case MOVE_DOWN:
				if(gridPosY < 2) { gridPosY++; moved = true; }
				break; 
			case MOVE_DOWN_LEFT:
				if(gridPosY < 2) { gridPosY++; moved = true; }
				if(gridPosX > 0) { gridPosX--; moved = true; }
				break;
			case MOVE_LEFT:
				if(gridPosX > 0) { gridPosX--; moved = true; }
				break;
			case MOVE_LEFT_UP:
				if(gridPosX > 0) { gridPosX--; moved = true; }
				if(gridPosY > 0) { gridPosY--; moved = true; }
				break;
		}
		//sfx
		if(moved && gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
			assets.getSound(SND_MOVE).play(1);
	}

	/** return a rough estimation of bounding box to lighten the load on collision */
	@Override
	public Rect getBounds()
	{
		return new Rect((int)(truePosX - GameScr.GRID_SQ_SIZE * 1.5), (int)(truePosY - GameScr.GRID_SQ_SIZE * 1.5), 
					    (int)(truePosX + GameScr.GRID_SQ_SIZE * 1.5), (int)(truePosY + GameScr.GRID_SQ_SIZE * 1.5));
	}

	public void shake(boolean onXAxis)
	{
		shakeOnX = onXAxis;
		shakeTimer = 8;
	}

	@Override
	public Rect getCollidable()
	{
		return getBounds();
	}

	@Override
	public boolean collidesWith(Collidable col)
	{
		return getBounds().intersect(col.getCollidable());
	}
	
	/** desaturate and saturate the color blocks when you are losing health*/
	public void fluctuateSaturation(float deltaTime)
	{
		//fluxuate saturation as time progresses
		saturationFlux += deltaTime/1000.0;
		while(saturationFlux >= 1)
			saturationFlux -= 1;

		int desatScale = (int)Math.round(saturationFlux  * 7.0);
		
		for(ColorBlock c : colorBlocks)
		{
			if(desatScale <= 3)	//if increasing
			{
				c.setDesatValue(desatScale);	//desaturate the block completely
			}
			else
				c.setDesatValue(7 - desatScale);	//desaturate the block completely
		}
	}
}
