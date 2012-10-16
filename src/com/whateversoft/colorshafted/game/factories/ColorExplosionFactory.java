package com.whateversoft.colorshafted.game.factories;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.game.ColorExplosion;
import com.whateversoft.colorshafted.screens.GameScr;

public class ColorExplosionFactory extends ObjectFactory<ColorExplosion>
{
	GameScr screen;

	public ColorExplosionFactory(GameScr s)
	{
		screen = s;
	}
	
	@Override
	public ColorExplosion newObject()
	{	
		return new ColorExplosion(-1, -1000, -1000, screen);
	}

	@Override
	public void throwInPool(ColorExplosion obj)
	{
		obj.visible   = false;
		obj.destroyed = true;
		obj.x = -1000;
		obj.y = -1000;
		
		objectPool.add(obj);
	}
	
	public ColorExplosion getFactoryObject(int x, int y, int color)
	{
		ColorExplosion returnObj = retrieveInstance();
		returnObj.setExplosionColor(color);
		returnObj.x = x;
		returnObj.y = y;
		returnObj.destroyed = false;
		returnObj.visible = true;
		
		if(returnObj.anims.get(0) != null)
		{
			returnObj.resetAnim();				//allow the explosion to animate again
			returnObj.animate(0);
		}

		return returnObj;
	}
}