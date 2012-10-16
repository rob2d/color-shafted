package com.whateversoft.colorshafted.screens;

import android.graphics.Color;
import android.graphics.Rect;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.Pixmap;
import com.whateversoft.android.framework.RectEntity;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.constants.CSScreens;

public class SplashScreen extends Screen
{
	RectEntity fadeRect;
	final int FADE_IN_TIMER = 25;
	Pixmap whateverSoftLogo;
	
	public SplashScreen(Game game)
	{
		super(game, null, 1);
		//ImageEntity companyLogo = new ImageEntity(0, 0, Assets.wsLogoIF, 0, this);	//add the logo to the screen
		
		//add the fading black rectangle
		fadeRect = new RectEntity(new Rect(0, 0, 801, 481), Color.argb(255, 0, 0, 0), 0, this);
	}

	@Override
	public void timedLogic()
	{
		if(gameTimer >= 50)
			goToScreen(new GameScr((ColorShafted) game));
	}

	@Override
	public void fadeInLogic(float deltaTime)
	{	
		if(fadeInTimer < FADE_IN_TIMER)
			fadeRect.color = Color.argb(255 - (int)(255/FADE_IN_TIMER * fadeInTimer), 0, 0, 0);
		else
		{
			fadeRect.color = Color.argb(0, 0, 0, 0);
			fadingIn = false;
		}
	}

	@Override
	public void fadeOutLogic(float deltaTime)
	{
		if(fadeOutTimer < FADE_IN_TIMER)
			fadeRect.color = Color.argb((int)(255/FADE_IN_TIMER * fadeOutTimer), 0, 0, 0);
		else
		{
			completeFadeOut();
		}
	}
	
	@Override
	public void backPressed()
	{
		System.exit(0);
	}

	@Override
	public void present()
	{
		Graphics g = game.getGraphics();
		drawEntities(g);
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_WHATEVERSOFT;
	}
}