package us.ktv.android.utils.databinding;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by nick on 15-10-6.
 */
public class SimpleDraweeViewConfig {

    private static final String TAG = SimpleDraweeViewConfig.class.getSimpleName();

    private SimpleDraweeViewConfig() {

    }

    public static ImageRequest getImageRequest(Uri uri) {
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

    }

    public static DraweeController getDraweeController(ImageRequest imageRequest, SimpleDraweeView mSimpleDraweeView) {
        return Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(mSimpleDraweeView.getController())
                .build();
    }
}
