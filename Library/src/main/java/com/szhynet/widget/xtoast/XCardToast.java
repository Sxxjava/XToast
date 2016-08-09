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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.szhynet.widget.xtoast.utils.OnClickWrapper;
import com.szhynet.widget.xtoast.utils.OnDismissWrapper;
import com.szhynet.widget.xtoast.utils.Style;
import com.szhynet.widget.xtoast.utils.SwipeDismissListener;
import com.szhynet.widget.xtoast.utils.Wrappers;

import java.util.LinkedList;

/**
 * SuperCardToasts are designed to be used inside of activities. SuperCardToasts
 * are designed to be displayed at the top of an activity to display messages.
 */
@SuppressWarnings("UnusedDeclaration")
public class XCardToast {

    private static final String TAG = "XCardToast";
    private static final String MANAGER_TAG = "XCardToast Manager";

    private static final String ERROR_ACTIVITYNULL = " - You cannot pass a null Activity as a parameter.";
    private static final String ERROR_CONTAINERNULL = " - You must have a LinearLayout with the id of card_container in your layout!";
    private static final String ERROR_VIEWCONTAINERNULL = " - Either the View or Container was null when trying to dismiss.";
    private static final String ERROR_NOTBUTTONTYPE = " is only compatible with BUTTON type SuperCardToasts.";
    private static final String ERROR_NOTPROGRESSHORIZONTALTYPE = " is only compatible with PROGRESS_HORIZONTAL type SuperCardToasts.";

    private static final String WARNING_PREHONEYCOMB = "Swipe to dismiss was enabled but the SDK version is pre-Honeycomb";

    /* Bundle tag with a hex as a string so it can't interfere with other tags in the bundle */
    private static final String BUNDLE_TAG = "0x532e432e542e";

    private Activity mActivity;
    private XToast.Animations mAnimations = XToast.Animations.FADE;
    private boolean mIsIndeterminate;
    private boolean mIsTouchDismissible;
    private boolean mIsSwipeDismissible;
    private boolean isProgressIndeterminate;
    private boolean showImmediate;
    private Button mButton;
    private Handler mHandler;
    private XToast.IconPosition mIconPosition;
    private int mDuration = XToast.Duration.SHORT;
    private int mIcon;
    private int mBackground = (R.drawable.background_standard_gray);
    private int mTypeface = Typeface.NORMAL;
    private int mButtonTypefaceStyle = Typeface.BOLD;
    private int mButtonIcon = XToast.Icon.Dark.UNDO;
    private int mDividerColor = Color.DKGRAY;
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
    private ViewGroup mViewGroup;
    private View mToastView;
    private View mDividerView;


    /**
     * Instantiates a new {@value #TAG}.
     *
     * @param activity {@link Activity}
     */
    @SuppressWarnings("ConstantConditions")
    public XCardToast(Activity activity) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = XToast.Type.STANDARD;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (LinearLayout) activity
                .findViewById(R.id.card_container);

