package com.mintedtech.quick_converter.lib;

import android.os.Build;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

class ViewIdUtil
{

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger (1);

    public static int generateViewId ()
    {
        int generatedId;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            generatedId = legacyGenerateViewID ();
        }
        else {
            generatedId = View.generateViewId ();
        }

        return generatedId;
    }

    private static int legacyGenerateViewID ()
    {
        int result=0, newID;
        boolean validIdWasGeneratedAndSet;

        for (boolean validIdFound=false; !validIdFound; validIdFound=validIdWasGeneratedAndSet) {
            result = sNextGeneratedId.get ();
            newID = result + 1;
            newID = newID > 0x00FFFFFF ? 1 : newID;
            validIdWasGeneratedAndSet = sNextGeneratedId.compareAndSet (result, newID);
        }

        return result;
    }

}