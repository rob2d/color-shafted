package com.whateversoft.colorshafted.screens;

import android.graphics.Color;
import android.graphics.Rect;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.constants.CSScreens;

public class WhateversoftScr extends Screen
{
	public static int FADE_LENGTH = 5;
	
	public static int DEFAULT_LAYER  = 0;
	
	ImageEntity logo = new ImageEntity(ScreenInfo.virtualWidth / 2, ScreenInfo.virtualHeight /2, 
									   assets.getImage(WhateversoftScrAssets.IMG_LOGO), DEFAULT_LAYER, this);
	
	public WhateversoftScr(Game game)
	{
		super(game, new WhateversoftScrAssets(game), 1);
	}

	@Override
	public void timedLogic()
	{
		if(gameTimer >= 25)
			goToScreen(new TitleScr((ColorShafted)game));
	}
	
	@Override
	public void drawEntities(Graphics g)
	{
		super.drawEntities(g);
		
		//draw fade in/fade out rects
		if(fadingIn)
		{
			int alpha = (int)(255.0f - (((float)fadeInTimer/(float)FADE_LENGTH) * 255.0f));
			g.drawRect(new Rect(0, 0, ScreenInfo.virtualWidth, ScreenInfo.virtualHeight), 
					Color.argb(alpha, 0, 0, 0), true);
		}
		if(fadingOut)
		{
			int alpha = (int)(((float)fadeOutTimer/(float)FADE_LENGTH) * 255.0f);
			g.drawRect(new Rect(0, 0, ScreenInfo.virtualWidth, ScreenInfo.virtualHeight), 
					Color.argb(alpha, 0, 0, 0), true);		
		}
	}
	
	@Override
	public void fadeInLogic(float deltaTime)
	{
		if(fadeInTimer >= FADE_LENGTH)
			fadingIn = false;
	}
	
	@Override
	public void fadeOutLogic(float deltaTime)
	{
		if(fadeOutTimer >= FADE_LENGTH)
			completeFadeOut();
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_WHATEVERSOFT;
	}
}
