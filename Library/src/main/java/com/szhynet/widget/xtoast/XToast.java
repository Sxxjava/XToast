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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szhynet.widget.xtoast.utils.Style;

/**
 * XToasts are designed to replace stock Android Toasts.
 * If you need to display a XToast inside of an Activity
 * please see {@link XActivityToast}.
 */
@SuppressWarnings("UnusedDeclaration")
public class XToast {

    private static final String TAG = "XToast";

    private static final String ERROR_CONTEXTNULL = " - 上下文对象为null.";
    private static final String ERROR_DURATIONTOOLONG = " - XToast弹出时间太长,已超过4.5秒";
    private Object mBackgroundDrawable=null;

    /**
     * 自定义的XToast按钮点击事件监听器
     * {@link .utils.OnClickWrapper}
     */
    public interface OnClickListener {

        void onClick(View view, Parcelable token);

    }

    /**
     * 自定义的XToast销毁监听器
     * {@link .utils.OnDismissWrapper}
     */
    public interface OnDismissListener {

        void onDismiss(View view);

    }

    /**
     * XToast的默认背景
     */
    public static class Background {

        public static final int BLACK = Style.getBackground(Style.BLACK);
        public static final int BLUE = Style.getBackground(Style.BLUE);
        public static final int GRAY = Style.getBackground(Style.GRAY);
        public static final int GREEN = Style.getBackground(Style.GREEN);
        public static final int ORANGE = Style.getBackground(Style.ORANGE);
        public static final int PURPLE = Style.getBackground(Style.PURPLE);
        public static final int RED = Style.getBackground(Style.RED);
        public static final int WHITE = Style.getBackground(Style.WHITE);

    }

    /**
     * XToast的默认动画
     */
    public enum Animations {
        FADE,
        FLYIN,
        SCALE,
        POPUP
    }

    /**
     * XTast的默认图标
     */
    public static class Icon {

        /**
         * 在深色色背景下XToast的默认图标
         */
        public static class Dark {

            public static final int EDIT = (R.drawable.icon_dark_edit);
            public static final int EXIT = (R.drawable.icon_dark_exit);
            public static final int INFO = (R.drawable.icon_dark_info);
            public static final int REDO = (R.drawable.icon_dark_redo);
            public static final int REFRESH = (R.drawable.icon_dark_refresh);
            public static final int SAVE = (R.drawable.icon_dark_save);
            public static final int SHARE = (R.drawable.icon_dark_share);
            public static final int UNDO = (R.drawable.icon_dark_undo);

        }

        /**
         * 在亮色背景下XToast的默认图标
         */
        public static class Light {

            public static final int EDIT = (R.drawable.icon_light_edit);
            public static final int EXIT = (R.drawable.icon_light_exit);
            public static final int INFO = (R.drawable.icon_light_info);
            public static final int REDO = (R.drawable.icon_light_redo);
            public static final int REFRESH = (R.drawable.icon_light_refresh);
            public static final int SAVE = (R.drawable.icon_light_save);
            public static final int SHARE = (R.drawable.icon_light_share);
            public static final int UNDO = (R.drawable.icon_light_undo);

        }

    }

    /**
     * XToast默认的弹出时间
     */
    public static class Duration {
        /**
         * 显示1秒
         * */
        public static final int VERY_SHORT = 1000;

        /**
         * 显示1.5秒
         * */
        public static final int MEDIUM_SHORT = 1500;
        /**
         * 显示2秒
         * */
        public static final int SHORT = 2000;
        /**
         * 显示2.75秒
         * */
        public static final int MEDIUM = 2750;
        /**
         * 显示3秒
         * */
        public static final int MEDIUM_LONG = 3000;
        /**
         * 显示3.5秒
         * */
        public static final int LONG = 3500;
        /**
         * 显示4.5秒
         * */
        public static final int EXTRA_LONG = 4500;

    }

    /**
     * XToast默认的字体大小
     */
    public static class TextSize {

        public static final int EXTRA_SMALL = (12);
        public static final int SMALL = (14);
        public static final int MEDIUM = (16);
        public static final int LARGE = (18);

    }

    /**
     * XToast显示类型
     */
    public enum Type {

        /**
         * 标准的Toast类,显示单文本消息
         */
        STANDARD,

