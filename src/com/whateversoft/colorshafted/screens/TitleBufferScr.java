package com.whateversoft.colorshafted.screens;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSScreens;

public class TitleBufferScr extends Screen
{
	float initialBgPos;
	boolean givenBgPos = false;
	
	GameMode gameMode;
	
	public TitleBufferScr(Game game)
	{
		super(game, null, 1);
	}
	
	public TitleBufferScr(Game game, float initialBGPos)
	{
		super(game, null, 1);
		givenBgPos = true;
	}

	@Override
	public void timedLogic()
	{
		if(givenBgPos)
			goToScreen(new TitleScr((ColorShafted)game, initialBgPos));
		else
			goToScreen(new TitleScr((ColorShafted)game));
	}
	
	@Override
	public void drawEntities(Graphics g)
	{}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_TITLE;
	}
}
