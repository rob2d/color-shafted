package com.whateversoft.colorshafted.screens;

import static com.whateversoft.colorshafted.screens.GameScrAssets.FONT_SFTELEVISED;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_BOMB_BUTTON;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_HUD_PANEL;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_SHAFT_BG_C;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_SHAFT_BG_L;
import static com.whateversoft.colorshafted.screens.GameScrAssets.IMG_SHAFT_BG_R;
import static com.whateversoft.colorshafted.screens.GameScrAssets.SND_BAD_HIT;
import static com.whateversoft.colorshafted.screens.GameScrAssets.SND_BOMB;
import static com.whateversoft.colorshafted.screens.GameScrAssets.SND_GALAXYEXPLODE;
import static com.whateversoft.colorshafted.screens.GameScrAssets.SND_GOOD_HIT;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.whateversoft.android.framework.FPSCounter;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.MusicJukebox;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.android.framework.TextEntity;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSScreens;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.game.BlackHole;
import com.whateversoft.colorshafted.game.BombExplosion;
import com.whateversoft.colorshafted.game.CSGlobalAssets;
import com.whateversoft.colorshafted.game.ColorBlockE;
import com.whateversoft.colorshafted.game.ColorBlockP;
import com.whateversoft.colorshafted.game.ComboText;
import com.whateversoft.colorshafted.game.ControlBlock;
import com.whateversoft.colorshafted.game.EnvObjSpawner;
import com.whateversoft.colorshafted.game.GameStats;
import com.whateversoft.colorshafted.game.LevelItem;
import com.whateversoft.colorshafted.game.PlayerControl;
import com.whateversoft.colorshafted.game.TutorialSequencer;
import com.whateversoft.colorshafted.game.factories.BlackHoleFactory;
import com.whateversoft.colorshafted.game.factories.ColorExplosionFactory;
import com.whateversoft.colorshafted.game.factories.ComboTextFactory;
import com.whateversoft.colorshafted.game.factories.EnemyBlockFactory;
import com.whateversoft.colorshafted.game.factories.EnemyCTBlockFactory;
import com.whateversoft.colorshafted.game.factories.EnemyDSBlockFactory;
import com.whateversoft.colorshafted.game.factories.ErrorExplosionFactory;
import com.whateversoft.colorshafted.game.factories.LevelItemFactory;
import com.whateversoft.colorshafted.game.factories.ShadowColorFactory;

/**
 * the basic game screen where everything happens Copyright 2011 Robert
 * Concepcion III
 */
public class GameScr extends Screen
{
	// ---------------------------------//
	// CONSTANTS //
	// ---------------------------------//
	public final static int LAYER_BACKGROUND = 0, // layer used for the far
													// background
			LAYER_FOREGROUND = 1, // layer used for the closer background
			LAYER_ENTITIES = 2, // default layer used for all active game
								// objects
			LAYER_ENTITIES_FRONT = 3, // layer used for objs that should be in
										// front of others
			LAYER_ENTITIES_FX = 4, // layer used for graphic special effects
									// such as explosions
			LAYER_HUD = 5, // layer for hud elements such as game status
							// display(lives, bombs, etc)
			LAYER_DEBUG = 6, // layer used for extra data to display for
								// debugging(shown last)

			GAME_CANVAS_WIDTH = 480,
			GAME_CANVAS_HEIGHT = 480,

			GRID_SQ_SIZE = 40,
			GRID_COLUMNS = 3,
			GRID_ROWS = 3,

			// grid boundaries are relative to the game canvas position!
			GRID_LEFT = 240 - (int) (GRID_SQ_SIZE * 1.5),
			GRID_TOP = 240 - (int) (GRID_SQ_SIZE * 1.5),
			GRID_RIGHT = 240 + (int) (GRID_SQ_SIZE * 1.5),
			GRID_BOTTOM = 240 + (int) (GRID_SQ_SIZE * 1.5);

	public static int GAME_CANVAS_LEFT, GAME_CANVAS_BOTTOM, GAME_CANVAS_TOP,
			GAME_CANVAS_RIGHT;

	public final static int SHAFT_UP_LEFT = 0, SHAFT_UP_CENTER = 1,
			SHAFT_UP_RIGHT = 2, SHAFT_DOWN_LEFT = 3, SHAFT_DOWN_CENTER = 4,
			SHAFT_DOWN_RIGHT = 5, SHAFT_RIGHT_TOP = 6, SHAFT_RIGHT_CENTER = 7,
			SHAFT_RIGHT_BOTTOM = 8, SHAFT_LEFT_TOP = 9, SHAFT_LEFT_CENTER = 10,
			SHAFT_LEFT_BOTTOM = 11;

