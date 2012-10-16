package com.whateversoft.colorshafted.constants;

/* class used to store the constants which represent different preference values in colorshafted */
public class CSSettings
{
	/** constants for adjusting when Http Timeouts should occur! */
	public final static int		VAL_HTTP_TIME_30SEC		  = 0,
								VAL_HTTP_TIME_60SEC		  = 1,
								VAL_HTTP_TIME_120SEC	  = 2;
	
	/** key reference strings for settings */
	public final static String  KEY_DIFFICULTY   	  	  = "initialDifficulty",
							    KEY_LIVES_ARCADE 	  	  = "initialArcadeLives",
							    KEY_BOMB_LONGTAP 	  	  = "bombLongTap",
							    KEY_BOMB_SHAKE_GESTURE 	  = "bombShakeGesture",
							    KEY_SHAKE_SENSITIVITY	  = "shakeSensitivity",
							    KEY_BOMB_SHAKE_ON_X		  = "bombShakeOnX",
							    KEY_BOMB_BUTTON		  	  = "bombButton",
							    KEY_ENABLE_SFX		  	  = "enableSfx",
							    KEY_ENABLE_MUS		  	  = "enableMusic",
							    KEY_MUSIC_PLAYMODE	 	  = "musicPlayMode",
							    KEY_PLAY_TRACK_A		  = "musicTrackA",
							    KEY_PLAY_TRACK_B		  = "musicTrackB",
							    KEY_PLAY_TRACK_C		  = "musicTrackC",
							    KEY_PLAY_TRACK_D		  = "musicTrackD", 
							    KEY_HIGHSCORE_PROMPT	  = "hSpromptForName",
							    KEY_HIGHSCORE_NAME	  	  = "hSdefaultName",
							    KEY_HIGHSCORE_RESET	  	  = "hSresetData",
							    KEY_ENABLE_GFX_ANTIALIAS  = "enableGfxAntiAlias",
							    KEY_ENABLE_GFX_MOTIONBLUR = "enableGfxMotionBlur",
								KEY_ENABLE_GFX_COMBO_HUD  = "enableGfxComboHUD",
								KEY_SHOW_TUTORIAL_PROMPT  = "showTutorialPrompt",
								KEY_DEVICE_ID			  = "deviceId",
								KEY_HTTP_TIMEOUT		  = "httpTimeOut",
								KEY_LICENSE_UUID		  = "licenseUUID",
								KEY_INTRO_ZOOMOUT		  = "introZoomOut",
								KEY_INTRO_SCROLLTXT	      = "introScrollTxt";
	
	public final static int    	DEFAULT_DIFFICULTY	  	  	  = 0;
	public final static String	DEFAULT_LIVES_ARCADE	  	  = "5";
	public final static boolean DEFAULT_BOMB_LONGTAP	  	  = false;
	public final static boolean DEFAULT_BOMB_SHAKE_GESTURE	  = false;
	public final static int		DEFAULT_SHAKE_SENSITIVITY	  = 50;
	public final static boolean DEFAULT_BOMB_BUTTON			  = false;
	public final static boolean DEFAULT_ENABLE_SFX		      = true;
	public final static boolean DEFAULT_ENABLE_MUS			  = true;
	public final static String  DEFAULT_MUSIC_PLAYMODE		  = "SHUFFLE";
	public final static boolean DEFAULT_PLAY_TRACK_A		  = true;
	public final static boolean DEFAULT_PLAY_TRACK_B		  = true;
	public final static boolean DEFAULT_PLAY_TRACK_C		  = true;
	public final static boolean DEFAULT_PLAY_TRACK_D		  = true;
	public final static boolean DEFAULT_HIGHSCORE_PROMPT      = true;
	public final static boolean DEFAULT_ENABLE_GFX_MOTIONBLUR = true;
	public final static boolean DEFAULT_ENABLE_GFX_COMBO_HUD  = true;
	public final static int		DEFAULT_DEVICE_ID			  = -1;
	public final static String	DEFAULT_HTTP_TIMEOUT  		  = "60000";
	public final static boolean DEFAULT_INTRO_ZOOMOUT         = false;
	public final static boolean DEFAULT_INTRO_SCROLLTXT		  = true;
	public final static boolean DEFAULT_ENABLE_GFX_ANTIALIAS  = false;
}
