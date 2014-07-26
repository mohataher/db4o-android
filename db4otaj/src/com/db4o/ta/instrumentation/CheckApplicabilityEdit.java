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
package com.db4o.ta.instrumentation;

import java.util.*;

import EDU.purdue.cs.bloat.editor.*;

import com.db4o.instrumentation.core.*;
import com.db4o.instrumentation.util.*;

/**
 * @exclude
 */
class CheckApplicabilityEdit implements BloatClassEdit {

	public InstrumentationStatus enhance(ClassEditor ce, ClassLoader origLoader, BloatLoaderContext loaderContext) {
		try {
			if (ce.isInterface()) {
				return InstrumentationStatus.FAILED;
			}
			if (BloatUtil.extendsInHierarchy(ce, Enum.class, loaderContext)) {
				return InstrumentationStatus.FAILED;
			}
			if (!isApplicableClass(ce, loaderContext)) {
				return InstrumentationStatus.FAILED;
			}
		} catch (ClassNotFoundException e) {
			return InstrumentationStatus.FAILED;
		}
		return InstrumentationStatus.NOT_INSTRUMENTED;
	}

	private boolean isApplicableClass(ClassEditor ce, BloatLoaderContext loaderContext) {
		ClassEditor curEditor = ce;
		try {
			while (curEditor != null && !isApplicablePlatformClass(curEditor)) {
				if (BloatUtil.isPlatformClassName(BloatUtil.normalizeClassName(curEditor.type()))) {
					return false;
				}
				curEditor = loaderContext.classEditor(curEditor.superclass());
			}
		} catch (ClassNotFoundException exc) {
			return false;
		}
		return true;
	}

	private boolean isApplicablePlatformClass(ClassEditor ce) {
		String className = BloatUtil.normalizeClassName(ce.name());
		return Enum.class.getName().equals(className) || isSupportedCollection(className) || Object.class.getName().equals(className);
	}

	private boolean isSupportedCollection(String className) {
		return ArrayList.class.getName().equals(className);
	}

	private boolean isEnum(Class curClazz) {
		return curClazz == Enum.class;
	}
	
}
