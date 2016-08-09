package com.szhynet.widget.xtoast.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import com.szhynet.widget.xtoast.R;
import com.szhynet.widget.xtoast.XToast;

/**
 *  Creates a reference to basic style options so that all types of XToasts
 *  will be themed the same way in a particular class.
 **/

@SuppressWarnings("UnusedDeclaration")
public class Style {

    public static final int BLACK = 0;
    public static final int BLUE = 1;
    public static final int GRAY = 2;
    public static final int GREEN = 3;
    public static final int ORANGE = 4;
    public static final int PURPLE = 5;
    public static final int RED = 6;
    public static final int WHITE = 7;

    public XToast.Animations animations = XToast.Animations.FADE;
    public int background = getBackground(GRAY);
    public Drawable customBackground = null;
    public int typefaceStyle = Typeface.NORMAL;
    public int textColor = Color.WHITE;
    public int dividerColor = Color.WHITE;
    public int buttonTextColor = Color.LTGRAY;

    /**
     * 获取一个预设的风格Style对象
     *
     * @param styleType {@link Style}
     *
     * @return {@link Style}
     */
    public static Style get(int styleType) {

        final Style style = new Style();

        switch (styleType) {

            case BLACK:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLACK);
                style.dividerColor = Color.WHITE;
                return style;

            case WHITE:

                style.textColor = Color.DKGRAY;
                style.background = getBackground(WHITE);
                style.dividerColor = Color.DKGRAY;
                style.buttonTextColor = Color.GRAY;
                return style;

            case GRAY:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                style.buttonTextColor = Color.GRAY;
                return style;

            case PURPLE:

                style.textColor = Color.WHITE;
                style.background = getBackground(PURPLE);
                style.dividerColor = Color.WHITE;
                return style;

            case RED:

                style.textColor = Color.WHITE;
                style.background = getBackground(RED);
                style.dividerColor = Color.WHITE;
                return style;

            case ORANGE:

                style.textColor = Color.WHITE;
                style.background = getBackground(ORANGE);
                style.dividerColor = Color.WHITE;
                return style;

            case BLUE:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLUE);
                style.dividerColor = Color.WHITE;
                return style;

            case GREEN:

                style.textColor = Color.WHITE;
                style.background = getBackground(GREEN);
                style.dividerColor = Color.WHITE;
                return style;