	public final static int GRID_TOP_LEFT = 0, GRID_TOP_CENTER = 1,
			GRID_TOP_RIGHT = 2, GRID_MID_LEFT = 3, GRID_MID_CENTER = 4,
			GRID_MID_RIGHT = 5, GRID_BOT_LEFT = 6, GRID_BOT_CENTER = 7,
			GRID_BOT_RIGHT = 8;

	int[] introTxtColorTarget = { Color.argb(200, 255, 255, 0),
			Color.argb(200, 255, 0, 0), Color.argb(200, 0, 0, 255),
			Color.argb(200, 0, 255, 0) };
	FPSCounter fpsLogger = new FPSCounter();

	// ----------------------------------|
	// OBJECT FACTORIES
	// ----------------------------------|
	public EnemyBlockFactory eBlockFactory = new EnemyBlockFactory(this,
			this.LAYER_ENTITIES_FRONT);
	public EnemyDSBlockFactory eDSBlockFactory = new EnemyDSBlockFactory(this,
			this.LAYER_ENTITIES);
	public EnemyCTBlockFactory eCTBlockFactory = new EnemyCTBlockFactory(this,
			this.LAYER_ENTITIES);
	public LevelItemFactory lItemFactory = new LevelItemFactory(this,
			this.LAYER_ENTITIES_FRONT);
	public BlackHoleFactory bHFactory = new BlackHoleFactory(this);
	public ColorExplosionFactory cExpFactory = new ColorExplosionFactory(this);
	public ErrorExplosionFactory eExpFactory = new ErrorExplosionFactory(this);
	public ComboTextFactory cTxtFactory = new ComboTextFactory(this);
	public ShadowColorFactory sColorFactory = new ShadowColorFactory(this);

	// ----------------------------------|
	// ENTITIES
	// ----------------------------------|
	public ControlBlock controlBlock;
	public ArrayList<ColorBlockE> eBlocks = new ArrayList<ColorBlockE>();
	public ArrayList<BlackHole> blackHoles = new ArrayList<BlackHole>();
	public ArrayList<LevelItem> lItems = new ArrayList<LevelItem>();
	public ImageEntity shaftBg, shaftFGL, shaftFGR;
	public ImageEntity bombButton, hudPanelImg;
	public TextEntity currentSongTxt;
	Rect fadeOutRect = new Rect(0, 0, ScreenInfo.virtualWidth,
			ScreenInfo.virtualHeight);

	// ----------------------------------|
	// CONTROLLERS
	// ----------------------------------|
	public PlayerControl playerController = new PlayerControl(this);
	public EnvObjSpawner envObjCreator = new EnvObjSpawner(this);
	public TutorialSequencer helpSequencer = null;

	// ----------------------------------|
	// DATA FIELDS
	// ----------------------------------|
	int lastBlockCreatedAt = 0;
	int hitComboCounter = 0;
	int highestHitCombo = 0;
	public int blockCreationRate = 70;
	int postBombTimer = 0;
	boolean gameInProgress = true;

	// ----------------------------------|
	// DISPLAY HUD FIELDS
	// ----------------------------------|
	TextEntity livesHUDTxt, scoreHUDTxt, diffHUDTxt, readyTxt, bombHUDTxt;

	// ------------------------------------------------//
	// FADE-IN SEQUENCER //
	// ------------------------------------------------///
	/**
	 * helps go through the different phases of the fade in because the timer is
	 * not compatibile with == operations!
	 */
	int fadeInSequencer = 0;
	boolean readySndPlayed = false;
	boolean fadeInSlideSndPlayed = false;
	//so that we can update the display before playing music during the fade in
	//(gets rid of a jitter on certain devices)
	boolean introFinished = false;
	Rect screenFadeRect = new Rect(0, 0, ScreenInfo.virtualWidth,
			ScreenInfo.virtualHeight);
	

	enum FadeOutStyle
	{
		TO_GAME_OVER, TO_BLACK
	};

	public FadeOutStyle fadeOutStyle;

