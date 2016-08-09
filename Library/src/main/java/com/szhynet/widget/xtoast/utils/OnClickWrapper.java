package com.szhynet.widget.xtoast.utils;

import android.os.Parcelable;
import android.view.View;

import com.szhynet.widget.xtoast.XToast;


/**
 *  Class that holds a reference to an OnClickListener set to button type XActivityToasts/XCardToasts.
 *  This is used for restoring listeners on orientation changes.
 */
@SuppressWarnings("UnusedParameters")
public class OnClickWrapper implements XToast.OnClickListener {

    private final String mTag;
    private final XToast.OnClickListener mOnClickListener;
    private Parcelable mToken;

    /**
     *  Creates an OnClickWrapper.
     *
     *  @param tag {@link CharSequence} Must be unique to this listener
     *  @param onClickListener {@link XToast.OnClickListener}
     */
    public OnClickWrapper(String tag, XToast.OnClickListener onClickListener) {
        this.mTag = tag;
        this.mOnClickListener = onClickListener;

    }

    /**
     *  Returns the tag associated with this OnClickWrapper. This is used to
     *  reattach {@link XToast.OnClickListener}.
     *
     *  @return {@link String}
     */
    public String getTag() {

        return mTag;

    }

    /**
     *  This is used during XActivityToast/XCardToast recreation and should
     *  never be called by the developer.
     *  @param token Parcelable token
     */
    public void setToken(Parcelable token) {

        this.mToken = token;

    }

    @Override
    public void onClick(View view, Parcelable token) {

        mOnClickListener.onClick(view, mToken);

    }

}