package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScrAssets.ANIM_B_EXPL;
import static com.whateversoft.colorshafted.screens.GameScrAssets.ANIM_G_EXPL;
import static com.whateversoft.colorshafted.screens.GameScrAssets.ANIM_R_EXPL;
import static com.whateversoft.colorshafted.screens.GameScrAssets.ANIM_Y_EXPL;

import com.whateversoft.android.framework.Anim;
import com.whateversoft.android.framework.AnimEntity;
import com.whateversoft.colorshafted.screens.GameScr;

public class ColorExplosion extends AnimEntity
{
	public int color;
	
	public ColorExplosion(float collisionX, float collisionY, int c, GameScr scr)
	{
		super(GameScr.LAYER_ENTITIES_FX, scr);
		anims.add(null);
		
		color = c;
		if(color != -1)
			setExplosionColor(color);
		
		//define points since constructor didn't include it
		x = collisionX;
		y = collisionY;
	}
	
	public void setExplosionColor(int color)
	{
		//attach explosion animation
		switch(color)
		{
			case ColorBlock.BLUE:
				anims.set(0, screen.assets.getAnim(ANIM_B_EXPL));
				break;
			case ColorBlock.GREEN:
				anims.set(0, screen.assets.getAnim(ANIM_G_EXPL));
				break;
			case ColorBlock.RED:
				anims.set(0, screen.assets.getAnim(  ANIM_R_EXPL));
				break;
			case ColorBlock.YELLOW:
				anims.set(0, screen.assets.getAnim(ANIM_Y_EXPL));
				break;
		}
	}
	
	//destroy this entity when if/when it's animation has completed!
	@Override
	public void animate(long time)
	{
		super.animate(time);
		if(isAnimFinished)
			((GameScr)screen).cExpFactory.throwInPool(this);
	}

}
