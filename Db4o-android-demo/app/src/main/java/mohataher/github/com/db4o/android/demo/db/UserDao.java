package mohataher.github.com.db4o.android.demo.db;

import android.content.Context;

import mohataher.github.com.db4o.android.demo.db.domain.User;

/**
 * Created by malrefaie on 8/9/2014.
 */
public class UserDao extends Db4OGenericDao<User>{

    /**
     * @param ctx
     */
    public UserDao(Context ctx) {
        super(ctx);
    }
}
