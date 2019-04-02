package com.ialex.stmvumeter.data;

import com.ialex.stmvumeter.data.local.prefs.PrefsRepository;
import com.ialex.stmvumeter.data.remote.Api;

/**
 * Created by ialex on 15.02.2017.
 */

public class DataRepository {

    private Api api;
    private PrefsRepository prefsRepository;

    public DataRepository(Api api, PrefsRepository prefsRepository) {
        this.api = api;
        this.prefsRepository = prefsRepository;
    }


}
