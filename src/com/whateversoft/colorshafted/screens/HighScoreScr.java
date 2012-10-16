package com.whateversoft.colorshafted.screens;

import static com.whateversoft.colorshafted.screens.HighScoreAssets.FONT_SFTELEVISED;
import static com.whateversoft.colorshafted.screens.HighScoreAssets.IMG_SCROLL_DOWN;
import static com.whateversoft.colorshafted.screens.HighScoreAssets.IMG_SCROLL_UP;
import static com.whateversoft.colorshafted.screens.HighScoreAssets.IMG_SPACE_BG;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.whateversoft.android.framework.AnimEntity;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.RectEntity;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.android.framework.TextEntity;
import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.android.framework.impl.android.network.HttpClientApp;
import com.whateversoft.android.framework.impl.android.network.HttpRequestTask;
import com.whateversoft.android.framework.impl.android.network.HttpTools;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSScreens;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.database.ScoreDataSource;
import com.whateversoft.colorshafted.database.ScoreEntry;
import com.whateversoft.colorshafted.game.GameStats;
import com.whateversoft.colorshafted.network.HighScoresRequest;
import com.whateversoft.colorshafted.screens.highscore.PageFieldsBuffer;
import com.whateversoft.colorshafted.screens.highscore.SectionScoreData;

public class HighScoreScr extends Screen implements HttpClientApp
{
	//---------------------------------------//
	//=======>      Constants	    <========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	
	/**offset of the different view scopes */
	final int   SCOPE_LOCAL      = 0,
				SCOPE_GLOBAL     = 1;
	/** used for indexing sections */
	final int   	   SECTION_COUNT			  = 4,
					   SECTION_ARCADE_LOCAL       = 0,
					   SECTION_ARCADE_GLOBAL      = 1,
					   SECTION_PSYCHOUT_LOCAL     = 2,
					   SECTION_PSYCHOUT_GLOBAL	  = 3;
	public final int   SCORES_ON_SCREEN = 6;
	
	/** page offset from the screen */
	public final static int   PAGE_LEFT  = 0,
					   		  PAGE_SHOWN = 1,
					   		  PAGE_RIGHT = 2;
	
	final int	SCREEN_WIDTH;
	/** how much we need to slide our finger in order to get the screen to completely slide;
	 *  is set on start based on the screen's virtual width */
	final int		SLIDE_L_THRESHHOLD,	
					SLIDE_R_THRESHHOLD;
	
	//---------------------------------------//
	//=======>    Member Fields	    <========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	
	//=========| Visible Entities |==========//
	
	ImageEntity		 	   headerImg,
    					   footerImg,
    					   scrollUpBtn,
    					   scrollDownBtn,
    					   localSBtn,
    					   localUBtn,
    					   globalSBtn,
    					   globalUBtn;
	ImageEntity 		   backgroundImg   = 
							new ImageEntity(0, 0, assets.getImage(IMG_SPACE_BG), 0, this);
	TextEntity			   titleTxt = 
							new TextEntity(game.getGraphics().getWidth() / 2, 80, 
													new StringBuffer("High Scores"),
							Color.rgb(255, 255, 255), assets.getFont(FONT_SFTELEVISED), 
							50, Paint.Align.CENTER, 1, this);
	TextEntity			   navTxt;
	TextEntity			   modeTxt;
	AnimEntity			   loadingBoxAnim;
	
	//=====| Screen Transition Fields |======//
	
	public boolean transitioning = false;
	
	/** gauge how much a screen has slid; 0 is centered, -1 is a full screen width left and 1 is right */
	public double	sliderX = 0.0 ; 				 
	/** how to offset text */
	public double 	sliderXTxt = 0.0;	
	/** whether the screen is touched or not */
	public boolean  touchIsDown = false;		 //whether the screen is touched or not
	/** initial finger down position horizontally */
	public int 		initTouchX;	 
	
	//=====| Data Orientation Fields |======//
	