        /**
         * 圆形进度类型,可以显示进度信息
         */
        PROGRESS,

        /**
         * 水平进度条类型,可以显示进度信息
         */
        PROGRESS_HORIZONTAL,

        /**
         * 按钮类型,需要点击按钮才能销毁XToast
         */
        BUTTON

    }

    /**
     * XToast的icon位置
     */
    public enum IconPosition {

        /**
         * 在文本的左边
         */
        LEFT,

        /**
         * 在文本右边
         */
        RIGHT,

        /**
         * 在文本上方
         */
        TOP,

        /**
         * 在文本下方
         */
        BOTTOM

    }

    private Animations mAnimations = Animations.FADE;
    private Context mContext;
    private int mGravity = Gravity.BOTTOM | Gravity.CENTER;
    private int mDuration = Duration.SHORT;
    private int mTypefaceStyle;
    private int mBackground;
    private int mXOffset = 0;
    private int mYOffset = 0;
    private LinearLayout mRootLayout;
    private OnDismissListener mOnDismissListener;
    private TextView mMessageTextView;
    private View mToastView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    /**
     * 构造XToast, new {@value #TAG}.
     * @param context {@link Context}
     */
    public XToast(Context context) {

        if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

        mYOffset = context.getResources().getDimensionPixelSize(
                R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

    }

    /**
     * 构造XToast, new {@value #TAG} with a specified style.
     *
     * @param context {@link Context}
     * @param style   {@link .utils.Style}
     */
    public XToast(Context context, Style style) {

        if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

        mYOffset = context.getResources().getDimensionPixelSize(
                R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        this.setStyle(style);

    }

    /**
     * 显示XToast
     */
    public void show() {

        mWindowManagerParams = new WindowManager.LayoutParams();

        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
        mWindowManagerParams.windowAnimations = getAnimation();
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowManagerParams.gravity = mGravity;
        mWindowManagerParams.x = mXOffset;
        mWindowManagerParams.y = mYOffset;

        ManagerXToast.getInstance().add(this);

    }

    /**
     * 设置显示文本
     *
     * @param text {@link CharSequence}
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * 获取显示文本
     *
     * @return {@link CharSequence}
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * 设置文本字体
     *
     * @param typeface {@link android.graphics.Typeface} int
     */
    public void setTypefaceStyle(int typeface) {

        mTypefaceStyle = typeface;

        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);

    }

    /**
     * 获取文本字体
     *
     * @return {@link android.graphics.Typeface} int
     */
    public int getTypefaceStyle() {

        return mTypefaceStyle;

    }

    /**
     * 设置字体颜色
     *
     * @param textColor {@link android.graphics.Color}
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * 设置字体颜色
     *
     * @return int
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

    /**
     * 设置字体大小
     *
     * @param textSize int
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(textSize);

    }

    /**
     * 获取字体大下
     *
     * @return float
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }

    /**
     * 设置显示时长
     *
     * @param duration {@link Duration}
     */
    public void setDuration(int duration) {

        if(duration > Duration.EXTRA_LONG) {

            Log.e(TAG, TAG + ERROR_DURATIONTOOLONG);

            this.mDuration = Duration.EXTRA_LONG;

        } else {

            this.mDuration = duration;

        }

    }

    /**
     * 获取显示时长
     *
     * @return int
     */
    public int getDuration() {

        return this.mDuration;

    }

    /**
     * 设置显示图标
     *
     * @param iconResource {@link Icon}
     * @param iconPosition {@link IconPosition}
     */
    public void setIcon(int iconResource, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, mContext.getResources().getDrawable(iconResource));

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(iconResource), null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getResources().getDrawable(iconResource), null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    mContext.getResources().getDrawable(iconResource), null, null);

        }

    }

    /**
     * 设置显示图标
     * @param icon Bitmap
     * @param iconPosition {@link IconPosition}
     */
    public void setIcon(Bitmap icon, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, new BitmapDrawable(icon));

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(icon), null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    new BitmapDrawable(icon), null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    new BitmapDrawable(icon), null, null);

        }
        icon.recycle();
    }

    /**
     * 设置显示图标
     * @param icon
     * @param iconPosition
     */
    public void setIcon(Drawable icon, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, icon);

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    icon, null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    icon, null, null);

        }

    }

    /**
     * 设置显示背景
     * @param background {@link Background}
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
     * 获取背景
     *
     * @return int
     */
    public int getBackground() {

        return this.mBackground;

    }

    /**
     * 设置显示位置
     *
     * @param gravity {@link Gravity} int
     * @param xOffset int
     * @param yOffset int
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {

        this.mGravity = gravity;
        this.mXOffset = xOffset;
        this.mYOffset = yOffset;

    }

    /**
     * 设置显示/隐藏动画
     *
     * @param animations {@link Animations}
     */
    public void setAnimations(Animations animations) {

        this.mAnimations = animations;

    }

    /**
     * 获取显示/隐藏动画
     *
     * @return {@link Animations}
     */
    public Animations getAnimations() {

        return this.mAnimations;

    }

    /**
     * 设置XToast销毁监听器
     *
     * @param onDismissListener {@link OnDismissListener}
     */
    public void setOnDismissListener(OnDismissListener onDismissListener) {

        this.mOnDismissListener = onDismissListener;

    }

    /**
     * 获取XToast销毁监听器
     *
     * @return {@link OnDismissListener}
     */
    public OnDismissListener getOnDismissListener() {

        return mOnDismissListener;

    }

    /**
     * 销毁一个XToast
     */
    public void dismiss() {

        ManagerXToast.getInstance().removeSuperToast(this);

    }

    /**
     * 获取XToast内部的显示消息的TextView
     *
     * @return {@link TextView}
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * 获取XToast的View对象
     *
     * @return {@link View}
     */
    public View getView() {

        return mToastView;

    }

    /**
     * 获取XToast是否正在显示
     *
     * @return boolean
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * 获取当前XToast的WindowManager对象
     *
     * @return {@link WindowManager}
     */
    public WindowManager getWindowManager() {

        return mWindowManager;

    }

    /**
     * 获取当前XToast的WindowManager对象的布局参数
     *
     * @return {@link WindowManager.LayoutParams}
     */
    public WindowManager.LayoutParams getWindowManagerParams() {

        return mWindowManagerParams;

    }

    /**
     * 获取动画的私有方法
     */
    private int getAnimation() {

        if (mAnimations == Animations.FLYIN) {

            return android.R.style.Animation_Translucent;

        } else if (mAnimations == Animations.SCALE) {

            return android.R.style.Animation_Dialog;

        } else if (mAnimations == Animations.POPUP) {

            return android.R.style.Animation_InputMethod;

        } else {

            return android.R.style.Animation_Toast;

        }

    }

    /**
     * 设置默认的Style的私有方法
     */
    private void setStyle(Style style) {

        this.setAnimations(style.animations);
        this.setTypefaceStyle(style.typefaceStyle);
        this.setTextColor(style.textColor);
        if (style.customBackground!=null)
            this.setBackground(style.customBackground);
        else
            this.setBackground(style.background);

    }

    /**
     * 创建一个XToast对象
     *
     * @param context          {@link Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link Duration}
     *
     * @return {@link XToast}
     */
    public static XToast create(Context context, CharSequence textCharSequence,
                                int durationInteger) {

        XToast xToast = new XToast(context);
        xToast.setText(textCharSequence);
        xToast.setDuration(durationInteger);

        return xToast;

    }

    /**
     * 创建一个XToast对象
     *
     * @param context          {@link Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link Duration}
     * @param animations       {@link Animations}
     *
     * @return {@link XToast}
     */
    public static XToast create(Context context, CharSequence textCharSequence,
                                int durationInteger, Animations animations) {

        final XToast xToast = new XToast(context);
        xToast.setText(textCharSequence);
        xToast.setDuration(durationInteger);
        xToast.setAnimations(animations);

        return xToast;

    }

    /**
     * 创建一个XToast对象
     *
     * @param context          {@link Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link Duration}
     * @param style            {@link .utils.Style}
     *
     * @return {@link XToast}
     */
    public static XToast create(Context context, CharSequence textCharSequence, int durationInteger, Style style) {

        final XToast xToast = new XToast(context);
        xToast.setText(textCharSequence);
        xToast.setDuration(durationInteger);
        xToast.setStyle(style);

        return xToast;

    }

    /**
     * 销毁并移除所有的XToast
     */
    public static void cancelAllSuperToasts() {

        ManagerXToast.getInstance().cancelAllSuperToasts();

    }

}


