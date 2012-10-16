package com.whateversoft.colorshafted.game.factories;

import android.util.Log;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.game.ColorBlockE;
import com.whateversoft.colorshafted.game.ColorBlockECT;
import com.whateversoft.colorshafted.screens.GameScr;

public class EnemyBlockFactory extends ObjectFactory<ColorBlockE>
{
	GameScr screen;
	public int layer;
	
	public EnemyBlockFactory(GameScr s, int l)
	{
		screen = s;
		layer  = l;
	}
	
	@Override
	public ColorBlockE newObject()
	{
			return new ColorBlockE(-1, -1, -1, -100, -100, layer, screen);
	}
	
	public ColorBlockE getFactoryObject(int color, int initialDirection, int initialShaft)
	{
		Log.d("COLORSHAFTED", "just retrieved a factory obect from the regular factory");
		//return instance from factory
		ColorBlockE returnObj = retrieveInstance();					  
		
		//set its initial parameters
		returnObj.destroyed = false;				//re-allow collisions
		returnObj.visible   = true;
		returnObj.instantiateAsEnemy(initialDirection, initialShaft, color); 
		returnObj.setColorAndGraphic(color);
		
		//return this new object with the correct parameters
		return returnObj;
	}
	
	public ColorBlockE getFactoryObject(int shaftIndex, int color)
	{
		Log.d("COLORSHAFTED", "just retrieved a factory obect from the regular factory");
		//return instance from factory
		ColorBlockE returnObj = retrieveInstance();
		
		//set its initial parameters
		returnObj.destroyed = false;
		returnObj.visible = true;
		returnObj.instantiateAsEnemy(shaftIndex, color);
		
		returnObj.setColorAndGraphic(color);
		
		//return this object with the defined parameters
		return returnObj;
	}

	@Override
	public void throwInPool(ColorBlockE obj)
	{
		obj.visible = false;
		obj.x = -100;
		obj.y = -100;
		obj.destroyed = true;	//to prevent collision
		screen.eBlocks.remove(obj);
		objectPool.add(obj);
	}
}
