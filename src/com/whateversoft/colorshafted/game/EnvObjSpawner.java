package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScr.GRID_BOT_CENTER;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_BOT_LEFT;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_BOT_RIGHT;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_MID_CENTER;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_MID_LEFT;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_MID_RIGHT;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_TOP_CENTER;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_TOP_LEFT;
import static com.whateversoft.colorshafted.screens.GameScr.GRID_TOP_RIGHT;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_DOWN_CENTER;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_DOWN_LEFT;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_DOWN_RIGHT;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_LEFT_BOTTOM;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_LEFT_CENTER;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_LEFT_TOP;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_RIGHT_BOTTOM;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_RIGHT_CENTER;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_RIGHT_TOP;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_UP_CENTER;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_UP_LEFT;
import static com.whateversoft.colorshafted.screens.GameScr.SHAFT_UP_RIGHT;

import java.util.Random;

import android.util.Log;

import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.screens.GameScr;
import com.whateversoft.colorshafted.screens.GameScrAssets;
/** creates objects that will be spawned during the course of a game in the game's environment
 *  (ColorBlocks, BlackHoles, Items) */
public class EnvObjSpawner
{
	public boolean debugging = false;
	/**  parent screen reference used to keep track of where the color block should be drawn*/
	public GameScr gameScreen;
	Random rand = new Random();
	
	//-------------------------------------------------------------//
	//					Defined Constants
	//-------------------------------------------------------------//
	
	final int MAX_DIFFICULTY = 20;
	final int BH_WARNING_TIME = 3;
	
	/** defines the orientation of an incoming black hole, though can come in two different 
	 * 	directions									     (can later be used to filter out
	 * 													  grid positions to orient around for 
	 * 													  block and item spawning algorithms)    */
	public static final int BH_OR_LEFT		  = 0,
					 		BH_OR_RIGHT      = 1,
					 		BH_OR_TOP		  = 2,
					 		BH_OR_BOTTOM	  = 3,
					 		BH_OR_TOPLEFT	  = 4,
					 		BH_OR_TOPRIGHT   = 5,
					 		BH_OR_BOTTOMLEFT = 6,
					 		BH_OR_BOTTOMRIGHT= 7;
	
	public final int R		 = ColorBlock.RED;
	public final int G		 = ColorBlock.GREEN;
	public final int B		 = ColorBlock.BLUE;
	public final int Y		 = ColorBlock.YELLOW;
	public final int ANY	 = -1;
	
	/** constant used for determining the sequence */
	public final int NO_SHIFT 	   = 0,
	 				 ONE_SHIFT_UNI = 1,
	 				 ONE_SHIFT_BI  = 2,
	 				 TWO_SHIFT     = 3,
	 				 RANDOM_SHIFT  = 4;
	
	public enum RotateShiftMethod   { NONE, ONE_SHIFT_UNI, ONE_SHIFT_BI, TWO_SHIFT, RANDOM };
	public enum PositionShiftMethod { NONE, ONE_SHIFT_RANDOM, TWO_SHIFT_RANDOM, RANDOM };
	
	
	/** direction a color blck is moving in */
	public final int DIR_LEFT 	= 0,
					 DIR_UP		= 1,
					 DIR_RIGHT	= 2,
					 DIR_DOWN	= 3;
	
	/** constant which defines whether a black hole will be spawned in one direction at a time
	 *  or two */
	public final int 		 BHSPAWN_UNIDIR  = 0,
					 		 BHSPAWN_DUALDIR = 1;

	//-------------------------------------------------------------//
	//					Block Spawning Variables
	//-------------------------------------------------------------//
	public int			  shaftSpawnedAt;
	/** map of which color should spawn at a certain grid position orientation and shaft id
	 *  (given as [gridPosOrientation, shaftId])*/
	public final int[][]		  colorSpawnMap = new int[9][12];
	
	/** the sequence pattern that the color configuration is rotated */
	public RotateShiftMethod rotateShiftMode = RotateShiftMethod.NONE;
	/** the sequence pattern that the base grid orientation is altered */
	public PositionShiftMethod positionShiftMode = PositionShiftMethod.NONE;
	/** number of times a position is repeated as a block is spawned before it is shifted in a given shift mode */
	public int positionRepeat = 0;
	/** number of times a grid position has left to repeat before it is shifted */
	public int repeatCountdown = 0;
	
