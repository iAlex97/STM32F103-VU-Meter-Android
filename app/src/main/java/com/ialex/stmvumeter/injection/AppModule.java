package com.ialex.stmvumeter.injection;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context provideContext(Application app) {
        return app.getApplicationContext();
    }

    /*@Provides
    @Singleton
    AppManager provideAppManager(DataRepository dataRepository, BadgeHandler badgeHandler,
                                 AnalyticsHandler analyticsHandler, PrefsRepository
                                         prefsRepository, LanguageUtils languageUtils) {
        return new AppManager(dataRepository, badgeHandler, analyticsHandler, prefsRepository, languageUtils);
    }

    @Provides
    @Singleton
    ForegroundDetector provideForeground(Application app) {
        return new ForegroundDetector(app);
    }

    @Provides
    @Singleton
    AnalyticsHandler provideAnalytics(Context context, Tracker tracker) {
        if (BuildConfig.DEBUG) {
            return new DummyAnalyticsTracker();
        } else {
            AppEventsLogger logger = AppEventsLogger.newLogger(context);
            return new AnalyticsHandler(tracker, logger);
        }
    }*/

    /*@Provides
    @Singleton
    Tracker provideTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        Tracker tracker = analytics.newTracker(""); //todo add this
        tracker.enableAdvertisingIdCollection(true);
        return tracker;
    }*/

   /* @Provides
    @Singleton
    StickerSharer provideStickerSharer(DatabaseRepository databaseRepository,
                                       BubbleForegroundPackageHandler
                                               bubbleForegroundPackageHandler, Context context,
                                       Gson gson) {
        return new StickerSharer(databaseRepository, bubbleForegroundPackageHandler, context, gson);
    }

    @Provides
    @Singleton
    BadgeHandler provideBadgeHandler() {
        return new BadgeHandler();
    }*/
}
