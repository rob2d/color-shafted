package com.whateversoft.android.framework;

import java.util.ArrayList;
import java.util.List;

/** Copyright 2011 Robert Concepcion III */
public class Pool<T>
{
	public interface PoolObjectFactory<T>
	{
		public T createObject();
	}
	/** stores pooled objects */
	private final List<T> freeObjects;
	/** used to generate new instance of the type specified */
	private final PoolObjectFactory<T> factory;
	/**maximum number of objects the pool factory can store */
	private final int maxSize;
	
	public Pool(PoolObjectFactory<T> f, int maxS)
	{
		factory = f;
		maxSize = maxS;
		freeObjects = new ArrayList<T>(maxSize);
	}
	
	/** Method to either create a new object of the factory type or recycle one already within the pool */
	public T newObject()
	{
		T object = null;
		
		if(freeObjects.size() == 0)
			object = factory.createObject();
		else
			object = freeObjects.remove(freeObjects.size() - 1);
		
		return object;
	}
	
	public void free(T object)
	{
		if(freeObjects.size() < maxSize)
		freeObjects.add(object);
	}
}
