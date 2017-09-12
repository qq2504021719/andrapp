package com.bignerdranch.android.xundian.comm;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class AtyContainer {

    private AtyContainer() {

    }

    private static AtyContainer instance = new AtyContainer();
    private static List<Activity> activityStack = new ArrayList<Activity>();

    public static AtyContainer getInstance() {
        return instance;
    }

    public static void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public static void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

}
