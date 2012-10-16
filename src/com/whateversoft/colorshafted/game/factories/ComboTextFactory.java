package com.whateversoft.colorshafted.game.factories;

import android.util.Log;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.game.ComboText;
import com.whateversoft.colorshafted.screens.GameScr;

public class ComboTextFactory extends ObjectFactory<ComboText>
{
	GameScr screen;

	public ComboTextFactory(GameScr s)
	{
		screen = s;
	}
	
	@Override
	public ComboText newObject()
	{	
		return new ComboText(-1, -1, new StringBuffer(), -1, screen);
	}

	@Override
	public void throwInPool(ComboText obj)
	{
		obj.visible   = false;
		obj.destroyed = true;
		obj.x = -1000;
		obj.y = -1000;
		objectPool.add(obj);
	}
	
	public ComboText getObject(int x, int y, StringBuffer str, int expColor)
	{
		ComboText returnObj = retrieveInstance();
		returnObj.initialValues(expColor);
		returnObj.x = x;
		returnObj.y = y;
		returnObj.destroyed = false;
		returnObj.visible = true;
		returnObj.string.setLength(0);
		returnObj.string.append(str);
		
		return returnObj;
	}
}