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

/**
 * For implementation of callback evaluations.
 * <br><br>
 * This is for adding your own constrains to a given query. 
 * Note that evaluations force db4o to instantiate your objects in order
 * to execute the query which slows down to query by an order of magnitude.
 * Pass your implementation of this interface to {@link Query#constrain(Object)}
 * <br><br>
 * Evaluations are called as the last step during query execution,
 * after all other constraints have been applied.
 * <br><br>Client-Server over a network only:<br>
 * Avoid evaluations, because the evaluation object needs to be serialized, which is hard
 * to manage correctly.
 */
public interface Evaluation extends java.io.Serializable {
	
	/**
	 * Callback method during {@link Query#execute() query execution}.
	 * @param candidate reference to the candidate persistent object.
	 */
	public void evaluate(Candidate candidate);
	
}
