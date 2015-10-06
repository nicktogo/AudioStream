package us.ktv.android.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import us.ktv.android.R;

/**
 * Created by nick on 15-10-6.
 */
public class Drawables {

    private Drawables() {

    }

    public static void init(final Resources resources) {
        if (sPlaceholderDrawable == null) {
            sPlaceholderDrawable = resources.getDrawable(R.drawable.default_avatar);
        }
        if (sErrorDrawable == null) {
            sErrorDrawable = resources.getDrawable(R.drawable.default_avatar);
        }
    }

    public static Drawable sPlaceholderDrawable;
    public static Drawable sErrorDrawable;
}
