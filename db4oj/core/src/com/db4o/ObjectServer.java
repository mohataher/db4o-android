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
package com.db4o;

import com.db4o.ext.*;

/**
 * The db4o server interface.
 * <br><br>- db4o servers can be opened with {@link Db4oClientServer#openServer(String, int)}.<br>
 * - Direct in-memory connections to servers can be made with {@link #openClient()} <br>
 * - TCP connections are available through {@link Db4oClientServer#openClient(String, int, String, String)}.
 * <br><br>Before connecting clients over TCP, you have to 
 * {@link #grantAccess(String, String)} to the username and password combination
 * that you want to use. 
 * @see Db4oClientServer#openServer(java.lang.String, int) Db4o.openServer
 * @see ExtObjectServer ExtObjectServer for extended functionality
 * 
 * @sharpen.extends System.IDisposable
 */
public interface ObjectServer {
    
    /**
    * Closes the {@link ObjectServer } and writes all cached data.
    * <br><br>
    * @return true - denotes that the last instance connected to the
    * used database file was closed.
    */
    public boolean close();

    /**
     * Returns an  {@link ExtObjectServer} with extended functionality.
     * <br><br>Use this method to conveniently access extended methods.
     * <br><br>The functionality is split to two interfaces to allow newcomers to
     * focus on the essential methods.
     */
    public ExtObjectServer ext();

    /**
     * Grants client access to the specified user with the specified password.
     * <br><br>If the user already exists, the password is changed to 
     * the specified password.<br><br>
     * @param userName the name of the user
     * @param password the password to be used
     */
    public void grantAccess(String userName, String password);

    /**
     * Opens a client against this server.
     * 
     * <br><br>A client opened with this method operates within the same VM
     * as the server. Since an embedded client use direct communication, without
     * an in-between socket connection, performance will be better than a client
     * opened with 
     * {@link Db4oClientServer#openClient(java.lang.String, int, java.lang.String, java.lang.String)}
     * 
     * <br><br>Every client has it's own transaction and uses it's own cache
     * for it's own version of all persistent objects.
     * 
     */
    public ObjectContainer openClient();
}
