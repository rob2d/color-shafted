package com.whateversoft.colorshafted.screens;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;

import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.ImageFrame;
import com.whateversoft.android.framework.RectEntity;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.android.framework.TextEntity;
import com.whateversoft.android.framework.impl.android.SystemInfo;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.ColorShaftedPrompter;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSScreens;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.game.GameStats;
import com.whateversoft.colorshafted.screens.title.RotatingColorWheel;

public class TitleScr extends Screen
{
	//DEBUGGING VARIABLE... BECAUSE TESTING IS SILLY AND MONOTONOUS :D
	private boolean DEBUG_Y0_FACE = false;
	
	public boolean initialized = false;
	
	private final int FADE_OUT_LENGTH = 35;
	private final int FADE_IN_LENGTH  = 5;
	
	ImageEntity spaceBackgroundImg;
	ImageEntity iconContainerImg;
	ImageEntity arcadeModeImg;
	ImageEntity psychoutModeImg;
	ImageEntity achievementIcon,
				webLinkIcon;
	ImageEntity logoImg;
	ImageEntity disableSndImg, disableMusImg;
	SelectableIcon hSBtn, settingsBtn, creditsBtn, soundBtn, musicBtn, tutorialBtn;
	
	TextEntity  copyrightTxt;
	
	Rect	   screenFadeRect = new Rect(0, 0, ScreenInfo.virtualWidth, ScreenInfo.virtualHeight);
	public int bgY;
	public float backgroundAngle = 0;
	public final int SELECTION_COUNT        = 10;	//number of possible selections on screen

	public final int SELECTION_ARCADE       = 0;	//to select arcade mode
	public final int SELECTION_PSYCHOUT	    = 1;	//to select survival mode
	public final int SELECTION_SETTINGS	    = 2;	//to select settings
	public final int SELECTION_HIGHSCORE    = 3;
	public final int SELECTION_TUTORIAL     = 4;
	public final int SELECTION_CREDITS	    = 5;
	public final int SELECTION_SOUND	    = 6;
	public final int SELECTION_MUSIC	    = 7;
	public final int SELECTION_WEB_LINK     = 8;
	public final int SELECTION_ACHIEVEMENTS = 9;
	
	public Rect[] selectionRects = new Rect[SELECTION_COUNT];
	RotatingColorWheel arcadeWheel;
	RotatingColorWheel psychoutWheel;
	StringBuffer loadingStr = new StringBuffer("Loading Assets");

	public enum SelectionAction { PLAY_ARCADE, PLAY_SURVIVAL, GO_TO_SETTINGS, 
								  TOGGLE_SOUND, TOGGLE_MUSIC, VIEW_TUTORIAL, VIEW_CREDITS, VIEW_HIGHSCORES }

	public ArrayList<ImageEntity> selectableObjects;
	
	int startMode = -1;
	int keyboardSelection = -1;
	
	int fadeInStyle = 0;
	
