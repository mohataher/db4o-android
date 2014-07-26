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
package com.db4o.internal.fieldindex;

import com.db4o.foundation.*;
import com.db4o.internal.query.processor.*;

public class FieldIndexProcessor {

	private final QCandidates _candidates;

	public FieldIndexProcessor(QCandidates candidates) {
		_candidates = candidates;
	}
	
	public FieldIndexProcessorResult run() {
		IndexedNode bestIndex = selectBestIndex();
		if (null == bestIndex) {
			return FieldIndexProcessorResult.NO_INDEX_FOUND;
		}
		IndexedNode resolved = resolveFully(bestIndex);
		
		if (! bestIndex.isEmpty()) {
			bestIndex.markAsBestIndex(_candidates);
			return new FieldIndexProcessorResult(resolved);
		}
		return FieldIndexProcessorResult.FOUND_INDEX_BUT_NO_MATCH;
	}

	private IndexedNode resolveFully(IndexedNode indexedNode) {
		if (null == indexedNode) {
			return null;
		}
		if (indexedNode.isResolved()) {
			return indexedNode;
		}
		return resolveFully(indexedNode.resolve());
	}
	
	public IndexedNode selectBestIndex() {		
		final Iterator4 i = collectIndexedNodes();
		IndexedNode best = null;
		while (i.moveNext()) {
			IndexedNode indexedNode = (IndexedNode)i.current();
			IndexedNode resolved = resolveFully(indexedNode);
			if(resolved == null){
				continue;
			}
			if(best == null){
				best = indexedNode;
				continue;
			}
			if (indexedNode.resultSize() < best.resultSize()) {
				best = indexedNode;
			}
		}
		return best;
	}

	public Iterator4 collectIndexedNodes() {
		return new IndexedNodeCollector(_candidates).getNodes();
	}	    
}
