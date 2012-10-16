package com.whateversoft.colorshafted.screens;

import android.app.Activity;
import android.graphics.Typeface;

import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageFrame;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.android.framework.Graphics.PixmapFormat;

public class WhateversoftScrAssets extends ScreenAssets
{
	public static int ASSET_COUNT 	   	   = 1;	/* be sure to always update this!!!*/
	
	public static int IMG_LOGO	 	   	   = 0;

	public WhateversoftScrAssets(Game g)
	{
		super(g);
	}

	@Override
	public void obtainAssets()
	{
		Graphics g = game.getGraphics();
		Audio	 a = game.getAudio();
		assets = new Object[ASSET_COUNT];
		assets[IMG_LOGO] = new ImageFrame(g.newPixmap("gfx/splash_screen/whateversoft_logo.png", 
										  PixmapFormat.RGB565), 400, 240, game);
	}
}
