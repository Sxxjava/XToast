/**
 * Copyright (c) 华影网络科技有限公司 宋小雄 2016.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.szhynet.widget.xtoast;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.szhynet.widget.xtoast.utils.OnClickWrapper;
import com.szhynet.widget.xtoast.utils.OnDismissWrapper;
import com.szhynet.widget.xtoast.utils.Style;
import com.szhynet.widget.xtoast.utils.Wrappers;

import java.util.LinkedList;


/**
 * SuperActivityToasts are designed to be used inside of Activities. When the
 * Activity is destroyed the XActivityToast is destroyed along with it.
 */
@SuppressWarnings({"UnusedDeclaration", "BooleanMethodIsAlwaysInverted", "ConstantConditions"})
public class XActivityToast {

    private static final String TAG = "XActivityToast";
    private static final String MANAGER_TAG = "XActivityToast Manager";

    private static final String ERROR_ACTIVITYNULL = " - You cannot pass a null Activity as a parameter.";
    private static final String ERROR_NOTBUTTONTYPE = " - is only compatible with BUTTON type SuperActivityToasts.";
    private static final String ERROR_NOTPROGRESSHORIZONTALTYPE = " - is only compatible with PROGRESS_HORIZONTAL type SuperActivityToasts.";
    private static final String ERROR_NOTEITHERPROGRESSTYPE = " - is only compatible with PROGRESS_HORIZONTAL or PROGRESS type SuperActivityToasts.";

    /* Bundle tag with a hex as a string so it can't interfere with other tags in the bundle */
    private static final String BUNDLE_TAG = "0x532e412e542e";

    private Activity mActivity;
    private XToast.Animations mAnimations = XToast.Animations.FADE;
    private boolean mIsIndeterminate;
    private boolean mIsTouchDismissible;
    private boolean isProgressIndeterminate;
    private boolean showImmediate;
    private Button mButton;
    private XToast.IconPosition mIconPosition;
    private int mDuration = XToast.Duration.SHORT;
    private int mBackground = Style.getBackground(Style.GRAY);
    private int mButtonIcon = XToast.Icon.Dark.UNDO;
    private int mDividerColor = Color.LTGRAY;
    private int mIcon;
    private int mTypefaceStyle = Typeface.NORMAL;
    private int mButtonTypefaceStyle = Typeface.BOLD;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mRootLayout;
    private OnDismissWrapper mOnDismissWrapper;
    private OnClickWrapper mOnClickWrapper;
    private Parcelable mToken;
    private ProgressBar mProgressBar;
    private String mOnClickWrapperTag;
    private String mOnDismissWrapperTag;
    private TextView mMessageTextView;
    private XToast.Type mType = XToast.Type.STANDARD;
    private View mDividerView;
    private ViewGroup mViewGroup;
    private View mToastView;
    private Drawable mBackgroundDrawable=null;

    /**
     * Instantiates a new {@value #TAG}.
     *
     * @param activity {@link Activity}
     */
    public XActivityToast(Activity activity) {

        if (activity == null) {
            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);
        }