	public GameScr(ColorShafted game)
	{
		super(game, new GameScrAssets(game), 6);

		// debugCode(); //LOAD ITEM GRAPHIC TESTING

		// instantiate tutorial sequencer if necessary
		if (GameStats.gameStyle == GameMode.TUTORIAL)
			helpSequencer = new TutorialSequencer(this);

		// ------------------------------------------------//
		// DEFINE THE GAME CANVAS and BORDERS //
		// ------------------------------------------------//
		GAME_CANVAS_LEFT = (ScreenInfo.virtualWidth / 2) - 240;
		GAME_CANVAS_RIGHT = (ScreenInfo.virtualWidth / 2) + 240;
		GAME_CANVAS_TOP = 0;
		GAME_CANVAS_BOTTOM = 480;

		// ------------------------------------------------//
		// SETUP GFX ENTITIES //
		// ------------------------------------------------//
		// set up the 3 backgrounds
		shaftBg = new ImageEntity(GAME_CANVAS_LEFT + GAME_CANVAS_WIDTH / 2,
				GAME_CANVAS_TOP + GAME_CANVAS_HEIGHT / 2,
				assets.getImage(IMG_SHAFT_BG_C), LAYER_BACKGROUND, this);
		shaftFGL = new ImageEntity(GAME_CANVAS_LEFT + GAME_CANVAS_WIDTH / 2,
				GAME_CANVAS_TOP + GAME_CANVAS_HEIGHT / 2,
				assets.getImage(IMG_SHAFT_BG_L), LAYER_ENTITIES_FX, this);
		shaftFGR = new ImageEntity(GAME_CANVAS_LEFT + GAME_CANVAS_WIDTH / 2,
				GAME_CANVAS_TOP + GAME_CANVAS_HEIGHT / 2,
				assets.getImage(IMG_SHAFT_BG_R), LAYER_ENTITIES_FX, this);
		if (game.getPreferences().getPref(CSSettings.KEY_INTRO_ZOOMOUT,
				CSSettings.DEFAULT_INTRO_ZOOMOUT))
			shaftBg.zoomPercentage = shaftFGL.zoomPercentage = shaftFGR.zoomPercentage = 400;
		hudPanelImg = new ImageEntity(0, 0, assets.getImage(IMG_HUD_PANEL),
				LAYER_HUD, this);
		bombButton = new ImageEntity(ScreenInfo.virtualWidth, 0,
				assets.getImage(IMG_BOMB_BUTTON), LAYER_HUD, this);
		bombButton.visible = false;

		// ------------------------------------------------//
		// SET UP HUD STUFFZ //
		// ------------------------------------------------//
		scoreHUDTxt = new TextEntity(176, 45, new StringBuffer("---"),
				Color.argb(0, 180, 180, 180), assets.getFont(FONT_SFTELEVISED),
				18, Paint.Align.RIGHT, LAYER_HUD, this);

		livesHUDTxt = new TextEntity(176, 76, new StringBuffer("---"),
				Color.argb(0, 180, 180, 180), assets.getFont(FONT_SFTELEVISED),
				20, Paint.Align.RIGHT, LAYER_HUD, this);

		diffHUDTxt = new TextEntity(176, 108, new StringBuffer("---"),
				Color.argb(0, 180, 180, 180), assets.getFont(FONT_SFTELEVISED),
				20, Paint.Align.RIGHT, LAYER_HUD, this);

		bombHUDTxt = new TextEntity(ScreenInfo.virtualWidth - 22, 55,
				new StringBuffer("4"), Color.argb(255, 255, 255, 255),
				assets.getFont(FONT_SFTELEVISED), 40, Paint.Align.RIGHT,
				LAYER_HUD, this);

		if (game.getPreferences().getPref(CSSettings.KEY_INTRO_SCROLLTXT,
				CSSettings.DEFAULT_INTRO_SCROLLTXT))
			readyTxt = new TextEntity(game.getGraphics().getWidth(), game
					.getGraphics().getHeight() / 2 + 36, new StringBuffer(
					GameStats.gameStyle != GameMode.TUTORIAL ? "ARE YOU READY?"
							: "TUTORIAL MODE"), Color.WHITE,
					assets.getFont(FONT_SFTELEVISED), 102, Paint.Align.LEFT,
					LAYER_HUD, this);
		else
			readyTxt = new TextEntity(game.getGraphics().getWidth(), game
					.getGraphics().getHeight() / 2 + 36, new StringBuffer(
					GameStats.gameStyle != GameMode.TUTORIAL ? "ARE YOU READY?"
							: "TUTORIAL MODE"), Color.WHITE,
					assets.getFont(FONT_SFTELEVISED), 72, Paint.Align.CENTER,
					LAYER_HUD, this);
		readyTxt.semiTrans = true;
		bombHUDTxt.visible = false;

		// ------------------------------------------------//
		// reset all game stats!
		// ------------------------------------------------//
		GameStats.newGameStats(game);

		// instantiate entities
		controlBlock = new ControlBlock(LAYER_ENTITIES, this);
		updateHUD();

		// Load difficulty/run creation rate logic!
		setDifficulty(GameStats.difficulty);

		fadingIn = true; // start fade in logic

		// SETUP MUSIC
		if (GameStats.gameStyle == GameMode.ARCADE
				|| GameStats.gameStyle == GameMode.PSYCHOUT)
		{
			game.getMusic().setPlayMode(
					MusicJukebox.PlayMode.valueOf(game.getPreferences()
							.getPref(CSSettings.KEY_MUSIC_PLAYMODE,
									CSSettings.DEFAULT_MUSIC_PLAYMODE)));
			game.getMusic().setTrack(game.getMusic().nextTrack());
			
			currentSongTxt = new TextEntity(0, 0, new StringBuffer(""),
					Color.WHITE,
					assets.getFont(GameScrAssets.FONT_SFTELEVISED), 16,
					Paint.Align.RIGHT, LAYER_HUD, this);
		} else if (GameStats.gameStyle == GameMode.TUTORIAL)
		{
			game.getMusic().setPlayMode(MusicJukebox.PlayMode.LOOP_SINGLE);
			// enable the tutorial music track and play, then re-disable it in
			// case of re-entry
			game.getMusic().setTrackAsEnabled(4, true);
			game.getMusic().setTrack(4);
			game.getMusic().setTrackAsEnabled(4, false);
		}
	}

