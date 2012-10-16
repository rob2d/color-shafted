package com.whateversoft.colorshafted.screens;

import android.app.Activity;
import android.graphics.Typeface;

import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageFrame;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.android.framework.Graphics.PixmapFormat;

public class TitleScrAssets extends ScreenAssets
{
	public static int ASSET_COUNT 	   	   = 25;	/* be sure to always update this!!!*/
	
	public static int IMG_SPACE_BG 	   	   	   = 0,
					  IMG_ARCADE_BTN   	   	   = 1,
					  IMG_PSYCHOUT_BTN	   	   = 2,
					  IMG_BUTTON_WHEEL	   	   = 3,
					  IMG_LOGO			   	   = 4,
					  IMG_ICON_CONTAINER   	   = 5,
					  IMG_ICON_SETTINGS_U	   = 6,
					  IMG_ICON_TUTORIAL_U	   = 7,
					  IMG_ICON_SOUND_U	       = 8,
					  IMG_ICON_MUSIC_U	       = 9,
					  IMG_ICON_CREDITS_U	   = 10,
					  IMG_ICON_HIGHSCORE_U     = 11,
	  				  IMG_ICON_SETTINGS_S	   = 12,
	  				  IMG_ICON_TUTORIAL_S	   = 13,
	  				  IMG_ICON_SOUND_S	       = 14,
	  				  IMG_ICON_MUSIC_S	       = 15,
	  				  IMG_ICON_CREDITS_S	   = 16,
	  				  IMG_ICON_HIGHSCORE_S     = 17,
	  				  IMG_ICON_DISABLED		   = 18,
	  				  IMG_ACHIEVEMENT_BTN	   = 19,
	  				  IMG_WEB_BTN			   = 20;
	
	
	
	
	public static int FONT_SFTELEVISED 	   = 21;
	public static int SND_BOMB		   	   = 22;
	public static int SND_ITEM	   	   	   = 23;
	public static int SND_ROTATE	   	   = 24;

	public TitleScrAssets(Game g)
	{
		super(g);
	}

	@Override
	public void obtainAssets()
	{
		Graphics g = game.getGraphics();
		Audio	 a = game.getAudio();
		assets = new Object[ASSET_COUNT];
		assets[IMG_SPACE_BG] = new ImageFrame(g.newPixmap("gfx/title_screen/background.png", PixmapFormat.RGB565), 0, 0, game);
		assets[IMG_ARCADE_BTN] = new ImageFrame(g.newPixmap("gfx/title_screen/button_arcade.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_PSYCHOUT_BTN] = new ImageFrame(g.newPixmap("gfx/title_screen/button_psychout.png", PixmapFormat.ARGB4444), 0, 0, game);
		
		assets[IMG_ACHIEVEMENT_BTN] = new ImageFrame(g.newPixmap("gfx/title_screen/achievements.png", PixmapFormat.ARGB4444), 55, 55, game);
		assets[IMG_WEB_BTN] = new ImageFrame(g.newPixmap("gfx/title_screen/webicon.png", PixmapFormat.ARGB4444), 55, 55, game);
		
		assets[IMG_BUTTON_WHEEL] = new ImageFrame(g.newPixmap("gfx/title_screen/wheel.png", PixmapFormat.ARGB4444), 82, 82, game);
		assets[IMG_LOGO]		 = new ImageFrame(g.newPixmap("gfx/title_screen/logo.png", PixmapFormat.ARGB4444), 378, 56, game);
		
		assets[IMG_ICON_CONTAINER] = new ImageFrame(g.newPixmap("gfx/title_screen/icon_container.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_HIGHSCORE_U]= new ImageFrame(g.newPixmap("gfx/title_screen/highscores_u.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_SETTINGS_U]= new ImageFrame(g.newPixmap("gfx/title_screen/settings_u.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_TUTORIAL_U]= new ImageFrame(g.newPixmap("gfx/title_screen/tutorial_u.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_MUSIC_U]= new ImageFrame(g.newPixmap("gfx/title_screen/music_u.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_SOUND_U]= new ImageFrame(g.newPixmap("gfx/title_screen/sound_u.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_CREDITS_U]= new ImageFrame(g.newPixmap("gfx/title_screen/credits_u.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_HIGHSCORE_S]= new ImageFrame(g.newPixmap("gfx/title_screen/highscores_s.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_SETTINGS_S]= new ImageFrame(g.newPixmap("gfx/title_screen/settings_s.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_TUTORIAL_S]= new ImageFrame(g.newPixmap("gfx/title_screen/tutorial_s.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_MUSIC_S]= new ImageFrame(g.newPixmap("gfx/title_screen/music_s.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_SOUND_S]= new ImageFrame(g.newPixmap("gfx/title_screen/sound_s.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_CREDITS_S]= new ImageFrame(g.newPixmap("gfx/title_screen/credits_s.png", PixmapFormat.ARGB4444), 0, 0, game);
		assets[IMG_ICON_DISABLED] = new ImageFrame(g.newPixmap("gfx/title_screen/selection_disabled.png", PixmapFormat.RGB565), 0, 0, game);
		assets[FONT_SFTELEVISED] = Typeface.createFromAsset(((Activity)game).getApplicationContext().getAssets(), "fonts/sfintellivised.ttf");
		assets[SND_BOMB]		 = a.newSound("snd/sndbomb.ogg");
		assets[SND_ITEM]		 = a.newSound("snd/snditem.ogg");
		assets[SND_ROTATE]		 = a.newSound("snd/sndrotate.ogg");
	}
}