        if (mViewGroup == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTAINERNULL);

        }

        mToastView = mLayoutInflater
                .inflate(R.layout.supercardtoast, mViewGroup, false);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

    }

    /**
     * Instantiates a new {@value #TAG} with a specified default style.
     *
     * @param activity     {@link Activity}
     * @param style {@link .utils.Style}
     */
    @SuppressWarnings("ConstantConditions")
    public XCardToast(Activity activity, Style style) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = XToast.Type.STANDARD;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (LinearLayout) activity
                .findViewById(R.id.card_container);

        if (mViewGroup == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTAINERNULL);

        }

        mToastView = mLayoutInflater
                .inflate(R.layout.supercardtoast, mViewGroup, false);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        this.setStyle(style);

    }

    /**
     * Instantiates a new {@value #TAG} with a type.
     *
     * @param activity {@link Activity}
     * @param type     {@link XToast.Type}
     */
    @SuppressWarnings("ConstantConditions")
    public XCardToast(Activity activity, XToast.Type type) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = type;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (LinearLayout) activity
                .findViewById(R.id.card_container);

        if (mViewGroup == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTAINERNULL);

        }

        if (type == XToast.Type.BUTTON) {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast_button, mViewGroup, false);

            mButton = (Button)
                    mToastView.findViewById(R.id.button);

            mDividerView = mToastView.findViewById(R.id.divider);

            mButton.setOnClickListener(mButtonListener);

        } else if (type == XToast.Type.PROGRESS) {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast_progresscircle, mViewGroup, false);

            mProgressBar = (ProgressBar)
                    mToastView.findViewById(R.id.progress_bar);

        } else if (type == XToast.Type.PROGRESS_HORIZONTAL) {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast_progresshorizontal, mViewGroup, false);

            mProgressBar = (ProgressBar)
                    mToastView.findViewById(R.id.progress_bar);

        } else {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast, mViewGroup, false);

        }

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

    }

    /**
     * Instantiates a new {@value #TAG} with a type and a specified style.
     *
     * @param activity     {@link Activity}
     * @param type         {@link XToast.Type}
     * @param style {@link .utils.Style}
     */
    @SuppressWarnings("ConstantConditions")
    public XCardToast(Activity activity, XToast.Type type, Style style) {

        if (activity == null) {

            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);

        }

        this.mActivity = activity;
        this.mType = type;

        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mViewGroup = (LinearLayout) activity
                .findViewById(R.id.card_container);

        if (mViewGroup == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTAINERNULL);

        }

        if (type == XToast.Type.BUTTON) {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast_button, mViewGroup, false);

            mButton = (Button)
                    mToastView.findViewById(R.id.button);

            mDividerView = mToastView.findViewById(R.id.divider);

            mButton.setOnClickListener(mButtonListener);

        } else if (type == XToast.Type.PROGRESS) {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast_progresscircle, mViewGroup, false);

            mProgressBar = (ProgressBar)
                    mToastView.findViewById(R.id.progress_bar);

        } else if (type == XToast.Type.PROGRESS_HORIZONTAL) {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast_progresshorizontal, mViewGroup, false);

            mProgressBar = (ProgressBar)
                    mToastView.findViewById(R.id.progress_bar);

        } else {

            mToastView = mLayoutInflater
                    .inflate(R.layout.supercardtoast, mViewGroup, false);

        }

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        this.setStyle(style);

    }

    /**
     * Shows the {@value #TAG}. If another {@value #TAG} is showing than
     * this one will be added underneath.
     */
    public void show() {

        ManagerXCardToast.getInstance().add(this);

        if (!mIsIndeterminate) {

            mHandler = new Handler();
            mHandler.postDelayed(mHideRunnable, mDuration);

        }

        mViewGroup.addView(mToastView);

        if (!showImmediate) {

            final Animation animation = this.getShowAnimation();

            /* Invalidate the ViewGroup after the show animation completes **/
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationEnd(Animation arg0) {

                    /* Must use Handler to modify ViewGroup in onAnimationEnd() **/
                    Handler mHandler = new Handler();
                    mHandler.post(mInvalidateRunnable);

                }

                @Override
                public void onAnimationRepeat(Animation arg0) {

                    /* Do nothing */

                }

                @Override
                public void onAnimationStart(Animation arg0) {

                    /* Do nothing */

                }

            });

            mToastView.startAnimation(animation);

        }

    }

    /**
     * Returns the {@link XToast.Type} of {@value #TAG}.
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
     * Sets the message {@link Typeface} style of the {@value #TAG}.
     *
     * @param typeface {@link Typeface}
     */
    public void setTypefaceStyle(int typeface) {

        mTypeface = typeface;

        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);

    }

    /**
     * Returns the message {@link Typeface} style of the {@value #TAG}.
     *
     * @return int
     */
    public int getTypefaceStyle() {

        return mTypeface;

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
     * @param icon         {@link XToast.Icon}
     * @param iconPosition {@link XToast.IconPosition}
     */
    public void setIcon(int icon, XToast.IconPosition iconPosition) {

        this.mIcon = icon;
        this.mIconPosition = iconPosition;

        if (iconPosition == XToast.IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, mActivity.getResources().getDrawable(icon));

        } else if (iconPosition == XToast.IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources()
                    .getDrawable(icon), null, null, null);

        } else if (iconPosition == XToast.IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mActivity.getResources().getDrawable(icon), null);

        } else if (iconPosition == XToast.IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    mActivity.getResources().getDrawable(icon), null, null);

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
     * Sets the background resource of the {@value #TAG}. The KitKat style backgrounds
     * included with this library are NOT compatible with {@value #TAG}.
     *
     * @param background {@link XToast.Background}
     */
    public void setBackground(int background) {

        this.mBackground = checkForKitKatBackgrounds(background);

        mRootLayout.setBackgroundResource(mBackground);

    }

    /**
     * Make sure KitKat style backgrounds are not used with {@value #TAG}.
     *
     * @return int
     */
    private int checkForKitKatBackgrounds(int background) {

        if(background == R.drawable.background_kitkat_black) {

            return (R.drawable.background_standard_black);

        } else if(background == R.drawable.background_kitkat_blue) {

            return (R.drawable.background_standard_blue);

        } else if(background == R.drawable.background_kitkat_gray) {

            return (R.drawable.background_standard_gray);

        } else if(background == R.drawable.background_kitkat_green) {

            return (R.drawable.background_standard_green);

        } else if(background == R.drawable.background_kitkat_orange) {

            return (R.drawable.background_standard_orange);

        } else if(background == R.drawable.background_kitkat_purple) {

            return (R.drawable.background_standard_purple);

        } else if(background == R.drawable.background_kitkat_red) {

            return (R.drawable.background_standard_red);

        } else if(background == R.drawable.background_kitkat_white) {

            return (R.drawable.background_standard_white);

        } else {

           return background;

        }

    }

    /**
     * Returns the background resource of the {@value #TAG}.
     *
     * @return int
     */
    public int getBackgroundResource() {

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
     *
     * @return boolean
     */
    public boolean isTouchDismissible() {

        return this.mIsTouchDismissible;

    }

    /**
     * If true will dismiss the {@value #TAG} if the user swipes it.
     *
     * @param swipeDismiss boolean
     */
    public void setSwipeToDismiss(boolean swipeDismiss) {

        this.mIsSwipeDismissible = swipeDismiss;

        if (swipeDismiss) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {

                final SwipeDismissListener swipeDismissListener = new SwipeDismissListener(
                        mToastView, new SwipeDismissListener.OnDismissCallback() {

                    @Override
                    public void onDismiss(View view) {

                        dismissImmediately();

                    }

                });

                mToastView.setOnTouchListener(swipeDismissListener);

            } else {

                Log.w(TAG, WARNING_PREHONEYCOMB);

            }

        } else {

            mToastView.setOnTouchListener(null);

        }

    }

    /**
     * Returns true if the {@value #TAG} is swipe dismissible.
     *
     * @return boolean
     */
    public boolean isSwipeDismissible() {

        return mIsSwipeDismissible;

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
     */
    private String getDismissListenerTag() {

        return mOnDismissWrapperTag;

    }

    /**
     * Dismisses the {@value #TAG}.
     */
    public void dismiss() {

        ManagerXCardToast.getInstance().remove(this);

        dismissWithAnimation();

    }

    /**
     * Dismisses the XCardToast without an animation.
     */
    public void dismissImmediately() {

        ManagerXCardToast.getInstance().remove(this);

        if (mHandler != null) {

            mHandler.removeCallbacks(mHideRunnable);
            mHandler.removeCallbacks(mHideWithAnimationRunnable);
            mHandler = null;

        }

        if (mToastView != null && mViewGroup != null) {

            mViewGroup.removeView(mToastView);

            if (mOnDismissWrapper != null) {

                mOnDismissWrapper.onDismiss(getView());

            }

            mToastView = null;

        } else {

            Log.e(TAG, ERROR_VIEWCONTAINERNULL);

        }

    }

    /**
     * Hide the XCardToast and animate the Layout. Post Honeycomb only. *
     */
    @SuppressLint("NewApi")
    private void dismissWithLayoutAnimation() {

        if (mToastView != null) {

            mToastView.setVisibility(View.INVISIBLE);

            final ViewGroup.LayoutParams layoutParams = mToastView.getLayoutParams();
            final int originalHeight = mToastView.getHeight();

            ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1)
                    .setDuration(mActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));

            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {

                    Handler mHandler = new Handler();
                    mHandler.post(mHideImmediateRunnable);

                }

            });

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                @SuppressWarnings("ConstantConditions")
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    if (mToastView != null) {

                        try {

                            layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                            mToastView.setLayoutParams(layoutParams);

                        } catch (NullPointerException e) {

                            /* Do nothing */

                        }


                    }

                }

            });

            animator.start();

        } else {

            dismissImmediately();

        }

    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void dismissWithAnimation() {

        Animation animation = this.getDismissAnimation();

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                    /* Must use Handler to modify ViewGroup in onAnimationEnd() **/
                    Handler handler = new Handler();
                    handler.post(mHideWithAnimationRunnable);

                } else {

                    /* Must use Handler to modify ViewGroup in onAnimationEnd() **/
                    Handler handler = new Handler();
                    handler.post(mHideImmediateRunnable);

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                /* Do nothing */

            }

            @Override
            public void onAnimationStart(Animation animation) {

                /* Do nothing */

            }

        });

        if (mToastView != null) {

            mToastView.startAnimation(animation);

        }

    }

    /**
     * Sets an OnClickWrapper to the button in a
     * a BUTTON {@link XToast.Type} {@value #TAG}.
     *
     * @param onClickWrapper {@link .utils.OnClickWrapper}
     */
    public void setOnClickWrapper(OnClickWrapper onClickWrapper) {

        if (mType != XToast.Type.BUTTON) {

            Log.w(TAG, "setOnClickListenerWrapper()" + ERROR_NOTBUTTONTYPE);

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

        return mToken;

    }

    /**
     * Used in orientation change recreation.
     */
    private String getOnClickWrapperTag() {

        return mOnClickWrapperTag;

    }

    /**
     * Sets the icon resource of the button in
     * a BUTTON {@link XToast.Type} {@value #TAG}.
     *
     * @param buttonIcon {@link XToast.Icon}
     */
    public void setButtonIcon(int buttonIcon) {

        if (mType != XToast.Type.BUTTON) {

            Log.w(TAG, "setButtonIcon()" + ERROR_NOTBUTTONTYPE);

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

            Log.w(TAG, "setDivider()" + ERROR_NOTBUTTONTYPE);

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

            Log.w(TAG, "setButtonText()" + ERROR_NOTBUTTONTYPE);

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

            Log.w(TAG, "setButtonTypefaceStyle()" + ERROR_NOTBUTTONTYPE);

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

            Log.w(TAG, "setButtonTextColor()" + ERROR_NOTBUTTONTYPE);

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

            Log.w(TAG, "setButtonTextSize()" + ERROR_NOTBUTTONTYPE);

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

        if(mButton != null){

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

            Log.w(TAG, "setProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

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

            Log.e(TAG, "ProgressBar" + ERROR_NOTPROGRESSHORIZONTALTYPE);

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

        if(mProgressBar != null){

            return mProgressBar.getMax();

        } else {

            Log.e(TAG, "getMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

            return mProgressBar.getMax();

        }

    }

    /**
     * Sets an indeterminate value to the progressbar of a PROGRESS
     * {@link XToast.Type} {@value #TAG}.
     *
     * @param isIndeterminate boolean
     */
    public void setProgressIndeterminate(boolean isIndeterminate) {

        if (mType != XToast.Type.PROGRESS_HORIZONTAL) {

            Log.e(TAG, "setProgressIndeterminate()" + ERROR_NOTPROGRESSHORIZONTALTYPE);

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
     * Private method used to set a default style to the {@value #TAG}
     */
    private void setStyle(Style style) {

        this.setAnimations(style.animations);
        this.setTypefaceStyle(style.typefaceStyle);
        this.setTextColor(style.textColor);
        this.setBackground(style.background);

        if(this.mType == XToast.Type.BUTTON) {

            this.setDividerColor(style.dividerColor);
            this.setButtonTextColor(style.buttonTextColor);

        }

    }

    /**
     * Runnable to dismiss the {@value #TAG} with animation.
     */
    private final Runnable mHideRunnable = new Runnable() {

        @Override
        public void run() {

            dismiss();

        }

    };

    /**
     * Runnable to dismiss the {@value #TAG} without animation.
     */
    private final Runnable mHideImmediateRunnable = new Runnable() {

        @Override
        public void run() {

            dismissImmediately();

        }

    };

    /**
     * Runnable to dismiss the {@value #TAG} with layout animation.
     */
    private final Runnable mHideWithAnimationRunnable = new Runnable() {

        @Override
        public void run() {

            dismissWithLayoutAnimation();

        }

    };

    /**
     * Runnable to invalidate the layout containing the {@value #TAG}.
     */
    private final Runnable mInvalidateRunnable = new Runnable() {

        @Override
        public void run() {

            if (mViewGroup != null) {

                mViewGroup.postInvalidate();

            }

        }

    };

    private Animation getShowAnimation() {

        if (this.getAnimations() == XToast.Animations.FLYIN) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimations() == XToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimations() == XToast.Animations.POPUP) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else {

            Animation animation = new AlphaAnimation(0f, 1f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            return animation;

        }


    }

    private Animation getDismissAnimation() {

        if (this.getAnimations() == XToast.Animations.FLYIN) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, .75f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new AccelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimations() == XToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (this.getAnimations() == XToast.Animations.POPUP) {

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.1f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else {

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setInterpolator(new AccelerateInterpolator());

            return alphaAnimation;

        }

    }

    /**
     * Returns a standard {@value #TAG}.
     * <br>
     * IMPORTANT: Activity layout should contain a linear layout
     * with the id card_container
     * <br>
     *
     * @param activity         {@link Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link XToast.Duration}
     *
     * @return XCardToast
     */
    public static XCardToast create(Activity activity, CharSequence textCharSequence, int durationInteger) {

        XCardToast xCardToast = new XCardToast(activity);
        xCardToast.setText(textCharSequence);
        xCardToast.setDuration(durationInteger);

        return xCardToast;

    }

    /**
     * Returns a standard {@value #TAG} with specified animations.
     * <br>
     * IMPORTANT: Activity layout should contain a linear layout
     * with the id card_container
     * <br>
     *
     * @param activity         {@link Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link XToast.Duration}
     * @param animations       {@link XToast.Animations}
     *
     * @return XCardToast
     */
    public static XCardToast create(Activity activity, CharSequence textCharSequence, int durationInteger, XToast.Animations animations) {

        XCardToast xCardToast = new XCardToast(activity);
        xCardToast.setText(textCharSequence);
        xCardToast.setDuration(durationInteger);
        xCardToast.setAnimations(animations);

        return xCardToast;

    }

    /**
     * Returns a {@value #TAG} with a specified style.
     * <br>
     * IMPORTANT: Activity layout should contain a linear layout
     * with the id card_container
     * <br>
     *
     * @param activity         {@link Activity}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link XToast.Duration}
     * @param style            {@link .utils.Style}
     *
     * @return XCardToast
     */
    public static XCardToast create(Activity activity, CharSequence textCharSequence, int durationInteger, Style style) {

        XCardToast xCardToast = new XCardToast(activity);
        xCardToast.setText(textCharSequence);
        xCardToast.setDuration(durationInteger);
        xCardToast.setStyle(style);

        return xCardToast;

    }



    /**
     * Dismisses and removes all showing/pending SuperCardToasts.
     */
    public static void cancelAllSuperCardToasts() {

        ManagerXCardToast.getInstance().cancelAllSuperActivityToasts();

    }

    /**
     * Saves pending/shown SuperCardToasts to a bundle.
     *
     * @param bundle Use onSaveInstanceState() bundle
     */
    public static void onSaveState(Bundle bundle) {

        ReferenceHolder[] list = new ReferenceHolder[ManagerXCardToast
                .getInstance().getList().size()];

        LinkedList<XCardToast> lister = ManagerXCardToast
                .getInstance().getList();

        for (int i = 0; i < list.length; i++) {

            list[i] = new ReferenceHolder(lister.get(i));

        }

        bundle.putParcelableArray(BUNDLE_TAG, list);

        XCardToast.cancelAllSuperCardToasts();

    }

    /**
     * Returns and shows pending/shown SuperCardToasts from orientation change.
     *
     * @param bundle   Use onCreate() bundle
     * @param activity The current activity
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

                new XCardToast(activity, (ReferenceHolder) parcelable, null, i);

            }

        }

    }

    /**
     * Returns and shows pending/shown {@value #TAG} from orientation change and
     * reattaches any OnClickWrappers/OnDismissWrappers.
     *
     * @param bundle          Use onCreate() bundle
     * @param activity        The current activity
     * @param wrappers        {@link .utils.Wrappers}
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

                new XCardToast(activity, (ReferenceHolder) parcelable, wrappers, i);

            }

        }

    }

    /**
     * Method used to recreate {@value #TAG} after orientation change
     */
    private XCardToast(Activity activity, ReferenceHolder referenceHolder, Wrappers wrappers, int position) {

        XCardToast xCardToast;

        if(referenceHolder.mType == XToast.Type.BUTTON) {

            xCardToast = new XCardToast(activity, XToast.Type.BUTTON);
            xCardToast.setButtonText(referenceHolder.mButtonText);
            xCardToast.setButtonTextSizeFloat(referenceHolder.mButtonTextSize);
            xCardToast.setButtonTextColor(referenceHolder.mButtonTextColor);
            xCardToast.setButtonIcon(referenceHolder.mButtonIcon);
            xCardToast.setDividerColor(referenceHolder.mButtonDivider);
            xCardToast.setButtonTypefaceStyle(referenceHolder.mButtonTypefaceStyle);

            if(wrappers != null) {

                for (OnClickWrapper onClickWrapper : wrappers.getOnClickWrappers()) {

                    if (onClickWrapper.getTag().equalsIgnoreCase(referenceHolder.mClickListenerTag)) {

                        xCardToast.setOnClickWrapper(onClickWrapper, referenceHolder.mToken);

                    }

                }
            }

        } else if (referenceHolder.mType == XToast.Type.PROGRESS) {

            /* PROGRESS style SuperCardToasts should be managed by the developer */

            return;

        } else if (referenceHolder.mType == XToast.Type.PROGRESS_HORIZONTAL) {

            /* PROGRESS_HORIZONTAL style SuperCardToasts should be managed by the developer */

            return;

        } else {

            xCardToast = new XCardToast(activity);

        }

        if (wrappers != null) {

            for (OnDismissWrapper onDismissListenerWrapper : wrappers.getOnDismissWrappers()) {

                if (onDismissListenerWrapper.getTag().equalsIgnoreCase(referenceHolder.mDismissListenerTag)) {

                    xCardToast.setOnDismissWrapper(onDismissListenerWrapper);

                }

            }
        }

        xCardToast.setAnimations(referenceHolder.mAnimations);
        xCardToast.setText(referenceHolder.mText);
        xCardToast.setTypefaceStyle(referenceHolder.mTypefaceStyle);
        xCardToast.setDuration(referenceHolder.mDuration);
        xCardToast.setTextColor(referenceHolder.mTextColor);
        xCardToast.setTextSizeFloat(referenceHolder.mTextSize);
        xCardToast.setIndeterminate(referenceHolder.mIsIndeterminate);
        xCardToast.setIcon(referenceHolder.mIcon, referenceHolder.mIconPosition);
        xCardToast.setBackground(referenceHolder.mBackground);

        /* Must use if else statements here to prevent erratic behavior */
        if (referenceHolder.mIsTouchDismissible) {

            xCardToast.setTouchToDismiss(true);

        } else if (referenceHolder.mIsSwipeDismissible) {

            xCardToast.setSwipeToDismiss(true);

        }

        xCardToast.setShowImmediate(true);
        xCardToast.show();

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
        boolean mIsSwipeDismissible;
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
        int mButtonDivider;
        int mButtonTypefaceStyle;
        Parcelable mToken;
        String mText;
        String mButtonText;
        String mClickListenerTag;
        String mDismissListenerTag;
        XToast.Type mType;

        public ReferenceHolder(XCardToast xCardToast) {

            mType = xCardToast.getType();

            if (mType == XToast.Type.BUTTON) {

                mButtonText = xCardToast.getButtonText().toString();
                mButtonTextSize = xCardToast.getButtonTextSize();
                mButtonTextColor = xCardToast.getButtonTextColor();
                mButtonIcon = xCardToast.getButtonIcon();
                mButtonDivider = xCardToast.getDividerColor();
                mClickListenerTag = xCardToast.getOnClickWrapperTag();
                mButtonTypefaceStyle = xCardToast.getButtonTypefaceStyle();
                mToken = xCardToast.getToken();

            }

            if (xCardToast.getIconResource() != 0 && xCardToast.getIconPosition() != null) {

                mIcon = xCardToast.getIconResource();
                mIconPosition = xCardToast.getIconPosition();

            }

            mDismissListenerTag = xCardToast.getDismissListenerTag();
            mAnimations = xCardToast.getAnimations();
            mText = xCardToast.getText().toString();
            mTypefaceStyle = xCardToast.getTypefaceStyle();
            mDuration = xCardToast.getDuration();
            mTextColor = xCardToast.getTextColor();
            mTextSize = xCardToast.getTextSize();
            mIsIndeterminate = xCardToast.isIndeterminate();
            mBackground = xCardToast.getBackgroundResource();
            mIsTouchDismissible = xCardToast.isTouchDismissible();
            mIsSwipeDismissible = xCardToast.isSwipeDismissible();

        }

        public ReferenceHolder(Parcel parcel) {

            mType = XToast.Type.values()[parcel.readInt()];

            if (mType == XToast.Type.BUTTON) {

                mButtonText = parcel.readString();
                mButtonTextSize = parcel.readFloat();
                mButtonTextColor = parcel.readInt();
                mButtonIcon = parcel.readInt();
                mButtonDivider = parcel.readInt();
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
            mIsSwipeDismissible = parcel.readByte() != 0;

        }


        @Override
        public void writeToParcel(Parcel parcel, int i) {

            parcel.writeInt(mType.ordinal());

            if (mType == XToast.Type.BUTTON) {

                parcel.writeString(mButtonText);
                parcel.writeFloat(mButtonTextSize);
                parcel.writeInt(mButtonTextColor);
                parcel.writeInt(mButtonIcon);
                parcel.writeInt(mButtonDivider);
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
            parcel.writeByte((byte) (mIsSwipeDismissible ? 1 : 0));

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
