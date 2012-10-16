package com.whateversoft.android.framework;

/** Handles the lifecycle of a music playlist for games<br>
 *  Copyright 2011-2012 Robert Concepcion III */
public interface MusicJukebox
{
	public void setPlayMode(PlayMode pM);
	public void setTrackResources(int... tR);
	public void play();
	public void stop();
	public void activityPauseCycle(boolean pause);
	public int nextTrack();
	public int nextRandomTrack();
	public boolean setTrack(int trackIndex);
	public void releaseMusic();
	public void setTrackAsEnabled(int track, boolean enabled);
	
	public enum PlayMode { NO_MUSIC, PLAY_SINGLE, LOOP_SINGLE, LOOP_ALL, SHUFFLE };
}
