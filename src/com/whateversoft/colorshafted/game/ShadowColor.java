package com.whateversoft.colorshafted.game;

import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.ImageFrame;
import com.whateversoft.colorshafted.screens.GameScr;

import static com.whateversoft.colorshafted.screens.GameScr.LAYER_ENTITIES;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_BLOCK_B;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_BLOCK_G;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_BLOCK_R;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_BLOCK_Y;

public class ShadowColor extends ImageEntity
{
	public boolean isEnemyShadow = false;
	
	/** represents a shadow color block of a player */
	public ShadowColor(GameScr s)
	{
		super(GameScr.LAYER_ENTITIES, s);
		semiTrans = true;
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		if(alpha >= 35 && !isEnemyShadow)
			alpha -= 35;
		else if(alpha >= 35 && isEnemyShadow)
			alpha -= 15;
		else
			((GameScr)screen).sColorFactory.throwInPool(this);
	}
	
	public void initializeObject(int c, float xOrig, float yOrig, int aOrig, int aIncrement, boolean rotateDir, boolean isRotationBlock)
	{
		isEnemyShadow = false;
		int angleOffset = 0; 		//used to calculate the x and y position relative to the colorblocks origin
		switch(c)
		{
			case ColorBlock.RED: 	 
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_R); 	
				angleOffset = aOrig + (rotateDir ? -aIncrement : aIncrement);
				break;
			case ColorBlock.BLUE:  
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_B);	
				angleOffset	= aOrig + 90 + (rotateDir ? -aIncrement : aIncrement);
				break;
			case ColorBlock.YELLOW: 
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_Y); 
				angleOffset = aOrig + 180 + (rotateDir ? -aIncrement : aIncrement);
				break;
			case ColorBlock.GREEN:  
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_G);	
				angleOffset = aOrig + 270 + (rotateDir ? -aIncrement : aIncrement);
				break;
		}
	
		 x = (float)(xOrig + ControlBlock.CB_DISTANCE * Math.cos(Math.toRadians(angleOffset)));
		 y = (float)(yOrig - ControlBlock.CB_DISTANCE * Math.sin(Math.toRadians(angleOffset))); 
		
		 if(isRotationBlock)
			 alpha = (int) (0 + ((90 - aIncrement) * 2.5));
		 else
			 alpha = 105;

		visible = true;
	}
	
	public void initializeObject(int c, float xOrig, float yOrig)
	{
		isEnemyShadow = true;
		switch(c)
		{
			case ColorBlock.RED: 	 
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_R); 
				break;
			case ColorBlock.BLUE:  
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_B);	
				break;
			case ColorBlock.YELLOW: 
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_Y); 
				break;
			case ColorBlock.GREEN:  
				imgFrame 	= screen.assets.getImage(IMG_BLOCK_G);
				break;
		}
		
		x = xOrig;
		y = yOrig;
		alpha = 150;
		visible = true;
	}
	
}
