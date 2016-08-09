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

import java.util.LinkedList;

/**
 * Manages the life of a XCardToast on orientation changes.
 */
class ManagerXCardToast {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "ManagerXCardToast";

    private static ManagerXCardToast mManagerXCardToast;

    private final LinkedList<XCardToast> mList;

    private ManagerXCardToast() {

        mList = new LinkedList<XCardToast>();

    }

    /**
     * Singleton method to ensure all SuperCardToasts are passed through the same manager.
     */
    protected static synchronized ManagerXCardToast getInstance() {

        if (mManagerXCardToast != null) {

            return mManagerXCardToast;

        } else {

            mManagerXCardToast = new ManagerXCardToast();

            return mManagerXCardToast;

        }

    }

    /**
     * Add a XCardToast to the list.
     */
    void add(XCardToast xCardToast) {

        mList.add(xCardToast);

    }

    /**
     * Removes a XCardToast from the list.
     */
    void remove(XCardToast xCardToast) {

        mList.remove(xCardToast);

    }

    /**
     * Removes all SuperCardToasts and clears the list
     */
    void cancelAllSuperActivityToasts() {

        for (XCardToast xCardToast : mList) {

            if (xCardToast.isShowing()) {

                xCardToast.getViewGroup().removeView(
                        xCardToast.getView());

                xCardToast.getViewGroup().invalidate();

            }

        }

        mList.clear();

    }

    /**
     * Used in XCardToast saveState().
     */
    LinkedList<XCardToast> getList() {

        return mList;

    }


}
