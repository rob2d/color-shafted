package com.whateversoft.colorshafted.screens;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;

import com.whateversoft.android.framework.Anim;
import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageFrame;
import com.whateversoft.android.framework.Pixmap;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.android.framework.Graphics.PixmapFormat;
import com.whateversoft.android.framework.impl.android.AndroidPixmap;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.game.GameStats;
public class GameScrAssets extends ScreenAssets
{
	public final static int ASSET_COUNT           =  54;
	public final static int	
							//basic color block images
							IMG_BLOCK_R           =  0,
							IMG_BLOCK_B        	  =  1,
							IMG_BLOCK_G       	  =  2,
							IMG_BLOCK_Y     	  =  3,
							IMG_BLOCK_FLASH       =  4,
							
							IMG_CB_CENTER	      =  5,
							IMG_CB_ARMS		      =  6,
							
							IMG_SHAFT_BG_L	      =  7,
							IMG_SHAFT_BG_R	      =  8,
							IMG_SHAFT_BG_C		  =  9,
							IMG_BOMB_BUTTON		  = 10,
							IMG_HUD_PANEL		  = 11,
							IMG_BLACK_HOLE		  = 12,
						
							//explosions
							ANIM_R_EXPL    		  = 13,
							ANIM_B_EXPL	  		  = 14,
							ANIM_Y_EXPL 		  = 15,
							ANIM_G_EXPL  		  = 16,
							ANIM_BAD_EXPL	  	  = 17,
							ANIM_BOMB_EXPL	  	  = 18,
						
							//sounds
							SND_GOOD_HIT		  = 19,
							SND_BAD_HIT			  = 20,
							SND_ROTATE			  = 21,
							SND_BOMB			  = 22,
							SND_MOVE			  = 23,
							SND_ITEM			  = 24,
							SND_BLACKHOLE		  = 25,
							SND_GALAXYEXPLODE	  = 26,
							SND_READY			  = 53,
							FONT_SFTELEVISED	  = 27,
							
							//desaturation conversions
							IMGA_BLOCK_B_DS	      = 28,
							IMGA_BLOCK_R_DS	      = 29,
							IMGA_BLOCK_G_DS	  	  = 30,
							IMGA_BLOCK_Y_DS  	  = 31,
							IMG_BLOCK_DS		  = 32,
							//psychout color conversions
							IMGA_BLOCK_R2G		  = 33,
							IMGA_BLOCK_R2B		  = 34,
							IMGA_BLOCK_R2Y	      = 35,
							IMGA_BLOCK_G2R		  = 36,
							IMGA_BLOCK_G2B		  = 37,
							IMGA_BLOCK_G2Y		  = 38,
							IMGA_BLOCK_Y2R		  = 39,
							IMGA_BLOCK_Y2G		  = 40,
							IMGA_BLOCK_Y2B		  = 41,
							IMGA_BLOCK_B2R		  = 42,
							IMGA_BLOCK_B2G		  = 43,
							IMGA_BLOCK_B2Y		  = 44,
							
							//items
							IMG_ITEM_XB_B		  = 45,
							IMG_ITEM_XB_R		  = 46,
							IMG_ITEM_XB_Y		  = 47,
							IMG_ITEM_XB_G		  = 48,
							

							IMG_ITEM_XL_B		  = 49,
							IMG_ITEM_XL_R		  = 50,
							IMG_ITEM_XL_Y		  = 51,
							IMG_ITEM_XL_G		  = 52;
	
	/** how many frames of desaturation we have when converting colors to black and white
	 *  (<b>excluding</b> black and white, itself) */
	public final static int DESAT_CONV_COUNT	  = 4,
	/** how many colors we have when converting from one base color(R,Y,G,B) to another
	 *  (<b>excluding</b> the color being converted to, itself) */
							COLOR_CONV_COUNT		  = 3;
	
	public GameScrAssets(Game 	g)
	{
		super(g);
	}

