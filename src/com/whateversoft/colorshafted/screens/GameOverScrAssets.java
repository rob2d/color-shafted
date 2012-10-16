package com.whateversoft.colorshafted.screens;

import android.app.Activity;
import android.graphics.Typeface;

import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageFrame;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.android.framework.Graphics.PixmapFormat;

public class GameOverScrAssets extends ScreenAssets
{
	public static int ASSET_COUNT 	   	   = 4;	/* be sure to always update this!!!*/
	
	public static int IMG_BG				   = 0;
	public static int IMG_GAMEOVER_TXT		   = 1;
	public static int IMG_GAMEOVER_TAPTXT	   = 2;
	public static int SND_GAMEOVER			   = 3;

	public GameOverScrAssets(Game g)
	{
		super(g);
	}

	@Override
	public void obtainAssets()
	{
		Graphics g = game.getGraphics();
		Audio	 a = game.getAudio();
		
		assets = new Object[ASSET_COUNT];
		
		//load images
		assets[IMG_BG]				  = new ImageFrame(g.newPixmap("gfx/game_over/end_screen_background.png", PixmapFormat.RGB565),
														 400, 400, game);
		assets[IMG_GAMEOVER_TXT]      = new ImageFrame(g.newPixmap("gfx/game_over/end_screen_text.png", PixmapFormat.RGB565), 
											   400, 90, game);
		assets[IMG_GAMEOVER_TAPTXT]   = new ImageFrame(g.newPixmap("gfx/game_over/end_screen_tap.png", PixmapFormat.RGB565),
												224, 129, game);
		assets[SND_GAMEOVER]		  = a.newSound("snd/sndgameover.ogg");
	}
}
