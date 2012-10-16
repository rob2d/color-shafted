package com.whateversoft.colorshafted.screens;

import android.graphics.Color;
import android.graphics.Rect;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.RectEntity;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.constants.CSScreens;

public class TestBlockScr extends Screen
{

	public TestBlockScr(Game game)
	{
		super(game, new GameScrAssets(game), 6);	
		
		for(int i = 0; i < 4; i++)
		{
			new ImageEntity(100 + 64 * i, 96 + 0 * 64, 
					assets.getImageInArray(GameScrAssets.IMGA_BLOCK_B_DS, i), 0, this);
			
			new ImageEntity(100 + 64 * i, 96 + 1 * 64, 
					assets.getImageInArray(GameScrAssets.IMGA_BLOCK_G_DS, i), 0, this);
			
			new ImageEntity(100 + 64 * i, 96 + 2 * 64, 
					assets.getImageInArray(GameScrAssets.IMGA_BLOCK_R_DS, i), 0, this);
		}
	}

	@Override
	public void timedLogic()
	{
		
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_LOADING;
	}
	
	@Override
	public void screenTapped()
	{
		goToScreen(new CSLoaderScreen((ColorShafted)game));
	}
}
