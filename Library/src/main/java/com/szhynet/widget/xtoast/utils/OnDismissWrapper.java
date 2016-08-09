package com.szhynet.widget.xtoast.utils;

import android.view.View;

import com.szhynet.widget.xtoast.XToast;

/**
 *  Class that holds a reference to OnDismissListeners set to button type SuperActivityToasts/SuperCardToasts.
 *  This is used for restoring listeners on orientation changes.
 */
public class OnDismissWrapper implements XToast.OnDismissListener {

    private final String mTag;
    private final XToast.OnDismissListener mOnDismissListener;

    /**
     *  Creates an OnClickWrapper.
     *
     *  @param tag {@link CharSequence} Must be unique to this listener
     *  @param onDismissListener {@link XToast.OnDismissListener}
     */
    public OnDismissWrapper(String tag, XToast.OnDismissListener onDismissListener) {

        this.mTag = tag;
        this.mOnDismissListener = onDismissListener;

    }

    /**
     *  Returns the tag associated with this OnDismissWrapper. This is used to
     *  reattach {@link XToast.OnDismissListener}s.
     *
     *  @return {@link String}
     */
    public String getTag() {

        return mTag;

    }

    @Override
    public void onDismiss(View view) {

        mOnDismissListener.onDismiss(view);

    }

}