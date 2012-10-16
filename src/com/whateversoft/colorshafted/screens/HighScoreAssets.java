package com.whateversoft.colorshafted.screens;

import android.app.Activity;
import android.graphics.Typeface;

import com.whateversoft.android.framework.Anim;
import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageFrame;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.android.framework.Graphics.PixmapFormat;

public class HighScoreAssets extends ScreenAssets
{
	public static int 		ASSET_COUNT           = 12;
	public static int 		IMG_SPACE_BG 	   	  =  0,
							IMG_SCROLL_UP	  	  =  1,
							IMG_SCROLL_DOWN	  	  =  2,
							IMG_HEADER		  	  =  3,
							IMG_FOOTER		  	  =  4,
							IMG_LOCAL_S			  =  5,
							IMG_LOCAL_U			  =  6,
							IMG_GLOBAL_S		  =  7,
							IMG_GLOBAL_U		  =  8;
	public static int		ANIM_LOADING		  =  9;
	public static int		SND_MOVE			  = 10;
	public static int		FONT_SFTELEVISED	  = 11;
	
	public HighScoreAssets(Game g)
	{
		super(g);
	}

	
	@Override
	public void obtainAssets()
	{
		Graphics g = game.getGraphics();
		Audio	 a = game.getAudio();
		
		assets 					 = new Object[ASSET_COUNT];
		assets[IMG_SPACE_BG]	 = new ImageFrame(g.newPixmap("gfx/title_screen/background.png", PixmapFormat.RGB565), 0, 0, game);
		assets[IMG_SCROLL_UP]    = new ImageFrame(g.newPixmap("gfx/menu_screens/scroll_up.png", PixmapFormat.ARGB4444), 18, 24, game);
		assets[IMG_SCROLL_DOWN]	 = new ImageFrame(g.newPixmap("gfx/menu_screens/scroll_down.png", PixmapFormat.ARGB4444), 18, 24, game);
		assets[IMG_HEADER]		 = new ImageFrame(g.newPixmap("gfx/menu_screens/hs_header.png", PixmapFormat.ARGB4444), 355, -72, game);
		assets[IMG_FOOTER]		 = new ImageFrame(g.newPixmap("gfx/menu_screens/footer.png", PixmapFormat.ARGB4444), 355, 80, game);
		assets[IMG_LOCAL_S]  	 = new ImageFrame(g.newPixmap("gfx/menu_screens/local_selected.png",  PixmapFormat.ARGB4444), 39, 14, game);
		assets[IMG_LOCAL_U]  	 = new ImageFrame(g.newPixmap("gfx/menu_screens/local_unselected.png", PixmapFormat.ARGB4444), 39, 14, game);
		assets[IMG_GLOBAL_S] 	 = new ImageFrame(g.newPixmap("gfx/menu_screens/global_selected.png", PixmapFormat.ARGB4444), 52, 12, game);
		assets[IMG_GLOBAL_U] 	 = new ImageFrame(g.newPixmap("gfx/menu_screens/global_unselected.png", PixmapFormat.ARGB4444), 52, 12, game);
		
		assets[ANIM_LOADING]	 = new Anim(80, 0, 
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_01.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_02.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_03.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_04.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_05.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_06.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_07.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_08.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_09.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_10.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_11.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_12.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_13.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_14.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_15.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_16.png", PixmapFormat.ARGB4444), 0, 0, game),
									new ImageFrame(g.newPixmap("gfx/menu_screens/loading/loading_bw_17.png", PixmapFormat.ARGB4444), 0, 0, game));
		assets[FONT_SFTELEVISED] = Typeface.createFromAsset(((Activity)game).getApplicationContext().getAssets(), "fonts/sfintellivised.ttf");
		assets[SND_MOVE]		 = a.newSound("snd/sndmove.ogg");
	}
}
