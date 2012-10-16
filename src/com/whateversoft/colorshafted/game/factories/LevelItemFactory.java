package com.whateversoft.colorshafted.game.factories;

import android.util.Log;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.game.ColorBlockE;
import com.whateversoft.colorshafted.game.ColorBlockECT;
import com.whateversoft.colorshafted.game.LevelItem;
import com.whateversoft.colorshafted.screens.GameScr;

public class LevelItemFactory extends ObjectFactory<LevelItem>
{
	GameScr screen;
	public int layer;
	
	public LevelItemFactory(GameScr s, int l)
	{
		screen = s;
		layer  = l;
	}
	
	@Override
	public LevelItem newObject()
	{
		return new LevelItem(screen);
	}
	
	public LevelItem getFactoryObject(int itemType, int color, int shaft, int dir)
	{
		//return instance from factory
		LevelItem returnedItem = retrieveInstance();
		
		//set its initial parameters
		returnedItem.destroyed = false;
		returnedItem.visible = true;
		returnedItem.instantiateItem(itemType, color, shaft, dir);
		
		//return this object with the defined parameters
		return returnedItem;
	}

	@Override
	public void throwInPool(LevelItem obj)
	{
		obj.visible = false;
		obj.x = -100;
		obj.y = -100;
		obj.destroyed = true;	//to prevent collision
		screen.lItems.remove(obj);
		objectPool.add(obj);
	}
}
