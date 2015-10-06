package us.ktv.android;

import android.app.Application;

/**
 * Created by nick on 15-10-6.
 */
public class MicApplication extends Application {

    private static Application application;

    private static Application getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
