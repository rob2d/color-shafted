package com.whateversoft.colorshafted.screens;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.constants.CSScreens;

public class GameOverRecycleScr extends Screen
{

	public GameOverRecycleScr(Game game)
	{
		super(game, null, 1);
		
	}

	@Override
	public void timedLogic()
	{
		goToScreen(new GameOverScr((ColorShafted)game));
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_GAME_OVER;
	}
}
