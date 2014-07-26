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

import com.db4o.internal.*;

public class PrintWriterLoggerInterceptor implements LoggingInterceptor {
	
	private	PrintWriter out;

	public PrintWriterLoggerInterceptor(PrintWriter out) {
		this.out = out;
	}
	
	@Override
	public void log(Level loggingLevel, String method, Object[] args) {
		
		List<Throwable> throwables = translateArguments(args);
		
		out.println(formatLine(Platform4.now(), loggingLevel, method, args));
		
		if (throwables != null) {
			for(Throwable t : throwables) {
				Platform4.printStackTrace(t, out);
			}
		}
	}

	private List<Throwable> translateArguments(Object[] args) {
		List<Throwable> throwables = null;
		if (args == null) {
			return null;
		}

		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			if (obj instanceof Throwable) {
				Throwable t = (Throwable) obj;
				args[i] = t.getClass().getSimpleName();
				if (throwables == null) {
					throwables = new ArrayList<Throwable>();
				}
				throwables.add(t);
			}
		}
		return throwables;
	}

	public static String formatLine(Date now, Level loggingLevel, String method, Object[] args) {
		return Platform4.format(now, true) + " " + formatMessage(loggingLevel, method, args);
	}

	public static String formatMessage(Level loggingLevel, String method, Object[] args) {
		
		String s = "";
		if (args != null) {
			for(Object obj : args) {
				if (s.length() > 0) {
					s += ", ";
				}
				s += obj;
			}
		}
		
		return "["+Logger.levelToString(loggingLevel)+"] "+ formatMethodName(method) + (args==null?"":"("+s+")");
	}

	private static String formatMethodName(String name) {
		return name;
	}
}