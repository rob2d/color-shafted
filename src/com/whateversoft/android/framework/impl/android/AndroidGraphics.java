package com.whateversoft.android.framework.impl.android;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.Typeface;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.Pixmap;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.colorshafted.constants.CSSettings;

/** Copyright 2011 Robert Concepcion III */
public class AndroidGraphics implements Graphics
{
	AssetManager assets;		//loads bitmap instances
	Bitmap frameBuffer;			//artificial buffer
	Canvas canvas;				//used to draw buffer
	Paint paint;				//used for drawing
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	
	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer, boolean antiAliasing)
	{
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);		//use the canvas to draw to the frameBuffer
		canvas.clipRect(0, 0, ScreenInfo.virtualWidth, ScreenInfo.virtualHeight, Op.REPLACE);
		this.paint = new Paint();
		setAntiAlias(antiAliasing);
	}
	
	public Pixmap newPixmap(Bitmap bitmap, PixmapFormat format)
	{
		Config config = null;
		if(format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if(format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;
		
		Options options = new Options();
		options.inPreferredConfig = config;
		
		return null;
	}
	
	@Override
	public Pixmap newPixmap(String fileName, PixmapFormat format)
	{
		Config config = null;
		if(format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if(format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;
		
		Options options = new Options();
		options.inPreferredConfig = config;
		
		InputStream in = null;
		Bitmap bitmap = null;
		try
		{
			in = assets.open(fileName);	//load a bitmap asset via the in InputStream
			bitmap = BitmapFactory.decodeStream(in);	//set the Bitmap to the opened stream loading the asset
			if(bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset " + fileName + "'");
		}
		catch(IOException e)
		{
			throw new RuntimeException("Couldn't load bitmap from asset " + fileName + "'");
		}
		finally
		{
			if(in != null)
			{
				try{ in.close(); }
				catch(IOException e) {}
			}
		}
		
		if(bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		else if(bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else
			format = PixmapFormat.ARGB8888;
		
		//return a new AndroidPixmap image with the bitmap loaded from the assetand the format
		return new AndroidPixmap(bitmap, format);	
	}
	
	@Override
	public void clear(int color)
	{
		canvas.drawRGB(color & 0xff0000, color & 0x00ff00, color & 0x0000ff);
	}
	
	@Override
	public void drawPixel(int x, int y, int color)
	{
		paint.setColor(color);
		canvas.drawPoint((float)x, (float)y, paint);
	}
	@Override
	public void drawLine(int x, int y, int x2, int y2, int color)
	{
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public void drawRect(int x, int y, int width, int height, int color, boolean fill)
	{
		paint.setColor(color);
		paint.setStyle(fill? Style.FILL : Style.STROKE);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) 
	{
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;
		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, paint);
	}
	
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int actionPointX, int actionPointY) 
	{
		if(pixmap != null)
			canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x - actionPointX, y - actionPointY, paint);
		
	}
	
	@Override
	public void drawPixmapScaled(Pixmap pixmap, int x, int y, int zoomPointX, int zoomPointY, float scale)
	{
		int xDraw = x - (int)Math.round(zoomPointX * scale);
		int widthDrawn = Math.round(pixmap.getWidth() * scale);
		int heightDrawn = Math.round(pixmap.getHeight() * scale);
		int yDraw  = y - (int)Math.round(zoomPointY * scale);
		srcRect.left = xDraw >= 0 ? 0 : Math.round(-xDraw / scale);
		srcRect.right = xDraw + widthDrawn > ScreenInfo.virtualWidth ? 
							Math.round((ScreenInfo.virtualWidth - xDraw)/scale) : 
							pixmap.getWidth();
		srcRect.top  = yDraw >= 0 ? 0 : Math.round(-yDraw / scale);
		srcRect.bottom = xDraw + heightDrawn > ScreenInfo.virtualHeight ? 
							Math.round((ScreenInfo.virtualHeight - yDraw)/scale) : 
							pixmap.getHeight();
		
		dstRect.left = xDraw >= 0 ? xDraw : 0;
		dstRect.top = yDraw >= 0 ? yDraw : 0;
		dstRect.right = xDraw + Math.round((pixmap.getWidth() - 1) * scale) < ScreenInfo.virtualWidth ? 
							xDraw + Math.round((pixmap.getWidth() - 1) * scale) :
							ScreenInfo.virtualWidth;
		dstRect.bottom = yDraw + Math.round((pixmap.getHeight() - 1) * scale) < ScreenInfo.virtualHeight ?
							yDraw + Math.round((pixmap.getHeight() - 1) * scale) :
							ScreenInfo.virtualHeight;
		
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, paint);
	}
	
	@Override
	public void drawPixmapRotated(Pixmap pixmap, int x, int y, int actionPointX, int actionPointY, int angle)
	{
		canvas.save();
		canvas.rotate(angle, x, y);
		drawPixmap(pixmap, (int)x, (int)y, actionPointX, actionPointY);					//no need for action points since rotation takes care of this!
		canvas.restore();
	}
	
	@Override
	public void drawPixmapAlpha(Pixmap pixmap, int x, int y, int actionPointX, int actionPointY, int alpha)
	{
		paint.setAlpha(alpha);
		drawPixmap(pixmap, x, y, actionPointX, actionPointY);
		paint.setAlpha(255);
	}

	@Override
	public void drawPixmapRotatedAlpha(Pixmap pixmap, int x, int y,
			int actionPointX, int actionPointY, int angle, int alpha)
	{
		paint.setAlpha(alpha);
		drawPixmapRotated(pixmap, x, y, actionPointX, actionPointY, angle);
		paint.setAlpha(255);
	}
	
	@Override
	public void drawPixmapAlpha(Pixmap pixmap, int x, int y, int srcX,
			int srcY, int srcWidth, int srcHeight, int alpha)
	{
		paint.setColor(Color.BLACK);
		paint.setAlpha(alpha);
		drawPixmap(pixmap, x, y, srcX, srcY, srcWidth, srcHeight);
		paint.setAlpha(255);
	}
	
	@Override
	public void drawShape(Path shape)
	{
		canvas.drawPath(shape, paint);
	}
	
	@Override
	public void drawText(StringBuffer txt, int x, int y, int c, int s, Typeface t, Paint.Align a)
	{
		paint.setColor(c);
		paint.setTypeface(t);
		paint.setTextSize(s);
		paint.setTextAlign(a);
		canvas.drawText(txt.toString(), x, y, paint);
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public void drawText(String txt, int x, int y, int c, int s, Typeface t, Paint.Align a)
	{
		paint.setColor(c);
		paint.setTypeface(t);
		paint.setTextSize(s);
		paint.setTextAlign(a);
		canvas.drawText(txt, x, y, paint);
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public int getWidth()
	{
		return frameBuffer.getWidth();
	}
	
	@Override
	public int getHeight()
	{
		Canvas c = new Canvas();
		return frameBuffer.getHeight();
	}

	@Override
	public void drawRect(Rect r, int color, boolean fill)
	{
		paint.setColor(color);
		paint.setStyle(fill? Style.FILL : Style.STROKE);
		canvas.drawRect(r.left, r.top, r.right, r.bottom, paint);
		paint.setColor(Color.WHITE);
	}

	@Override
	public void setBitmapBuffer(Bitmap bitmap)
	{
		if(frameBuffer != bitmap)	//if it isn't the original bitmap, reset given values
		{
			frameBuffer = null;
			frameBuffer = bitmap;
			canvas.setBitmap(frameBuffer);
		}
	}
	
	public Paint getPainter()
	{
		return paint;
	}

	@Override
	public void setAntiAlias(boolean antiAliasing)
	{
		paint.setAntiAlias(antiAliasing);
	}
}
