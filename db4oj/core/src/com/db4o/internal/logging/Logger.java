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
package com.db4o.internal.logging;

import java.io.*;
import java.util.*;

import decaf.*;


public class Logger {

	static ThreadLocal<Level> currentThreadLoggingLevel = new ThreadLocal<Level>();
	
	private static Map<Class<?>, Logging<?>> cache = new HashMap<Class<?>, Logging<?>>();

	public static final Level TRACE = new Level("TRACE", 0);
	public static final Level DEBUG = new Level("DEBUG", 1);
	public static final Level INFO = new Level("INFO", 2);
	public static final Level WARN = new Level("WARN", 3);
	public static final Level ERROR = new Level("ERROR", 4);
	public static final Level FATAL = new Level("FATAL", 5);
	
	static LoggingInterceptor rootInterceptor = new PrintWriterLoggerInterceptor(new PrintWriter(System.out, true));
	static Level loggingLevel = TRACE;

	public static void intercept(LoggingInterceptor interceptor) {
		Logger.rootInterceptor = interceptor;
	}

	@decaf.RemoveFirst(unlessCompatible=Platform.JDK15)
	public static <T> Logging<T> get(final Class<T> clazz) {
		checkAnnotation(clazz);
		
		synchronized (cache) {
			Logging<T> logging = (Logging<T>) cache.get(clazz);
			if (logging == null) {
				logging = new LoggingWrapper<T>(clazz);
				cache.put(clazz, logging);
			}
			return logging;
		}
	}

	@decaf.Ignore(unlessCompatible=Platform.JDK15)
	private static <T> void checkAnnotation(final Class<T> clazz) {
		if (clazz.getAnnotation(LogInterface.class) == null) {
			throw new IllegalArgumentException("Logger interfaces must be annotated with @"+LogInterface.class.getName());
		}
	}

	public static void loggingLevel(Level loggingLevel) {
		Logger.loggingLevel = loggingLevel;
	}

	public static String levelToString(Level loggingLevel) {
		return loggingLevel.toString();
	}

	public static Level contextLoggingLevel() {
		return currentThreadLoggingLevel.get();
	}

	public static void purgeCache() {
		synchronized (cache) {
			cache.clear();
		}
	}
}
