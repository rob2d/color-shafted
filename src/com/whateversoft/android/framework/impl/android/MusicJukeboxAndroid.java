package com.whateversoft.android.framework.impl.android;

import java.util.Random;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.whateversoft.android.framework.MusicJukebox;
import com.whateversoft.colorshafted.constants.CSSettings;

/** Represent's an application's music playlist.<br>
 *  The playlist can have specified songs disabled<br>
 *  Playmodes supported are <b>PLAY_SINGLE</b>, <b>LOOP_SINGLE</b>, <b>LOOP_ALL</b>, <b>SHUFFLE</b><br>
 *  The playlist utilizes a JemsMusicPlayer to play/stop songs */
public class MusicJukeboxAndroid implements MusicJukebox
{
	public CanvasGame game;
	
	Random rand = new Random();
	
	public PlayMode playMode;
	
	private MusicHandlerAndroid musicPlayer = null;
	/** list of resources from the R class to be used for music tracks */
	private int[] 	  trackResourceList;
	/** which track is enabled */
	private boolean[] trackEnabledList;
	private int currentTrackNo;
	
	//define all on-Completion listeners for the different play modes!
	OnCompletionListener loopSingleListener =
			new OnCompletionListener()
		{
			@Override
			public void onCompletion(MediaPlayer arg0)
			{
				musicPlayer.play();
			}
		};
	
	/** constructor to create a playlist. If a song isn't declared to be initially in the playlist, 
	 *  we must later add it and set the initial track */
	public MusicJukeboxAndroid(CanvasGame context, PlayMode pM, int... tR)
	{
		//set track-able Context class
		game = context;
		
		//initialize the music player
		musicPlayer = new MusicHandlerAndroid(context);
		
		setPlayMode(pM);
		setTrackResources(tR);
	}
	
	public void setPlayMode(PlayMode pM)
	{
		if(musicPlayer.isPlaying())	//if it's already playing, stop, change then re-play after setting new play mode
		{
			stop();
			playMode = pM;
			play();
		}
		else playMode = pM;			//otherwise, just set new playmode
	}
	
	@Override
	public void setTrackAsEnabled(int track, boolean enabled)
	{
		if(trackResourceList.length > 0 && track >= 0 && track <= trackResourceList.length)
			trackEnabledList[track] =  enabled;
		//go to an available track
		currentTrackNo -=1;
		currentTrackNo = nextTrack();
	}
	
	/** set the track resources */
	public void setTrackResources(int... tR)
	{
		releaseMusic();				//release music if it's loaded
		trackResourceList = tR;
		trackEnabledList  = new boolean[tR.length];	//set which tracks are enabled
		for(int i = 0; i < trackEnabledList.length; i++)
			trackEnabledList[i] = true;
		
		//set current track to the first/load it's resource id	
		if(trackResourceList.length > 0)
		{
			if(playMode != PlayMode.SHUFFLE)
				setTrack(0);
			else
				setTrack(nextRandomTrack());
		}
	}
	
	public void activityPauseCycle(boolean pause)
	{
		musicPlayer.activityPauseCycle(pause);
	}
	
	/** Begin playing the given track list */
	public void play()
	{
		if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_MUS, CSSettings.DEFAULT_ENABLE_MUS) && 
		   musicPlayer != null)
		{
			
		switch(playMode)
		{
			case NO_MUSIC:
				musicPlayer.release();
				break;
			case PLAY_SINGLE: 
				musicPlayer.play();
				musicPlayer.setResource(trackResourceList[currentTrackNo]);
				musicPlayer.getMediaPlaying().setOnCompletionListener(new OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						stop();
					}
				});
				break;
			case LOOP_SINGLE:
			{
				musicPlayer.play();
				musicPlayer.getMediaPlaying().setOnCompletionListener(new OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						setTrack(currentTrackNo);
						play();	
					}
				});
			}
				break;
			case LOOP_ALL:
				musicPlayer.setResource(trackResourceList[currentTrackNo]);
				musicPlayer.play();
				musicPlayer.getMediaPlaying().setOnCompletionListener(new OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						musicPlayer.stop();
						setTrack(nextTrack());
						play();
					}
				});
				break;
			case SHUFFLE:
				currentTrackNo = nextRandomTrack();
				musicPlayer.setResource(trackResourceList[currentTrackNo]);
				musicPlayer.play();
				musicPlayer.getMediaPlaying().setOnCompletionListener(new OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						musicPlayer.stop();
						setTrack(nextRandomTrack());
						play();
					}
				});
				break;
		}
		}
	}
	
	public void stop()
	{
		musicPlayer.stop();
	}
	
	/** get the index of the next available track */
	public int nextTrack()
	{
		int nextTrackNo = (currentTrackNo + 1 >= (trackResourceList.length) ? 0 : currentTrackNo + 1);
		if(playMode != PlayMode.NO_MUSIC)				//break out of infinite loop error searching for a valid track!
			while(!trackEnabledList[nextTrackNo])		//find next available track while the current is not available
			{	
				nextTrackNo++;
				if(nextTrackNo >= trackResourceList.length)
					nextTrackNo = 0;
			}
		
		return nextTrackNo;
	}
	
	/** get index of next available random track */
	public int nextRandomTrack()
	{
		int randomTrack = 0;
		if(playMode != PlayMode.NO_MUSIC)
		{
			do
			{ randomTrack = (int)(rand.nextDouble() * trackResourceList.length); }   
			while(!trackEnabledList[randomTrack]);
		}

		return randomTrack;
	}
	
	/** try to set the current playing track index to the one specified */
	public boolean setTrack(int trackIndex)
	{
		if(trackIndex >= 0 && trackIndex < trackResourceList.length)
		{
			currentTrackNo = trackIndex;
			musicPlayer.setResource(trackResourceList[currentTrackNo]);
			return true;
		}
		else return false;
	}
	
	/** release music player and let the garbage collector clear it from RAM */
	public void releaseMusic()
	{
		musicPlayer.release();
	}
}