	public final static int FADE_IN_CONTINUOUS = 0;
	public final static int FADE_IN_BLACKRECT  = 1;
	
	
	public TitleScr(ColorShafted game)
	{
		super(game, new TitleScrAssets(game), 2);
		
		fadingIn = true;
		
		//if we have entere the Title Screen with no parameters supplied to the constructor, 
		//have a black fade in
		fadeInStyle = FADE_IN_BLACKRECT;
		
		copyrightTxt = new TextEntity(ScreenInfo.virtualWidth - 20, ScreenInfo.virtualHeight - 20, 
				new StringBuffer("Copyright 2012 Robert Concepción III & Christopher Hoyos"), Color.argb(255, 180, 180, 180), assets.getFont(TitleScrAssets.FONT_SFTELEVISED), 16, Paint.Align.RIGHT, 1, this);
		
		spaceBackgroundImg = new ImageEntity(0, 0, assets.getImage(TitleScrAssets.IMG_SPACE_BG), 0, this);
		
		logoImg			   = new ImageEntity(game.getGraphics().getWidth()/2, 80, 
									assets.getImage(TitleScrAssets.IMG_LOGO), 1, this);
		
		arcadeModeImg	   = new ImageEntity(0, 120, assets.getImage(TitleScrAssets.IMG_ARCADE_BTN), 0, this);
		psychoutModeImg	   = new ImageEntity(0, 260, assets.getImage(TitleScrAssets.IMG_PSYCHOUT_BTN), 0, this);
		psychoutModeImg.x  = ScreenInfo.virtualWidth - psychoutModeImg.getBounds().width();
		
		int psychoutImgWidth = psychoutModeImg.getBounds().width();
		arcadeWheel   = new RotatingColorWheel(arcadeModeImg.x + 90, 141 + 70, this);
		psychoutWheel = new RotatingColorWheel(psychoutModeImg.x + psychoutImgWidth - 90, 281 + 70, this);
		
		int arcadeImgWidth = arcadeModeImg.getBounds().width() - 50;	//used to calculate position of the web link/achievements in relative terms
		webLinkIcon     = new ImageEntity(ScreenInfo.virtualWidth - ((ScreenInfo.virtualWidth - arcadeImgWidth) / 3 * 2), 141 + 70, assets.getImage(TitleScrAssets.IMG_WEB_BTN), 0, this);
		achievementIcon = new ImageEntity(ScreenInfo.virtualWidth - ((ScreenInfo.virtualWidth - arcadeImgWidth) / 3 * 1), 141 + 70, assets.getImage(TitleScrAssets.IMG_ACHIEVEMENT_BTN), 0, this);
		
		iconContainerImg	   = new ImageEntity(0, 0, assets.getImage(TitleScrAssets.IMG_ICON_CONTAINER), 0, this);
		iconContainerImg.y 	   =  game.getGraphics().getHeight() - iconContainerImg.getBounds().height();
		
		hSBtn 	   = new SelectableIcon(20, 336, 0, this, assets.getImage(TitleScrAssets.IMG_ICON_HIGHSCORE_U), 
															   assets.getImage(TitleScrAssets.IMG_ICON_HIGHSCORE_S));

		soundBtn   = new SelectableIcon(84, 400, 0, this, assets.getImage(TitleScrAssets.IMG_ICON_SOUND_U), 
				   										   assets.getImage(TitleScrAssets.IMG_ICON_SOUND_S));
		
		musicBtn   = new SelectableIcon(148, 400, 0, this, assets.getImage(TitleScrAssets.IMG_ICON_MUSIC_U), 
				   										   assets.getImage(TitleScrAssets.IMG_ICON_MUSIC_S));
		
		settingsBtn   = new SelectableIcon(20, 400, 0, this, assets.getImage(TitleScrAssets.IMG_ICON_SETTINGS_U), 
				   											 assets.getImage(TitleScrAssets.IMG_ICON_SETTINGS_S));
		
		tutorialBtn   = new SelectableIcon(148, 336, 0, this, assets.getImage(TitleScrAssets.IMG_ICON_TUTORIAL_U), 
					  										  assets.getImage(TitleScrAssets.IMG_ICON_TUTORIAL_S));
		
		creditsBtn   = new SelectableIcon(84, 336, 0, this, assets.getImage(TitleScrAssets.IMG_ICON_CREDITS_U), 
				  											assets.getImage(TitleScrAssets.IMG_ICON_CREDITS_S)); 
		
		disableSndImg = new ImageEntity(soundBtn.x, soundBtn.y,assets.getImage(TitleScrAssets.IMG_ICON_DISABLED), 0, this)
		{
			@Override
			public void update(float deltaTime)
			{
				visible = !screen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX);
			}
		};
		
