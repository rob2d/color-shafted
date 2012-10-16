package com.whateversoft.colorshafted.game;

import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.ScreenAssets;

public class CSGlobalAssets extends ScreenAssets

{
	public CSGlobalAssets(Game g)
	{
		super(g);
		obtainAssets();
	}

	public final static int 	ASSET_COUNT		  = 1,
								SND_READY = 0;

	@Override
	public void obtainAssets()
	{
		Audio	 a = game.getAudio();
		
		assets 				= new Object[ASSET_COUNT];
		assets[SND_READY]	= a.newSound("snd/sndready.ogg");
	}
}