	@Override
	public void obtainAssets()
	{
		//get our multimedia instances from GameAPI
		Graphics g = game.getGraphics();
		Audio	 a = game.getAudio();
		
		assets = new Object[ASSET_COUNT];
		
		//load images
		loadBaseColorBlocks(g);
		loadItems(g); //WE WILL LOAD ITEMS IN NEXT RELEASE
		
		assets[IMG_BLOCK_FLASH]  = new ImageFrame(g.newPixmap("gfx/in_game/cs_cbflash.png", PixmapFormat.RGB565),
											   18, 18, game);
		assets[IMG_CB_CENTER] 	 = new ImageFrame(g.newPixmap("gfx/in_game/cs_cbcenter.png", PixmapFormat.RGB565), 9, 9, game);
		assets[IMG_CB_ARMS]   	 = new ImageFrame(g.newPixmap("gfx/in_game/cs_cbarms.png", PixmapFormat.RGB565), 19, 19, game);
		assets[IMG_SHAFT_BG_L]  = new ImageFrame(g.newPixmap("gfx/in_game/cs_bg_left.png", PixmapFormat.RGB565), 400, 240, game);
		assets[IMG_SHAFT_BG_R]	 = new ImageFrame(g.newPixmap("gfx/in_game/cs_bg_right.png", PixmapFormat.RGB565), -240, 240, game);
		assets[IMG_SHAFT_BG_C]	 = new ImageFrame(g.newPixmap("gfx/in_game/cs_bg_center.png", PixmapFormat.RGB565), 241, 240, game);
		assets[IMG_BOMB_BUTTON]  = new ImageFrame(g.newPixmap("gfx/in_game/cs_bombbutton.png", PixmapFormat.RGB565), 125, -6, game);
		assets[IMG_HUD_PANEL]  = new ImageFrame(g.newPixmap("gfx/in_game/cs_bghud.png", PixmapFormat.RGB565), -11, -9, game);
		assets[IMG_BLACK_HOLE]  = new ImageFrame(g.newPixmap("gfx/in_game/cs_blackhole.png", PixmapFormat.RGB565), 21, 21, game);
		
		//allocate space for the color conversions
		assets[IMGA_BLOCK_R_DS] = new ImageFrame[DESAT_CONV_COUNT];
		assets[IMGA_BLOCK_G_DS] = new ImageFrame[DESAT_CONV_COUNT];
		assets[IMGA_BLOCK_B_DS] = new ImageFrame[DESAT_CONV_COUNT];
		assets[IMGA_BLOCK_Y_DS] = new ImageFrame[DESAT_CONV_COUNT];
		
		//load explosion anims
		loadExplosionAnims(g);
		
		//load sounds
		loadSounds(a);		
		
		//load font
		assets[FONT_SFTELEVISED] = Typeface.createFromAsset(((Activity)game).getApplicationContext().getAssets(), "fonts/sfintellivised.ttf");
		
		createDesatBmps();
		
		//we only need the conversion BMPs in Arcade
		if(GameStats.gameStyle == GameMode.PSYCHOUT)
			createConvBmps();
	}
	
	public void loadItems(Graphics g)
	{
		assets[IMG_ITEM_XB_B]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xb_b.png", PixmapFormat.RGB565),
				   18, 18, game);
		
