package com.realm.database;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            LogUtility.informationMessage(TAG, "LANDSCAPE");
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            LogUtility.informationMessage(TAG, "PORTRAIT");
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        /*
         * Initialize Realm (just once per application)
         * The default Realm file is "default.realm" in Context.getFilesDir();
         * we'll change it to "realm_demo.realm"
         */
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("realm_demo.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        /*
         * The default Realm RealmConfiguration set
         * The getDefaultInstance method instantiates the Realm with a default RealmConfiguration.
         * In activity we get realm instance using getDefaultInstance method with above configuration
         */
        Realm.setDefaultConfiguration(realmConfig);

        if(BuildConfig.DEBUG)
        {
            LogUtility.LOG_ENABLE = true;
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle)
            {

            }

            @Override
            public void onActivityStarted(Activity activity)
            {

            }

            @Override
            public void onActivityResumed(Activity activity)
            {
                LogUtility.informationMessage(TAG, "CURRENT ACTIVITY : "+activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity)
            {

            }

            @Override
            public void onActivityStopped(Activity activity)
            {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle)
            {

            }

            @Override
            public void onActivityDestroyed(Activity activity)
            {

            }
        });
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try
        {
            MultiDex.install(base);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Helper to get the {@link BaseApplication} from the application context.
     */
    @NonNull
    public static BaseApplication getInstance(@NonNull final Context context){
        return (BaseApplication) context.getApplicationContext();
    }
}