            default:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                return style;

        }

    }

    /**
     * 获取一个自定义的Style
     * @param context 上下文
     * @param backgroundColor 背景颜色
     * @param textColor 文本颜色
     * @param dividerColor 分割线颜色
     * @param buttonTextColor 按钮文本颜色
     * @param radius 圆角大小
     * @return
     */
    public static Style getCustom(Context context,int backgroundColor, int textColor, int dividerColor, int buttonTextColor,int radius, XToast.Animations animations){
        Style style = new Style();
        style.animations = animations;
        style.customBackground = getCustomBackground(context,backgroundColor,radius);
        style.textColor = textColor;
        style.dividerColor = dividerColor;
        style.buttonTextColor = buttonTextColor;
        return style;
    }

    /**
     * 获取一个自定义的Style
     * @param context 上下文
     * @param backgroundColor 背景颜色
     * @param textColor 文本颜色
     * @param dividerColor 分割线颜色
     * @param buttonTextColor 按钮文本颜色
     * @return
     */
    public static Style getCustom(Context context, int backgroundColor, int textColor, int dividerColor, int buttonTextColor, XToast.Animations animations){
        Style style = new Style();
        style.animations = animations;
        style.customBackground = getCustomBackground(context,backgroundColor,25);
        style.textColor = textColor;
        style.dividerColor = dividerColor;
        style.buttonTextColor = buttonTextColor;
        return style;
    }

    /**
     * 获取一个自定义的Style
     * @param context 上下文
     * @param backgroundColor 背景颜色
     * @param textColor 文本颜色
     * @param dividerColor 分割线颜色
     * @param buttonTextColor 按钮文本颜色
     * @param radius 圆角大小
     * @return
     */
    public static Style getCustom(Context context,int backgroundColor, int textColor, int dividerColor, int buttonTextColor,int radius){
        Style style = new Style();
        style.customBackground = getCustomBackground(context,backgroundColor,radius);
        style.textColor = textColor;
        style.dividerColor = dividerColor;
        style.buttonTextColor = buttonTextColor;
        return style;
    }

    /**
     * 获取一个自定义的Style
     * @param context 上下文
     * @param backgroundColor 背景颜色
     * @param textColor 文本颜色
     * @param dividerColor 分割线颜色
     * @param buttonTextColor 按钮文本颜色
     * @return
     */
    public static Style getCustom(Context context,int backgroundColor, int textColor, int dividerColor, int buttonTextColor){
        Style style = new Style();
        style.customBackground = getCustomBackground(context,backgroundColor,25);
        style.textColor = textColor;
        style.dividerColor = dividerColor;
        style.buttonTextColor = buttonTextColor;
        return style;
    }
    /**
     * 获取一个自定义的Style
     * @param context 上下文
     * @param backgroundColor 背景颜色
     * @param textColor 文本颜色
     * @param dividerColor 分割线颜色
     * @return
     */
    public static Style getCustom(Context context,int backgroundColor, int textColor, int dividerColor){
        Style style = new Style();
        style.customBackground = getCustomBackground(context,backgroundColor,25);
        style.textColor = textColor;
        style.dividerColor = dividerColor;
        return style;
    }
    /**
     * 获取一个自定义的Style
     * @param context 上下文
     * @param backgroundColor 背景颜色
     * @param textColor 文本颜色
     * @return
     */
    public static Style getCustom(Context context,int backgroundColor, int textColor){
        Style style = new Style();
        style.customBackground = getCustomBackground(context,backgroundColor,25);
        style.textColor = textColor;
        return style;
    }
    /**
     * 获取一个自定义的Style
     * @param context 上下文
     * @param backgroundColor 背景颜色
     * @return
     */
    public static Style getCustom(Context context,int backgroundColor){
        Style style = new Style();
        style.customBackground = getCustomBackground(context,backgroundColor,25);
        return style;
    }
    /**
     * 获取一个带预设动画和预设风格的Style对象
     *
     * @param styleType {@link Style}
     * @param animations {@link XToast.Animations}
     * @return {@link Style}
     */
    public static Style get(int styleType, XToast.Animations animations) {

        final Style style = new Style();
        style.animations = animations;

        switch (styleType) {

            case BLACK:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLACK);
                style.dividerColor = Color.WHITE;
                return style;

            case WHITE:

                style.textColor = Color.DKGRAY;
                style.background = getBackground(WHITE);
                style.dividerColor = Color.DKGRAY;
                style.buttonTextColor = Color.GRAY;
                return style;

            case GRAY:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                style.buttonTextColor = Color.GRAY;
                return style;

            case PURPLE:

                style.textColor = Color.WHITE;
                style.background = getBackground(PURPLE);
                style.dividerColor = Color.WHITE;
                return style;

            case RED:

                style.textColor = Color.WHITE;
                style.background = getBackground(RED);
                style.dividerColor = Color.WHITE;
                return style;

            case ORANGE:

                style.textColor = Color.WHITE;
                style.background = getBackground(ORANGE);
                style.dividerColor = Color.WHITE;
                return style;

            case BLUE:

                style.textColor = Color.WHITE;
                style.background = getBackground(BLUE);
                style.dividerColor = Color.WHITE;
                return style;

            case GREEN:

                style.textColor = Color.WHITE;
                style.background = getBackground(GREEN);
                style.dividerColor = Color.WHITE;
                return style;

            default:

                style.textColor = Color.WHITE;
                style.background = getBackground(GRAY);
                style.dividerColor = Color.WHITE;
                return style;

        }

    }

    public static int getBackground(int style) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            switch (style) {
                case BLACK:
                    return (R.drawable.background_kitkat_black);
                case WHITE:
                    return (R.drawable.background_kitkat_white);
                case GRAY:
                    return (R.drawable.background_kitkat_gray);
                case PURPLE:
                    return (R.drawable.background_kitkat_purple);
                case RED:
                    return (R.drawable.background_kitkat_red);
                case ORANGE:
                    return (R.drawable.background_kitkat_orange);
                case BLUE:
                    return (R.drawable.background_kitkat_blue);
                case GREEN:
                    return (R.drawable.background_kitkat_green);
                default:
                    return (R.drawable.background_kitkat_gray);
            }

        } else {
            switch (style) {
                case BLACK:
                    return (R.drawable.background_standard_black);
                case WHITE:
                    return (R.drawable.background_standard_white);
                case GRAY:
                    return (R.drawable.background_standard_gray);
                case PURPLE:
                    return (R.drawable.background_standard_purple);
                case RED:
                    return (R.drawable.background_standard_red);
                case ORANGE:
                    return (R.drawable.background_standard_orange);
                case BLUE:
                    return (R.drawable.background_standard_blue);
                case GREEN:
                    return (R.drawable.background_standard_green);
                default:
                    return (R.drawable.background_standard_gray);
            }
        }
    }

    /**
     *获取自定义的背景
     * @param context
     * @param backgroundColor
     * @return
     */
    private static Drawable getCustomBackground(Context context,int backgroundColor,int radius){
        int strokeWidth = 0;
        int roundRadius = dip2px(context,radius);
        int strokeColor = backgroundColor;
        int fillColor = backgroundColor;

        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }
    public static int dip2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale+0.5f);
    }

    public static int px2dip(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
}