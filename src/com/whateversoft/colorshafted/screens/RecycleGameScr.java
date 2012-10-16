package com.whateversoft.colorshafted.screens;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.constants.CSScreens;

public class RecycleGameScr extends Screen
{

	public RecycleGameScr(Game game)
	{
		super(game, null, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void timedLogic()
	{
		goToScreen(new GameScr((ColorShafted)game));
		
	}
	
	@Override
	public void drawEntities(Graphics g)
	{}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_GAME;
	}

}