	//-------------------------------------------------------------//
	//					Black Hole Variables
	//-------------------------------------------------------------//
	public int 			  incomingBHDir = -1;
	/** how often black holes will come in */
	public int bhCreateInterval = -1;
	public int bhCountDown = -1;
	
	//-------------------------------------------------------------//
	//					Psychout Variables	
	//-------------------------------------------------------------//
	public boolean	giveDSBlock = false;
	public boolean	giveCTBlock = false;
	public int		percentDSLikely = 0;
	public int		percentCTLikely = 0;
	
	
	/** the current rotation of incoming block angles*/
	public int colorConfig;
	/** where the next grid orientation position will be */
	public int spawnTargetPos = GameScr.GRID_MID_CENTER;
	/** which direction the colorConfig will rotate in(-1 is left and 1 is right) */
	public int directionShift;
	/** how many blocks have been created*/
	public int blocksCreated;
	/** what difficulty the current color configuration is based on(also used as a buffer to track changing difficulty) */
	public int difficulty = -1;		//set by default to -1 to detect starting difficulty change in shiftDiffConfig

	public EnvObjSpawner(GameScr s)
	{
		//-------------------------------------------------------//
		//	INITIALIZE VARIABLES
		//-------------------------------------------------------//
		gameScreen = s;				//keep track of the parent screen
		rotateShiftMode = RotateShiftMethod.NONE;	//set the sequence pattern of incoming blocks to no configuration shifting
		blocksCreated = 0;		//no blocks at start
		directionShift = 0;		//arbitrary directional shift value
		colorConfig = 0;		//set the default configuration
		
		defineColorMap();
		setUpDifficulty(difficulty);
	}
	
