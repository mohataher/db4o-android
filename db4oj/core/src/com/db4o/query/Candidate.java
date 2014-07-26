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

import com.db4o.*;

/**
 * Candidate for {@link Evaluation} callbacks.
 * <br><br>
 * During {@link Query#execute() query execution} all registered {@link Evaluation} callback
 * handlers are called with {@link Candidate} proxies that represent the persistent objects that
 * meet all other {@link Query} criteria.
 * <br><br>
 * A {@link Candidate} provides access to the query candidate object. It
 * represents and allows to specify whether it is to be included in the query result
 */
public interface Candidate {
	
	/**
	 * Returns the persistent object that is represented by this query
	 * {@link Candidate}.
	 * @return Object the persistent object.
	 */
	public Object getObject();
	
	/**
	 * Specify whether the Candidate is to be included in the result
	 * <br><br>
	 * This method may be called multiple times. The last call prevails.
	 * @param flag true to include that object. False otherwise.
	 */
	public void include(boolean flag);
	
	
	/**
	 * Returns the object container the query is executed on
	 * @return the {@link ObjectContainer}
	 */
	public ObjectContainer objectContainer();
	
}
