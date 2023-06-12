package com.sid.androsid.ecodrive;

import android.app.Application;

import com.sid.androsid.ecodrive.User;
/**
 * Created by sid on 2019-02-19.
 */

public class UserClient extends Application {
    private User mUser = null;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