	/** creates orientation targets for an array of shafts that correlate to which color should be propagated from each
	 *  shaft position at a grid position*/
	public void defineColorMap()
	{
		//TOP LEFT GRID POSITION
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_DOWN_LEFT]   = R;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_DOWN_CENTER] = G;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_DOWN_RIGHT]  = ANY;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_UP_LEFT]     = Y;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_UP_CENTER] 	= G;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_UP_RIGHT]  	= ANY;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_RIGHT_TOP] 	= B;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_RIGHT_CENTER]= Y;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_RIGHT_BOTTOM]= ANY;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_LEFT_TOP]  	= G;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_LEFT_CENTER] = Y;
		colorSpawnMap[GRID_TOP_LEFT][SHAFT_LEFT_BOTTOM] = ANY;
		
		//TOP CENTER GRID POSITION
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_DOWN_LEFT]   	= B;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_DOWN_CENTER] 	= R;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_DOWN_RIGHT]  	= G;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_UP_LEFT]    	= B;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_UP_CENTER] 	= Y;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_UP_RIGHT]  	= G;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_RIGHT_TOP] 	= B;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_RIGHT_CENTER]	= Y;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_RIGHT_BOTTOM]	= ANY;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_LEFT_TOP]  	= G;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_LEFT_CENTER] 	= Y;
		colorSpawnMap[GRID_TOP_CENTER][SHAFT_LEFT_BOTTOM] 	= ANY;
		
		//TOP RIGHT GRID POSITION
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_DOWN_LEFT]   	= ANY;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_DOWN_CENTER] 	= B;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_DOWN_RIGHT]  	= R;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_UP_LEFT]     	= ANY;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_UP_CENTER] 		= B;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_UP_RIGHT]  		= Y;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_RIGHT_TOP] 		= B;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_RIGHT_CENTER]	= Y;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_RIGHT_BOTTOM]	= ANY;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_LEFT_TOP]  		= G;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_LEFT_CENTER] 	= Y;
		colorSpawnMap[GRID_TOP_RIGHT][SHAFT_LEFT_BOTTOM] 	= ANY;
		
		//MID LEFT GRID POSITION
		colorSpawnMap[GRID_MID_LEFT][SHAFT_DOWN_LEFT]   = R;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_DOWN_CENTER] = G;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_DOWN_RIGHT]  = ANY;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_UP_LEFT]     = Y;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_UP_CENTER] 	= G;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_UP_RIGHT]  	= ANY;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_RIGHT_TOP] 	= R;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_RIGHT_CENTER]= B;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_RIGHT_BOTTOM]= Y;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_LEFT_TOP]  	= R;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_LEFT_CENTER] = G;
		colorSpawnMap[GRID_MID_LEFT][SHAFT_LEFT_BOTTOM] = Y;
		
		//MID CENTER GRID POSITION
		colorSpawnMap[GRID_MID_CENTER][SHAFT_DOWN_LEFT]   = B;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_DOWN_CENTER] = R;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_DOWN_RIGHT]  = G;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_UP_LEFT]     = B;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_UP_CENTER]   = Y;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_UP_RIGHT]    = G;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_RIGHT_TOP]   = R;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_RIGHT_CENTER]= B;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_RIGHT_BOTTOM]= Y;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_LEFT_TOP]    = R;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_LEFT_CENTER] = G;
		colorSpawnMap[GRID_MID_CENTER][SHAFT_LEFT_BOTTOM] = Y;
		
		//MID RIGHT GRID POSITION
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_DOWN_LEFT]   = ANY;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_DOWN_CENTER] = B;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_DOWN_RIGHT]  = R;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_UP_LEFT]     = ANY;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_UP_CENTER] 	 = B;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_UP_RIGHT]  	 = Y;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_RIGHT_TOP] 	 = R;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_RIGHT_CENTER]= B;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_RIGHT_BOTTOM]= Y;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_LEFT_TOP]  	 = R;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_LEFT_CENTER] = G;
		colorSpawnMap[GRID_MID_RIGHT][SHAFT_LEFT_BOTTOM] = Y;
		
		//BOTTOM LEFT GRID POSITION
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_DOWN_LEFT]   = R;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_DOWN_CENTER] = G;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_DOWN_RIGHT]  = ANY;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_UP_LEFT]     = Y;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_UP_CENTER] 	= G;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_UP_RIGHT]  	= ANY;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_RIGHT_TOP] 	= ANY;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_RIGHT_CENTER]= R;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_RIGHT_BOTTOM]= B;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_LEFT_TOP]  	= ANY;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_LEFT_CENTER] = R;
		colorSpawnMap[GRID_BOT_LEFT][SHAFT_LEFT_BOTTOM] = G;
		
		//BOTTOM CENTER GRID POSITION
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_DOWN_LEFT]   = B;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_DOWN_CENTER] = R;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_DOWN_RIGHT]  = G;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_UP_LEFT]     = B;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_UP_CENTER]   = Y;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_UP_RIGHT]    = G;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_RIGHT_TOP]   = ANY;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_RIGHT_CENTER]= R;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_RIGHT_BOTTOM]= B;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_LEFT_TOP]    = ANY;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_LEFT_CENTER] = R;
		colorSpawnMap[GRID_BOT_CENTER][SHAFT_LEFT_BOTTOM] = G;
		
		//BOTTOM RIGHT GRID POSITION
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_DOWN_LEFT]   = ANY;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_DOWN_CENTER] = B;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_DOWN_RIGHT]  = R;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_UP_LEFT]     = ANY;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_UP_CENTER] 	 = B;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_UP_RIGHT]  	 = Y;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_RIGHT_TOP] 	 = ANY;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_RIGHT_CENTER]= R;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_RIGHT_BOTTOM]= B;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_LEFT_TOP]  	 = ANY;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_LEFT_CENTER] = R;
		colorSpawnMap[GRID_BOT_RIGHT][SHAFT_LEFT_BOTTOM] = G;
	}
	
	public void setUpDifficulty(int difficulty)
	{
			switch(difficulty)
			{
				case 0:
					rotateShiftMode   = RotateShiftMethod.ONE_SHIFT_UNI;
					positionShiftMode = PositionShiftMethod.NONE;
					positionRepeat    = 4;
					bhCreateInterval  =-1;
					percentDSLikely   = 25;
					percentCTLikely	  = 0;
					break;
				case 1:
					rotateShiftMode   = RotateShiftMethod.ONE_SHIFT_UNI;
					positionShiftMode = PositionShiftMethod.ONE_SHIFT_RANDOM;
					positionRepeat	  = 4;
					bhCreateInterval		  = -1;
					percentDSLikely   = 25;
					percentCTLikely	  = 10;
					break;
				case 2:
					rotateShiftMode   = RotateShiftMethod.ONE_SHIFT_BI;
					positionShiftMode = PositionShiftMethod.ONE_SHIFT_RANDOM;
					positionRepeat    = 3;
					bhCreateInterval		  = 6 + rand.nextInt(5);
					percentDSLikely   = 25;
					percentCTLikely	  = 25;
					break;
				case 3:
					rotateShiftMode	  = RotateShiftMethod.ONE_SHIFT_BI;
					positionShiftMode = PositionShiftMethod.ONE_SHIFT_RANDOM;
					positionRepeat	  = 2;
					bhCreateInterval		  = 6 + rand.nextInt(4);
					percentDSLikely   = 34;
					percentCTLikely	  = 34;
					break;
				case 4:
					rotateShiftMode	  = RotateShiftMethod.ONE_SHIFT_BI;
					positionShiftMode = PositionShiftMethod.ONE_SHIFT_RANDOM;
					positionRepeat	  = 2;
					bhCreateInterval		  = 5 + rand.nextInt(4);
					percentDSLikely   = 40;
					percentCTLikely	  = 34;
					break;
				case 5:
					rotateShiftMode	  = RotateShiftMethod.ONE_SHIFT_BI;
					positionShiftMode = PositionShiftMethod.TWO_SHIFT_RANDOM;
					positionRepeat	  = 2;
					bhCreateInterval		  = 5 + rand.nextInt(4);
					percentDSLikely   = 40;
					percentCTLikely	  = 50;
					break;
				case 6:
					rotateShiftMode   = RotateShiftMethod.ONE_SHIFT_BI;
					positionShiftMode = PositionShiftMethod.TWO_SHIFT_RANDOM;
					positionRepeat	  = 1;
					bhCreateInterval		  = 4 + rand.nextInt(4);
					percentDSLikely   = 40;
					percentCTLikely	  = 50;
					break;
				case 7:
					rotateShiftMode	  = RotateShiftMethod.ONE_SHIFT_BI;
					positionShiftMode = PositionShiftMethod.RANDOM;
					positionRepeat	  = 1;
					bhCreateInterval		  = 4 + rand.nextInt(3);
					percentDSLikely   = 40;
					percentCTLikely	  = 50;
					break;
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				default:
					rotateShiftMode	  = RotateShiftMethod.RANDOM;
					positionShiftMode = PositionShiftMethod.RANDOM;
					positionRepeat	  = 0;
					bhCreateInterval  = 3 + rand.nextInt(3);
					percentDSLikely   = 40;
					percentCTLikely	  = 67;
					break;
			}

		repeatCountdown = positionRepeat;
		
		if(debugging)
		{
			Log.d("COLORSHAFTED", "Just set a difficulty with the following values: ");
			Log.d("COLORSHAFTED", "\t\tdifficulty = " + difficulty);
			Log.d("COLORSHAFTED", "\t\trotateShiftMode = " + rotateShiftMode);
			Log.d("COLORSHAFTED", "\t\tpositionShiftMode = " + positionShiftMode);
			Log.d("COLORSHAFTED", "\t\tpositionRepeat = " + positionRepeat);
			Log.d("COLORSHAFTED", "\t\tbhFrequency = " + bhCreateInterval);
			Log.d("COLORSHAFTED", "\t\tincomingBHDir = " + incomingBHDir);
		}
		
		//if we haven't yet initialized the direction of the next black hole, do so
		if(bhCreateInterval != -1)
			bhCountDown = bhCreateInterval;
	}
	
	/** Creates a new ColorBlock in a position and direction according to the current difficulty/creation 
	 * sequence */
	public ColorBlockE spawnColorBlock()
	{
		blocksCreated++;
		setNextConfig();
		bhCountDown--;
		if(bhCountDown == 0 )
		{
			bhCountDown = bhCreateInterval;
			nextBlackHoleDir();
		}
		
		int initColor = rand.nextInt(4),	//set the incoming color block randomly
			initShaft  = rand.nextInt(3);	//set the initial shaft randomly
		int initDir = ((initColor + colorConfig) % 4);		//set the initial direction relative to the color and configuration
	
		/** block we're about to propagate */
		ColorBlockE newBlock = null;
		//create the block and instantiate it as an enemy
		newBlock = gameScreen.eBlockFactory.getFactoryObject(initColor, initDir, initShaft);
		return newBlock;
	}
	
	/** create an enemy block with specific(non-random parameters) */
	public ColorBlockE spawnColorBlock(int initColor, int initDir, int initShaft)
	{
		ColorBlockE newBlock = gameScreen.eBlockFactory.getFactoryObject(initColor, initDir, initShaft);
		return newBlock;
	}

	public int nextBlackHoleDir()
	{
		//for now, we won't be using any dual directional black holes(so only direction 0-3).. too cheap...
		
		int nextBHDir = -1;
		switch(difficulty)
		{
			case 0:
			case 1:
				nextBHDir = -1;
				break;
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				nextBHDir = rand.nextInt(4);
				break;
			case 8:
			case 9:
				nextBHDir = rand.nextInt(4);
				break;
			case 10:
				nextBHDir = rand.nextInt(4);
				break;
		}
		return nextBHDir;
	}
	
	public void spawnNextObject()
	{
		//DETERMINE IF ITEMS ARE COMING IN.
		//if so, do not spawn other objects(break from method)
		boolean newLifeItem = false,
				newBombItem = false;
		
		newBombItem = determineNextBombItem();
		newLifeItem = determineNextLifeItem();
		if(newLifeItem)
		{
			gameScreen.lItemFactory.getFactoryObject(LevelItem.TYPE_XTRA_LIFE, 
					(int)(Math.random() * 4), (int)(Math.random() * 3),  (int)(Math.random() * 4));
			return;
		}
		else if(newBombItem)
		{
			gameScreen.lItemFactory.getFactoryObject(LevelItem.TYPE_XTRA_BOMB, 
					(int)(Math.random() * 4), (int)(Math.random() * 3),  (int)(Math.random() * 4));
			return;	
		}
		
		
		/* 1 - determine whether a black hole is coming in */
		if(bhCountDown > 0)
			bhCountDown--;
		
		//determine whether a black hole is coming in or not
		//take note that black holes never come in during any mode except ARCADE currently
		boolean blackHoleIncoming;
		if(GameStats.gameStyle == GameMode.ARCADE)
			blackHoleIncoming = (bhCountDown == 0);
		else
			blackHoleIncoming = false;
		
		/* 2 - determine whether to create a color block or not */
		if(!blackHoleIncoming)
		{
		/* 2a - create a color block */
			/* 2a1 - determine a new incoming block config orientation*/
			setNextConfig();
			/* 2a2 - spawn the incoming color block */
			blocksCreated++;		
			int initColor = ANY;
			do			//set the color block's shaft id to the next random one
			{
				shaftSpawnedAt = rand.nextInt(12);
				//retrieve the color from the color spawn map based on orientation and shaft id
				initColor = colorSpawnMap[spawnTargetPos][shaftSpawnedAt];
			} while(initColor == ANY);
			initColor += colorConfig; initColor %= 4;
			/** block we're about to propagate */
			ColorBlockE newBlock = null;
			
			switch(GameStats.gameStyle)
			{
				case ARCADE:
					//create the block and instantiate it as an enemy
					newBlock = gameScreen.eBlockFactory.getFactoryObject(shaftSpawnedAt, initColor);
					break;
				case PSYCHOUT:
					//generate random number from 0-100(representative of percentages to compare against)
					int randomCalc = (int)(101.0 * Math.random());
					
					//if we generated a number lower than the percent likely, then we know we're going to propagate a DS block
					giveDSBlock = (randomCalc <= percentDSLikely);
					Log.d("COLORSHAFTED", "randomCalc == " + randomCalc);
					Log.d("COLORSHAFTED", "percentDSLikely == " + percentDSLikely);
					
					if(giveDSBlock)
					{
						newBlock = gameScreen.eDSBlockFactory.getFactoryObject(shaftSpawnedAt, initColor);
					}
					else
					{
						//now we re-generate a random number, and compare it to see whether we're in the threshhold for a CT block
						randomCalc = (int)(101.0 * Math.random());
						giveCTBlock = (randomCalc < percentCTLikely);
						
						if(giveCTBlock)
							newBlock = gameScreen.eCTBlockFactory.getFactoryObject(shaftSpawnedAt, initColor);
						else
							newBlock = gameScreen.eBlockFactory.getFactoryObject(shaftSpawnedAt, initColor);
					}
					break;
			}
			
			/* 2a3 - if we're ready to start preparing for another black hole, do that! */
			if(bhCountDown == 3 && GameStats.gameStyle == GameMode.ARCADE)
			{
				incomingBHDir = nextBlackHoleDir();
			}
		}
		else
		{
		/*2b Create a black hole */
			bhCountDown = bhCreateInterval;
			//if only creating one black hole....
			if(incomingBHDir < 4 && bhCreateInterval != -1)
			{
				BlackHole blackHole = gameScreen.bHFactory.getFactoryObject(incomingBHDir);
				if(gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
					gameScreen.assets.getSound(GameScrAssets.SND_BLACKHOLE).play(1f);
			}
			else if(bhCreateInterval != -1)
			{
				BlackHole blackHole1, blackHole2;	//black holes to be instantiated
				switch(incomingBHDir )
				{
					case BH_OR_TOPLEFT:
						blackHole1 = gameScreen.bHFactory.getFactoryObject(BH_OR_LEFT);
						blackHole2 = gameScreen.bHFactory.getFactoryObject(BH_OR_TOP);
						if(gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
							gameScreen.assets.getSound(GameScrAssets.SND_BLACKHOLE).play(1f);
						break;
					case BH_OR_TOPRIGHT:
						blackHole1 = gameScreen.bHFactory.getFactoryObject(BH_OR_RIGHT);
						blackHole2 = gameScreen.bHFactory.getFactoryObject(BH_OR_TOP);
						if(gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
							gameScreen.assets.getSound(GameScrAssets.SND_BLACKHOLE).play(1f);
						break;
					case BH_OR_BOTTOMLEFT:
						blackHole1 = gameScreen.bHFactory.getFactoryObject(BH_OR_LEFT);
						blackHole2 = gameScreen.bHFactory.getFactoryObject(BH_OR_BOTTOM);
						if(gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
							gameScreen.assets.getSound(GameScrAssets.SND_BLACKHOLE).play(1f);
						break;
					case BH_OR_BOTTOMRIGHT:
						blackHole1 = gameScreen.bHFactory.getFactoryObject(BH_OR_RIGHT);
						blackHole2 = gameScreen.bHFactory.getFactoryObject(BH_OR_BOTTOM);
						if(gameScreen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
							gameScreen.assets.getSound(GameScrAssets.SND_BLACKHOLE).play(1f);
						break;
				}
			}
		}
	}
	
	public boolean gridOrientationValid(int gridOrientation, int blackHoleDir)
	{
		switch(blackHoleDir)
		{
			case -1:
				return true;
			case BH_OR_LEFT:
				if(gridOrientation == GRID_TOP_LEFT || 
				   gridOrientation == GRID_MID_LEFT || 
				   gridOrientation == GRID_BOT_LEFT || 
				   gridOrientation == GRID_TOP_CENTER ||
				   gridOrientation == GRID_MID_CENTER ||
				   gridOrientation == GRID_BOT_CENTER)
				{
					return false;
				}
				else
					return true;
			case BH_OR_RIGHT:
				if(gridOrientation == GRID_TOP_RIGHT || 
				   gridOrientation == GRID_MID_RIGHT || 
				   gridOrientation == GRID_BOT_RIGHT ||
				   gridOrientation == GRID_TOP_CENTER ||
				   gridOrientation == GRID_MID_CENTER ||
				   gridOrientation == GRID_BOT_CENTER)
				{
					return false;
				}
				else
					return true;
			case BH_OR_TOP:
				if(gridOrientation == GRID_TOP_LEFT   ||
				   gridOrientation == GRID_TOP_CENTER ||
				   gridOrientation == GRID_TOP_RIGHT  ||
				   gridOrientation == GRID_MID_LEFT   ||
				   gridOrientation == GRID_MID_CENTER || 
				   gridOrientation == GRID_MID_RIGHT)
				{
					return false;
				}
				else
					return true;
			case BH_OR_BOTTOM:
				if(gridOrientation == GRID_TOP_RIGHT || 
				   gridOrientation == GRID_TOP_CENTER || 
				   gridOrientation == GRID_TOP_RIGHT  ||
				   gridOrientation == GRID_MID_LEFT   ||
				   gridOrientation == GRID_MID_CENTER ||
				   gridOrientation == GRID_MID_RIGHT)
				{
					return false;
				}
				else
					return true;
			case BH_OR_TOPLEFT:
				break;
			case BH_OR_TOPRIGHT:
				break;
			case BH_OR_BOTTOMLEFT:
				break;
			case BH_OR_BOTTOMRIGHT:
				break;
		}
		return true;
	}

	/** determine what the next rotation and position orientation should be */
	public void setNextConfig()
	{
		//-------------------------------------------------------//
		//	TEST HOW MANY BLOCKS HAVE BEEN CREATED TO SWITCH SEQUENCE 
		//  AT GIVEN INTERVALS
		//-------------------------------------------------------//
		//check when the difficulty changes to alter the directional shift algorithm
		if(GameStats.difficulty != difficulty)
		{
			difficulty = GameStats.difficulty;	//update the difficulty buffer
			setUpDifficulty(difficulty);
		}
			
		//-------------------------------------------------------//
		// CHANGE THE COLOR ROTATION CONFIGURATION WHEN NECESSARY
		//-------------------------------------------------------//
		switch(rotateShiftMode)
		{
			case ONE_SHIFT_UNI:
				colorConfig = (directionShift == 1 ) ? colorConfig - 1 : colorConfig + 1;
				break;
			case ONE_SHIFT_BI:
				directionShift = (rand.nextBoolean() ? 1 : -1);
				colorConfig   += directionShift;	//shift the configuration of colors
				break;
			case TWO_SHIFT:
				directionShift = (rand.nextBoolean() ? 1 : -1);
				colorConfig   += (directionShift * rand.nextInt(2));
				break;
			case RANDOM:
				colorConfig += (directionShift * rand.nextInt(4));
				break;
		}
		if(this.repeatCountdown > 0)
		{
			repeatCountdown -= 1;
		}
		else if(repeatCountdown <= 0)
		{
			repeatCountdown = positionRepeat - 1;
			boolean shiftWasValid = false;
			int shiftTo = -1;
			//determine how the next position will be shifted
			switch(positionShiftMode)
			{
				//shift once randomly
				case ONE_SHIFT_RANDOM:
					//1 -- select a random direction
					do
					{
						shiftTo = -1;
						shiftWasValid = false;
						int directionShifted = rand.nextInt(4);
						switch(directionShifted)
						{
							case 0:	//upwards
								shiftWasValid = (spawnTargetPos > 2);
								shiftTo 	  = spawnTargetPos - 3;
								break;
							case 1:	//downwards
								shiftWasValid = (spawnTargetPos < 6);
								shiftTo = spawnTargetPos + 3;
								break;
							case 2:	//left
								shiftWasValid = (spawnTargetPos != 0 && spawnTargetPos != 3 && spawnTargetPos != 6);
								shiftTo = spawnTargetPos - 1;
								break;
							case 3:	//right
								shiftWasValid = (spawnTargetPos != 2 && spawnTargetPos != 5 && spawnTargetPos != 8);
								shiftTo = spawnTargetPos + 1;
								break;
						}						
					}
					while(!shiftWasValid);
					//after determining whether the shift made was valid, double check that the shift
					//is not going to be put somewhere a black hole 
					
					//go to a non-black hole spot as a last resort
					while(!gridOrientationValid(shiftTo, incomingBHDir))
					{
						shiftTo += 1;
						shiftTo %= 9;
					}
					spawnTargetPos = shiftTo;
					break;
				case TWO_SHIFT_RANDOM:
					shiftTo = -1;
					shiftWasValid = false;
					/**whether to shift one or two places */
					int shiftCount = rand.nextInt(2) + 1;
					int initialPos = spawnTargetPos;
					for(int i = 0; i < shiftCount; i++)
					{
						do
						{
							shiftTo = -1;
							shiftWasValid = false;
							int directionShifted = rand.nextInt(4);
							switch(directionShifted)
							{
								case 0:	//upwards
									shiftWasValid = (spawnTargetPos > 2);
									shiftTo 	  = spawnTargetPos - 3;
									break;
								case 1:	//downwards
									shiftWasValid = (spawnTargetPos < 6);
									shiftTo = spawnTargetPos + 3;
									break;
								case 2:	//left
									shiftWasValid = (spawnTargetPos != 0 && spawnTargetPos != 3 && spawnTargetPos != 6);
									shiftTo = spawnTargetPos - 1;
									break;
								case 3:	//right
									shiftWasValid = (spawnTargetPos != 2 && spawnTargetPos != 5 && spawnTargetPos != 8);
									shiftTo = spawnTargetPos + 1;
									break;
							}
						} while(!shiftWasValid || shiftTo == initialPos);
						
						//go to a non-black hole spot as a last resort
						while(!gridOrientationValid(shiftTo, incomingBHDir))
						{
							shiftTo += 1;
							shiftTo %= 9;
						}
						spawnTargetPos = shiftTo;
					}
					break;
				case RANDOM:
					do   {shiftTo = rand.nextInt(9);} 
					while(shiftTo == spawnTargetPos);
					spawnTargetPos = shiftTo;
					break;
				}
			}
		//keep the initial configuration in bounds
		while(colorConfig > 3)
			colorConfig -= 4;
		while(colorConfig < 0)
			colorConfig += 4;
	}

	public boolean determineNextBombItem()
	{
		boolean bombItemComing = false;
		
		int frequencyOfIncoming;
		
		switch(GameStats.difficulty)
		{
			case 0:
			case 1:
			case 2:
				frequencyOfIncoming = 25;
			case 3:
				frequencyOfIncoming = 32;
				break;
			case 4:
				frequencyOfIncoming = 24;
				break;
			case 5:
				frequencyOfIncoming = 16;
			case 6:
				frequencyOfIncoming = 24;
				break;
			case 7:
				frequencyOfIncoming = 32;
				break;
			case 8:
				frequencyOfIncoming = 44;
				break;
			case 9:
				frequencyOfIncoming = 62;
				break;
			case 10:
			default:
				frequencyOfIncoming = 62;
				break;

		}
		
		int threshHoldCreator = (int)(Math.random() * 1001.0);
		if(threshHoldCreator <= frequencyOfIncoming)
			return true;
		else
			return false;
	}
	
	public boolean determineNextLifeItem()
	{
		boolean bombItemComing = false;
		
		int frequencyOfIncoming;
		
		switch(GameStats.lives)
		{
			case 5:
			default:
				frequencyOfIncoming = (int)((GameStats.difficulty / 5.0) * 10.0);
				break;
			case 4:
				frequencyOfIncoming = (int)((GameStats.difficulty / 5.0) * 10.0);
				break;
			case 3:
				frequencyOfIncoming = (int)(((GameStats.difficulty / 4.0) + 0.25) * 10.0);
				break;
			case 2:
				frequencyOfIncoming = (int)(((GameStats.difficulty / 4.0) + 0.35) * 10.0);
				break;
			case 1:
				frequencyOfIncoming = (int)(((GameStats.difficulty / 3.5) + 0.5) * 10.0);
				break;
		}
		
		int threshHoldCreator = (int)(Math.random() * 1001.0);
		if(threshHoldCreator <= frequencyOfIncoming)
			return true;
		else
			return false;
	}
}