		assets[IMG_ITEM_XB_R]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xb_r.png", PixmapFormat.RGB565),
				   18, 18, game);
		
		assets[IMG_ITEM_XB_Y]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xb_y.png", PixmapFormat.RGB565),
				   18, 18, game);
		
		assets[IMG_ITEM_XB_G]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xb_g.png", PixmapFormat.RGB565),
				   18, 18, game);
		
		assets[IMG_ITEM_XL_B]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xl_b.png", PixmapFormat.RGB565),
				   18, 18, game);
		
		assets[IMG_ITEM_XL_R]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xl_r.png", PixmapFormat.RGB565),
				   18, 18, game);
		
		assets[IMG_ITEM_XL_Y]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xl_y.png", PixmapFormat.RGB565),
				   18, 18, game);
		
		assets[IMG_ITEM_XL_G]  = 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_i_xl_g.png", PixmapFormat.RGB565),
				   18, 18, game);
	}
	
	public void loadBaseColorBlocks(Graphics g)
	{
		assets[IMG_BLOCK_R]    = new ImageFrame(g.newPixmap("gfx/in_game/cs_cbred.png", PixmapFormat.RGB565), 
				   18, 18, game);
		
		//create other colors based on the red block using color transformations
				ColorMatrix cM = new ColorMatrix();
				//create yellow from red
				cM.set(new float[] {
		                				1f, 0.85f, 0, 0, 0f,
		                				0.85f, 1f, 0, 0, 0f,
		                				0, 0, 1, 0, 0,
		                				0, 0, 0, 1, 0 
									});
				ImageFrame newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
				assets[IMG_BLOCK_Y] = newImg;
				//create blue from red
				cM.set(new float[] {
								0f, 0f, 1f, 0, 0f,
								0f, 1f, 0, 0, 0f,
								0.75f, 0, 1, 0, 0,
								0, 0, 0, 1, 0 
					});;
				newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
				assets[IMG_BLOCK_B] = newImg;
				
				//create green from red
						cM.set(new float[] {
								0f, 0f, 1f, 0, 0f,
								0.75f, 1f, 0, 0, 0f,
								0f, 0, 1, 0, 0,
								0, 0, 0, 1, 0 
							});
				newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
				assets[IMG_BLOCK_G] = newImg;
	}
	
	public void loadSounds(Audio a)
	{
		assets[SND_BAD_HIT] 	 = a.newSound("snd/sndbadhit.ogg");
		assets[SND_GOOD_HIT]	 = a.newSound("snd/sndgoodhit.ogg");
		assets[SND_ROTATE]		 = a.newSound("snd/sndrotate.ogg");
		assets[SND_BOMB]		 = a.newSound("snd/sndbomb.ogg");
		assets[SND_MOVE]		 = a.newSound("snd/sndmove.ogg");
		assets[SND_ITEM]		 = a.newSound("snd/snditem.ogg");
		assets[SND_BLACKHOLE]	 = a.newSound("snd/sndblackhole.ogg");
		assets[SND_GALAXYEXPLODE]= a.newSound("snd/sndgalaxyend.ogg");
	}
	
	public void loadExplosionAnims(Graphics g)
	{
		//load animations
		assets[ANIM_R_EXPL]  = new Anim(97, 9, 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp00.png", PixmapFormat.ARGB4444), 48, 48, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp01.png", PixmapFormat.ARGB4444), 52, 52, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp02.png", PixmapFormat.ARGB4444), 64, 64, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp03.png", PixmapFormat.ARGB4444), 67, 67, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp04.png", PixmapFormat.ARGB4444), 88, 88, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp05.png", PixmapFormat.ARGB4444), 100, 100, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp06.png", PixmapFormat.ARGB4444), 116, 116, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp07.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp08.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbrexp09.png", PixmapFormat.ARGB4444), 128, 128, game)); 
		assets[ANIM_B_EXPL] =  new Anim(97, 9,
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp00.png", PixmapFormat.ARGB4444), 48, 48, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp01.png", PixmapFormat.ARGB4444), 52, 52, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp02.png", PixmapFormat.ARGB4444), 64, 64, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp03.png", PixmapFormat.ARGB4444), 67, 67, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp04.png", PixmapFormat.ARGB4444), 88, 88, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp05.png", PixmapFormat.ARGB4444), 100, 100, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp06.png", PixmapFormat.ARGB4444), 116, 116, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp07.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp08.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbbexp09.png", PixmapFormat.ARGB4444), 128, 128, game)); 
		
		assets[ANIM_G_EXPL] =  new Anim(97, 9,
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp00.png", PixmapFormat.ARGB4444), 48, 48, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp01.png", PixmapFormat.ARGB4444), 52, 52, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp02.png", PixmapFormat.ARGB4444), 64, 64, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp03.png", PixmapFormat.ARGB4444), 67, 67, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp04.png", PixmapFormat.ARGB4444), 88, 88, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp05.png", PixmapFormat.ARGB4444), 100, 100, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp06.png", PixmapFormat.ARGB4444), 116, 116, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp07.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp08.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbgexp09.png", PixmapFormat.ARGB4444), 128, 128, game)); 
		
		assets[ANIM_Y_EXPL] =  new Anim(97, 9,
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp00.png", PixmapFormat.ARGB4444), 48, 48, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp01.png", PixmapFormat.ARGB4444), 52, 52, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp02.png", PixmapFormat.ARGB4444), 64, 64, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp03.png", PixmapFormat.ARGB4444), 67, 67, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp04.png", PixmapFormat.ARGB4444), 88, 88, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp05.png", PixmapFormat.ARGB4444), 100, 100, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp06.png", PixmapFormat.ARGB4444), 116, 116, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp07.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp08.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_cbyexp09.png", PixmapFormat.ARGB4444), 128, 128, game)); 
		
		assets[ANIM_BAD_EXPL] = new Anim(96, 13, 
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp00.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp01.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp02.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp03.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp04.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp05.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp06.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp07.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp08.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp09.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp10.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp11.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp12.png", PixmapFormat.ARGB4444), 57, 57, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_badexp13.png", PixmapFormat.ARGB4444), 57, 57, game));
		
		assets[ANIM_BOMB_EXPL] = new Anim(95, 10,
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen01.png", PixmapFormat.ARGB4444), 32, 32, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen02.png", PixmapFormat.ARGB4444), 64, 64, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen03.png", PixmapFormat.ARGB4444), 98, 98, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen04.png", PixmapFormat.ARGB4444), 128, 128, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen05.png", PixmapFormat.ARGB4444), 200, 200, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen06.png", PixmapFormat.ARGB4444), 300, 240, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen07.png", PixmapFormat.ARGB4444), 400, 240, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen08.png", PixmapFormat.ARGB4444), 400, 240, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen09.png", PixmapFormat.ARGB4444), 400, 240, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen10.png", PixmapFormat.ARGB4444), 400, 240, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen11.png", PixmapFormat.ARGB4444), 400, 20, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen12.png", PixmapFormat.ARGB4444), 400, 240, game),
				new ImageFrame(g.newPixmap("gfx/in_game/cs_bombscreen13.png", PixmapFormat.ARGB4444), 400, 240, game));
	}
	
	public void createDesatBmps()
	{
		Graphics g = game.getGraphics();
		//get example block bitmap for dimensions and config
		Bitmap sampleBmp = null;
		
		//get the format to work with
		PixmapFormat pixFormat = null;
		//set an example block for the dimensions and configuration to be retrieved
		sampleBmp = ((AndroidPixmap)(getImage(IMG_BLOCK_G).getImg())).getBmp();
		
		switch(sampleBmp.getConfig())
		{
			case ARGB_4444:
				pixFormat = PixmapFormat.ARGB4444;
				break;
			case ARGB_8888:
				pixFormat = PixmapFormat.ARGB8888;
				break;
			case RGB_565:
				pixFormat = PixmapFormat.RGB565;
				break;
		}
		
		//create an empty bitmap to draw on
		Bitmap newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
												sampleBmp.getConfig());
		//drawing tool instances
		Canvas bmpCanvas = new Canvas(newBmp);
		Paint  paint	  = new Paint();
		ColorMatrix cM;
		ColorMatrixColorFilter cMF;
		
		final int PIVOT_POINT_X = 18;
		final int PIVOT_POINT_Y = 18;	
		
		//create the desaturated block
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
				sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		
		cM = new ColorMatrix();					//create color matrix and then desaturate it
		cM.setSaturation(0F);					//...	
		cM.setSaturation(0.0F);					//...
		cMF = new ColorMatrixColorFilter(cM);	//..
		paint.setColorFilter(cMF);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		AndroidPixmap newPixmap = new AndroidPixmap(newBmp, pixFormat);
		ImageFrame newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		assets[IMG_BLOCK_DS] = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		cM.setSaturation(1.0f);				 //reset color matrix after drawing the desaturated block...
		cMF = new ColorMatrixColorFilter(cM);
		paint.setColorFilter(cMF);
		
		//create blue desaturated block images
		sampleBmp = ((AndroidPixmap)(getImage(IMG_BLOCK_B).getImg())).getBmp();
		
		//first desaturation block(25% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(63);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_B_DS, 0, newImage);
		
		//second desaturation block(50% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(127);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_B_DS, 1, newImage);
		
		//third desaturation block(75% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(190);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_B_DS, 2, newImage);
		
		//create blue desaturated block images
		sampleBmp = ((AndroidPixmap)(getImage(IMG_BLOCK_Y).getImg())).getBmp();
		
		//first desaturation block(25% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(63);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_Y_DS, 0, newImage);
		
		//second desaturation block(50% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(127);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_Y_DS, 1, newImage);
		
		//third desaturation block(75% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(190);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_Y_DS, 2, newImage);
		
		//create red desaturated block images
		sampleBmp = ((AndroidPixmap)(getImage(IMG_BLOCK_R).getImg())).getBmp();
		
		//first desaturation block(25% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(63);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_R_DS, 0, newImage);
		
		//second desaturation block(50% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(127);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_R_DS, 1, newImage);
		
		//third desaturation block(75% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(190);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_R_DS, 2, newImage);
		
		//create blue desaturated block images
		sampleBmp = ((AndroidPixmap)(getImage(IMG_BLOCK_G).getImg())).getBmp();

		//first desaturation block(25% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(63);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_G_DS, 0, newImage);
		
		//second desaturation block(50% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(127);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_G_DS, 1, newImage);
		
		//third desaturation block(75% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
						sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		paint.setAlpha(255);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		paint.setAlpha(190);
		bmpCanvas.drawBitmap(((AndroidPixmap)(getImage(IMG_BLOCK_DS).getImg())).getBmp(),0, 0, paint);
		newPixmap = new AndroidPixmap(newBmp, pixFormat);
		newImage = new ImageFrame(newPixmap, PIVOT_POINT_X, PIVOT_POINT_Y, game);
		setImageInArray(IMGA_BLOCK_G_DS, 2, newImage);
	}

	/**
	 * 
	 */
	public void createConvBmps()
	{
		//inialize the asset arrays
		assets[IMGA_BLOCK_R2Y]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_R2B]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_R2G]  = new ImageFrame[COLOR_CONV_COUNT]; 
		assets[IMGA_BLOCK_G2Y]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_G2B]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_Y2B]  = new ImageFrame[COLOR_CONV_COUNT];
		
		assets[IMGA_BLOCK_Y2R]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_B2R]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_G2R]  = new ImageFrame[COLOR_CONV_COUNT]; 
		assets[IMGA_BLOCK_Y2G]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_B2G]  = new ImageFrame[COLOR_CONV_COUNT];
		assets[IMGA_BLOCK_B2Y]  = new ImageFrame[COLOR_CONV_COUNT];
		
		Graphics g = game.getGraphics();
		//get example block bitmap for dimensions and config
		Bitmap sampleBmp = null;
		
		//get the format to work with
		PixmapFormat pixFormat = null;
		//set an example block for the dimensions and configuration to be retrieved
		sampleBmp = ((AndroidPixmap)(getImage(IMG_BLOCK_B).getImg())).getBmp();
		
		switch(sampleBmp.getConfig())
		{
			case ARGB_4444:
				pixFormat = PixmapFormat.ARGB4444;
				break;
			case ARGB_8888:
				pixFormat = PixmapFormat.ARGB8888;
				break;
			case RGB_565:
				pixFormat = PixmapFormat.RGB565;
				break;
		}
		
		//create an empty bitmap to draw on
		Bitmap newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
												sampleBmp.getConfig());
		
		//the drawing tool instances we will need
		Canvas bmpCanvas = new Canvas(newBmp);
		Paint  paint	  = new Paint();
		ColorMatrix cM;
		ColorMatrixColorFilter cMF;
		
		final int PIVOT_POINT_X = 18;
		final int PIVOT_POINT_Y = 18;		
		
		//get red base block into a BMP for modification
		sampleBmp = ((AndroidPixmap)(getImage(IMG_BLOCK_R).getImg())).getBmp();
		//first desaturation block(25% desaturated)
		newBmp	  = Bitmap.createBitmap(sampleBmp.getWidth(), sampleBmp.getHeight(), 
					sampleBmp.getConfig());
		bmpCanvas = new Canvas(newBmp);
		cM = new ColorMatrix();
		cM.set(new float[] 
				{
                	1f, 0, 0, 0, 0,
                	0, 1.5f, 0, 0, 0,
                	0, 0, 1, 0, 0,
                	0, 0, 0, 1, 0 
                });
		cMF = new ColorMatrixColorFilter(cM);
		paint.setColorFilter(cMF);
		bmpCanvas.drawBitmap(sampleBmp,0, 0, paint);
		Pixmap newPixmap = new AndroidPixmap(newBmp, pixFormat);
		ImageFrame newImg;

		//transformed red to orange here...
		cM.set(new float[] 
				{
                	1f, 0.35f, 0f, 0f, 0,
                	0.35f, 1f, 0f, 0f, 0,
                	0f, 0f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_G), cM);
		setImageInArray(IMGA_BLOCK_G2Y, 0, newImg);
		
		cM.set(new float[] 
				{
                	1f, 0.5f, 0f, 0f, 0,
                	0.5f, 1f, 0f, 0f, 0,
                	0f, 0f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_G), cM);
		setImageInArray(IMGA_BLOCK_G2Y, 1, newImg);
		//transformed red to yellow here...
		cM.set(new float[] 
				{
                	1f, 0.85f, 0, 0, 0f,
                	0.5f, 1f, 0, 0, 0f,
                	0, 0, 1, 0, 0,
                	0, 0, 0, 1, 0 
                });
		newImg = this.createColorFilteredImg(getImage(IMG_BLOCK_G), cM);
		setImageInArray(IMGA_BLOCK_G2Y, 2, newImg);
		
		setImageInArray(IMGA_BLOCK_Y2G, 0, getImageInArray(IMGA_BLOCK_G2Y, 2));
		setImageInArray(IMGA_BLOCK_Y2G, 1, getImageInArray(IMGA_BLOCK_G2Y, 1));
		setImageInArray(IMGA_BLOCK_Y2G, 2, getImageInArray(IMGA_BLOCK_G2Y, 0));
		
		//transformed red to orange here...
		cM.set(new float[] 
				{
                	1f, 0f, 0, 0f, 0,
                	0f, 0.75f, 0f, 0f, 0,
                	0f, 0.25f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_G), cM);
		setImageInArray(IMGA_BLOCK_G2B, 0, newImg);
		
		cM.set(new float[] 
				{
                	1f, 0f, 0f, 0f, 0,
                	0f, 0.5f, 0f, 0f, 0,
                	0f, 0.5f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_G), cM);
		setImageInArray(IMGA_BLOCK_G2B, 1, newImg);
		//transformed red to yellow here...
		cM.set(new float[] 
				{
                	1f, 0f, 0, 0, 0f,
                	0f, 0.25f, 0, 0, 0f,
                	0,  0.75f, 1, 0, 0,
                	0,      0, 0, 1, 0 
                });
		newImg = this.createColorFilteredImg(getImage(IMG_BLOCK_G), cM);
		setImageInArray(IMGA_BLOCK_G2B, 2, newImg);
		//now put references to all of the images we created from green to blue in blue to green
		//(to think straight later...)
		setImageInArray(IMGA_BLOCK_B2G, 0, getImageInArray(IMGA_BLOCK_G2B, 2));
		setImageInArray(IMGA_BLOCK_B2G, 1, getImageInArray(IMGA_BLOCK_G2B, 1));
		setImageInArray(IMGA_BLOCK_B2G, 2, getImageInArray(IMGA_BLOCK_G2B, 0));
		
		
		//transformed red to orange here...
		cM.set(new float[] 
				{
                	1f, 0.25f, 0f, 0f, 0,
                	0.25f, 1f, 0f, 0f, 0,
                	0f, 0f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
		setImageInArray(IMGA_BLOCK_R2Y, 0, newImg);
				
		cM.set(new float[] 
				{
                	1f, 0.5f, 0f, 0f, 0,
                	0.5f, 1f, 0f, 0f, 0,
                	0f, 0f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
		setImageInArray(IMGA_BLOCK_R2Y, 1, newImg);
		//transformed red to yellow here...
		cM.set(new float[] 
				{
                	1f, 0.75f, 0, 0, 0f,
                	0.75f, 1f, 0, 0, 0f,
                	0, 0, 1, 0, 0,
                	0, 0, 0, 1, 0 
                });
		
		newImg = this.createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
		setImageInArray(IMGA_BLOCK_R2Y, 2, newImg);
		
		//now put references to all of the images we created from green to blue in blue to green
		//(to think straight later...)
		setImageInArray(IMGA_BLOCK_Y2R, 0, getImageInArray(IMGA_BLOCK_R2Y, 2));
		setImageInArray(IMGA_BLOCK_Y2R, 1, getImageInArray(IMGA_BLOCK_R2Y, 1));
		setImageInArray(IMGA_BLOCK_Y2R, 2, getImageInArray(IMGA_BLOCK_R2Y, 0));
		
		//transformed red to blue here...
		cM.set(new float[] 
				{
                	0.63f, 0f, 0f, 0f, 0,
                	0f, 0f, 1f, 0f, 0,
                	0.37f, 0f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
		setImageInArray(IMGA_BLOCK_R2B, 0, newImg);
				
		cM.set(new float[] 
				{
                	0.5f, 0f, 0f, 0f, 0,
                	0f,    1f, 0f, 0f, 0,
                	0.5f, 0f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
		setImageInArray(IMGA_BLOCK_R2B, 1, newImg);
		cM.set(new float[] 
				{
                	0.25f, 0f, 0f, 0f, 0,
                	0f,    1f, 0f, 0f, 0,
                	0.75f, 0f, 1f, 0f, 0,
                	0f, 0f, 0f, 1f, 0 
                });
		newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
		setImageInArray(IMGA_BLOCK_R2B, 2, newImg);
		
		//transformed red to green here...
				cM.set(new float[] 
						{
		                	0.75f, 0.25f, 0f, 0f, 0,
		                	0.25f, 0.75f, 0f, 0f, 0,
		                	0f, 0f, 1f, 0f, 0,
		                	1f, 0f, 0f, 1f, 0 
		                });
		//now put references to all of the images we created from red to blue in blue to red
		//(to think straight later...)
		setImageInArray(IMGA_BLOCK_B2R, 0, getImageInArray(IMGA_BLOCK_R2B, 2));
		setImageInArray(IMGA_BLOCK_B2R, 1, getImageInArray(IMGA_BLOCK_R2B, 1));
		setImageInArray(IMGA_BLOCK_B2R, 2, getImageInArray(IMGA_BLOCK_R2B, 0));
				
				newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
				setImageInArray(IMGA_BLOCK_R2G, 0, newImg);
						
				cM.set(new float[] 
						{
		                	0.5f, 0.5f, 0f, 0f, 0,
		                	0.5f, 0.5f, 0f, 0f, 0,
		                	0f,     0f, 1f, 0f, 0,
		                	0f,     0f, 0f, 1f, 0 
		                });
				
				newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
				setImageInArray(IMGA_BLOCK_R2G, 1, newImg);
				cM.set(new float[] 
						{
		                	0.35f, 0.65f, 0f, 0f, 0,
		                	0.65f, 0.35f, 0f, 0f, 0,
		                	0f,       0f, 1f, 0f, 0,
		                	0f,       0f, 0f, 1f, 0 
		                });
				newImg = createColorFilteredImg(getImage(IMG_BLOCK_R), cM);
				setImageInArray(IMGA_BLOCK_R2G, 2, newImg);
				
				setImageInArray(IMGA_BLOCK_G2R, 0, getImageInArray(IMGA_BLOCK_R2G, 2));
				setImageInArray(IMGA_BLOCK_G2R, 1, getImageInArray(IMGA_BLOCK_R2G, 1));
				setImageInArray(IMGA_BLOCK_G2R, 2, getImageInArray(IMGA_BLOCK_R2G, 0));
				
				
			//transformed red to green here...
			cM.set(new float[] 
					{
	                	0.37f, 0.37f, 0f, 0f, 0,
	                	0.37f, 0.37f, 0f, 0f, 0,
	                	0.25f, 0f, 0.75f, 0f, 0,
	                	1f, 0f, 0f, 1f, 0 
	                });
			
			newImg = createColorFilteredImg(getImage(IMG_BLOCK_Y), cM);
			setImageInArray(IMGA_BLOCK_Y2B, 0, newImg);
					
			cM.set(new float[] 
					{
	                	0.25f, 0.25f, 0f, 0f, 0,
	                	0.25f, 0.25f, 0f, 0f, 0,
	                	0.5f,     0f, 0.5f, 0f, 0,
	                	0f,     0f, 0f, 1f, 0 
	                });
			
			newImg = createColorFilteredImg(getImage(IMG_BLOCK_Y), cM);
			setImageInArray(IMGA_BLOCK_Y2B, 1, newImg);
			cM.set(new float[] 
					{
	                	0.175f, 0.175f, 0f, 0f, 0,
	                	0.175f, 0.175f, 0f, 0f, 0,
	                	0.65f,       0f, 0.65f, 0f, 0,
	                	0f,       0f, 0f, 1f, 0 
	                });
			newImg = createColorFilteredImg(getImage(IMG_BLOCK_Y), cM);
			setImageInArray(IMGA_BLOCK_Y2B, 2, newImg);
			
			//now put references to all of the images we created from green to blue in blue to green
			//(to think straight later...)
			setImageInArray(IMGA_BLOCK_B2Y, 0, getImageInArray(IMGA_BLOCK_Y2B, 2));
			setImageInArray(IMGA_BLOCK_B2Y, 1, getImageInArray(IMGA_BLOCK_Y2B, 1));
			setImageInArray(IMGA_BLOCK_B2Y, 2, getImageInArray(IMGA_BLOCK_Y2B, 0));
	}
	
	public ImageFrame createColorFilteredImg(ImageFrame originalImg, ColorMatrix cM)
	{	
		//get container for the original bitmap in the associated ImgFrame
		Bitmap originalBmp = ((AndroidPixmap)originalImg.getImg()).getBmp();
		
		Bitmap newBmp = Bitmap.createBitmap(originalBmp.getWidth(), originalBmp.getHeight(), 
				originalBmp.getConfig());
		
		//drawing tool instances
		Canvas newBmpCanvas = new Canvas(newBmp);
		Paint  paint	  = new Paint();
		ColorMatrixColorFilter cMF = new ColorMatrixColorFilter(cM);
		PixmapFormat pixFormat = null;
		
		switch(originalBmp.getConfig())
		{
			case ARGB_4444:
				pixFormat = PixmapFormat.ARGB4444;
				break;
			case ARGB_8888:
				pixFormat = PixmapFormat.ARGB8888;
				break;
			case RGB_565:
				pixFormat = PixmapFormat.RGB565;
				break;
		}
		
		//	perorm drawing operations by encapsulating the Color Matrix given in constructor,
		//	then passing to paint, and finally drawing it using our bitmap canvas
		paint.setColorFilter(cMF);
		newBmpCanvas.drawBitmap(originalBmp,0, 0, paint);
		
		//create a pixmap container to store the modified bmp
		AndroidPixmap newPixmap = new AndroidPixmap(newBmp, pixFormat);

		return new ImageFrame(newPixmap, originalImg.actionPointX, originalImg.actionPointY, (Object)game);
	}

}