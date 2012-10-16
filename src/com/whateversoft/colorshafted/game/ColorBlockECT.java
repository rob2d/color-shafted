package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_B2G;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_B2R;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_B2Y;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_G2B;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_G2R;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_G2Y;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_R2B;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_R2G;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_R2Y;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_Y2B;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_Y2G;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMGA_BLOCK_Y2R;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.util.Pair;

import com.whateversoft.colorshafted.screens.GameScr;

/** a ColorBlock which has been instantiated as a part of a player's control block 
 *  Copyright 2011 Robert Concepcion III */
public class ColorBlockECT extends ColorBlockE
{
	/** How much the color has been transformed towards its final color */
	public float xformAmt = 0.0f;
	
	public boolean rotated360 = false;
	
	/** the color a block will become after transforming */
	public int     targetColor;
	public int	   preColor;

	/** the asset within the game assets that this color transformation represents<br/>
	    (e.g., if we are transforming from Red to Blue, it is set to "GameScrAssets.IMGA_BLOCK_R2B"*/
	public int    xFormAnimAsset = -1;
	public int    xFormFrame = -1;
	
	public static Map<Pair<Integer, Integer>, Integer> 
		xformColorMap = new HashMap<Pair<Integer, Integer>, Integer>();
	
	static	{	setUpXformMap();   }
	
	public ColorBlockECT(int c, int initDir, int initShaft, int xInit, int yInit, int layer, GameScr s)
	{
		super(c, initDir, initShaft, xInit, yInit, layer, s);
	}
	
	/** where the true initialization happens for this object. 
	 *  we set the beginning color, target color and animation in this function as well
	 *  as the typical enemy's instantiation through the super.instantiateAsEnemy() call */
	@Override
	public void instantiateAsEnemy(int shaft, int c)
	{
		super.instantiateAsEnemy(shaft, c);
		Log.d("COLORSHAFTED", "just ran color transformations' instantiation w 2 parameters...");
		resetCTBlock(c);
		this.setGraphicColor(preColor);
		Log.d("COLORSHAFTED", "successfully ran resetCTBlock(color)");
	}

	/** sets the color transform block to a reset state;
	 *  gives it a random first color to transform from as well as sets its rotated
	 *  flag as false among other things
	 * @param targetC - target color to transform to
	 */
	public void resetCTBlock(int targetC)
	{	
		xformAmt = 0;
		xFormFrame = 0;
		targetColor = targetC;
		rotated360 = false;

		//assign the color to transform from to something other than the target color
		do{
			preColor = (int)(Math.random() * 4);
			Log.d("COLORSHAFTED", "assigning the preColor for the color transformation block...");
		} 
		while(preColor == targetColor);
		
		setLogicalColor(targetColor);
		setGraphicColor(preColor);
		//load the color map
		if(targetColor != -1)
			xFormAnimAsset = xformColorMap.get(new Pair<Integer, Integer>(preColor, targetColor));
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		calcDistFromCenter();
		if(xFormAnimAsset != -1)
			adjustXformColor();
		 
		xformAmt += deltaTime / 1000.0f;
		rotation = Math.round(xformAmt * 360.0f);
		
		if(rotation >= 0 && rotated360)
			rotation = 0;
		
		if(rotation >= 340)
			rotated360 = true;
	}
	
	@Override
	public void setLogicalColor(int c)
	{
		super.setLogicalColor(c);
		targetColor = c;
	}
	
	/** set up the map which says which animation a block will be transforming with */
	public static void setUpXformMap()
	{
		xformColorMap.put(new Pair<Integer, Integer>(BLUE, RED),     IMGA_BLOCK_B2R);
		xformColorMap.put(new Pair<Integer, Integer>(BLUE, YELLOW),  IMGA_BLOCK_B2Y);
		xformColorMap.put(new Pair<Integer, Integer>(BLUE, GREEN),   IMGA_BLOCK_B2G);
		xformColorMap.put(new Pair<Integer, Integer>(YELLOW, RED),   IMGA_BLOCK_Y2R);
		xformColorMap.put(new Pair<Integer, Integer>(YELLOW, BLUE),  IMGA_BLOCK_Y2B);
		xformColorMap.put(new Pair<Integer, Integer>(YELLOW,GREEN),  IMGA_BLOCK_Y2G);
		xformColorMap.put(new Pair<Integer, Integer>(RED, BLUE), 	   IMGA_BLOCK_R2B);
		xformColorMap.put(new Pair<Integer, Integer>(RED, YELLOW),   IMGA_BLOCK_R2Y);
		xformColorMap.put(new Pair<Integer, Integer>(RED, GREEN),    IMGA_BLOCK_R2G);
		xformColorMap.put(new Pair<Integer, Integer>(GREEN, RED),    IMGA_BLOCK_G2R);
		xformColorMap.put(new Pair<Integer, Integer>(GREEN, BLUE),   IMGA_BLOCK_G2B);
		xformColorMap.put(new Pair<Integer, Integer>(GREEN, YELLOW), IMGA_BLOCK_G2Y);
	}
	
	/** adjust the saturation as the block goes towards the center of the screen */
	public void adjustXformColor()
	{
		int prevXFormFrame = xFormFrame;
		
		final int prevSaturationLevel = saturationLevel;
		final int endXformDist = 210 - (GameStats.difficulty * 2);	//distance we start transforming
		
		//calculate the saturation level
		if(distFromCenter < endXformDist)
			xFormFrame = 4;
		else if(distFromCenter < endXformDist + 5)
			xFormFrame = 3;
		else if(distFromCenter < endXformDist + 10)
			xFormFrame = 2;
		else if(distFromCenter < endXformDist + 15)
			xFormFrame = 1;
		else if(distFromCenter >= endXformDist + 20)
			xFormFrame = 0;
		
		if(xFormFrame != prevXFormFrame)
		{
			if(xFormFrame > 0 && xFormFrame < 4)
				imgFrame = gameScreen.assets.getImageInArray(xFormAnimAsset, xFormFrame - 1);
			else if(xFormFrame == 0)
				setGraphicColor(preColor);
			else if(xFormFrame == 4)
				setGraphicColor(targetColor);
		}
	}
}