	/** Local SQLite database to work with */
	public ScoreDataSource dataSource;
	/** these variables are used if the player has just entered the 
	 *  high score screen from a certain mode */
	int 			   playerOnlineRank =      -1;
	GameMode		   playerPlayedMode;
	/** whether or not the player is coming in from a game or not(if not, presumably from the menu) */
	boolean			   playerPlayedGame = false;
	/** whether or not we've loaded local data to the high score screen yet */
	boolean			   loadedLocalData = false;
	/** the different sections of data in the app */
	SectionScoreData[] dataSections = new SectionScoreData[SECTION_COUNT];
	/** The current data section we are viewing */
	int				   dataSectionIndex;
	/** set to -1 if never submitted */
	int				   ourGameMode = -1;
	/** set to -1 if never submitted */
	int				   ourRank     = -1;
	
	//===========| UI Fields |============//
	
	/** page buffers to show on screen */
	PageFieldsBuffer[] pageBuffers = new PageFieldsBuffer[3];
	/** rectangles for the areas to select local and global mode */
	RectEntity[] selectViewRects = new RectEntity[2];
	public int bgY;
	public float backgroundAngle = 0; 
	
	//=====| Online Data Fields |====//
	/** how many online requests are currently being made; used to tell whether or not
	 *  we are displaying the current loading anim */
	int onlineReqCount = 0;

	//---------------------------------------//
	//========>     Constructors    <========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	
	/** constructor to go to high score screen with no input prompt -- 
	 * also called before constructor which is called when we exit a game */
	public HighScoreScr(Game game)	
	{
		super(game, new HighScoreAssets(game), 2);
		
		dataSource = new ScoreDataSource(((CanvasGame)game));
		dataSource.open();
		SCREEN_WIDTH = ScreenInfo.virtualWidth;		//save var to be referenced for screen dim calculations
		SLIDE_L_THRESHHOLD = SCREEN_WIDTH * 1 / 4;
		SLIDE_R_THRESHHOLD = SCREEN_WIDTH * 3 / 4;
		
		for(int i = 0; i < dataSections.length; i++)
			dataSections[i] = new SectionScoreData();
		
		//=======| Instantiate UI Elements |========//
		
		//instantiate the page buffers
		for(int i = 0; i < pageBuffers.length; i++)
		{
			pageBuffers[i] = new PageFieldsBuffer(i, this);
		}
		
		headerImg = new ImageEntity(game.getGraphics().getWidth() / 2, -8, 
							assets.getImage(HighScoreAssets.IMG_HEADER), 1, this);
		
		footerImg = new ImageEntity(game.getGraphics().getWidth() / 2, game.getGraphics().getHeight(), 
							assets.getImage(HighScoreAssets.IMG_FOOTER), 1, this);
		
		scrollUpBtn = new ImageEntity(game.getGraphics().getWidth() - 32, 176,   
							assets.getImage(IMG_SCROLL_UP), 1, this)
		{
			@Override
			public Rect getBounds()
			{
				Rect returnRect   = super.getBounds();
				returnRect.left   = returnRect.left   - 4;
				returnRect.top    = returnRect.top    - 4;
				returnRect.right  = returnRect.right  + 4;
				returnRect.bottom = returnRect.bottom + 4;
				return returnRect;
			}
		};
		
		scrollDownBtn = new ImageEntity(game.getGraphics().getWidth() - 32, 362, 
				assets.getImage(IMG_SCROLL_DOWN), 1, this)
		{
			@Override
			public Rect getBounds()
			{
				Rect returnRect   = super.getBounds();
				returnRect.left   = returnRect.left   - 8;
				returnRect.top    = returnRect.top    - 8;
				returnRect.right  = returnRect.right  + 8;
				returnRect.bottom = returnRect.bottom + 8;
				return returnRect;
			}
		};
		
		scrollUpBtn.semiTrans = true;
		scrollDownBtn.semiTrans = true;
		
		localSBtn = new ImageEntity(170, 104, assets.getImage(HighScoreAssets.IMG_LOCAL_S), 1, this);
		localUBtn = new ImageEntity(170, 104, assets.getImage(HighScoreAssets.IMG_LOCAL_U), 1, this);
		globalSBtn = new ImageEntity(game.getGraphics().getWidth() - 170, 104, assets.getImage(HighScoreAssets.IMG_GLOBAL_S), 1, this);
		globalUBtn = new ImageEntity(game.getGraphics().getWidth() - 170, 104, assets.getImage(HighScoreAssets.IMG_GLOBAL_U), 1, this);
		
		loadingBoxAnim = new AnimEntity(game.getGraphics().getWidth() - 48, 16, 1, this, assets.getAnim(HighScoreAssets.ANIM_LOADING));
		
		StringBuffer modeStr = new StringBuffer();
		
		modeTxt = new TextEntity(game.getGraphics().getWidth() / 2, 120, modeStr,
				Color.argb(60, 255, 255, 255), assets.getFont(FONT_SFTELEVISED), 40, 
				Paint.Align.CENTER, 1, this);	
		
		navTxt = new TextEntity(game.getGraphics().getWidth() / 2, 462, new StringBuffer("Loading..."),
				Color.argb(60, 255, 255, 255), assets.getFont(FONT_SFTELEVISED), 
				16, Paint.Align.CENTER, 1, this);
		
		if(!loadedLocalData)
		{
			setDataScope(getViewedMode(), getViewedScope());
			loadLocalScores();
			refreshScoreNavigation();
		}
		
		switch(getViewedMode())
		{
			case ARCADE:
				modeStr.append("ARCADE");
				break;
			case PSYCHOUT:
				modeStr.append("PSYCHOUT");
				break;
		}
	}
	
