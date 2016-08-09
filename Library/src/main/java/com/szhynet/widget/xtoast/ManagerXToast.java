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

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/* Manages the life of a XToast. Initially copied from the Crouton library */
public class ManagerXToast extends Handler {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "ManagerXToast";

    /* Potential messages for the handler to send **/
    private static final class Messages {

        /* Hexadecimal numbers that represent acronyms for the operation **/
        private static final int DISPLAY_SUPERTOAST = 0x445354;
        private static final int ADD_SUPERTOAST = 0x415354;
        private static final int REMOVE_SUPERTOAST = 0x525354;

    }

    private static ManagerXToast mManagerXToast;

    private final Queue<XToast> mQueue;

    /* Private method to create a new list if the manager is being initialized */
    private ManagerXToast() {

        mQueue = new LinkedBlockingQueue<XToast>();

    }

    /* Singleton method to ensure all SuperToasts are passed through the same manager */
    protected static synchronized ManagerXToast getInstance() {

        if (mManagerXToast != null) {

            return mManagerXToast;

        } else {

            mManagerXToast = new ManagerXToast();

            return mManagerXToast;

        }

    }

    /* Add XToast to queue and try to show it */
    protected void add(XToast xToast) {

        /* Add XToast to queue and try to show it */
        mQueue.add(xToast);
        this.showNextSuperToast();

    }

    /* Shows the next XToast in the list */
    private void showNextSuperToast() {

        if (mQueue.isEmpty()) {

            /* There is no XToast to display next */

            return;

        }

        /* Get next XToast in the queue */
        final XToast xToast = mQueue.peek();

        /* Show XToast if none are showing (not sure why this works but it does) */
        if (!xToast.isShowing()) {

            final Message message = obtainMessage(Messages.ADD_SUPERTOAST);
            message.obj = xToast;
            sendMessage(message);

        } else {

            sendMessageDelayed(xToast, Messages.DISPLAY_SUPERTOAST,
                    getDuration(xToast));

        }

    }

    /* Show/dismiss a XToast after a specific duration */
    private void sendMessageDelayed(XToast xToast, final int messageId, final long delay) {

        Message message = obtainMessage(messageId);
        message.obj = xToast;
        sendMessageDelayed(message, delay);

    }

    /* Get duration and add one second to compensate for show/hide animations */
    private long getDuration(XToast xToast) {

        long duration = xToast.getDuration();
        duration += 1000;

        return duration;

    }

    @Override
    public void handleMessage(Message message) {

        final XToast xToast = (XToast)
                message.obj;

        switch (message.what) {

            case Messages.DISPLAY_SUPERTOAST:

                showNextSuperToast();

                break;

            case Messages.ADD_SUPERTOAST:

                displaySuperToast(xToast);

                break;

            case Messages.REMOVE_SUPERTOAST:

                removeSuperToast(xToast);

                break;

            default: {

                super.handleMessage(message);

                break;

            }

        }

    }

    /* Displays a XToast */
    private void displaySuperToast(XToast xToast) {

        if (xToast.isShowing()) {

            /* If the XToast is already showing do not show again */

            return;

        }

        final WindowManager windowManager = xToast
                .getWindowManager();

        final View toastView = xToast.getView();

        final WindowManager.LayoutParams params = xToast
                .getWindowManagerParams();

        if(windowManager != null) {

            windowManager.addView(toastView, params);

        }

        sendMessageDelayed(xToast, Messages.REMOVE_SUPERTOAST,
                xToast.getDuration() + 500);

    }

    /* Hide and remove the XToast */
    protected void removeSuperToast(XToast xToast) {

        final WindowManager windowManager = xToast
                .getWindowManager();

        final View toastView = xToast.getView();

        if (windowManager != null) {

            mQueue.poll();

            windowManager.removeView(toastView);

            sendMessageDelayed(xToast,
                    Messages.DISPLAY_SUPERTOAST, 500);

            if(xToast.getOnDismissListener() != null) {

                xToast.getOnDismissListener().onDismiss(xToast.getView());

            }

        }

    }

    /* Cancels/removes all showing pending SuperToasts */
    protected void cancelAllSuperToasts() {

        removeMessages(Messages.ADD_SUPERTOAST);
        removeMessages(Messages.DISPLAY_SUPERTOAST);
        removeMessages(Messages.REMOVE_SUPERTOAST);

        for (XToast xToast : mQueue) {

            if (xToast.isShowing()) {

                xToast.getWindowManager().removeView(
                        xToast.getView());

            }

        }

        mQueue.clear();

    }

}
