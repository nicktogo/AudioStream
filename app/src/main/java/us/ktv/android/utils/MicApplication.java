package us.ktv.android.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import us.ktv.android.utils.databinding.ImagePipelineConfigFactory;

/**
 * Created by nick on 15-10-6.
 */
public class MicApplication extends Application {

    private static Application application;

    public static Application getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initFresco();
    }

    private void initFresco() {
        Fresco.initialize(getApplicationContext(), ImagePipelineConfigFactory.getImagePipelineConfig(getApplicationContext()));
    }
}
