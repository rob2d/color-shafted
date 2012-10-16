package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScrAssets.ANIM_BAD_EXPL;

import com.whateversoft.android.framework.AnimEntity;
import com.whateversoft.colorshafted.screens.GameScr;

public class ErrorExplosion extends AnimEntity
{

	public ErrorExplosion(int collisionX, int collisionY, GameScr scr)
	{
		super(scr.LAYER_ENTITIES_FX, scr);
		
		//attach the explosion animation
		anims.add(scr.assets.getAnim(ANIM_BAD_EXPL));
		
		//define points since constructor didn't include it
		x = collisionX;
		y = collisionY;
	}
	
	//destroy this entity when if/when it's animation has completed!
		@Override
		public void animate(long time)
		{
			super.animate(time);
			if(isAnimFinished)
				((GameScr)screen).eExpFactory.throwInPool(this);
		}
}