		disableMusImg = new ImageEntity(musicBtn.x, musicBtn.y,assets.getImage(TitleScrAssets.IMG_ICON_DISABLED), 0, this)
		{
			@Override
			public void update(float deltaTime)
			{
				visible = !screen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_MUS, CSSettings.DEFAULT_ENABLE_MUS);
			}
		};
		
		disableSndImg.visible = !game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX);		
		disableMusImg.visible = !game.getPreferences().getPref(CSSettings.KEY_ENABLE_MUS, CSSettings.DEFAULT_ENABLE_MUS);
		
		selectionRects[SELECTION_SETTINGS]     = new Rect(4, game.getGraphics().getHeight() - 64, 
													  4 + 64, game.getGraphics().getHeight());
		selectionRects[SELECTION_ARCADE]	   = new Rect(20, 140, 20 + 440, 140 + 140);
		selectionRects[SELECTION_PSYCHOUT]	   = new Rect(640 - 480 + 140, 280, 640 + 140, 280 + 140);
		selectionRects[SELECTION_HIGHSCORE]	   = hSBtn.getBounds();
		selectionRects[SELECTION_SOUND]	       = soundBtn.getBounds();
		selectionRects[SELECTION_MUSIC]	       = musicBtn.getBounds();
		selectionRects[SELECTION_SETTINGS]	   = settingsBtn.getBounds();
		selectionRects[SELECTION_TUTORIAL]	   = tutorialBtn.getBounds();
		selectionRects[SELECTION_CREDITS]	   = creditsBtn.getBounds();
		selectionRects[SELECTION_ACHIEVEMENTS] = achievementIcon.getBounds();
		selectionRects[SELECTION_WEB_LINK]     = webLinkIcon.getBounds(); 
		
		if(!SystemInfo.hasTouchInput() || SystemInfo.isGoogleTV())
		{
			keyboardSelection = SELECTION_ARCADE;
		}
	}
	
	/** when called from other menu screens, set the initial background Y to be continuous */
	public TitleScr(ColorShafted game, float bgInitPos)
	{
		this(game);
		backgroundAngle = bgInitPos;
	}

	@Override
	public void timedLogic()
	{	
		//if the app has never been run before, prompt for the tutorial mode and then save the device's ID for online prompts!
		if(!initialized)
		{
			if(game.getPreferences().getPref(CSSettings.KEY_SHOW_TUTORIAL_PROMPT, true))
			{
				//set app as run once before
				game.getPreferences().setPref(CSSettings.KEY_SHOW_TUTORIAL_PROMPT, false);
				//save device id
				if(game.getPrompter().showDualOption(new StringBuilder("Thank you for downloading Color Shafted! Would you like to start off with tutorial mode? "),
											  new StringBuilder("OKAY!"), new StringBuilder("SKIP>>")) == 1)
				{
					tutorialBtn.select();
					if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
						assets.getSound(TitleScrAssets.SND_ITEM).play(1);
					GameStats.gameStyle = GameMode.TUTORIAL;
					startMode = SELECTION_TUTORIAL;
					goToScreen(new RecycleGameScr((ColorShafted)game));
				}
			}
			initialized = true;
		}
		
		//MOVE THE SPACE BACKGROUND
		backgroundAngle+= 0.25;
		backgroundAngle %= 360;
		spaceBackgroundImg.y = 
				 -(880 - game.getGraphics().getHeight())/2 + 
				 Math.round(Math.cos(Math.toRadians(backgroundAngle)) * (880 - game.getGraphics().getHeight())/2);
		keyboardNavigation();
	}
	
	@Override
	public void drawEntities(Graphics g)
	{
		super.drawEntities(g);
		
		if((!SystemInfo.hasTouchInput() || SystemInfo.isGoogleTV()) && keyboardSelection != -1 && !fadingOut)
		{
			g.drawRect(selectionRects[keyboardSelection], Color.argb(20, 255, 255, 255), true);
			g.drawRect(selectionRects[keyboardSelection], Color.WHITE, false);
		}
		
		//draw the fade in rectangle
		if(fadingIn && fadeInStyle == FADE_IN_BLACKRECT)
		{
			final int alpha = 255 - (int)((float)fadeInTimer/(float)FADE_IN_LENGTH * 255.0f);
			g.drawRect(screenFadeRect, Color.argb(alpha, 0, 0, 0), true);
		}
		
		final int FADERECT_LENGTH = 10;
		if(fadingOut && fadeOutTimer > FADE_OUT_LENGTH - FADERECT_LENGTH)
		{
			final int fadeRectTimer = fadeOutTimer - (FADE_OUT_LENGTH - FADERECT_LENGTH);
			final int alpha = (int)((float)fadeRectTimer/(float)FADERECT_LENGTH * 255.0f);
			g.drawRect(screenFadeRect, Color.argb(alpha <= 255 ? alpha : 255, 0, 0, 0), true);
		}
		if(fadingOut && fadeOutTimer >= FADE_OUT_LENGTH)
		{
			g.drawText(loadingStr, ScreenInfo.virtualWidth - 20, ScreenInfo.virtualHeight - 20, 
					Color.rgb(200, 200, 200), 20, assets.getFont(TitleScrAssets.FONT_SFTELEVISED), Paint.Align.RIGHT);
			g.drawText(loadingStr, ScreenInfo.virtualWidth - 20, ScreenInfo.virtualHeight - 20, 
					Color.rgb(200, 200, 200), 20, assets.getFont(TitleScrAssets.FONT_SFTELEVISED), Paint.Align.RIGHT);
		}
	}
	
	@Override
	public void screenTapped()
	{
		int selection = selectionAt(game.getInput().getTouchX(0), game.getInput().getTouchY(0));
		selectionChosen(selection);
	}

	public void selectionChosen(int selectionIndex)
	{
		switch(selectionIndex)
		{
			case SELECTION_SETTINGS:
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				settingsBtn.select();
				game.launchSettings();
				break;
			case SELECTION_ARCADE:
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				GameStats.gameStyle = GameMode.ARCADE;
				startMode = SELECTION_ARCADE;
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				goToScreen(new RecycleGameScr((ColorShafted)game));
				break;
			case SELECTION_PSYCHOUT:
			{
				//begin psychout mode!
				GameStats.gameStyle = GameMode.PSYCHOUT;
				startMode = SELECTION_PSYCHOUT;
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				goToScreen(new RecycleGameScr((ColorShafted)game));
				//disable psychout code...
				//if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
				//	assets.getSound(TitleScrAssets.SND_BOMB).play(1);
				//game.getPrompter().showMsg(new StringBuilder("Sorry, this mode is not yet available but is coming very soon in an update!"));
			}
			break;
			case SELECTION_HIGHSCORE:
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				hSBtn.select();
				goToScreen(new HighScoreBufferScr(game));
				break;
				
			case SELECTION_SOUND:
				game.getPreferences().setPref(CSSettings.KEY_ENABLE_SFX, !game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX));
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ROTATE).play(1);
				break;
			case SELECTION_MUSIC:
				game.getPreferences().setPref(CSSettings.KEY_ENABLE_MUS, !game.getPreferences().getPref(CSSettings.KEY_ENABLE_MUS, CSSettings.DEFAULT_ENABLE_MUS));
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ROTATE).play(1);
				break;
			case SELECTION_TUTORIAL:
				tutorialBtn.select();
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				GameStats.gameStyle = GameMode.TUTORIAL;
				startMode = SELECTION_TUTORIAL;
				goToScreen(new RecycleGameScr((ColorShafted)game));
				break;
			case SELECTION_CREDITS:
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				creditsBtn.select();
				((ColorShaftedPrompter)game.getPrompter()).showCredzDialog();
				break;
			case SELECTION_WEB_LINK:
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_ITEM).play(1);
				game.launchWebsite("https://play.google.com/store/apps/developer?id=Whateversoft#?t=" +
						"W251bGwsbnVsbCxudWxsLDEsImNvbS53aGF0ZXZlcnNvZnQiXQ..");
				break;
			case SELECTION_ACHIEVEMENTS:
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(TitleScrAssets.SND_BOMB).play(1);
				creditsBtn.select();
				((ColorShaftedPrompter)game.getPrompter()).
				showMsg(new StringBuilder("Sorry, Achievements Mode is not yet available but will be arriving " +
																				"soon in a future update!"));
				break;
		}
	}

	public void keyboardNavigation()
	{
		if(	keyboardSelection != -1 && 
			(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_CENTER) == 1 || 
			 game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_SPACE) == 1 ||
			 game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_K) == 1 || 
			 game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_L) == 1))
			selectionChosen(keyboardSelection);
		
		boolean selectionChanged = false;
		boolean keyLeftPressed = game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_LEFT) == 1 ||
				 				 game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_A) == 1 ,
				keyRightPressed = game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_RIGHT) == 1 ||
						 		  game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_D) == 1,
				keyUpPressed = game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_UP) == 1 ||
						 	   game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_W) == 1,
				keyDownPressed = game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_DOWN) == 1 ||
								 game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_S) == 1 ;
		switch(keyboardSelection)
		{
			case SELECTION_ARCADE:
				if(keyDownPressed)
				{
					changeKeyboardSelection(SELECTION_HIGHSCORE);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_WEB_LINK);
					selectionChanged = true;
				}
				break;
			case SELECTION_PSYCHOUT:
				if(keyLeftPressed)
				{
					changeKeyboardSelection(SELECTION_TUTORIAL);
					selectionChanged = true;
				}
				if(keyUpPressed)
				{
					changeKeyboardSelection(SELECTION_WEB_LINK);
					selectionChanged = true;
				}	
				break;
			case SELECTION_HIGHSCORE:
				if(keyUpPressed)
				{
					changeKeyboardSelection(SELECTION_ARCADE);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_CREDITS);
					selectionChanged = true;
				}
				if(keyDownPressed)
				{
					changeKeyboardSelection(SELECTION_SETTINGS);
					selectionChanged = true;
				}
				break;
			case SELECTION_CREDITS:
				if(keyUpPressed)
				{
					changeKeyboardSelection(SELECTION_ARCADE);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_TUTORIAL);
					selectionChanged = true;
				}
				if(keyLeftPressed)
				{	
					changeKeyboardSelection(SELECTION_HIGHSCORE);
					selectionChanged = true;
				}
				if(keyDownPressed)
				{
					changeKeyboardSelection(SELECTION_SOUND);
					selectionChanged = true;
				}
				break;
			case SELECTION_TUTORIAL:
				if(keyUpPressed)
				{
					changeKeyboardSelection(SELECTION_ARCADE);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_PSYCHOUT);
					selectionChanged = true;
				}
				if(keyLeftPressed)
				{
					changeKeyboardSelection(SELECTION_CREDITS);
					selectionChanged = true;
				}
				if(keyDownPressed)
				{
					changeKeyboardSelection(SELECTION_MUSIC);
					selectionChanged = true;
				}
				break;
			case SELECTION_SETTINGS:
				if(keyUpPressed)
				{
					changeKeyboardSelection(SELECTION_HIGHSCORE);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_SOUND);
					selectionChanged = true;
				}
				break;
			case SELECTION_SOUND:
				if(keyUpPressed)
				{
					changeKeyboardSelection(SELECTION_CREDITS);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_MUSIC);
					selectionChanged = true;
				}
				if(keyLeftPressed)
				{
					changeKeyboardSelection(SELECTION_SETTINGS);
					selectionChanged = true;
				}
				break;
			case SELECTION_MUSIC:
				if(keyUpPressed)
				{
					changeKeyboardSelection(SELECTION_TUTORIAL);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_PSYCHOUT);
					selectionChanged = true;
				}
				if(keyLeftPressed)
				{
					changeKeyboardSelection(SELECTION_SOUND);					
					selectionChanged = true;
				}
					
				break;	
			case SELECTION_WEB_LINK:
				if(keyDownPressed)
				{
					changeKeyboardSelection(SELECTION_PSYCHOUT);
					selectionChanged = true;
				}
				if(keyLeftPressed)
				{
					changeKeyboardSelection(SELECTION_ARCADE);
					selectionChanged = true;
				}
				if(keyRightPressed)
				{
					changeKeyboardSelection(SELECTION_ACHIEVEMENTS);
					selectionChanged = true;
				}
				break;
			case SELECTION_ACHIEVEMENTS:
				if(keyDownPressed)
				{
					changeKeyboardSelection(SELECTION_PSYCHOUT);
					selectionChanged = true;
				}
				if(keyLeftPressed)
				{
					changeKeyboardSelection(SELECTION_WEB_LINK);
					selectionChanged = true;
				}
				break;
		}
		
		if(selectionChanged)
		{
			if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
				assets.getSound(TitleScrAssets.SND_ROTATE).play(1f);
		}
	}
	
	public int selectionAt(int x, int y)
	{
		for(int i = 0; i < selectionRects.length; i++)
		{
			if(selectionRects[i].contains(x, y))
				return i;
		}
		return -1;
	}
	
	@Override
	public void fadeOutLogic(float deltaTime)
	{
		hSBtn.update(0.02f);
		settingsBtn.update(0.02f);
		creditsBtn.update(0.02f);
		soundBtn.update(0.02f);
		musicBtn.update(0.02f);
		tutorialBtn.update(0.02f);
		
		//make space background move fast!
		backgroundAngle+= 8;
		backgroundAngle %= 360;
		spaceBackgroundImg.y = 
				 -(880 - game.getGraphics().getHeight())/2 + 
				 Math.round(Math.cos(Math.toRadians(backgroundAngle)) * (880 - game.getGraphics().getHeight())/2);
	
		switch(startMode)
		{
			case SELECTION_ARCADE:
			{
				arcadeWheel.rotationSpeed -= 5 * deltaTime / 20.0f;
				if(psychoutWheel.alpha > 30)
				{
					psychoutWheel.alpha -= 15 * deltaTime / 20.0f;
				}
				if(psychoutWheel.alpha < 0)
					psychoutWheel.alpha = 0;
				
				if(fadeOutTimer == 0)
				{
					//move the arcade wheel to the top layer!(or, add a copy.. whatever :P)
					entities[1].add(arcadeWheel);
					
					arcadeWheel.rotationSpeed = 0;
					psychoutWheel.semiTrans = true;
					psychoutModeImg.semiTrans = true;
				}
			}
				break;
			case SELECTION_PSYCHOUT:
			{
				psychoutWheel.rotationSpeed += 5 * deltaTime / 20.0f;
				if(arcadeWheel.alpha > 30)
				{
					arcadeWheel.alpha -= 15 * deltaTime / 20.0f;
					
				}
				if(arcadeWheel.alpha < 0)
					arcadeWheel.alpha = 0;
	
				if(fadeOutTimer == 0)
				{
					//move the survival wheel to the top layer!(or, add a copy.. whatever :P)
					entities[1].add(psychoutWheel);
					
					psychoutWheel.rotationSpeed = 0;
					arcadeWheel.semiTrans = true;
					arcadeModeImg.semiTrans = true;
				}
			}
				break;
				
		}
		
		arcadeWheel.rotation   -= arcadeWheel.rotationSpeed;
		psychoutWheel.rotation -= psychoutWheel.rotationSpeed;
		
		if(fadeOutTimer >= FADE_OUT_LENGTH)
		{
			arcadeWheel.destroy();
			psychoutWheel.destroy();
		}
			
		if(fadeOutTimer >= FADE_OUT_LENGTH + 1)
			completeFadeOut();
	}
	
	@Override
	public void backPressed()
	{
		System.exit(1);
	}
	
	public class SelectableIcon extends ImageEntity
	{
		boolean selected = false;
		int		selectedTimer = 0;
		ImageFrame[] images;
		public SelectableIcon(float x, float y, int l, Screen s, ImageFrame... i)
		{
			super(x, y, i[0], l, s);
			images = i;
		}
		
		@Override
		public void update(float deltaTime)
		{
			super.update(deltaTime);
			if(selectedTimer > 0)
			{
				selectedTimer --;
				
				if(selectedTimer == 0)		//if the timer has reset itself, restore the unselect frame to the button
					imgFrame = images[0];
			}
		}
		
		public void select()
		{
			selected = true;
			selectedTimer = 10;
			this.imgFrame = images[1];
		}
	}

	public void changeKeyboardSelection(int selection)
	{
		keyboardSelection = selection;
	}
	
	@Override
	public void fadeInLogic(float deltaTime)
	{
		if(fadeInTimer >= FADE_IN_LENGTH)
		{
			fadingIn = false;
		}
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_TITLE;
	}
}
