package us.ktv.android.utils.databinding;

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
    public static void loadImage(SimpleDraweeView mSimpleDraweeView, String uriStr) {
        if (!TextUtils.isEmpty(uriStr)) {
            Uri uri = Uri.parse(uriStr);
            mSimpleDraweeView.setController(SimpleDraweeViewConfig.getDraweeController(SimpleDraweeViewConfig.getImageRequest(uri), mSimpleDraweeView));
        }
    }
}
