package com.ialex.stmvumeter.data.local.prefs;

import com.google.gson.Gson;
import com.ialex.stmvumeter.component.CustomApplication;
import com.ialex.stmvumeter.data.local.prefs.util.StringPreference;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by ialex on 15.02.2017.
 */

public class PrefsRepository {

    @Inject
    @Named("username")
    StringPreference usernamePreference;

    @Inject
    @Named("password")
    StringPreference passwordPreference;

    @Inject
    @Named("auth_bearer")
    StringPreference authBearerPreference;

    @Inject
    Gson gson;

    public PrefsRepository() {
        CustomApplication.component().inject(this);
    }

    /**
     *
     */

    public String getUsername() {
        return usernamePreference.get();
    }

    public String getPassword() {
        return passwordPreference.get();
    }

    public void setUsername(String username) {
        usernamePreference.set(username);
    }

    public void setPassword(String password) {
        passwordPreference.set(password);
    }

    public void setAuthBearerToken(String newToken) {
        authBearerPreference.set(newToken);
    }

    public String getAuthBearerToken() {
        return authBearerPreference.get();
    }

    public boolean isAuthenticated() {
        return getAuthBearerToken() != null;
    }
}