	@Override
	public void timedLogic()
	{
		// update display values
		updateHUD();

		// update tutorial sequencer if necessary
		if (helpSequencer != null)
			helpSequencer.update();

		// detect gestures while input is enabled
		if (playerController.isPlayerInputEnabled())
		{
			playerController.detectPlayerInputEvents(); // allow the player
														// controller to
														// sequence with the
														// screen
			if (GameStats.bombs > 0
					&& game.getPreferences().getPref(
							CSSettings.KEY_BOMB_SHAKE_GESTURE,
							CSSettings.DEFAULT_BOMB_SHAKE_GESTURE))
				playerController.detectScreenShake();
		}

		// if not in tutorial mode, run normal game logic
		if (GameStats.gameStyle != GameMode.TUTORIAL
				|| (GameStats.gameStyle == GameMode.TUTORIAL && helpSequencer.currentStep == TutorialSequencer.S_FREEPLAY))
		{
			// create enemy blocks at necessary intervals
			if (gameTimer - lastBlockCreatedAt > blockCreationRate)
			{
				lastBlockCreatedAt += blockCreationRate;
				spawnNextObject();
			}

			if (GameStats.gameStyle == GameMode.SURVIVAL)
				GameStats.shield -= 1;

			// add miliseonds to timer
			if (gameInProgress)
				GameStats.playTime += 20;
		}

		// subtract from bomb timer if player just launched it
		if (postBombTimer > 0)
		{
			postBombTimer--;
		}

		// detect object
		detectObjectCollisions();
	}

	public void detectEnemyCollisions()
	{
		for (int i = 0; i < eBlocks.size(); i++)
		{
			if (eBlocks.get(i).collidesWith(controlBlock))
			{
				ColorBlockE e = eBlocks.get(i);
				for (ColorBlockP c : controlBlock.colorBlocks)
				{
					if (e.collidesWith(c))
					{
						if (GameStats.gameStyle != GameMode.TUTORIAL)
							GameStats.enemiesHit++;
						// SUCCESSFUL BLOCK HIT MATCH
						if (c.color == e.color)
						{
							// flash the block that just collided white
							c.flashTimer = 5;
							// play sound effect if necessary
							if (game.getPreferences().getPref(
									CSSettings.KEY_ENABLE_SFX,
									CSSettings.DEFAULT_ENABLE_SFX))
							{
								assets.getSound(SND_GOOD_HIT).play(0.5f);
							}
							successfulBlockHit((int) e.x, (int) e.y, e.color);
						} else if (e.color != c.color)
						{
							// UNSUCCESSFUL MATCH
							unsuccessfulBlockHit(e);
						}
						eBlockFactory.throwInPool(e);
					}
				}
			}
		}
	}

	public void detectItemCollisions()
	{
		for (int i = 0; i < lItems.size(); i++)
		{
			if (lItems.get(i).collidesWith(controlBlock))
			{
				LevelItem l = lItems.get(i);
				for (ColorBlockP c : controlBlock.colorBlocks)
				{
					if (l.collidesWith(c))
					{
						if (GameStats.gameStyle != GameMode.TUTORIAL)
							GameStats.enemiesHit++;
						// SUCCESSFUL ITEM COLLECTION
						if (c.color == l.color)
						{
							switch (l.type)
							{
								case LevelItem.TYPE_XTRA_BOMB:
									GameStats.bombs += 1;
									break;
								case LevelItem.TYPE_XTRA_LIFE:
									GameStats.lives += 1;
									cTxtFactory.getObject(168, 76,
											new StringBuffer("+1"),
											ComboText.HUD_UP);
									break;
							}
							// flash the block that just collided white
							c.flashTimer = 5;
							// play sound effect if necessary
							if (game.getPreferences().getPref(
									CSSettings.KEY_ENABLE_SFX,
									CSSettings.DEFAULT_ENABLE_SFX))
								assets.getSound(GameScrAssets.SND_ITEM).play(
										0.5f);
							// add to score/hit counter and create graphic
							successfulBlockHit((int) l.x, (int) l.y, l.color);
						} else if (l.color != c.color)
						{
							// UNSUCCESSFUL ITEMGRAB
						}
						lItemFactory.throwInPool(l);
					}
				}
			}
		}
	}

	public void detectObjectCollisions()
	{
		synchronized (this)
		{
			detectEnemyCollisions();
			detectBlackHoleCollisions();
			detectItemCollisions();
		}
	}

