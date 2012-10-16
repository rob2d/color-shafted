package com.whateversoft.android.framework;

import android.graphics.Typeface;
import android.util.Log;

public abstract class ScreenAssets
{
	public Object[] assets;
	public Game		game;
	public abstract void obtainAssets();
	
	public ScreenAssets(Game g)
	{
		game = g;
	}
	
	/** clear all assets */
	public void freeAssets()
	{
		for(Object o : assets)
		{
			if(o instanceof Pixmap)
			{
				((Pixmap) o).dispose();
				o = null;
			}
			else if(o instanceof ImageFrame)
			{
				((ImageFrame)o).getImg().dispose();
				((ImageFrame)o).img = null;
				o = null;
			}
			else if(o instanceof Anim)
			{
				for(ImageFrame iF : ((Anim) o).frames)
				{
					iF.getImg().dispose();
					iF.img = null;
					iF = null;
				}
				((Anim)o).frames = null;
				o = null;
			}
			else if(o instanceof Sound)
			{
				((Sound)o).dispose();
				o = null;
			}
			else if(o instanceof Typeface)
			{
				o = null;
			}
		}
	}
	public <T> T getAsset(int assetId, T assetType)
	{
		return (T)assets[assetId];
	}
	
	public Sound getSound(int assetId)
	{
		return (Sound)assets[assetId];
	}
	
	public Anim getAnim(int assetId)
	{
		return (Anim)assets[assetId];
	}
	
	public ImageFrame getImage(int assetId)
	{
		return (ImageFrame)assets[assetId];
	}
	
	public ImageFrame[] getImageArray(int assetId)
	{
		return (ImageFrame[])(assets[assetId]);
	}
	
	/** retrieve a specific ImageFrame from an asset resource in a convenient method */
	public ImageFrame getImageInArray(int assetId, int index)
	{
		return ((ImageFrame[])assets[assetId])[index];
	}
	
	/** set an image in an ImageFrame array asset resource to a given image frame at a given position */
	public void setImageInArray(int assetId, int index, ImageFrame newImage)
	{
		((ImageFrame[])assets[assetId])[index] = newImage;
	}
	
	public Pixmap getPixmap(int assetId)
	{
		return (Pixmap)assets[assetId];
	}
	
	public Typeface getFont(int assetId)
	{
		return (Typeface)assets[assetId];
	}
}
