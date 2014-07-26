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
package com.db4o.internal.query.processor;

import com.db4o.foundation.*;
import com.db4o.internal.*;

public class QPrimitiveCandidate extends QCandidateBase {

	private Object _obj;
	
	public QPrimitiveCandidate(QCandidates candidates, Object obj) {
		super(candidates, candidates.generateCandidateId());
		_obj = obj;
	}

	@Override
	public Object getObject() {
		return _obj;
	}

	@Override
	public boolean evaluate(QConObject a_constraint, QE a_evaluator) {
		return a_evaluator.evaluate(a_constraint, this, a_constraint.translate(_obj));
	}

	@Override
	public PreparedComparison prepareComparison(ObjectContainerBase container, Object constraint) {
		ClassMetadata classMetadata = classMetadata();
		if (classMetadata == null) {
			return null;
		}
		return classMetadata.prepareComparison(container.transaction().context(), constraint);
	}

	@Override
	public ClassMetadata classMetadata() {
		return container().classMetadataForReflectClass(container().reflector().forObject(_obj));
	}

	@Override
	public boolean fieldIsAvailable(){
		return false;
	}

}
