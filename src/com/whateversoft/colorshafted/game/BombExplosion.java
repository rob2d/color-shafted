package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScrAssets.ANIM_BOMB_EXPL;

import com.whateversoft.android.framework.AnimEntity;
import com.whateversoft.colorshafted.screens.GameScr;

public class BombExplosion extends AnimEntity
{
	public BombExplosion(int centerX, int centerY, GameScr scr)
	{
		super(scr.LAYER_HUD, scr);
		
		//attach the explosion animation
		anims.add(scr.assets.getAnim(ANIM_BOMB_EXPL));
		
		//define points since constructor didn't include it
		x = centerX;
		y = centerY;
	}
	

	@Override //destroy this entity when if/when it's animation has completed!
	public void animate(long time)
	{
		super.animate(time);
		if(isAnimFinished) destroy();
	}
}
