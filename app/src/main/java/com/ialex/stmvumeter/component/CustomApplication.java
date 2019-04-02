package com.ialex.stmvumeter.component;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.ialex.stmvumeter.BuildConfig;
import com.ialex.stmvumeter.injection.AppComponent;
import com.ialex.stmvumeter.injection.AppModule;
import com.ialex.stmvumeter.injection.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;

import androidx.appcompat.app.AppCompatDelegate;
import timber.log.Timber;


/**
 * Created by alex on 23.10.2015.
 */
public class CustomApplication extends Application {
    // This flag should be set to true to enable VectorDrawable support for API < 21
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private AppComponent appComponent;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /* Get the context when you don't have access to it in any other way */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CustomApplication.context = getApplicationContext();

        //Logger.setLogLevel(Logger.LogLevel.DEBUG);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            LeakCanary.install(this);
        } else {
            /*Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new
                    StickersUncaughtExceptionHandler(getContext());
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);*/
            //startCrashlytics();
            Timber.plant(new CrashlyticsTree());
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public static AppComponent component() {
        return ((CustomApplication) getContext()).appComponent;
    }
}