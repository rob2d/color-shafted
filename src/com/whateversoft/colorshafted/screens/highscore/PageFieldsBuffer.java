package com.whateversoft.colorshafted.screens.highscore;

import java.util.Random;

import android.util.Log;

import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.database.ScoreEntry;
import com.whateversoft.colorshafted.screens.HighScoreScr;


/** Visually represents*/
public class PageFieldsBuffer
{
	//---------------------------------------//
	//=======>   Reference Fields   <========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	HighScoreScr screen;
	
	//---------------------------------------//
	//=======>    Member Fields  	<========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	/** page that this buffer currently represents; 0 - LEFT, 1 - SHOWN, 2 - RIGHT */
	public int bufferIndex;
	/** for scrolling to transition screens! */
	public int xOffset;
	/**entries which will be displayed on screen */
	OnScreenEntry[] entriesShown;
	
	/** notes whether our rank is on this page buffer. If so, it will be listed in 0-index.
	 *  otherwise, we set this attribute to -1. -1 represents a flag that which states don't have 
	 *  to worry about tracking a rank of ours														*/
	int ourRank = -1;
	
	public PageFieldsBuffer(int sIndex, HighScoreScr scr)
	{
		//update references
		screen 		 = scr;
		bufferIndex = sIndex;
		entriesShown = new OnScreenEntry[screen.SCORES_ON_SCREEN];
		
		//instantiate new entries
		Random rand = new Random();
		for(int i = 0; i < entriesShown.length; i++)
			entriesShown[i] = new OnScreenEntry(
					new ScoreEntry("", 100, 0, 100, System.currentTimeMillis(), rand.nextInt(2)), 
						i, i, GameMode.ARCADE, this);
	}
	
	/** set the visible on screen entry to specified score entries */
	public void setScoreEntries(int scrollIndex, ScoreEntry[] entries, int ourRank)
	{
		Log.d("COLORSHAFTED", "pageBuffers[" + bufferIndex + "] is calling setScoreEntries to an array of size " + entries.length);
		
		for(int i = 0; i < entriesShown.length; i++)
		{
			if(entries.length >= i)	//if entry does exist
			{
				Log.d("COLORSHAFTED", "added an entry that was supposedly not null and its name contained " + entries[i]);
				entriesShown[i].setEntry(i + scrollIndex, entries[i]);
			}
			else
			{
				//if entry doesn't exist
				entriesShown[i].setEntry(i + scrollIndex, null);
			}
			
			//flag this entry if it is ours
			entriesShown[i].setAsOurScore(ourRank == scrollIndex + i);
		}
	}
	
	public void setXOffset(int offset)
	{
		xOffset = offset;
		
		for(OnScreenEntry oSE : entriesShown)
		{
			oSE.positionElements();
		}
	}
	
	public int getXOffset()
	{
		return xOffset;
	}
	
	/** return which page on screen this buffer represents(0 - LEFT, 1 - SHOWN, 2 - RIGHT) */
	public int getBufferIndex()
	{
		return bufferIndex;
	}
}