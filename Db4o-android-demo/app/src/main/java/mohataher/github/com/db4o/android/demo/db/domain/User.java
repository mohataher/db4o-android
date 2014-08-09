package mohataher.github.com.db4o.android.demo.db.domain;

import java.util.UUID;

/**
 * Created by malrefaie on 8/9/2014.
 */
public class User {

    String username;
    UUID id;

    public User(String username, UUID id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public static User randomUser(){
        return new User("Mohataher", UUID.randomUUID());
    }

}