        this.mActivity = activity;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                mViewGroup, false);

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

    }

    /**
     * Instantiates a new {@value #TAG} with a specified style.
     *
     * @param activity {@link Activity}
     * @param style    {@link .utils.Style}
     */
    public XActivityToast(Activity activity, Style style) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                mViewGroup, false);

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

        this.setStyle(style);

    }

    /**
     * Instantiates a new {@value #TAG} with a type.
     *
     * @param activity {@link Activity}
     * @param type     {@link XToast.Type}
     */
    public XActivityToast(Activity activity, XToast.Type type) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = type;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        if (type == XToast.Type.STANDARD) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.supertoast, mViewGroup, false);

        } else if (type == XToast.Type.BUTTON) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.superactivitytoast_button, mViewGroup, false);

            mButton = (Button) mToastView
                    .findViewById(R.id.button);

            mDividerView = mToastView
                    .findViewById(R.id.divider);

            mButton.setOnClickListener(mButtonListener);

        } else if (type == XToast.Type.PROGRESS) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresscircle,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        } else if (type == XToast.Type.PROGRESS_HORIZONTAL) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresshorizontal,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        }

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

    }

    /**
     * Instantiates a new {@value #TAG} with a type and a specified style.
     *
     * @param activity {@link Activity}
     * @param type     {@link XToast.Type}
     * @param style    {@link .utils.Style}
     */
    public XActivityToast(Activity activity, XToast.Type type, Style style) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = type;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (ViewGroup) activity
                .findViewById(android.R.id.content);

        if (type == XToast.Type.STANDARD) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.supertoast, mViewGroup, false);

        } else if (type == XToast.Type.BUTTON) {

            mToastView = mLayoutInflater.inflate(
                    R.layout.superactivitytoast_button, mViewGroup, false);

            mButton = (Button) mToastView
                    .findViewById(R.id.button);

            mDividerView = mToastView
                    .findViewById(R.id.divider);

            mButton.setOnClickListener(mButtonListener);

        } else if (type == XToast.Type.PROGRESS) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresscircle,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        } else if (type == XToast.Type.PROGRESS_HORIZONTAL) {

            mToastView = mLayoutInflater.inflate(R.layout.superactivitytoast_progresshorizontal,
                    mViewGroup, false);

            mProgressBar = (ProgressBar) mToastView
                    .findViewById(R.id.progress_bar);

        }

        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout) mToastView
                .findViewById(R.id.root_layout);

        this.setStyle(style);

    }

    /**
     * Shows the {@value #TAG}. If another {@value #TAG} is showing than
     * this one will be added to a queue and shown when the previous {@value #TAG}
     * is dismissed.
     */
    public void show() {

        ManagerXActivityToast.getInstance().add(this);

    }

    /**
     * Returns the type of the {@value #TAG}.
     *
     * @return {@link XToast.Type}
     */
    public XToast.Type getType() {

        return mType;

    }

    /**
     * Sets the message text of the {@value #TAG}.
     *
     * @param text {@link CharSequence}
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Returns the message text of the {@value #TAG}.
     *
     * @return {@link CharSequence}
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * Sets the message typeface style of the {@value #TAG}.
     *
     * @param typeface {@link Typeface} int
     */
    public void setTypefaceStyle(int typeface) {

        mTypefaceStyle = typeface;

        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);

    }

    /**
     * Returns the message typeface style of the {@value #TAG}.
     *
     * @return {@link Typeface} int
     */
    public int getTypefaceStyle() {

        return mTypefaceStyle;

    }

    /**
     * Sets the message text color of the {@value #TAG}.
     *
     * @param textColor {@link Color}
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * Returns the message text color of the {@value #TAG}.
     *
     * @return int
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

    /**
     * Sets the text size of the {@value #TAG} message.
     *
     * @param textSize int
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(textSize);

    }

    /**
     * Used by orientation change recreation
     */
    private void setTextSizeFloat(float textSize) {

        mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }

    /**
     * Returns the text size of the {@value #TAG} message in pixels.
     *
     * @return float
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }

    /**
     * Sets the duration that the {@value #TAG} will show.
     *
     * @param duration {@link XToast.Duration}
     */
    public void setDuration(int duration) {

        this.mDuration = duration;

    }

    /**
     * Returns the duration of the {@value #TAG}.
     *
     * @return int
     */
    public int getDuration() {

        return this.mDuration;

    }

    /**
     * If true will show the {@value #TAG} for an indeterminate time period and ignore any set duration.
     *
     * @param isIndeterminate boolean
     */
    public void setIndeterminate(boolean isIndeterminate) {

        this.mIsIndeterminate = isIndeterminate;

    }

    /**
     * Returns true if the {@value #TAG} is indeterminate.
     *
     * @return boolean
     */
    public boolean isIndeterminate() {

        return this.mIsIndeterminate;

    }

    /**
     * Sets an icon resource to the {@value #TAG} with a specified position.
     *
     * @param iconResource {@link XToast.Icon}
     * @param iconPosition {@link XToast.IconPosition}
     */
    public void setIcon(int iconResource, XToast.IconPosition iconPosition) {

        this.mIcon = iconResource;
        this.mIconPosition = iconPosition;

        if (iconPosition == XToast.IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, mActivity.getResources().getDrawable(iconResource));

        } else if (iconPosition == XToast.IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources()
                    .getDrawable(iconResource), null, null, null);

        } else if (iconPosition == XToast.IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mActivity.getResources().getDrawable(iconResource), null);

        } else if (iconPosition == XToast.IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    mActivity.getResources().getDrawable(iconResource), null, null);

        }

    }

    /**
     * Returns the icon position of the {@value #TAG}.
     *
     * @return {@link XToast.IconPosition}
     */
    public XToast.IconPosition getIconPosition() {

        return this.mIconPosition;

    }

    /**
     * Returns the icon resource of the {@value #TAG}.
     *
     * @return int
     */
    public int getIconResource() {

        return this.mIcon;

    }

    /**
     * Sets the background resource of the {@value #TAG}.
     *
     * @param background {@link XToast.Background}
     */
    public void setBackground(int background) {
        this.mBackground = background;
        mRootLayout.setBackgroundResource(background);
    }
    /**
     * 设置显示背景
     * @param customBackground
     */
    public void setBackground(Drawable customBackground) {
        this.mBackgroundDrawable = customBackground;
        mRootLayout.setBackgroundDrawable(customBackground);
    }
    /**
     * Returns the background resource of the {@value #TAG}.
     *
     * @return int
     */
    public int getBackground() {

        return this.mBackground;

    }

    /**
     * Sets the show/hide animations of the {@value #TAG}.
     *
     * @param animations {@link XToast.Animations}
     */
    public void setAnimations(XToast.Animations animations) {

        this.mAnimations = animations;

    }

    /**
     * Returns the show/hide animations of the {@value #TAG}.
     *
     * @return {@link XToast.Animations}
     */
    public XToast.Animations getAnimations() {

        return this.mAnimations;

    }

    /**
     * If true will show the {@value #TAG} without animation.
     *
     * @param showImmediate boolean
     */
    public void setShowImmediate(boolean showImmediate) {

        this.showImmediate = showImmediate;
    }

    /**
     * Returns true if the {@value #TAG} is set to show without animation.
     *
     * @return boolean
     */
    public boolean getShowImmediate() {

        return this.showImmediate;

    }

    /**
     * If true will dismiss the {@value #TAG} if the user touches it.
     *
     * @param touchDismiss boolean
     */
    public void setTouchToDismiss(boolean touchDismiss) {

        this.mIsTouchDismissible = touchDismiss;

        if (touchDismiss) {

            mToastView.setOnTouchListener(mTouchDismissListener);

        } else {

            mToastView.setOnTouchListener(null);

        }

    }

    /**
     * Returns true if the {@value #TAG} is touch dismissible.
     * @return isTouchDismissible
     */
    public boolean isTouchDismissible() {

        return this.mIsTouchDismissible;

    }

    /**
     * Sets an OnDismissWrapper defined in this library
     * to the {@value #TAG}.
     *
     * @param onDismissWrapper {@link .utils.OnDismissWrapper}
     */
    public void setOnDismissWrapper(OnDismissWrapper onDismissWrapper) {

        this.mOnDismissWrapper = onDismissWrapper;
        this.mOnDismissWrapperTag = onDismissWrapper.getTag();

    }

    /**
     * Used in {@value #MANAGER_TAG}.
     * @return OnDismissWrapper
     */
    protected OnDismissWrapper getOnDismissWrapper() {

        return this.mOnDismissWrapper;

    }

    /**
     * Used in orientation change recreation.
     * @return OnDismissWrapperTag
     */
    private String getOnDismissWrapperTag() {

        return this.mOnDismissWrapperTag;

    }

    /**
     * Dismisses the {@value #TAG}.
     */
    public void dismiss() {

        ManagerXActivityToast.getInstance().removeSuperToast(this);

    }

    /**
     * Sets an OnClickWrapper to the button in a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param onClickWrapper {@link .utils.OnClickWrapper}
     */
    public void setOnClickWrapper(OnClickWrapper onClickWrapper) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setOnClickListenerWrapper()" + ERROR_NOTBUTTONTYPE);

        }

        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();

    }

    /**
     * Sets an OnClickWrapper with a parcelable object to the button in a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param onClickWrapper {@link .utils.OnClickWrapper}
     * @param token {@link Parcelable}
     */
    public void setOnClickWrapper(OnClickWrapper onClickWrapper, Parcelable token) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setOnClickListenerWrapper()" + ERROR_NOTBUTTONTYPE);

        }

        onClickWrapper.setToken(token);

        this.mToken = token;
        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();

    }

    /**
     * Used in orientation change recreation.
     */
    private Parcelable getToken(){

        return this.mToken;

    }

    /**
     * Used in orientation change recreation.
     */
    private String getOnClickWrapperTag() {

        return this.mOnClickWrapperTag;

    }

    /**
     * Sets the icon resource of the button in a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param buttonIcon {@link XToast.Icon}
     */
    public void setButtonIcon(int buttonIcon) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setButtonIcon()" + ERROR_NOTBUTTONTYPE);

        }

        this.mButtonIcon = buttonIcon;

        if (mButton != null) {

            mButton.setCompoundDrawablesWithIntrinsicBounds(mActivity
                    .getResources().getDrawable(buttonIcon), null, null, null);

        }

    }

    /**
     * Sets the icon resource and text of the button in
     * a BUTTON {@link XToast.Type} {@value #TAG}.
     *
     * @param buttonIcon {@link XToast.Icon}
     * @param buttonText {@link CharSequence}
     */
    public void setButtonIcon(int buttonIcon, CharSequence buttonText) {

        if (mType != XToast.Type.BUTTON) {

            Log.w(TAG, "setButtonIcon()" + ERROR_NOTBUTTONTYPE);

        }

        this.mButtonIcon = buttonIcon;

        if (mButton != null) {

            mButton.setCompoundDrawablesWithIntrinsicBounds(mActivity
                    .getResources().getDrawable(buttonIcon), null, null, null);

            mButton.setText(buttonText);

        }

    }

    /**
     * Returns the icon resource of the button in
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getButtonIcon() {

        return this.mButtonIcon;

    }

    /**
     * Sets the divider color of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param dividerColor int
     */
    public void setDividerColor(int dividerColor) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setDivider()" + ERROR_NOTBUTTONTYPE);

        }

        this.mDividerColor = dividerColor;

        if (mDividerView != null) {

            mDividerView.setBackgroundColor(dividerColor);

        }

    }

    /**
     * Returns the divider color of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getDividerColor() {

        return this.mDividerColor;

    }

    /**
     * Sets the button text of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param buttonText {@link CharSequence}
     */
    public void setButtonText(CharSequence buttonText) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setButtonText()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButton.setText(buttonText);

        }

    }

    /**
     * Returns the button text of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return {@link CharSequence}
     */
    public CharSequence getButtonText() {

        if(mButton != null) {

            return mButton.getText();

        } else {

            Log.e(TAG, "getButtonText()" + ERROR_NOTBUTTONTYPE);

            return "";

        }

    }

    /**
     * Sets the typeface style of the button in a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param typefaceStyle {@link Typeface}
     */
    public void setButtonTypefaceStyle(int typefaceStyle) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setButtonTypefaceStyle()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButtonTypefaceStyle = typefaceStyle;

            mButton.setTypeface(mButton.getTypeface(), typefaceStyle);

        }

    }

    /**
     * Returns the typeface style of the button in a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getButtonTypefaceStyle() {

        return this.mButtonTypefaceStyle;

    }

    /**
     * Sets the button text color of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param buttonTextColor {@link Color}
     */
    public void setButtonTextColor(int buttonTextColor) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setButtonTextColor()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButton.setTextColor(buttonTextColor);

        }

    }

    /**
     * Returns the button text color of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getButtonTextColor() {

        if(mButton != null) {

            return mButton.getCurrentTextColor();

        } else {

            Log.e(TAG, "getButtonTextColor()" + ERROR_NOTBUTTONTYPE);

            return 0;

        }

    }

    /**
     * Sets the button text size of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param buttonTextSize int
     */
    public void setButtonTextSize(int buttonTextSize) {

        if (mType != XToast.Type.BUTTON) {

            Log.e(TAG, "setButtonTextSize()" + ERROR_NOTBUTTONTYPE);

        }

        if (mButton != null) {

            mButton.setTextSize(buttonTextSize);

        }

    }

    /**
     * Used by orientation change recreation
     */
    private void setButtonTextSizeFloat(float buttonTextSize) {

        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);

    }

    /**
     * Returns the button text size of a BUTTON
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return float
     */
    public float getButtonTextSize() {

        if(mButton != null) {

            return mButton.getTextSize();

        } else {

            Log.e(TAG, "getButtonTextSize()" + ERROR_NOTBUTTONTYPE);

            return 0.0f;

        }

    }

    /**
     * Sets the progress of the progressbar in a PROGRESS_HORIZONTAL
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param progress int
     */
    public void setProgress(int progress) {

        if (mType != XToast.Type.PROGRESS_HORIZONTAL) {

            Log.e(TAG, "setProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

        }

        if (mProgressBar != null) {

            mProgressBar.setProgress(progress);

        }

    }

    /**
     * Returns the progress of the progressbar in a PROGRESS_HORIZONTAL
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getProgress() {

        if(mProgressBar != null) {

            return mProgressBar.getProgress();

        } else {

            Log.e(TAG, "getProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

            return 0;

        }

    }

    /**
     * Sets the maximum value of the progressbar in a PROGRESS_HORIZONTAL
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param maxProgress int
     */
    public void setMaxProgress(int maxProgress) {

        if (mType != XToast.Type.PROGRESS_HORIZONTAL) {

            Log.e(TAG, "setMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

        }

        if (mProgressBar != null) {

            mProgressBar.setMax(maxProgress);

        }

    }

    /**
     * Returns the maximum value of the progressbar in a PROGRESS_HORIZONTAL
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return int
     */
    public int getMaxProgress() {

        if(mProgressBar != null) {

            return mProgressBar.getMax();

        } else {

            Log.e(TAG, "getMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

            return 0;

        }

    }

    /**
     * Sets an indeterminate value to the progressbar of a PROGRESS
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param isIndeterminate boolean
     */
    public void setProgressIndeterminate(boolean isIndeterminate) {

        if (mType != XToast.Type.PROGRESS_HORIZONTAL && mType != XToast.Type.PROGRESS) {

            Log.e(TAG, "setProgressIndeterminate()" + ERROR_NOTEITHERPROGRESSTYPE);

        }

        this.isProgressIndeterminate = isIndeterminate;

        if (mProgressBar != null) {

            mProgressBar.setIndeterminate(isIndeterminate);

        }

    }

    /**
     * Returns an indeterminate value to the progressbar of a PROGRESS
     * {@link XToast.Type} {@value #TAG}.
     *
     * @return boolean
     */
    public boolean getProgressIndeterminate() {

        return this.isProgressIndeterminate;

    }

    /**
     * Returns the {@value #TAG} message textview.
     *
     * @return {@link TextView}
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the {@value #TAG} view.
     *
     * @return {@link View}
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the {@value #TAG} is showing.
     *
     * @return boolean
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the calling activity of the {@value #TAG}.
     *
     * @return {@link Activity}
     */
    public Activity getActivity() {

        return mActivity;

    }

    /**
     * Returns the viewgroup that the {@value #TAG} is attached to.
     *
     * @return {@link ViewGroup}
     */
    public ViewGroup getViewGroup() {

        return mViewGroup;

    }

    /**
     * Returns the LinearLayout that the {@value #TAG} is attached to.
     *
     * @return {@link LinearLayout}
     */
    private LinearLayout getRootLayout(){

        return mRootLayout;

    }

    /**
     * Private method used to set a default style to the {@value #TAG}
     */
    private void setStyle(Style style) {

        this.setAnimations(style.animations);
        this.setTypefaceStyle(style.typefaceStyle);
        this.setTextColor(style.textColor);
        if (style.customBackground!=null)
            this.setBackground(style.customBackground);
        else
            this.setBackground(style.background);

        if (this.mType == XToast.Type.BUTTON) {

            this.setDividerColor(style.dividerColor);
            this.setButtonTextColor(style.buttonTextColor);

        }

    }

    /**
     * Returns a standard {@value #TAG}.
     *
     * @param activity         {@link Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link XToast.Duration}
     *
     * @return {@link XActivityToast}
     */
    public static XActivityToast create(Activity activity, CharSequence textCharSequence, int durationInteger) {

        final XActivityToast xActivityToast = new XActivityToast(activity);
        xActivityToast.setText(textCharSequence);
        xActivityToast.setDuration(durationInteger);

        return xActivityToast;

    }

    /**
     * Returns a standard {@value #TAG} with specified animations.
     *
     * @param activity         {@link Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link XToast.Duration}
     * @param animations       {@link XToast.Animations}
     *
     * @return {@link XActivityToast}
     */
    public static XActivityToast create(Activity activity, CharSequence textCharSequence, int durationInteger, XToast.Animations animations) {

        final XActivityToast xActivityToast = new XActivityToast(activity);
        xActivityToast.setText(textCharSequence);
        xActivityToast.setDuration(durationInteger);
        xActivityToast.setAnimations(animations);

        return xActivityToast;

    }

    /**
     * Returns a {@value #TAG} with a specified style.
     * @param activity         {@link Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link XToast.Duration}
     * @param style            {@link .utils.Style}
     *
     * @return {@link XActivityToast}
     */
    public static XActivityToast create(Activity activity, CharSequence textCharSequence, int durationInteger, Style style) {

        final XActivityToast xActivityToast = new XActivityToast(activity);
        xActivityToast.setText(textCharSequence);
        xActivityToast.setDuration(durationInteger);
        xActivityToast.setStyle(style);

        return xActivityToast;

    }

    /**
     * Dismisses and removes all pending/showing {@value #TAG}.
     */
    public static void cancelAllSuperActivityToasts() {

        ManagerXActivityToast.getInstance().cancelAllSuperActivityToasts();

    }

    /**
     * Dismisses and removes all pending/showing {@value #TAG}
     * for a specific activity.
     *
     * @param activity {@link Activity}
     */
    public static void clearSuperActivityToastsForActivity(Activity activity) {

        ManagerXActivityToast.getInstance()
                .cancelAllSuperActivityToastsForActivity(activity);

    }

    /**
     * Saves pending/showing {@value #TAG} to a bundle.
     *
     * @param bundle {@link Bundle}
     */
    public static void onSaveState(Bundle bundle) {

        ReferenceHolder[] list = new ReferenceHolder[ManagerXActivityToast
                .getInstance().getList().size()];

        LinkedList<XActivityToast> lister = ManagerXActivityToast
                .getInstance().getList();

        for (int i = 0; i < list.length; i++) {

            list[i] = new ReferenceHolder(lister.get(i));

        }

        bundle.putParcelableArray(BUNDLE_TAG, list);

        XActivityToast.cancelAllSuperActivityToasts();

    }

    /**
     * Recreates pending/showing {@value #TAG} from orientation change.
     *
     * @param bundle   {@link Bundle}
     * @param activity {@link Activity}
     */
    public static void onRestoreState(Bundle bundle, Activity activity) {

        if (bundle == null) {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);

        int i = 0;

        if (savedArray != null) {

            for (Parcelable parcelable : savedArray) {

                i++;

                new XActivityToast(activity, (ReferenceHolder) parcelable, null, i);

            }

        }

    }

    /**
     * Recreates pending/showing {@value #TAG} from orientation change and
     * reattaches any OnClickWrappers/OnDismissWrappers.
     *
     * @param bundle   {@link Bundle}
     * @param activity {@link Activity}
     * @param wrappers {@link .utils.Wrappers}
     */
    public static void onRestoreState(Bundle bundle, Activity activity, Wrappers wrappers) {

        if (bundle == null) {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);

        int i = 0;

        if (savedArray != null) {

            for (Parcelable parcelable : savedArray) {

                i++;

                new XActivityToast(activity, (ReferenceHolder) parcelable, wrappers, i);

            }

        }

    }

    /**
     * Method used to recreate {@value #TAG} after orientation change
     */
    private XActivityToast(Activity activity, ReferenceHolder referenceHolder, Wrappers wrappers, int position) {

        XActivityToast xActivityToast;

        if (referenceHolder.mType == XToast.Type.BUTTON) {

            xActivityToast = new XActivityToast(activity, XToast.Type.BUTTON);
            xActivityToast.setButtonText(referenceHolder.mButtonText);
            xActivityToast.setButtonTextSizeFloat(referenceHolder.mButtonTextSize);
            xActivityToast.setButtonTextColor(referenceHolder.mButtonTextColor);
            xActivityToast.setButtonIcon(referenceHolder.mButtonIcon);
            xActivityToast.setDividerColor(referenceHolder.mDivider);
            xActivityToast.setButtonTypefaceStyle(referenceHolder.mButtonTypefaceStyle);

            int screenSize = activity.getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;

            /* Changes the size of the BUTTON type XActivityToast to mirror Gmail app */
            if(screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                layoutParams.bottomMargin = (int) activity.getResources().getDimension(R.dimen.buttontoast_hover);
                layoutParams.rightMargin = (int) activity.getResources().getDimension(R.dimen.buttontoast_x_padding);
                layoutParams.leftMargin = (int) activity.getResources().getDimension(R.dimen.buttontoast_x_padding);

                xActivityToast.getRootLayout().setLayoutParams(layoutParams);

            }

            /* Reattach any OnClickWrappers by matching tags sent through parcel */
            if (wrappers != null) {

                for (OnClickWrapper onClickWrapper : wrappers.getOnClickWrappers()) {

                    if (onClickWrapper.getTag().equalsIgnoreCase(referenceHolder.mClickListenerTag)) {

                        xActivityToast.setOnClickWrapper(onClickWrapper, referenceHolder.mToken);

                    }

                }
            }

        } else if (referenceHolder.mType == XToast.Type.PROGRESS) {

            /* PROGRESS {@value #TAG} should be managed by the developer */

            return;

        } else if (referenceHolder.mType == XToast.Type.PROGRESS_HORIZONTAL) {

            /* PROGRESS_HORIZONTAL {@value #TAG} should be managed by the developer */

            return;

        } else {

            xActivityToast = new XActivityToast(activity);

        }

        /* Reattach any OnDismissWrappers by matching tags sent through parcel */
        if (wrappers != null) {

            for (OnDismissWrapper onDismissWrapper : wrappers.getOnDismissWrappers()) {

                if (onDismissWrapper.getTag().equalsIgnoreCase(referenceHolder.mDismissListenerTag)) {

                    xActivityToast.setOnDismissWrapper(onDismissWrapper);

                }

            }
        }

        xActivityToast.setAnimations(referenceHolder.mAnimations);
        xActivityToast.setText(referenceHolder.mText);
        xActivityToast.setTypefaceStyle(referenceHolder.mTypefaceStyle);
        xActivityToast.setDuration(referenceHolder.mDuration);
        xActivityToast.setTextColor(referenceHolder.mTextColor);
        xActivityToast.setTextSizeFloat(referenceHolder.mTextSize);
        xActivityToast.setIndeterminate(referenceHolder.mIsIndeterminate);
        xActivityToast.setIcon(referenceHolder.mIcon, referenceHolder.mIconPosition);
        xActivityToast.setBackground(referenceHolder.mBackground);
        xActivityToast.setTouchToDismiss(referenceHolder.mIsTouchDismissible);

        /* Do not use show animation on recreation of {@value #TAG} that was previously showing */
        if (position == 1) {

            xActivityToast.setShowImmediate(true);

        }

        xActivityToast.show();

    }

    /* This OnTouchListener handles the setTouchToDismiss() function */
    private OnTouchListener mTouchDismissListener = new OnTouchListener() {

        int timesTouched;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            /* Hack to prevent repeat touch events causing erratic behavior */
            if (timesTouched == 0) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    dismiss();

                }

            }

            timesTouched++;

            return false;

        }

    };

    /* This OnClickListener handles the button click event */
    private View.OnClickListener mButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (mOnClickWrapper != null) {

                mOnClickWrapper.onClick(view, mToken);

            }

            dismiss();

            /* Make sure the button cannot be clicked multiple times */
            mButton.setClickable(false);

        }
    };

    /**
     * Parcelable class that saves all data on orientation change
     */
    private static class ReferenceHolder implements Parcelable {

        XToast.Animations mAnimations;
        boolean mIsIndeterminate;
        boolean mIsTouchDismissible;
        float mTextSize;
        float mButtonTextSize;
        XToast.IconPosition mIconPosition;
        int mDuration;
        int mTextColor;
        int mIcon;
        int mBackground;
        int mTypefaceStyle;
        int mButtonTextColor;
        int mButtonIcon;
        int mDivider;
        int mButtonTypefaceStyle;
        Parcelable mToken;
        String mText;
        String mButtonText;
        String mClickListenerTag;
        String mDismissListenerTag;
        XToast.Type mType;

        public ReferenceHolder(XActivityToast xActivityToast) {

            mType = xActivityToast.getType();

            if (mType == XToast.Type.BUTTON) {

                mButtonText = xActivityToast.getButtonText().toString();
                mButtonTextSize = xActivityToast.getButtonTextSize();
                mButtonTextColor = xActivityToast.getButtonTextColor();
                mButtonIcon = xActivityToast.getButtonIcon();
                mDivider = xActivityToast.getDividerColor();
                mClickListenerTag = xActivityToast.getOnClickWrapperTag();
                mButtonTypefaceStyle = xActivityToast.getButtonTypefaceStyle();
                mToken = xActivityToast.getToken();

            }

            if (xActivityToast.getIconResource() != 0 && xActivityToast.getIconPosition() != null) {

                mIcon = xActivityToast.getIconResource();
                mIconPosition = xActivityToast.getIconPosition();

            }

            mDismissListenerTag = xActivityToast.getOnDismissWrapperTag();
            mAnimations = xActivityToast.getAnimations();
            mText = xActivityToast.getText().toString();
            mTypefaceStyle = xActivityToast.getTypefaceStyle();
            mDuration = xActivityToast.getDuration();
            mTextColor = xActivityToast.getTextColor();
            mTextSize = xActivityToast.getTextSize();
            mIsIndeterminate = xActivityToast.isIndeterminate();
            mBackground = xActivityToast.getBackground();
            mIsTouchDismissible = xActivityToast.isTouchDismissible();

        }

        public ReferenceHolder(Parcel parcel) {

            mType = XToast.Type.values()[parcel.readInt()];

            if (mType == XToast.Type.BUTTON) {

                mButtonText = parcel.readString();
                mButtonTextSize = parcel.readFloat();
                mButtonTextColor = parcel.readInt();
                mButtonIcon = parcel.readInt();
                mDivider = parcel.readInt();
                mButtonTypefaceStyle = parcel.readInt();
                mClickListenerTag = parcel.readString();
                mToken = parcel.readParcelable(((Object) this).getClass().getClassLoader());

            }

            boolean hasIcon = parcel.readByte() != 0;

            if (hasIcon) {

                mIcon = parcel.readInt();
                mIconPosition = XToast.IconPosition.values()[parcel.readInt()];

            }

            mDismissListenerTag = parcel.readString();
            mAnimations = XToast.Animations.values()[parcel.readInt()];
            mText = parcel.readString();
            mTypefaceStyle = parcel.readInt();
            mDuration = parcel.readInt();
            mTextColor = parcel.readInt();
            mTextSize = parcel.readFloat();
            mIsIndeterminate = parcel.readByte() != 0;
            mBackground = parcel.readInt();
            mIsTouchDismissible = parcel.readByte() != 0;

        }


        @Override
        public void writeToParcel(Parcel parcel, int i) {

            parcel.writeInt(mType.ordinal());

            if (mType == XToast.Type.BUTTON) {

                parcel.writeString(mButtonText);
                parcel.writeFloat(mButtonTextSize);
                parcel.writeInt(mButtonTextColor);
                parcel.writeInt(mButtonIcon);
                parcel.writeInt(mDivider);
                parcel.writeInt(mButtonTypefaceStyle);
                parcel.writeString(mClickListenerTag);
                parcel.writeParcelable(mToken, 0);

            }

            if (mIcon != 0 && mIconPosition != null) {

                parcel.writeByte((byte) 1);

                parcel.writeInt(mIcon);
                parcel.writeInt(mIconPosition.ordinal());

            } else {

                parcel.writeByte((byte) 0);

            }

            parcel.writeString(mDismissListenerTag);
            parcel.writeInt(mAnimations.ordinal());
            parcel.writeString(mText);
            parcel.writeInt(mTypefaceStyle);
            parcel.writeInt(mDuration);
            parcel.writeInt(mTextColor);
            parcel.writeFloat(mTextSize);
            parcel.writeByte((byte) (mIsIndeterminate ? 1 : 0));
            parcel.writeInt(mBackground);
            parcel.writeByte((byte) (mIsTouchDismissible ? 1 : 0));

        }

        @Override
        public int describeContents() {

            return 0;

        }

        public static final Creator CREATOR = new Creator() {

            public ReferenceHolder createFromParcel(Parcel parcel) {

                return new ReferenceHolder(parcel);

            }

            public ReferenceHolder[] newArray(int size) {

                return new ReferenceHolder[size];

            }

        };

    }

}
