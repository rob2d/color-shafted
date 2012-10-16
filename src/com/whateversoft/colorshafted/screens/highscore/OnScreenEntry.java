package com.whateversoft.colorshafted.screens.highscore;

import android.graphics.Color;
import android.graphics.Paint;

import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.android.framework.TextEntity;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.database.ScoreEntry;
import com.whateversoft.colorshafted.screens.HighScoreScr;

import static com.whateversoft.colorshafted.screens.HighScoreAssets.FONT_SFTELEVISED;
import static com.whateversoft.colorshafted.screens.HighScoreScr.PAGE_SHOWN;


/** Visual representation of high score entries displayed on screen;
 *  contains all ImageEntities associated */	
public class OnScreenEntry
{
	public static final int COLUMN_RANK		= 0;
	public static final int COLUMN_NAME 	= 1;
	public static final int	COLUMN_SCORE	= 2;
	public static final int COLUMN_PLAYTIME = 3;
	public static final int COLUMN_DATE		= 4;
	
	public final int		FONT_SIZE 		= 20,
							LAYER		    = 01;
	final PageFieldsBuffer  pageBuffer;
	final HighScoreScr 		screen;
	final int 				sectionW;
	final int			    screenWidth;
	final int				yPosition;
	
	/** mode which this entry is found under */
	final GameMode onMode;
	
	/** column division multiple of each of the columns positions */
	public final int[]		xSections = {3, 4, 10, 15, 22};
	
	/** whether or not this score is ours for highlighting */
	public boolean isOurScore = false;
	
	/** the different columns which are all represented as visual text entities 
	 *  for this score entry */
	public TextEntity[] fields = new TextEntity[5];
	
	public OnScreenEntry(ScoreEntry entry, int rankNumber, int entryNumber, GameMode m, PageFieldsBuffer pFB)
	{
		pageBuffer = pFB;
		screen = pageBuffer.screen;
		screenWidth = ScreenInfo.virtualWidth;
		yPosition = 180 + entryNumber * 40;
		sectionW	= screenWidth / 24;
		onMode = m;
					
		setEntry(rankNumber, entry);
	}
		
	/** set our entry to an associated rank listed and provide the data fields necessary */
	public void setEntry(int rankListed, ScoreEntry entry)
	{
		if(entry == null)
		{
			fields[COLUMN_RANK].string.setLength(0);
			fields[COLUMN_NAME].string.setLength(0);
			fields[COLUMN_SCORE].string.setLength(0);
			fields[COLUMN_PLAYTIME].string.setLength(0);
			fields[COLUMN_DATE].string.setLength(0);
		}
		else
		{
			if(fields[COLUMN_RANK] == null)
			{
				fields[COLUMN_RANK] = new TextEntity((sectionW/24) * 2, yPosition, new StringBuffer((rankListed+1) + "."),
						Color.rgb(255, 255, 255), screen.assets.getFont(FONT_SFTELEVISED), 
						FONT_SIZE, Paint.Align.RIGHT, LAYER, screen);
			}
			else
			{
				fields[COLUMN_RANK].string.setLength(0);
				fields[COLUMN_RANK].string.append((rankListed+1) + ".");
			}
				
			// initialize all columns to match screen data
			if(fields[COLUMN_NAME] == null)
			{
				fields[COLUMN_NAME] = new TextEntity((sectionW/24) * 3, yPosition, new StringBuffer(entry.getName() + ""),
												Color.rgb(255, 255, 255), screen.assets.getFont(FONT_SFTELEVISED), 
												FONT_SIZE, Paint.Align.LEFT, LAYER, screen);
			}
			else
			{
				fields[COLUMN_NAME].string.setLength(0); 
				fields[COLUMN_NAME].string.append(entry.getName());
			}
				
			if(fields[COLUMN_SCORE] == null)
			{
				fields[COLUMN_SCORE] = new TextEntity((sectionW / 24) * 7, yPosition, new StringBuffer(entry.getFormattedScore() + ""),
											Color.rgb(255, 255, 255), screen.assets.getFont(FONT_SFTELEVISED), 
											FONT_SIZE, Paint.Align.RIGHT, LAYER, screen);
			}
			else
			{
				fields[COLUMN_SCORE].string.setLength(0);
				fields[COLUMN_SCORE].string.append(entry.getScore());
			}
			
			if(fields[COLUMN_PLAYTIME] == null)
			{
				fields[COLUMN_PLAYTIME] = new TextEntity(sectionW * 15, yPosition, new StringBuffer(entry.getFormattedPlayTime() + ""),
											Color.rgb(255, 255, 255), screen.assets.getFont(FONT_SFTELEVISED), 
											FONT_SIZE, Paint.Align.RIGHT, LAYER, screen);
			}
			else
			{
				fields[COLUMN_PLAYTIME].string.setLength(0);
				fields[COLUMN_PLAYTIME].string.append(entry.getFormattedPlayTime());
			}
				
			if(fields[COLUMN_DATE] == null)
			{
				fields[COLUMN_DATE] = new TextEntity(sectionW * 20, yPosition, new StringBuffer(entry.getFormattedDate() + ""),
											Color.rgb(255, 255, 255), screen.assets.getFont(FONT_SFTELEVISED), 
											FONT_SIZE, Paint.Align.RIGHT, LAYER, screen); 
			}
			else
			{
				fields[COLUMN_DATE].string.setLength(0);
				fields[COLUMN_DATE].string.append(entry.getFormattedDate());
			}
		}
	}
		
	/** update position relative to our finger swipe */
	public void positionElements()
	{
		for(int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++)
		{
			//position each field relative to the page buffer its on
			fields[fieldIndex].x   = (float)(sectionW * xSections[fieldIndex]) + pageBuffer.getXOffset();
			{	
				int alphaValue =  pageBuffer.getBufferIndex() == PAGE_SHOWN ? 
										(int)((1 - Math.abs(screen.sliderXTxt)) * 255) :
										(int)((Math.abs(screen.sliderXTxt)) * 255);
				if(isOurScore)
					fields[fieldIndex].color = Color.argb(alphaValue, 255, 255, 0);
				else
					fields[fieldIndex].color = Color.argb(alphaValue, 255, 255, 255);
			}
		}
	}
	
	/** this method is used to determing if the entry is appropriately ours(true),
	 *  and this data is used to update the color highlight while positioning */
	public void setAsOurScore(boolean ourScore)
	{
		isOurScore = ourScore;
	}
}
