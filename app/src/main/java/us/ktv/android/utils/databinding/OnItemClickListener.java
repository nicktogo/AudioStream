package us.ktv.android.utils.databinding;

import android.view.View;

/**
 * Created by nick on 15-10-6.
 */
public interface OnItemClickListener<T> {
    void onClick(T t, View view);
}
