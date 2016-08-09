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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Manages the life of a XActivityToast. Initial code derived from the Crouton library.
 */
class ManagerXActivityToast extends Handler {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "ManagerXActivityToast";

    /* Potential messages for the handler to send **/
    private static final class Messages {

        /* Hexadecimal numbers that represent acronyms for the operation. **/
        private static final int DISPLAY = 0x44534154;
        private static final int REMOVE = 0x52534154;

    }

    private static ManagerXActivityToast mManagerXActivityToast;

    private final LinkedList<XActivityToast> mList;

    /* Private method to create a new list if the manager is being initialized */
    private ManagerXActivityToast() {

        mList = new LinkedList<XActivityToast>();

    }

    /**
     * Singleton method to ensure all SuperActivityToasts are passed through the same manager.
     */
    protected static synchronized ManagerXActivityToast getInstance() {

        if (mManagerXActivityToast != null) {

            return mManagerXActivityToast;

        } else {

            mManagerXActivityToast = new ManagerXActivityToast();

            return mManagerXActivityToast;

        }

    }

    /**
     * Add a XActivityToast to the list. Will show immediately if no other SuperActivityToasts
     * are in the list.
     */
    void add(XActivityToast xActivityToast) {

        mList.add(xActivityToast);

        this.showNextSuperToast();

    }

    /**
     * Shows the next XActivityToast in the list. Called by add() and when the dismiss animation
     * of a previously showing XActivityToast ends.
     */
    private void showNextSuperToast() {

        final XActivityToast xActivityToast = mList.peek();

        if (mList.isEmpty() || xActivityToast.getActivity() == null) {

            return;

        }

        if (!xActivityToast.isShowing()) {

            final Message message = obtainMessage(Messages.DISPLAY);
            message.obj = xActivityToast;
            sendMessage(message);

        }

    }


    @Override
    public void handleMessage(Message message) {

        final XActivityToast xActivityToast = (XActivityToast)
                message.obj;

        switch (message.what) {

            case Messages.DISPLAY:

                displaySuperToast(xActivityToast);

                break;

            case Messages.REMOVE:

                removeSuperToast(xActivityToast);

                break;

            default: {

                super.handleMessage(message);

                break;

            }

        }

    }

    /**
     * Displays a XActivityToast.
     */
    private void displaySuperToast(XActivityToast xActivityToast) {

        /* If this XActivityToast is somehow already showing do nothing */
        if(xActivityToast.isShowing()) {

            return;

        }

        final ViewGroup viewGroup = xActivityToast.getViewGroup();

        final View toastView = xActivityToast.getView();

        if(viewGroup != null) {

            try {

                viewGroup.addView(toastView);

                if(!xActivityToast.getShowImmediate()) {

                    toastView.startAnimation(getShowAnimation(xActivityToast));

                }

            } catch(IllegalStateException e) {

                this.cancelAllSuperActivityToastsForActivity(xActivityToast.getActivity());

            }

        }

        /* Dismiss the XActivityToast at the set duration time unless indeterminate */
        if(!xActivityToast.isIndeterminate()) {

            Message message = obtainMessage(Messages.REMOVE);
            message.obj = xActivityToast;
            sendMessageDelayed(message, xActivityToast.getDuration() +
                    getShowAnimation(xActivityToast).getDuration());

        }

    }

    /**
     *  Hide and remove the XActivityToast
     */
    void removeSuperToast(final XActivityToast xActivityToast) {

        /* If XActivityToast has been dismissed before it shows, do not attempt to show it */
        if(!xActivityToast.isShowing()) {

            mList.remove(xActivityToast);

            return;

        }

        /* If being called somewhere else get rid of delayed remove message */
        removeMessages(Messages.REMOVE, xActivityToast);

        final ViewGroup viewGroup = xActivityToast.getViewGroup();

        final View toastView = xActivityToast.getView();

        if (viewGroup != null) {

            Animation animation = getDismissAnimation(xActivityToast);

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                    /* Do nothing */

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if(xActivityToast.getOnDismissWrapper() != null){

                        xActivityToast.getOnDismissWrapper().onDismiss(xActivityToast.getView());

                    }

                    /* Show the XActivityToast next in the list if any exist */
                    ManagerXActivityToast.this.showNextSuperToast();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                    /* Do nothing */

                }
            });

            toastView.startAnimation(animation);

            viewGroup.removeView(toastView);

            mList.poll();

        }

    }

    /**
     * Removes all SuperActivityToasts and clears the list
     */
    void cancelAllSuperActivityToasts() {

        removeMessages(Messages.DISPLAY);
        removeMessages(Messages.REMOVE);

        for (XActivityToast xActivityToast : mList) {

            if (xActivityToast.isShowing()) {

                xActivityToast.getViewGroup().removeView(
                        xActivityToast.getView());

                xActivityToast.getViewGroup().invalidate();

            }

        }

        mList.clear();

    }

    /**
     * Removes all SuperActivityToasts and clears the list for a specific activity
     */
    void cancelAllSuperActivityToastsForActivity(Activity activity) {

        Iterator<XActivityToast> superActivityToastIterator = mList
                .iterator();

        while (superActivityToastIterator.hasNext()) {

            XActivityToast xActivityToast = superActivityToastIterator
                    .next();

            if ((xActivityToast.getActivity()) != null
                    && xActivityToast.getActivity().equals(activity)) {

                if (xActivityToast.isShowing()) {

                    xActivityToast.getViewGroup().removeView(
                            xActivityToast.getView());

                }

                removeMessages(Messages.DISPLAY, xActivityToast);
                removeMessages(Messages.REMOVE, xActivityToast);

                superActivityToastIterator.remove();

            }

        }

    }

    /**
     * Used in XActivityToast saveState().
     */
    LinkedList<XActivityToast> getList(){

        return mList;

    }

    /**
     * Returns an animation based on the {@link .XToast.Animations} enums
     */
    private Animation getShowAnimation(XActivityToast xActivityToast) {

        if (xActivityToast.getAnimations() == XToast.Animations.FLYIN) {

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

        } else if (xActivityToast.getAnimations() == XToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (xActivityToast.getAnimations() == XToast.Animations.POPUP) {

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

            Animation animation= new AlphaAnimation(0f, 1f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            return animation;

        }

    }

    /**
     *  Returns an animation based on the {@link .XToast.Animations} enums
     */
    private Animation getDismissAnimation(XActivityToast xActivityToast) {

        if (xActivityToast.getAnimations() == XToast.Animations.FLYIN) {

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

        } else if (xActivityToast.getAnimations() == XToast.Animations.SCALE) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setInterpolator(new DecelerateInterpolator());
            animationSet.setDuration(250);

            return animationSet;

        } else if (xActivityToast.getAnimations() == XToast.Animations.POPUP) {

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

}
