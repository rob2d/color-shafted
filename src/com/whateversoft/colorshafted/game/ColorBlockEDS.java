package com.whateversoft.colorshafted.game;

import com.whateversoft.colorshafted.screens.GameScr;
import com.whateversoft.colorshafted.screens.GameScrAssets;

import static com.whateversoft.colorshafted.screens.GameScr.*;

/** Enemy instantiation of a ColorBlock on the game screen with the special property of
 *  rotating and transforming into a desaturated block while travelling to the grid
 *  Copyright 2011 Robert Concepcion III  */
public class ColorBlockEDS extends ColorBlockE
{	
	/** How much the color has been transformed towards its final color */
	public float xformAmt = 0.0f;
	
	public boolean rotated360 = false;
	
	/** default constructor. if the initial direction and initial shaft are specified as -1, we do not yet
	 *  call instantiate as enemy */
	public ColorBlockEDS(int c, int initDir, int initShaft, int xInit, int yInit, int layer, GameScr s)
	{
		super(c, initDir, initShaft,xInit, yInit, layer, s);
		
		if(initDir != -1 && initShaft != -1)
			instantiateAsEnemy(initDir, initShaft, c);
	}
	
	public ColorBlockEDS(int c, int xInit, int yInit, int layer, GameScr s)
	{
		super((int)(Math.random() * 4), xInit, yInit, layer, s);
		int initDir = 0, initShaft = 0;
		instantiateAsEnemy(initDir, initShaft, c);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		xformAmt += deltaTime / 1000.0f;
		rotation = Math.round(xformAmt * 360.0f);
		
		if(rotation >= 0 && rotated360)
			rotation = 0;
		
		if(rotation >= 340)
			rotated360 = true;
		
		calcDistFromCenter();	//if moving, calculate the distance from the center
		adjustSaturation();		//adjust saturation as necessary
	}
	
	/** adjust the saturation as the block goes towards the center of the screen */
	public void adjustSaturation()
	{
		final int prevSaturationLevel = saturationLevel;
		final int endXformDist = 140 + (GameStats.difficulty * 3);	//distance we start transforming
		//calculate the saturation level
		if(distFromCenter < (endXformDist + 0))
			setDesatValue(4);
		else if(distFromCenter < (endXformDist + 12))
			setDesatValue(3);
		else if(distFromCenter < (endXformDist + 24))
			setDesatValue(2);
		else if(distFromCenter < (endXformDist + 36))
			setDesatValue(1);
		else if(distFromCenter >= (endXformDist + 48))
			setDesatValue(0);
	}
	
	public void setUpDSBlock(int c)
	{
		xformAmt = 0;
		setDesatValue(0);
		rotated360 = false;
		setGraphicColor(c);
	}
	
	@Override
	public void instantiateAsEnemy(int shaft, int c)
	{
		super.instantiateAsEnemy(shaft, c);
		setUpDSBlock(c);

	}
}