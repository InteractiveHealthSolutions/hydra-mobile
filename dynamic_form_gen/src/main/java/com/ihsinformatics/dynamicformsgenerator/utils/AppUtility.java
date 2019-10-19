package com.ihsinformatics.dynamicformsgenerator.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nabil shafi on 6/14/2018.
 */

public class AppUtility {

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Resources r, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
