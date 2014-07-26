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

import com.db4o.*;
import com.db4o.foundation.*;
import com.db4o.internal.*;

public abstract class QCandidateBase extends TreeInt implements InternalCandidate {

	protected final QCandidates _candidates;
	protected List4 _dependants;
	boolean _include = true;
	Tree _pendingJoins;
	InternalCandidate _root;

	public QCandidateBase(QCandidates candidates, int id) {
		super(id);
		if (DTrace.enabled) {
			DTrace.CREATE_CANDIDATE.log(id);
		}
        _candidates = candidates;
        
        if(id == 0){
            _key = candidates.generateCandidateId();
        }
        if(Debug4.queries){
            System.out.println("Candidate identified ID:" + _key );
        }
	}

	protected void addDependant(InternalCandidate a_candidate) {
		_dependants = new List4(_dependants, a_candidate);
	}

	@Override
	public void doNotInclude() {
		include(false);
		if (_dependants != null) {
			Iterator4 i = new Iterator4Impl(_dependants);
			_dependants = null;
			while (i.moveNext()) {
				((InternalCandidate) i.current()).doNotInclude();
			}
		}
	}

	@Override
	public boolean evaluate(QPending pending) {
	
		if (Debug4.queries) {
			System.out.println("Pending arrived Join: " + pending._join.id()
					+ " Constraint:" + pending._constraint.id() + " res:"
					+ pending._result);
		}
	
		QPending oldPending = (QPending) Tree.find(_pendingJoins, pending);
	
		if (oldPending == null) {
			pending.changeConstraint();
			_pendingJoins = Tree.add(_pendingJoins, pending.internalClonePayload());
			return true;
		} 
		_pendingJoins = _pendingJoins.removeNode(oldPending);
		oldPending._join.evaluatePending(this, oldPending, pending._result);
		return false;
	}

	public ObjectContainer objectContainer() {
		return container();
	}

	public InternalCandidate getRoot() {
		return _root == null ? this : _root;
	}

	final LocalObjectContainer container() {
		return transaction().localContainer();
	}

	@Override
	public final LocalTransaction transaction() {
		return _candidates.i_trans;
	}

	@Override
	public boolean include() {
		return _include;
	}

	/**
	 * For external interface use only. Call doNotInclude() internally so
	 * dependancies can be checked.
	 */
	public void include(boolean flag) {
		// TODO:
		// Internal and external flag may need to be handled seperately.
		_include = flag;
		if(Debug4.queries){
		    System.out.println("Candidate include " + flag + " ID: " + _key);
	    }
	
	}

	@Override
	public Tree onAttemptToAddDuplicate(Tree oldNode) {
		_size = 0;
		_root = (InternalCandidate) oldNode;
		return oldNode;
	}

	@Override
	public boolean duplicates() {
		return _root != null;
	}

	public QCandidates candidates() {
		return _candidates;
	}
	
	@Override
	public void root(InternalCandidate root) {
		_root = root;
	}
	
	@Override
	public Tree pendingJoins() {
		return _pendingJoins;
	}
	
	@Override
	public int id() {
		return _key;
	}
}