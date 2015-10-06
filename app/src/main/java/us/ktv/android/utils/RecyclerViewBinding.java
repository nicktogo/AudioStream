package us.ktv.android.utils;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by nick on 15-10-6.
 */
public class RecyclerViewBinding {
    @SuppressWarnings("unchecked")
    @BindingAdapter("imageUrl")
    public static void loadImage(SimpleDraweeView mSimpleDraweeView, String url) {
        if (TextUtils.isEmpty(url)) return;
        Uri uri = Uri.parse(url);
        mSimpleDraweeView.setController(SimpleDraweeViewConfig.getDraweeController(SimpleDraweeViewConfig.getImageRequest(uri), mSimpleDraweeView));
    }
}
