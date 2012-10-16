package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_BLACK_HOLE;

import java.util.Random;

import android.graphics.Rect;

import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.colorshafted.screens.GameScr;

public class BlackHole extends ImageEntity implements Collidable
{
	public final int SPEED = 4;
	public final int ROTATE_PER_SEC = 560;
	GameScr			 gameScreen;
	float			 actualRotation;
	
	public BlackHole(GameScr s)
	{
		super(-100, -100, s.assets.getImage(IMG_BLACK_HOLE), GameScr.LAYER_ENTITIES_FRONT, s);
		gameScreen = s;
	}
	
	public BlackHole(int orientation, GameScr s)
	{
		super(-100, -100, s.assets.getImage(IMG_BLACK_HOLE), GameScr.LAYER_ENTITIES_FRONT, s);
		setOrientation(orientation);		//begin the black hole's directional values
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		actualRotation -= (deltaTime * 540.0f)/1000.0f;
		actualRotation %= 360;
		rotation = Math.round(actualRotation);
	}
	
	public void setOrientation(int orientation)
	{
		boolean isGoingDown;
		boolean isGoingRight;
		switch(orientation)
		{
			case EnvObjSpawner.BH_OR_LEFT:
				isGoingDown = (new Random()).nextBoolean();
				dx = 0;
				dy = SPEED * (isGoingDown? 1 : -1);
				x  = GameScr.GAME_CANVAS_LEFT + GameScr.GRID_LEFT + (int)(GameScr.GRID_SQ_SIZE * 0.5);
				y  = isGoingDown ? GameScr.GAME_CANVAS_TOP - (int)(GameScr.GRID_SQ_SIZE * 4): 
					   			   GameScr.GAME_CANVAS_BOTTOM +  (int)(GameScr.GRID_SQ_SIZE * 4);
				break;
			case EnvObjSpawner.BH_OR_RIGHT:
				isGoingDown = (new Random()).nextBoolean();
				dx = 0;
				dy = SPEED * (isGoingDown? 1 : -1);
				x  = GameScr.GAME_CANVAS_LEFT + GameScr.GRID_RIGHT - (int)(GameScr.GRID_SQ_SIZE * 0.5);
				y  = isGoingDown ? GameScr.GAME_CANVAS_TOP - (int)(GameScr.GRID_SQ_SIZE * 4): 
								   GameScr.GAME_CANVAS_BOTTOM +  (int)(GameScr.GRID_SQ_SIZE * 4);
				break;
			case EnvObjSpawner.BH_OR_TOP:
				isGoingRight = (new Random()).nextBoolean();
				dx = SPEED * (isGoingRight? 1 : -1);
				x  = isGoingRight? GameScr.GAME_CANVAS_LEFT  - (float)(GameScr.GRID_SQ_SIZE * 4) : 
					   GameScr.GAME_CANVAS_RIGHT + (float)(GameScr.GRID_SQ_SIZE * 4);
				y  = GameScr.GAME_CANVAS_TOP + GameScr.GRID_TOP + (int)(GameScr.GRID_SQ_SIZE * 0.5);
				break;
			case EnvObjSpawner.BH_OR_BOTTOM:
				isGoingRight = (new Random()).nextBoolean();
				dx = SPEED * (isGoingRight? 1 : -1);
				dy = 0;
				x  = isGoingRight? GameScr.GAME_CANVAS_LEFT  - (float)(GameScr.GRID_SQ_SIZE * 4) : 
								   GameScr.GAME_CANVAS_RIGHT + (float)(GameScr.GRID_SQ_SIZE * 4);
				y  = GameScr.GAME_CANVAS_TOP + GameScr.GRID_BOTTOM - (int)(GameScr.GRID_SQ_SIZE * 0.5);
				break;
		}
	}

	/** */
	public void initializeObject(int orientation)
	{
		visible = true;
		destroyed = false;
		setOrientation(orientation);
		
		//-----------------------------------------//
		//	ADD THIS BLACKHOLE TO THE LIST OF BLACK HOLES TO TRACK
		//-----------------------------------------//
		synchronized(screen)
		{
			gameScreen.blackHoles.add(this);
		}
	}

	/** define a rectangle the size of a grid to collide with */
	@Override
	public Rect getCollidable()
	{
		return new Rect(Math.round(x) - 18, Math.round(y) - 18, Math.round(x) + 18, Math.round(y) + 18);
	}

	@Override
	public boolean collidesWith(Collidable col)
	{
		return getBounds().intersect(col.getCollidable());
	}
}
