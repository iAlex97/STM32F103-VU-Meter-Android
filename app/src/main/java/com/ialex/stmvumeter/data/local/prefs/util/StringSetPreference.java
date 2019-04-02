package com.ialex.stmvumeter.data.local.prefs.util;

import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by ialex on 15.02.2017.
 */

public class StringSetPreference {
    private final SharedPreferences preferences;
    private final String key;
    private final Set<String> defaultValue;

    public StringSetPreference(SharedPreferences preferences, String key) {
        this(preferences, key, null);
    }

    public StringSetPreference(SharedPreferences preferences, String key, Set<String> defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public Set<String> get() {
        return preferences.getStringSet(key, defaultValue);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(Set<String> value) {
        SharedPreferences.Editor editor = preferences.edit().putStringSet(key, value);
        editor.apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}
