package com.whateversoft.android.framework.impl.android;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;

/** encapsulates a media player and adds functionality.<br>
 *  we do not use a subclass because we do not want to concern ourselves with the intricate details of the mediaplayer's
 *  factory instantiation using the static "create(context, resid)" method.
 * @author Rob */
public class MusicHandlerAndroid
{
	public static int	NO_MUSIC_LOADED = -1;
	private MediaPlayer mediaPlayer;
	private boolean 	isPaused 	  = false;
	private boolean		isPlaying	  = false;
	private Context 	context;
	private int  		seekPosition = -1;
	private int			resourceId	 = -1;
	private boolean		loopingEnabled = false;

	/** Constructor which takes the calling Android activity as a possible parameter used to create the Media Player later */
	public MusicHandlerAndroid(Context c)
	{
		context = c;
	}
	
	public void setResource(int resid)
	{
		resourceId = resid;
	}
	
	/** start the current music track if one is available, otherwise return false */
	public boolean play()
	{
		if(isFileLoaded())
		{
			if(mediaPlayer != null && mediaPlayer.isPlaying())
				stop();	//we will assume the media being played will restart!
			{
				isPlaying = true;
				mediaPlayer = MediaPlayer.create(context, resourceId);
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						if(loopingEnabled)
							play();
						else
						{
							release();
						}
					}
				});
			}
		}
			return true;
	}
	
	public void pause()
	{
		if(mediaPlayer != null && mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
			seekPosition = mediaPlayer.getCurrentPosition();
			isPaused = true;
		}
	}

	/** */
	public void stop()
	{
		isPlaying = false;
		isPaused  = false;
		if(mediaPlayer != null)
		{
			if(mediaPlayer.isPlaying())
				mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		
	}
	
	/** free the track from further use by the app, save memory! */
	public void release()
	{
		if(isFileLoaded() && mediaPlayer != null)
		{
			isPaused = false;
			isPlaying= false;
			resourceId = -1;
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	/** if music was loaded and paused, this method will reload and resume it. Otherwise this will pause 
	 *  It is different than the normal pause method in that this is not for general user control and more for
	 *  the activity cycle*/
	public void activityPauseCycle(boolean pausing)
	{
		if(isFileLoaded())
		{
			if(pausing)		//pause
			{
				if(mediaPlayer != null)			//hardcode/inline code the "stop()" method without adjusting the isPlaying
				{
					seekPosition = mediaPlayer.getCurrentPosition();
					if(mediaPlayer.isPlaying())
						mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
			}
			else			//resume
			{
				if(isPlaying())
				{
					play();
					mediaPlayer.seekTo(seekPosition);
				}
			}
		}
	}
	
	/** get the currently playing song if it is available to set an on completion listener or do other things */
	public MediaPlayer getMediaPlaying()
	{
		if(mediaPlayer != null)
			return mediaPlayer;
		else return null;
	}
	
	/** whether the music has been paused or not */
	public boolean isPaused()
	{
		return isPaused;
	}
	
	public boolean isPlaying()
	{
		return isPlaying;
	}
	
	/** whether a resource has been set */
	public boolean isFileLoaded()
	{
		return (resourceId != NO_MUSIC_LOADED);
	}
}