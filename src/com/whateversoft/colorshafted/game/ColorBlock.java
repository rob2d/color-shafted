package com.whateversoft.colorshafted.game;

import android.graphics.Rect;

import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.ScreenAssets;
import com.whateversoft.colorshafted.screens.GameScr;
import com.whateversoft.colorshafted.screens.GameScrAssets;

import static com.whateversoft.colorshafted.screens.GameScrAssets.*;

/** a color block in the game, either subclassed as an enemy or a player 
 *  Copyright 2011 Robert Concepcion III */
public class ColorBlock extends ImageEntity implements Collidable
{
	/** possible colors for the color block */
	public static final int RED    = 0, 
							GREEN  = 1, 
							YELLOW = 2, 
							BLUE   = 3;
	
	/** color of the block(0 - RED, 1 - GREEN, 2 - YELLOW, 3 - BLUE) */
	public int color;
	
	/** counts down the block flashing white(used for successful collisions with a player block color) */
	public int flashTimer = 0;
	
	/** how much the block appears saturated; at level 4, the block is completely saturated and at 0, it is desaturated */
	int	   saturationLevel = 4;
	
	public GameScr	gameScreen;
	
	/** constructor to create a default color block. set color to -1 if you do not want to attach a graphic to it yet) */
	public ColorBlock(int c, int xInit, int yInit, int layer, GameScr s)
	{
		super(layer, s);
		//-----------------------------------------//
		//	SAVE INITIAL VALUES/REFERENCES
		//-----------------------------------------//
		color = c;
		x = xInit;
		y = yInit;
		gameScreen = s;
		
		//-----------------------------------------//
		//	CREATE THE SPRITE BASED ON COLOR, IF APPLICABLE!
		//-----------------------------------------//
		if(color != -1)
			setColorAndGraphic(c);
		else
			setLogicalColor(c);
	}
	
	/** set the logical color of the block... this is used to check for color collisions
	 *  and whether they match. This is different from setting the graphic which only changes
	 *  a color's graphic appearance
	 * @param c
	 */
	public void setLogicalColor(int c)
	{
		color = c;
	}
	
	public void setGraphicColor(int c)
	{
		ScreenAssets assets = screen.assets;
		switch(c)
		{
			case RED: 	 imgFrame = assets.getImage(IMG_BLOCK_R); 	break;
			case YELLOW: imgFrame = assets.getImage(IMG_BLOCK_Y); 	break;
			case GREEN:  imgFrame = assets.getImage(IMG_BLOCK_G);	break;
			case BLUE:   imgFrame = assets.getImage(IMG_BLOCK_B);	break;
		}
	}
	
	/** set the graphic representation of the block to one on-screen.
	 *  used to conveniently set images for color transformations
	 * @param asset
	 */
	public void setGraphicAsset(int asset)
	{
		ScreenAssets assets = screen.assets;
		imgFrame = assets.getImage(asset);
	}
	
	public void setColorAndGraphic(int c)
	{
		setLogicalColor(c);
		setGraphicColor(c);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);	//make sure to update dx/dy
		//if block is flashing, set image explicitly to flashing block and decrement timer
		if(flashTimer > 0)
		{
			imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_FLASH);
			flashTimer--;
			if(flashTimer == 0)	//if timer is ready to reset, restore proper image
			{
				switch(color)
				{
					case RED: 	 imgFrame = screen.assets.getImage(IMG_BLOCK_R); 	break;
					case YELLOW: imgFrame = screen.assets.getImage(IMG_BLOCK_Y); break;
					case GREEN:  imgFrame = screen.assets.getImage(IMG_BLOCK_G);	break;
					case BLUE:   imgFrame = screen.assets.getImage(IMG_BLOCK_B);	break;
				}
			}
		}
	}

	@Override
	public Rect getCollidable()
	{
		return new Rect((int)x - 15, (int)y - 15, (int)x + 16, (int)y + 16);
	}

	public boolean collidesWith(Collidable col)
	{
		//collisions can only be registered if the object is alive!
		if(!destroyed)
			return (getCollidable().intersect(col.getCollidable()));
		else
			return false;
	}
	
	public void setDesatValue(int desatVal)
	{
		if(this.flashTimer == 0)
		{
			if(desatVal != saturationLevel)
			{
				switch(color)
				{
					case BLUE:
						switch(desatVal)
						{
							case 0: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_B);     break;
							case 1: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_B_DS, 0); break;
							case 2: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_B_DS, 1); break;
							case 3: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_B_DS, 2); break;
							case 4: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_DS); 	     break;
						}
						break;
					case RED:
						switch(desatVal)
						{
							case 0: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_R);     break;
							case 1: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_R_DS, 0); break;
							case 2: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_R_DS, 1); break;
							case 3: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_R_DS, 2); break;
							case 4: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_DS); 	     break;
						}
						break;
					case GREEN:
						switch(desatVal)
						{
							case 0: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_G);     			break;
							case 1: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_G_DS, 0); break;
							case 2: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_G_DS, 1); break;
							case 3: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_G_DS, 2); break;
							case 4: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_DS); 	     		break;
						}
						break;
					case YELLOW:
						switch(desatVal)
						{
							case 0: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_Y);     			break;
							case 1: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_Y_DS, 0); break;
							case 2: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_Y_DS, 1); break;
							case 3: imgFrame = screen.assets.getImageInArray(GameScrAssets.IMGA_BLOCK_Y_DS, 2); break;
							case 4: imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_DS); 	     		break;
						}
						break;
				}
				saturationLevel = desatVal;
			}
		}
		else		//if the color block should be a flashing block, we adjust it accordingly
		{
			imgFrame = screen.assets.getImage(GameScrAssets.IMG_BLOCK_FLASH);
		}
	}
}