	/** constructor to create a high score screen after a game has been played 
	 * @param rank - what place we were in -- given the style of course; used to scroll to the index automatically
	 * @param gameMode - the style we were currently playing in
	 * @param scoreUploaded - whether or not we uploaded a score to the online database(or attempted), if we did, we know to start
	 * 						  in an online data section view
	 * @param game - for quick reference
	 * */
	public HighScoreScr(int rank, GameMode gameMode, boolean scoreUploaded, Game game)
	{
		this(game);
		
		//store the player's place on the highscore board to highlight his name when it is being listed
		if(scoreUploaded)
			playerOnlineRank = rank - 1;
		playerPlayedMode = gameMode;
		playerPlayedGame = true;
		
		Log.d("COLORSHAFTED", "got an online rank of " + playerOnlineRank + ", in the mode " + playerPlayedMode);
		
		//set initial mode viewed depending on what the game was on
		setDataScope(gameMode, scoreUploaded ? SCOPE_GLOBAL : SCOPE_LOCAL);
		
		//scroll to the rank last created both online and offline as necessary
		if(scoreUploaded)
			dataSections[getViewedMode().getServerValue() * 2 + SCOPE_GLOBAL].setScrollIndex(playerOnlineRank);
		dataSections[getViewedMode().getServerValue() * 2 + SCOPE_LOCAL].setScrollIndex(GameStats.localScoreRanking - 1);
		
		//load local data as necessary
		if(!loadedLocalData)
		{
			loadLocalScores();
			refreshScoreNavigation();
		}
	}

	//---------------------------------------//
	//========>   Class Methods	    <========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	
	//===========| Game Logic |============//
	
