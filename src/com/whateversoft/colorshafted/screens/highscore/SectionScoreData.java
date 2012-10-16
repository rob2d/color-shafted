package com.whateversoft.colorshafted.screens.highscore;

import java.util.ArrayList;
import java.util.List;

import com.whateversoft.colorshafted.database.ScoreEntry;

/** Contains high score data for each section in a high score list; also storing navigation information
 *  such as scroll index */
public class SectionScoreData
{
	//---------------------------------------//
	//=======>    Member Fields  	<========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	/** whether we have retrieved any data yet for this data section.<br>
	 *  this is <i>critically</i> important for not redundantly checking all data */
	boolean firstDataFetched = false;
	
	/** list of entries in this section scores(if online, it will just collect what has been stored so far */
	ArrayList<ScoreEntry> scoreEntries;
	
	/** the index of the current section as it was scrolled using the High Score Screen UI */
	int scrollIndex = 0;
	
	/** the number of scores that must be loaded for this section to be completed */
	int resultTotalCount = 0;
	
	//---------------------------------------//
	//=======>     ClassMethods	    <========//
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	
	//===========| Constructors |============//
	
	public SectionScoreData()
	{
		scoreEntries = new ArrayList<ScoreEntry>();
	}
	
	/** Constructor -> set our entries to a certain set at start
	 *  @param entries entries we are using to initialize section's data */
	public SectionScoreData(ArrayList<ScoreEntry> entries)
	{
		scoreEntries = entries;
	}
	
	/** Constructor -> set our entries to a certain set at start
	 *  @param entries entries we are using to initialize section's data */
	public SectionScoreData(ScoreEntry[] entries)
	{
		appendScores(entries);
	}

	/** clear all high score entries from this section */
	public void clearData()
	{
		scoreEntries.clear();
		scrollIndex = 0;
	}
	
	//===========| Mutators |============//
	
	/** append scores; used for updates from the server side for online views */
	public void appendScores(ScoreEntry[] scores)
	{
		if(!firstDataFetched)		//update whether or not we've gotten section data so far
			firstDataFetched = true;
		
		/** ensure that our score entries is a valid list */
		if(scoreEntries == null)
			scoreEntries = new ArrayList<ScoreEntry>();
		
		//add all required score entries
		for(ScoreEntry sE : scores)
			scoreEntries.add(sE);
	}
	
	public void setScores(ScoreEntry[] scores)
	{
		if(!firstDataFetched)		//update whether or not we've gotten section data so far
			firstDataFetched = true;
		
		scoreEntries.clear();
		
		for(ScoreEntry sE : scores)
			scoreEntries.add(sE);
	}
	
	/** set scores to a new list of scores */
	public void setScores(ArrayList<ScoreEntry> list)
	{
		if(!firstDataFetched)		//update whether or not we've gotten section data so far
			firstDataFetched = true;
		
		//clear data
		scoreEntries.clear();
		//append new data
		for(ScoreEntry sE : list)
			scoreEntries.add(sE);
	}
	
	/** set current index view scroll of section */
	public boolean setScrollIndex(int index)
	{
		if(index >= 0 && index < scoreEntries.size())
		{
			scrollIndex = index;
			return true;
		}
		else
			return false;
	}
	
	//===========| Accessors |============//
	/** get score entries for this section */
	public ArrayList<ScoreEntry> getScores()
	{
		return scoreEntries;
	}
	
	/** get current index view scroll of section */
	public int getScrollIndex()
	{
		return scrollIndex;
	}
	
	/** whether data for this section was fetched or not
	 *  (used as a flag when pulling things online) */
	public boolean wasDataFetched()
	{
		return firstDataFetched;
	}
	
	/** set the eventual score count for this section(used for online buffering) */
	public void setResultTotalCount(int count)
	{
		resultTotalCount = count;
	}
	
	public int getResultTotalCount()
	{
		return resultTotalCount;
	}
}