	/** 
	 *   detect whether the player control block has collided with a black hole
	 */
	public void detectBlackHoleCollisions()
	{
		for (int i = 0; i < blackHoles.size(); i++)
		{
			if (blackHoles.get(i).collidesWith(controlBlock))
			{
				Log.d("COLORSHAFTED", "Collision With Black Hole Occured!");
				BlackHole bH = blackHoles.get(i);
				for (ColorBlockP c : controlBlock.colorBlocks)
				{
					if (bH.collidesWith(c))
					{
						if (controlBlock.invincibleTimer == 0)
						{
							// shake the control block
							controlBlock.shake(bH.dx != 0);
							eExpFactory.getFactoryObject((int) c.x, (int) c.y);
							// play sound effect if necessary
							if (game.getPreferences().getPref(
									CSSettings.KEY_ENABLE_SFX,
									CSSettings.DEFAULT_ENABLE_SFX))
								assets.getSound(SND_BAD_HIT).play(0.5f);
							playerWasHit();
						}
					}
				}
			}
		}
	}

	/**
	 * create a new enemy block and instantiate it on the game screen randomly;
	 * also returns instance of the block for tracking
	 */
	public void spawnNextObject()
	{
		envObjCreator.spawnNextObject();
	}

	/**
	 * 
	 */
	public void clearEnemyBlocks()
	{
		synchronized (eBlocks)		//< to prevent concurrent modification errors
		{
			for (ColorBlockE e : eBlocks)
			{
				successfulBlockHit((int) e.x, (int) e.y, e.color);
				e.destroy();
			}
			eBlocks.clear();
		}
	}

	public void playerControlEvent(int eventCode)
	{
		if (helpSequencer != null && helpSequencer.trackingPlayerInput)
			helpSequencer.trackPlayerInput(eventCode);

		switch (eventCode)
		{
			case PlayerControl.ROTATE_LEFT:
				controlBlock.rotate(ControlBlock.TURN_LEFT);
				break;
			case PlayerControl.ROTATE_RIGHT:
				controlBlock.rotate(ControlBlock.TURN_RIGHT);
				break;
			case PlayerControl.SWIPE_DOWN:
				controlBlock.move(ControlBlock.MOVE_DOWN);
				break;
			case PlayerControl.SWIPE_DOWN_LEFT:
				controlBlock.move(ControlBlock.MOVE_DOWN_LEFT);
				break;
			case PlayerControl.SWIPE_LEFT:
				controlBlock.move(ControlBlock.MOVE_LEFT);
				break;
			case PlayerControl.SWIPE_LEFT_UP:
				controlBlock.move(ControlBlock.MOVE_LEFT_UP);
				break;
			case PlayerControl.SWIPE_UP:
				controlBlock.move(ControlBlock.MOVE_UP);
				break;
			case PlayerControl.SWIPE_UP_RIGHT:
				controlBlock.move(ControlBlock.MOVE_UP_RIGHT);
				break;
			case PlayerControl.SWIPE_RIGHT:
				controlBlock.move(ControlBlock.MOVE_RIGHT);
				break;
			case PlayerControl.SWIPE_RIGHT_DOWN:
				controlBlock.move(ControlBlock.MOVE_RIGHT_DOWN);
				break;
			case PlayerControl.BOMB_SCREEN:
				if (GameStats.bombs > 0 && postBombTimer == 0)
				{
					bombScreen();
					GameStats.bombs--;
					GameStats.bombsUsed++; // GAMESTATS>> add to bombs used
					postBombTimer = 30;
					if (GameStats.bombs == 0)
					{
						bombButton.semiTrans = true;
						bombButton.alpha = 50;
						bombHUDTxt.color = Color.argb(50, 255, 255, 255);
					} else
					{
						bombButton.semiTrans = false;
						bombButton.alpha = 255;
						bombHUDTxt.color = Color.argb(255, 255, 255, 255);
					}
				}
				break;
		}
	}

	public int gameCanvasX(int origX)
	{
		return origX + GAME_CANVAS_LEFT;
	}

	public int gameCanvasY(int origY)
	{
		return origY + GAME_CANVAS_TOP;
	}

