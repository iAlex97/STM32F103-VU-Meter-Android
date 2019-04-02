package com.ialex.stmvumeter.injection;

import android.content.Context;

import com.ialex.stmvumeter.MainActivity;
import com.ialex.stmvumeter.component.CustomApplication;
import com.ialex.stmvumeter.data.DataModule;
import com.ialex.stmvumeter.data.DataRepository;
import com.ialex.stmvumeter.data.local.prefs.PrefsModule;
import com.ialex.stmvumeter.data.local.prefs.PrefsRepository;
import com.ialex.stmvumeter.data.remote.RemoteModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class, RemoteModule.class, PrefsModule.class})
public interface AppComponent {

    void inject(CustomApplication application);

    void inject(MainActivity x);

    void inject(PrefsRepository prefs);

    PrefsRepository prefsRepository();

    DataRepository dataManager();

    Context context();

    /*AnalyticsHandler analytics();*/

}
