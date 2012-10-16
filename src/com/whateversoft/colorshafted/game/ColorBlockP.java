package com.whateversoft.colorshafted.game;

import java.util.HashMap;
import java.util.Map;

import android.util.Pair;

import com.whateversoft.colorshafted.screens.GameScr;

import static com.whateversoft.colorshafted.screens.GameScrAssets.*;

/** a ColorBlock which has been instantiated as a part of a player's control block 
 *  Copyright 2011 Robert Concepcion III */
public class ColorBlockP extends ColorBlock
{
	/** How much the color has been transformed towards its final color */
	public float xformAmt = 0.0f;
	
	public boolean rotated360 = false;

	public ColorBlockP(int c, int xInit, int yInit, int layer, GameScr s)
	{
		super(c, xInit, yInit, layer, s);
	}

}
