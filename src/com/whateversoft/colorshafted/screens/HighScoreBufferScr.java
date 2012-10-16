package com.whateversoft.colorshafted.screens;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSScreens;

public class HighScoreBufferScr extends Screen
{
	int rank;
	GameMode gameMode;
	boolean scoreSubmitted;
	
	public HighScoreBufferScr(Game game)
	{
		super(game, null, 1);
		this.gameMode = null;
		this.rank = 0;
	}
	
	public HighScoreBufferScr(int rank, GameMode gameMode, boolean scoreSubmitted, Game game)
	{
		super(game, null, 1);
		this.gameMode = gameMode;
		this.rank = rank;
		this.scoreSubmitted = scoreSubmitted;
	}

	@Override
	public void timedLogic()
	{
		if(gameMode != null)
		{
			goToScreen(new HighScoreScr(rank, gameMode, scoreSubmitted, game));
		}
		else
		{
			goToScreen(new HighScoreScr(game));
		}
	}
	
	@Override
	public void drawEntities(Graphics g)
	{}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_HIGH_SCORE;
	}

}
