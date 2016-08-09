package com.szhynet.widget.xtoast.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to store any OnClickWrappers and OnDismissWrappers set to SuperActivityToasts/SuperCardToasts.
 * This should be sent through the onRestoreState() methods of the SuperActivityToasts/SuperCardToasts
 * classes in order for those methods to reattach any listeners.
 */
public class Wrappers {

    private List<OnClickWrapper> onClickWrapperList = new ArrayList<OnClickWrapper>();

    private List<OnDismissWrapper> onDismissWrapperList = new ArrayList<OnDismissWrapper>();

    /**
     * Adds an onclickwrapper to a list that will be reattached on orientation change.
     *
     * @param onClickWrapper {@link OnClickWrapper}
     */
    public void add(OnClickWrapper onClickWrapper){

        onClickWrapperList.add(onClickWrapper);

    }

    /**
     * Adds an ondismisswrapper to a list that will be reattached on orientation change.
     *
     * @param onDismissWrapper {@link OnDismissWrapper}
     */
    public void add(OnDismissWrapper onDismissWrapper){

        onDismissWrapperList.add(onDismissWrapper);

    }

    /**
     * Used during recreation of SuperActivityToasts/SuperCardToasts.
     *  @return OnClickWrapper List
     */
    public List<OnClickWrapper> getOnClickWrappers() {

        return onClickWrapperList;

    }

    /**
     * Used during recreation of SuperActivityToasts/SuperCardToasts.
     * @return OnDismissWrapper List
     */
    public List<OnDismissWrapper> getOnDismissWrappers() {

        return onDismissWrapperList;

    }

}
