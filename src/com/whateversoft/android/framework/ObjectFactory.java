package com.whateversoft.android.framework;

import java.util.ArrayList;

import android.util.Log;

/** factorizes objects. in some cases, the concrete versions of this class must accept certain parameters
 *  in the constructor in order to initialize new objects(such as entities to screens). Ideally, there will be no
 *  need to call the factory objects' constructor; instead, everything will be handled by simply passing parameters 
 *  to the factory. The only implication from this is that we have to take care to instantiate each type of object 
 *  factory correctly(thankfully, this will happen in limited cases as object memory handling isn't too crazy atm) */
public abstract class ObjectFactory<T>
{
	protected ArrayList<T> objectPool;
	
	/** constructor; initialize empty object pool to factorize objects of its subtype*/
	public ObjectFactory()
	{
		objectPool = new ArrayList<T>();
	}
	
	/** retrieve a new instance of the object; if one is available, it is recycled. Otherwise, ovverridden
	 *  "createObject()" abstract method is called */
	public T retrieveInstance()
	{
		if(objectPool.size() > 0)
			return objectPool.remove(0);
		else
			return newObject();
	}
	
	/** method to create specific instance of factorized class */
	public abstract T newObject();
	/** store the object in the object pool to be used again */
	public abstract void throwInPool(T obj);
}