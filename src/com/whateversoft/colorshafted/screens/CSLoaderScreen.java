package com.whateversoft.colorshafted.screens;

import android.graphics.Color;
import android.graphics.Rect;

import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.game.CSGlobalAssets;

public class CSLoaderScreen extends Screen
{
	//this screen is only in place so that the game can retrieve its preferences before going to the first
	//(just in case for debugging)
	public CSLoaderScreen(ColorShafted game)
	{
		super(game, null, 0);
	}

	@Override
	public void timedLogic()
	{	
		((ColorShafted)game).globalAssets = new CSGlobalAssets(game);	//load global assets here since constructor has already run!
		goToScreen(new WhateversoftScr((ColorShafted) game));
	}
	
	@Override
	public void drawEntities(Graphics g)
	{
		g.drawRect(new Rect(0, 0, g.getWidth(), g.getHeight()), Color.BLACK, true);
	}

	@Override
	public int getScreenCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
