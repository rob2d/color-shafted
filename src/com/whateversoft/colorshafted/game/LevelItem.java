package com.whateversoft.colorshafted.game;

import android.graphics.Rect;
import android.util.Log;

import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.colorshafted.screens.GameScr;
import com.whateversoft.colorshafted.screens.GameScrAssets;

/** an item flying by the screen to be collected by the player. */
public class LevelItem extends ImageEntity implements Collidable
{
	/** possible colors for the item */
	public static final int RED    = 0, 
							GREEN  = 1, 
							YELLOW = 2, 
							BLUE   = 3;
	
	/** possible type for the item */
	public static final int TYPE_XTRA_BOMB = 0,
							TYPE_XTRA_LIFE = 1;
	
	/** for quick reference to the game screen */
	final GameScr gameScreen;
	/** whether the item is a bomb or an extra life */
	public int  	type;
	/** what color the item is */
	public int  	color;
	/** determines what frame the item is flashing at in an animation sequence(since we don't wish to create
	 *  additional Animation Object resources in game this is much more efficient) */
	int		animFrameSequencer = 0;
	public final static int ANIM_FRAME_COUNT = 6;
	/** whether or not the item has been obtained by the player yet */
	boolean obtained;
	
	float speed;
	
	public LevelItem(GameScr scr)
	{
		super(GameScr.LAYER_ENTITIES_FRONT, scr);
		gameScreen = scr;
	}
	
	
	public void instantiateItem(int itemType, int color, int initShaft, int initDir)
	{
		//-----------------------------------------------//
		//	INITIALIZE THE STARTING MOVEMENT AND POSITIONS
		//-----------------------------------------------//
		speed = (2.5f + GameStats.difficulty * 0.24f);
		
		switch(initDir)
		{
		case 0:	//going down
		{
			x =  gameScreen.gameCanvasX(GameScr.GRID_LEFT + (GameScr.GRID_SQ_SIZE / 2) + (int)(GameScr.GRID_SQ_SIZE * initShaft));
			y =  0;
			dx = 0;
			dy = (float)speed;
		}
			break;
		case 1:	//going up
			x = gameScreen.gameCanvasX(GameScr.GRID_LEFT + (GameScr.GRID_SQ_SIZE / 2) + (int)(GameScr.GRID_SQ_SIZE * initShaft));
			y = 480;
			dx = 0;
			dy = (float)-speed;
			break;
		case 2:	//going right
			x = gameScreen.gameCanvasX(0);
			y = GameScr.GRID_TOP + (GameScr.GRID_SQ_SIZE / 2) + GameScr.GRID_SQ_SIZE * initShaft;
			dx = (float)speed;
			dy = 0;
			break;
		case 3:	//going left
			x = gameScreen.gameCanvasX(GameScr.GAME_CANVAS_WIDTH);
			y = GameScr.GRID_TOP + (GameScr.GRID_SQ_SIZE / 2) + GameScr.GRID_SQ_SIZE * initShaft;
			dx = (float)-speed;
			dy = 0;
			break;
		default:
			break;
		}
		
		type = itemType;
		this.color = color;
		updateImage();
		instantiateItem();
		Log.d("ITEMDEBUG", "Supposedly created an item with item type of " + this.type + " and a color of " + this.color + ". It is at (" + x + ", " + y + ")");
	}
	
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		animFrameSequencer++;
		animFrameSequencer %= ANIM_FRAME_COUNT;
		updateImage();
		
	}
	
	/** after defining the instantiated item's color, shaft, dir, we call this overriden method
	 *  to make the item able to interact with the game
	 */
	public void instantiateItem()
	{
		visible   = true;
		destroyed = false;
		//-----------------------------------------------//
		//	ADD THIS COLORBLOCK TO THE LIST OF ITEMS	 
		//-----------------------------------------------//
		synchronized(gameScreen.eBlocks)
		{
			gameScreen.lItems.add(this);
		}
	}

	/** updates the item with its current visible frame of animation */
	public void updateImage()
	{
		//the sequence for the animation of items is
		// (0)ITEM, (1)COLOR, (2)ITEM, (3)WHITE (4)ITEM, (5)COLOR **repeat**
		
		if(animFrameSequencer == 0 || animFrameSequencer == 2 || animFrameSequencer == 4)
		switch(type)
		{
			case TYPE_XTRA_BOMB:
				switch(color)
				{
					case RED:    imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XB_R); break;
					case BLUE:   imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XB_B); break;
					case YELLOW: imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XB_Y); break;
					case GREEN:  imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XB_G); break;
				}
				break;
			case TYPE_XTRA_LIFE:
				switch(color)
				{
					case RED:	  imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XL_R); break;
					case BLUE:	  imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XL_B); break;
					case YELLOW:  imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XL_Y); break;
					case GREEN:	  imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_ITEM_XL_G); break;
				}
				break;
		}
		else if(animFrameSequencer == 1 || animFrameSequencer == 5)
		{
			switch(color)
			{
				case RED:		imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_BLOCK_R); break;
				case BLUE:		imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_BLOCK_B); break;
				case GREEN:		imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_BLOCK_G); break;
				case YELLOW:	imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_BLOCK_Y); break;
			}
		}
		else if(animFrameSequencer == 3)
		{
			imgFrame = gameScreen.assets.getImage(GameScrAssets.IMG_BLOCK_FLASH);
		}
	}
	
	@Override
	public Rect getCollidable()
	{
		return new Rect((int)x - 15, (int)y - 15, (int)x + 16, (int)y + 16);
	}

	@Override
	public boolean collidesWith(Collidable col)
	{
		//collisions can only be registered if the object is alive!
		if(!destroyed)
			return (getCollidable().intersect(col.getCollidable()));
		else
			return false;
	}
}
