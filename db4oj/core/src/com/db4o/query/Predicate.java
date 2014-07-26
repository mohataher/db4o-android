/* This file is part of the db4o object database http://www.db4o.com

Copyright (C) 2004 - 2011  Versant Corporation http://www.versant.com

db4o is free software; you can redistribute it and/or modify it under
the terms of version 3 of the GNU General Public License as published
by the Free Software Foundation.

db4o is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see http://www.gnu.org/licenses/. */
package com.db4o.query;

import com.db4o.internal.Platform4;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base class for native queries. See {@link com.db4o.ObjectContainer#query(Predicate)}
 * <br><br>
 * @see com.db4o.ObjectContainer#query(Predicate)
 */
@SuppressWarnings("serial")
public abstract class Predicate<ExtentType> implements Serializable{
    
    /**
     * public for implementation reasons, please ignore.
     */
	public final static String PREDICATEMETHOD_NAME="match";
	
	private Class<? extends ExtentType> _extentType;

	private transient Method cachedFilterMethod=null;
	
	public Predicate() {
		this(null);
	}

	public Predicate(Class<? extends ExtentType> extentType) {
		_extentType=extentType;
	}

	public Method getFilterMethod() {
		if(cachedFilterMethod!=null) {
			return cachedFilterMethod;
		}
		Method[] methods=getClass().getMethods();
		for (int methodIdx = 0; methodIdx < methods.length; methodIdx++) {
			Method method=methods[methodIdx];
			if((!method.getName().equals(PredicatePlatform.PREDICATEMETHOD_NAME)) || method.getParameterTypes().length != 1) {
				continue;
			}
			cachedFilterMethod = method;
			String targetName=method.getParameterTypes()[0].getName();
			if(!"java.lang.Object".equals(targetName)) {
				break;
			}
		}
		if(cachedFilterMethod == null) {
			throw new IllegalArgumentException("Invalid predicate.");
		}
		return cachedFilterMethod;
	}

    /**
     * public for implementation reasons, please ignore.
     */
	public Class<? extends ExtentType> extentType() {
		if (_extentType == null) {
			_extentType = figureOutExtentType();
		}
		return _extentType;
	}
	
	/**
     * The match method that needs to be implemented by the user.
     * @param candidate the candidate object passed from db4o 
     * @return true to include an object in the resulting ObjectSet
     * 
     * @sharpen.ignore
     */
	@decaf.Ignore(platforms = {decaf.Platform.JDK11, decaf.Platform.JDK12})
    public abstract boolean match(ExtentType candidate);

	/**
	 * @sharpen.remove FilterParameterType()
	 */
	@decaf.ReplaceFirst(value = "return filterParameterType();", platforms = {decaf.Platform.JDK11, decaf.Platform.JDK12})
	private Class<? extends ExtentType> figureOutExtentType() {		
		return extentTypeFromGenericParameter();
	}

	/**
	 * @sharpen.ignore
	 */
	@decaf.Ignore(platforms = {decaf.Platform.JDK11, decaf.Platform.JDK12})
    private Class<? extends ExtentType> extentTypeFromGenericParameter() {
		Class<? extends ExtentType> extentType=filterParameterType();
		try {
			Type genericType=((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			if((genericType instanceof Class)&&(extentType.isAssignableFrom((Class)genericType))) {
				extentType=(Class<? extends ExtentType>)genericType;
			}
		} catch (RuntimeException e) {
		}
		return extentType;
	}

	private Class<? extends ExtentType> filterParameterType() {
		return (Class<? extends ExtentType>)getFilterMethod().getParameterTypes()[0];
	}    
	
    /**
     * public for implementation reasons, please ignore.
     */
	public boolean appliesTo(ExtentType candidate) {
		try {
			Method filterMethod=getFilterMethod();
			Platform4.setAccessible(filterMethod);
			Object ret=filterMethod.invoke(this,new Object[]{candidate});
			return ((Boolean)ret).booleanValue();
		} catch (Exception e) {
			// TODO: log this exception somewhere?
//			e.printStackTrace();
			return false;
		}
	}
}