	public void bombScreen()
	{
		lastBlockCreatedAt = (int) gameTimer + 25;// make it so that blocks are
													// not created immediately
													// after bombing
		if (game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX,
				CSSettings.DEFAULT_ENABLE_SFX))
			assets.getSound(SND_BOMB).play(1);
		new BombExplosion(GAME_CANVAS_LEFT + GAME_CANVAS_WIDTH / 2,
				GAME_CANVAS_TOP + GAME_CANVAS_HEIGHT / 2, this);
		clearEnemyBlocks();
	}

	/**
	 * update the HUD(heads up display) so that lives, score and difficulty are
	 * shown
	 */
	public void updateHUD()
	{
		livesHUDTxt.string.setLength(0);
		scoreHUDTxt.string.setLength(0);

		switch (GameStats.gameStyle)
		{
			case ARCADE:
				livesHUDTxt.string.append("" + GameStats.lives);
				scoreHUDTxt.string.append("" + GameStats.score);
				break;
			case PSYCHOUT:
				livesHUDTxt.string.append("" + GameStats.lives);
				scoreHUDTxt.string.append("" + GameStats.score);
				break;
		}

		diffHUDTxt.string.setLength(0);
		diffHUDTxt.string.append("" + GameStats.difficulty);

		bombHUDTxt.string.setLength(0);
		if (helpSequencer == null || helpSequencer != null
				&& helpSequencer.currentStep < TutorialSequencer.S_FREEPLAY)
			bombHUDTxt.string.append("" + GameStats.bombs);
		else
		{
			bombHUDTxt.string.append(" ++");
			bombHUDTxt.color = Color.WHITE;
			bombButton.semiTrans = false;
		}
	}

	/**
	 * called when a block successfully collides with a matching color; creates
	 * an explosion, modifies game stats
	 */
	public void successfulBlockHit(int collisionX, int collisionY, int color)
	{
		// create an explosion from the color which was destroyed!
		cExpFactory.getFactoryObject(collisionX, collisionY, color);

		// modify stats accordingly...
		GameStats.score += blockScoreAlgo();
		GameStats.successfulCollisions++;

		if (GameStats.gameStyle == GameMode.SURVIVAL)
			GameStats.shield += 50;

		// gradually increment difficulty
		if (GameStats.successfulCollisions
				% Math.round(12 + (GameStats.difficulty > 4 ? GameStats.difficulty / 4
						: 0)) == 0)
			setDifficulty(GameStats.difficulty + 1);

		// add to hit combo counter & create the hit combo graphic!(also record
		// highest)
		hitComboCounter++;

		if (hitComboCounter > highestHitCombo)
		{
			highestHitCombo = hitComboCounter;
			GameStats.maxCombo = highestHitCombo; // GAMESTATS>> save the
													// highest combo to the game
													// statistics
		}

		// display the HUD graphic if the setting is enabled!
		if (game.getPreferences().getPref(CSSettings.KEY_ENABLE_GFX_COMBO_HUD,
				true))
		{
			if (hitComboCounter >= 10)
				cTxtFactory.getObject(collisionX, collisionY, new StringBuffer(
						"x" + hitComboCounter), color);
		}
	}

	public void unsuccessfulBlockHit(ColorBlockE e)
	{
		// create the explosion and define it's behavior!
		eExpFactory.getFactoryObject((int) e.x, (int) e.y);

		if (controlBlock.invincibleTimer == 0)
		{
			playerWasHit();

			// shake the control block
			controlBlock.shake(e.dx != 0);
		}
	}

	/**
	 * subtracts a life and makes the player invincible(or whatever effect
	 * hitting an invalid block or blackhole has on a player)
	 */
	public void playerWasHit()
	{
		if (GameStats.gameStyle == GameMode.ARCADE
				|| GameStats.gameStyle == GameMode.PSYCHOUT)
		{
			GameStats.lives -= 1;
			if (GameStats.lives == 0)
			{
				if (game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX,
						CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(SND_GALAXYEXPLODE).play(0.5f);
				fadeOutStyle = FadeOutStyle.TO_GAME_OVER;
				goToScreen(new GameOverRecycleScr(game));
			}

			cTxtFactory.getObject(168, 76, new StringBuffer("-1"),
					ComboText.HUD_DOWN);
		} else if (GameStats.gameStyle == GameMode.SURVIVAL)
		{
		}

		hitComboCounter = 0;

		// start invincibility sequence
		controlBlock.invincibleTimer = ControlBlock.INVINCIBILITY_TIMER;

		// play sound effect if necessary
		if (game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX,
				CSSettings.DEFAULT_ENABLE_SFX))
			assets.getSound(SND_BAD_HIT).play(0.7f);
	}

	@Override
	public void backPressed()
	{
		if (game.getPrompter()
				.showDualOption(
						new StringBuilder(
								(GameStats.gameStyle != GameMode.TUTORIAL ? "Exit from the current game?"
										: "Go back to the title screen?")),
						new StringBuilder("YES"), new StringBuilder("NO")) == 1)
		{
			fadeOutStyle = FadeOutStyle.TO_BLACK;
			goToScreen(new TitleScr((ColorShafted) game));
		}
	}

	/**
	 * adjust the game diffiaculty; this also sets the block creation rate to be
	 * as frequent as necessary
	 */
	public void setDifficulty(int d)
	{
		// if difficulty is increasing, create a text object on the HUD
		// which lets the player know(+1 typically)
		if (d > GameStats.difficulty)
			cTxtFactory.getObject(168, 106, new StringBuffer("+"
					+ (d - GameStats.difficulty)), ComboText.HUD_UP);

		// now adjust the difficulty
		GameStats.difficulty = d;
		blockCreationRate = 75;
		// subtract from the base block creation rate iteratively to ensure that
		// the algorithm is curved
		for (int diffCumul = 0; diffCumul < d; diffCumul++)
			blockCreationRate -= (int) (6.8f - (diffCumul / 4));
	}

	@Override
	public void goToScreen(Screen nScreen)
	{
		game.getMusic().releaseMusic(); // stop and release music resources when
										// leaving the game screen!
		super.goToScreen(nScreen);
	}

	@Override
	public void fadeInLogic(float deltaTime)
	{
		final int ZOOM_OUT_LENGTH = 50;
		
		if(introFinished)
		{
			game.getMusic().play(); // START THE MUSEKS!
			fadingIn = false;
		}
		
		if (fadeInSequencer == 0) // move out the hud components from view
									// before zooming!
		{
			Log.d("COLORSHAFTED", "bombButtonHButton.x = " + bombButton.x);
			Log.d("COLORSHAFTED", "bombHUDTxt.x = " + bombHUDTxt.x);
			Log.d("COLORSHAFTED", "scoreHUDTxt.x = " + scoreHUDTxt.x);
			Log.d("COLORSHAFTED", "diffHUDTxt.x = " + diffHUDTxt.x);
			Log.d("COLORSHAFTED", "livesHUDTxt.x = " + livesHUDTxt.x);
			Log.d("COLORSHAFTED", "hudPanelImg.x = " + hudPanelImg.x);
			fadeInSequencer++; // go to next segment
			bombButton.x += 200;
			bombHUDTxt.x += 200;
			scoreHUDTxt.x -= 200;
			diffHUDTxt.x -= 200;
			livesHUDTxt.x -= 200;
			hudPanelImg.x -= 200;

			// play "ARE YOU READY!?"
			Log.d("COLORSHAFTED", ((ColorShafted) game).globalAssets.toString());
			if (game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX,
					CSSettings.DEFAULT_ENABLE_SFX))
				((ColorShafted) game).globalAssets.getSound(
						CSGlobalAssets.SND_READY).play(1f);
		}
		if (fadeInTimer <= ZOOM_OUT_LENGTH - 1) // zoom out
		{
			if (game.getPreferences().getPref(CSSettings.KEY_INTRO_ZOOMOUT,
					CSSettings.DEFAULT_INTRO_ZOOMOUT))
				// scroll out the bg from a 300% zoom to a 100% zoom
				shaftBg.zoomPercentage = shaftFGL.zoomPercentage = shaftFGR.zoomPercentage = 100 + (ZOOM_OUT_LENGTH - fadeInTimer) * 3;
			// READY??? scrolling text
			if (game.getPreferences().getPref(CSSettings.KEY_INTRO_SCROLLTXT,
					CSSettings.DEFAULT_INTRO_SCROLLTXT))
				readyTxt.x = game.getGraphics().getWidth() - fadeInTimer
						* (game.getGraphics().getWidth() + readyTxt.getWidth())
						/ ZOOM_OUT_LENGTH;
			else
				readyTxt.x = game.getGraphics().getWidth() / 2;
		} 
		//zoom out just finished sequence and ready to slide in HUD
		else if(fadeInTimer < ZOOM_OUT_LENGTH + 10)
		{
			//play slide-in sound for the HUD components
			if(!fadeInSlideSndPlayed)
			{
				Log.d("COLORSHAFTED", ((ColorShafted) game).globalAssets.toString());
				if (game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX,
						CSSettings.DEFAULT_ENABLE_SFX))
					assets.getSound(GameScrAssets.SND_MOVE).play(1f);
				fadeInSlideSndPlayed = true;
			}
			
			shaftBg.zoomPercentage = shaftFGL.zoomPercentage = shaftFGR.zoomPercentage = 100;
			if (game.getPreferences().getPref(CSSettings.KEY_BOMB_BUTTON,
					CSSettings.DEFAULT_BOMB_BUTTON))
			{
				bombButton.visible = true;
				bombHUDTxt.visible = true;
			}

			scoreHUDTxt.color = Color.rgb(200, 200, 200);
			livesHUDTxt.color = Color.rgb(200, 200, 200);
			diffHUDTxt.color = Color.rgb(200, 200, 200);

			if (readyTxt.visible)
			{
				readyTxt.visible = false;
				readyTxt.string.setLength(0);
			}

			int slideCounter = fadeInTimer - ZOOM_OUT_LENGTH;
			bombButton.x = (float) (1000.0 - (20.0 * slideCounter));
			bombHUDTxt.x = (float) (998.0 - (20.0 * slideCounter));
			scoreHUDTxt.x = (float) (-36.0 + (20.0 * slideCounter));
			diffHUDTxt.x = (float) (-36.0 + (20.0 * slideCounter));
			livesHUDTxt.x = (float) (-36.0 + (20.0 * slideCounter));
			hudPanelImg.x = (float) (-200.0 + (20.0 * slideCounter));
		}

		if (!introFinished && fadeInTimer >= ZOOM_OUT_LENGTH + 10) // stop fading in
		{
			// fade in complete, make the HUD components visible and destroy the
			// ready text
			// (we run this code twice to ensure that it happens on the fade out
			// event if there is
			// massive frame skip)

			if (game.getPreferences().getPref(CSSettings.KEY_BOMB_BUTTON,
					CSSettings.DEFAULT_BOMB_BUTTON))
			{
				bombButton.visible = true;
				bombHUDTxt.visible = true;
			}
			
			scoreHUDTxt.color = Color.rgb(200, 200, 200);
			livesHUDTxt.color = Color.rgb(200, 200, 200);
			diffHUDTxt.color = Color.rgb(200, 200, 200);
			bombButton.x = 800.0f;
			bombHUDTxt.x = 778.0f;
			scoreHUDTxt.x = 176.0f;
			diffHUDTxt.x = 176.0f;
			livesHUDTxt.x = 176.0f;
			hudPanelImg.x = 0f;
			introFinished = true;
		}
	}

	/**
	 * calculate how much should be added to the score when a player
	 * successfully matches a block
	 */
	public int blockScoreAlgo()
	{
		int score;
		int baseScore = 100 + ((GameStats.difficulty * 20 < 200) ? GameStats.difficulty * 20
				: 200);
		float startDiffMult = 1.0f + ((GameStats.startingDifficulty <= 10 ? GameStats.startingDifficulty * 0.22f
				: 2.2f));
		float comboMult = 1.0f + ((hitComboCounter <= 100 ? hitComboCounter * 0.0025f
				: 0.25f));

		score = (int) ((float) baseScore * startDiffMult * comboMult);
		return score;
	}

	/** override draw method for optimized fade out screen sequence */
	@Override
	public void drawEntities(Graphics g)
	{
		super.drawEntities(g);

		// draw the fade in rectangle
		final int FADE_IN_RECT_LENGTH = 20;
		if (fadingIn && fadeInTimer <= FADE_IN_RECT_LENGTH)
		{
			final int alpha = 255 - (int) ((float) fadeInTimer
					/ (float) FADE_IN_RECT_LENGTH * 255.0f);
			g.drawRect(screenFadeRect, Color.argb(alpha, 0, 0, 0), true);
		}

		if (fadingOut)
		{
			if (this.fadeOutStyle == FadeOutStyle.TO_GAME_OVER)
				g.drawRect(this.fadeOutRect, Color.argb(
						(int) ((255.0 / 25.0) * fadeOutTimer), 255, 255, 255),
						true);
			else
				g.drawRect(this.fadeOutRect, Color.argb(
						(int) ((255.0 / 25.0) * fadeOutTimer), 0, 0, 0), true);
		}
	}

	@Override
	public void fadeOutLogic(float deltaTime)
	{
		if (fadeOutTimer >= 25)
			completeFadeOut();
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_GAME;
	}

	public void debugCode()
	{
		LevelItem newItem1 = new LevelItem(this);
		newItem1.instantiateItem(LevelItem.TYPE_XTRA_BOMB, LevelItem.RED, 1, 3);
		LevelItem newItem2 = new LevelItem(this);
		newItem2.instantiateItem(LevelItem.TYPE_XTRA_BOMB, LevelItem.BLUE, 0, 2);
		LevelItem newItem3 = new LevelItem(this);
		newItem3.instantiateItem(LevelItem.TYPE_XTRA_BOMB, LevelItem.GREEN, 2,
				1);
		LevelItem newItem4 = new LevelItem(this);
		newItem4.instantiateItem(LevelItem.TYPE_XTRA_BOMB, LevelItem.YELLOW,
				(int) Math.round(Math.random() * 3),
				(int) Math.round(Math.random() * 4));

		LevelItem newItem5 = new LevelItem(this);
		newItem5.instantiateItem(LevelItem.TYPE_XTRA_LIFE, LevelItem.RED, 0, 0);
		LevelItem newItem6 = new LevelItem(this);
		newItem6.instantiateItem(LevelItem.TYPE_XTRA_LIFE, LevelItem.BLUE, 0, 0);
		LevelItem newItem7 = new LevelItem(this);
		newItem7.instantiateItem(LevelItem.TYPE_XTRA_LIFE, LevelItem.GREEN, 0,
				0);
		LevelItem newItem8 = new LevelItem(this);
		newItem8.instantiateItem(LevelItem.TYPE_XTRA_LIFE, LevelItem.YELLOW, 0,
				0);
	}
}