	@Override
	public void timedLogic()
	{
		//===========| Visible UI |============//
		
		//update space background
		updateBackground();	

		//set text alphas
		setTextAlphas();
		
		//update finger gesture slide values
		if(sliderXTxt != sliderX)
		{
			if(sliderXTxt < sliderX)
				sliderXTxt = ((sliderXTxt + 0.05) < sliderX) ? sliderXTxt + 0.05 : sliderX;
			else
				sliderXTxt = ((sliderXTxt - 0.05) > sliderX) ? sliderXTxt - 0.05 : sliderX;
		}
		
		//perform mode transition logic if we slid far enough to be transitioning
		if(transitioning)
			transitionScreen();
		
		//===========| Check for User Input |============//
		
		//check whether screen is tapped, held, released, change sliderX value and also initiate transitions
		screenTapHoldRelease();
		
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_UP) == 1 || 
		   game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_W) == 1)
				scrollUp();
		
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_DOWN) == 1 || 
		   game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_D) == 1)
				scrollDown();
		
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_CENTER) == 1 || 
		   game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_K) == 1 ||
		   game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_L) == 1 ||
		   game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_SPACE) == 1)
		{
			this.setDataScope(getViewedMode(), getViewedScope() == 1 ? 0 : 1);
		}
		
		loadingBoxAnim.visible = (onlineReqCount > 0);
		
		//position page buffers
		positionPageBuffers();
		
		//update the online score sections if highlighting a section which requires it
		if(dataSections[dataSectionIndex].getScores().size() < dataSections[dataSectionIndex].getResultTotalCount() && onlineReqCount == 0)
		{
			createdRequest();
			Log.d("HttpRequests", "About to start another HighScoresRequest task. the current dataSection size is " + dataSections[dataSectionIndex].getScores().size()
					+ " and its total result count is " + dataSections[dataSectionIndex].getResultTotalCount());
			final HttpClientApp thisScr = this;
			final int indexRequestedAt  = dataSections[dataSectionIndex].getScores().size();
			ColorShafted.context.runOnUiThread(
					new Thread()
					{
						@Override
						public void run()
						{
							new HighScoresRequest(getViewedMode().getServerValue(), indexRequestedAt, 10, thisScr)
								.execute();
						}
					});
		}
	}

	/** handle the logic that transitions a screen as it is sliding; specifically -- we are 
	 * determining whether or not we have scrolled enough to warrant a screen switch*/
	public void transitionScreen()
	{
		sliderX = sliderX > 0.0 ? 1.0 : -1.0;
		
		//finishing a transition
		if(sliderXTxt == sliderX)
		{
			setDataScope(getViewedMode() == GameMode.ARCADE ? GameMode.PSYCHOUT : GameMode.ARCADE, 
							getViewedScope());
			
			//dataSectionIndex = (dataSectionIndex == SECTION_ARCADE_LOCAL || dataSectionIndex == SECTION_ARCADE_GLOBAL ? 
			//					dataSectionIndex + 2 : dataSectionIndex - 2);
			modeTxt.string = new StringBuffer(getViewedMode() == GameMode.ARCADE ? "ARCADE" : "PSYCHOUT");
			
			transitioning = false;
			
			sliderX = 0.0;
			sliderXTxt = 0.0;
		}
	}

	/** load all scores into array lists of the necessary modes */
	public void loadLocalScores()
	{
		//score list to parse from database for each section
		ArrayList<ScoreEntry> scoresToLoad = new ArrayList<ScoreEntry>();
		
		//start iteration in ARCADE_LOCAL and hop to each proceeding
		for(int sectionIndex = 0; sectionIndex < SECTION_COUNT; sectionIndex+=2)
		{
			int sectionStyle = sectionIndex / 2;
			Log.d("COLORSHAFTED", "sectionStyle == " + sectionStyle);
			scoresToLoad.clear();		//clear at start of each loop
			//scan through and collect neccessary scores to add to a specific section
			for(ScoreEntry sE : dataSource.getAllScores())
			{
				if(sE.getGameStyle() == sectionStyle)
					scoresToLoad.add(sE);
			}
			
			//add those scores
			dataSections[sectionIndex].setScores(scoresToLoad);
			refreshScoreNavigation();
			Log.d("COLORSHAFTED", "just set dataSections[" + sectionIndex + "].setScores() with an array size of " + 
			dataSections[sectionIndex].getScores().size());
		}
	}
	
	//===========| UI Navigation Logic |============//
	
	/** navigate between different data scopes by supplying a parameter for both the gameMode and the viewMode(local/global) */
	public void setDataScope(GameMode gameMode, int viewScope)
	{
			switch(viewScope)
			{
				case SCOPE_LOCAL:
					if(getViewedScope() != viewScope)
						dataSectionIndex  -= 1;		//toggle to local mode of the current index
					//set all required GUI button visibilities...
					localSBtn.visible  = true;
					localUBtn.visible  = false;
					globalSBtn.visible = false;
					globalUBtn.visible = true;
					break;
				case SCOPE_GLOBAL:
					if(getViewedScope() != viewScope)
						dataSectionIndex  += 1;		//toggle to global mode of the current index		
					//set all required GUI button visibilities...
					globalSBtn.visible = true;
					globalUBtn.visible = false;
					localSBtn.visible  = false;
					localUBtn.visible  = true;
					break;
		}
			
			switch(gameMode)
			{
				case ARCADE:
					dataSectionIndex = SECTION_ARCADE_LOCAL + getViewedScope();
					break;
				case PSYCHOUT:
					dataSectionIndex = SECTION_PSYCHOUT_LOCAL + getViewedScope();
					break;
			}
			
			//show connection prompt
			//TODO-> INSERT LOGIC FOR WHEN IT IS APPROPRIATE TO FETCH NEW SCORES
			if(getViewedScope() == SCOPE_GLOBAL && !dataSections[dataSectionIndex].wasDataFetched())
			{
				final HighScoreScr thisScr = this;
				onlineReqCount++;
				ColorShafted.context.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						new HighScoresRequest(getViewedMode().getServerValue(), 0, 10, thisScr).execute();
					}
				});
			}
		
		refreshScoreNavigation();
		Log.d("COLORSHAFTED", "dataSectionIndex == " + dataSectionIndex);
	}
	
	/** updates visible scores based on the current mode using the ScoreSectionData for the current
	 *  section we are browsing */
	public void refreshScoreNavigation()
	{
		//===========|  Algorithm Data Fields |============//
		SectionScoreData currentSection;
		ArrayList<ScoreEntry> scores = null;
		int scrollIndex = 0;
		int scoreCount = 0;
		int sectionIndex = 0;
		
		//==| Scroll through Page Buffers and Append Displayed Score Entries |==//
		for(int bufferIndex = 0; bufferIndex < pageBuffers.length; bufferIndex++)
		{
			//determine the section index of the current page buffer we're working with...
			switch(bufferIndex)
			{
				case PAGE_SHOWN:
					sectionIndex = dataSectionIndex;
					break;
				case PAGE_LEFT:
					sectionIndex = dataSectionIndex - 2;//set page buffer section to the next to the left
					if(sectionIndex < 0)
						sectionIndex += SECTION_COUNT;
					break;
				case PAGE_RIGHT:
					sectionIndex = dataSectionIndex + 2;	//set page buffer section to the next to the right
					if(sectionIndex >= SECTION_COUNT)
						sectionIndex -= SECTION_COUNT;
					break;
			}
			
			Log.d("COLORSHAFTED", "bufferIndex == " + bufferIndex + ", sectionIndex == " + sectionIndex);
			//ref data to work with the current page buffer's section conveniently
			currentSection = dataSections[sectionIndex];
			scores		   = currentSection.getScores();
			scrollIndex	   = currentSection.getScrollIndex();
			scoreCount 	   = scores.size();
			
			ScoreEntry[] scoresOnBuffer = new ScoreEntry[SCORES_ON_SCREEN];	//where we will store the scores to show
			
			//scroll through all entries and update them based on the scrollIndex
			for(int i = 0; i < SCORES_ON_SCREEN; i++)
			{
				if(scrollIndex + i < scoreCount)
				{
					scoresOnBuffer[i] = scores.get(i + scrollIndex);
					if(scoresOnBuffer[i] != null)
					Log.d("COLORSHAFTED", "HighScoreScr added a score with a name of " + scoresOnBuffer[i].getName());
				}
				else	//if the score scanned is available, instantiate entity
					scoresOnBuffer[i] = null;
			}
			
			//set the entries, supplying our rank if necessary
			if(playerPlayedGame)
			{
				if(getViewedMode() == playerPlayedMode && getViewedScope() == SCOPE_GLOBAL && playerOnlineRank != -1)
					pageBuffers[bufferIndex].setScoreEntries(scrollIndex, scoresOnBuffer, playerOnlineRank);
				else if(getViewedMode() == playerPlayedMode && getViewedScope() == SCOPE_LOCAL)
					pageBuffers[bufferIndex].setScoreEntries(scrollIndex, scoresOnBuffer, GameStats.localScoreRanking - 1);
				else
					pageBuffers[bufferIndex].setScoreEntries(scrollIndex, scoresOnBuffer, -1);

			}
			else
			{
				pageBuffers[bufferIndex].setScoreEntries(scrollIndex, scoresOnBuffer, -1);
			}
		}
		
		//we will now be working with the current viewed page to set the scroll navigation and
		//pagination string data
		currentSection = dataSections[dataSectionIndex];
		scores		   = currentSection.getScores();
		scrollIndex	   = currentSection.getScrollIndex();
		scoreCount 	   = scores.size();
		
		//scroll buttons
		scrollDownBtn.visible = scoreCount > (scrollIndex + SCORES_ON_SCREEN);
		scrollUpBtn.visible   = scrollIndex > 0;
		
		//edit our pagination string
		navTxt.string.setLength(0);
		navTxt.string.append("Viewing scores " + (scrollIndex+1) + " through " + 
				( ((scrollIndex + SCORES_ON_SCREEN) < scoreCount) ? (scrollIndex + SCORES_ON_SCREEN) : scoreCount)
				+ " out of " + scoreCount);
	}
	
	public int getViewedScope()
	{
		if(dataSectionIndex % 2 == 0)
			return SCOPE_LOCAL;
		else
			return SCOPE_GLOBAL;
	}
	
	/** returns what game style/mode section we are currently on based on the current value of the <b>dataSectionIndex</b> */
	public GameMode getViewedMode()
	{
		switch(dataSectionIndex)
		{
			case SECTION_ARCADE_LOCAL:
			case SECTION_ARCADE_GLOBAL:
				return GameMode.ARCADE;
			case SECTION_PSYCHOUT_LOCAL:
			case SECTION_PSYCHOUT_GLOBAL:
				return GameMode.PSYCHOUT;
			default: 
				return GameMode.ARCADE;
		}
	}
	
	public void scrollUp()
	{
		SectionScoreData currentSection = dataSections[dataSectionIndex];
		if(currentSection.getScrollIndex() > 0)
		{
			dataSections[dataSectionIndex].setScrollIndex(currentSection.getScrollIndex() - 1);
			refreshScoreNavigation();
			if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, true))
				assets.getSound(HighScoreAssets.SND_MOVE).play(1);	
		}
	}
	
	public void scrollDown()
	{
		SectionScoreData currentSection = dataSections[dataSectionIndex];
		
		if(currentSection.getScrollIndex() < currentSection.getScores().size() - SCORES_ON_SCREEN)
		{
			dataSections[dataSectionIndex].setScrollIndex(currentSection.getScrollIndex() + 1);
			refreshScoreNavigation();
			
			if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, true))
				assets.getSound(HighScoreAssets.SND_MOVE).play(1);
		}	
	}
	
	//===========| Finger Gesture Detection |============//
	/**check whether we've touched an element on the UI which would trigger a response;
	 * (e.g. scroll up & down buttons, or hit the rects that detect switches from local to glo bal )*/
	@Override
	public void screenTapped()
	{
		//===========| Check for Scroll Up/Down button taps |============//
		if(scrollDownBtn.getBounds().contains(game.getInput().getTouchX(0), game.getInput().getTouchY(0)))
			scrollDown();
		
		if(scrollUpBtn.getBounds().contains(game.getInput().getTouchX(0), game.getInput().getTouchY(0)))
			scrollUp();
		
		//===========| Check for local/global button tap |============//
		//check for local-bounding area...
		Rect selRectTest = localSBtn.getBounds();
		selRectTest.left -= 64;
		selRectTest.right += 16;
		selRectTest.top -= 12;
		selRectTest.bottom += 8;
		if(selRectTest.contains(game.getInput().getTouchX(0), game.getInput().getTouchY(0)))
		{
			setDataScope(getViewedMode(), SCOPE_LOCAL);
		}
		//check for global-bounding area...
		selRectTest = globalSBtn.getBounds();
		selRectTest.left -= 16;
		selRectTest.right += 64;
		selRectTest.top -= 12;
		selRectTest.bottom += 8;
		if(selRectTest.contains(game.getInput().getTouchX(0), game.getInput().getTouchY(0)))
		{
			setDataScope(getViewedMode(), SCOPE_GLOBAL);
		}
	}

	/** perform logic based on whether we've tapped, held and released our finger from the screen */
	public void screenTapHoldRelease()
	{
		//detect whether the screen was tapped and initialize all holding checks
		if(touchTimer[0] == 1 && !touchIsDown && !transitioning)
		{
			touchIsDown  = true;
			initTouchX = game.getInput().getTouchX(0);
		}
		
		if(touchIsDown && touchTimer[0] > 1 && !transitioning)
		{
			sliderX = 0.0 + (game.getInput().getTouchX(0) - (double)initTouchX)/SCREEN_WIDTH;
			sliderXTxt = sliderX;
		}
		
		//~release touch
		//check if the position is beyond the threshhold to set transition flag to true
		if(!transitioning && ((touchIsDown && touchTimer[0] == 0) || 
		   (game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_LEFT) == 1 || 
			game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_RIGHT) == 1)) ||
			(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_A) == 1 || 
			game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_D) == 1))
		{
			touchIsDown = false;
			if(((Math.abs(sliderX) > 0.05 && (game.getInput().getTouchX(0) < SLIDE_L_THRESHHOLD || game.getInput().getTouchX(0) > SLIDE_R_THRESHHOLD))
				|| Math.abs(sliderX) > 0.20) ||
				game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_LEFT) == 1 || 
				game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_RIGHT) == 1 ||
				(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_A) == 1 || 
				game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_D) == 1))
			{
				transitioning = true;
				if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, true))
					assets.getSound(HighScoreAssets.SND_MOVE).play(1);	
			}
			else
				sliderX = 0.0;
		}
		
		if(transitioning)
			touchIsDown = false;
	}
	
	//===========| Online Connectivity |============/
	/** called by the game when we send a server request and get a response on a high score screen */
	public void onServerResponse(HttpRequestTask httpTask, int serverRequestCode, String responseStr)
	{
		completedARequest();		//acknowledge a completed request from the server
		Log.d("HttpRequests", "running onServerResponse() in HighScoreScr.java");
		game.stopLoadingDialog();
		if(responseStr != null)
		{
			String jsonStr = HttpTools.htmlParser.clearHTMLTags(responseStr);
			//retrieve an array of ScoreEntrys from the server
			/*IMPORTANT NOTE: ScoreEntry which is in the last index of the array we are retrieving contains
			  it's "resultCount" variable in the "score" section! We must be sure to parse this out of the array 
			  before we add it to a data section 
			  */
			ScoreEntry[] retrievedArray = HttpTools.gson.fromJson(jsonStr, ScoreEntry[].class);
			
			ScoreEntry queryDataEntry = retrievedArray[retrievedArray.length - 1];	//for convenient reference to query data entry
			//retrieve the data section size of the online section
			int sectionScoreCount    = queryDataEntry.getScore();
			int indexRetrieved = queryDataEntry.getLevel();
			int scoreCountRequested = queryDataEntry.getGameStyle();
			
			//copy our retrieved data into the a ScoreEntry[] array to assign to a section
			ScoreEntry[] retrievedScores = new ScoreEntry[retrievedArray.length -1];
			System.arraycopy(retrievedArray, 0, retrievedScores, 0, retrievedScores.length);
			//figure out which section was updated and append the new scores to that section
			int gameStyleUpdated = ((HighScoresRequest)httpTask).getGameStyle();
			int sectionUpdated   = (gameStyleUpdated * 2 + SCOPE_GLOBAL);
		
			//if it is our first scores of the section
			if(indexRetrieved == 0)
			{
				dataSections[sectionUpdated].setScores(retrievedScores);
			}
			//if more scores to retrieve, append them!
			else if(indexRetrieved > 0 && (dataSections[sectionUpdated].getScores().size() < dataSections[sectionUpdated].getResultTotalCount()))
				dataSections[sectionUpdated].appendScores(retrievedScores);
			
			dataSections[sectionUpdated].setResultTotalCount(sectionScoreCount);
		
			//only update the navigation if we're still on the high score mode that was update
			if(dataSectionIndex == sectionUpdated)
				refreshScoreNavigation();
		}
		else
			Toast.makeText((Context)game, "online data is currently unavailable", 
			Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onRequestTimeout(HttpRequestTask httpTask)
	{
		completedARequest();	//acknowledge a finished request from the server
		Toast.makeText((Context)game, "Unfortunately, the request with the server has timed out. " +
				"If this persists, please let us know at whateversoftapps@gmail.com", Toast.LENGTH_LONG).show();
	}

	//===========| API Methods |============//
	
	@Override
	public void fadeInLogic(float deltaTime)
	{
		fadingIn = false;
	}
	
	/** close the database before we exit the screen */
	@Override
	public void goToScreen(Screen nScreen)
	{
		dataSource.close();
		super.goToScreen(nScreen);
	}
	
	/**  */
	@Override
	public void backPressed()
	{
		goToScreen(new TitleBufferScr((ColorShafted)game, backgroundAngle));
	}
	
	//===========| UI Logic |============//
	
	/** update the space background behind the UI */
	public void updateBackground()
	{
		backgroundAngle+= 0.25;
		backgroundAngle %= 360;
		backgroundImg.y = 
				 -(880 - game.getGraphics().getHeight())/2 + 
				 Math.round(Math.cos(Math.toRadians(backgroundAngle)) 
						 * (880 - game.getGraphics().getHeight())/2);
	}
	
	/** position the three page buffers relative to the slide offset and which index they are set 
	 *  at on-screen */
	public void positionPageBuffers()
	{
		for(int i = 0; i < pageBuffers.length; i++)
		{
			int pageOffset = 0;
			switch(i)
			{
				case PAGE_LEFT:
					pageOffset = SCREEN_WIDTH; //-SCREEN_WIDTH
					break;
				case PAGE_SHOWN:
					pageOffset = 0; //0
					break;
				case PAGE_RIGHT:
					pageOffset = SCREEN_WIDTH;
					break;
			}
			pageBuffers[i].setXOffset(pageOffset + (int)(sliderXTxt * SCREEN_WIDTH));
		}
	}
	
	/** set the Alpha values of all of the text fields on the screen */
	public void setTextAlphas()
	{
		//set alpha level for the navigation and title text
		modeTxt.color = Color.argb((int)((1 - Math.abs(sliderXTxt)) * 100), 255, 255, 255);
		navTxt.color  = Color.argb((int)((1 - Math.abs(sliderXTxt)) * 100), 255, 255, 255);
		scrollUpBtn.alpha = (int)((1 - Math.abs(sliderXTxt)) * 255);
		scrollDownBtn.alpha = (int)((1 - Math.abs(sliderXTxt)) * 255);
	}
	
	public void completedARequest()
	{
		if(onlineReqCount > 0)
			onlineReqCount--;
		loadingBoxAnim.visible = (onlineReqCount != 0);
	}
	
	public void createdRequest()
	{
		onlineReqCount++;
		loadingBoxAnim.visible = (onlineReqCount != 0);
	}

	@Override
	public void showServerErrorMsg()
	{
		Toast.makeText((Context)game, "Sorry, there was a problem communicating with the server. Please ensure you have connection to the network. If this error persists, please email us at" +
				"whateversoftapps@gmail.com", Toast.LENGTH_LONG).show();
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_HIGH_SCORE;
	}
}