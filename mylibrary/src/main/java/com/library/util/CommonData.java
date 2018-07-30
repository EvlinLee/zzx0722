package com.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;

public class CommonData {

	public static int screenWidth;

	public static int screenHeight;

	public static float screenDensity;

	private static ExecutorService executorService = Executors
			.newFixedThreadPool(5);

	/** The activity list. */
	private static List<Activity> activityList = new ArrayList<Activity>();

	public static void addActivityStack(final Activity activity) {
		if (activityList == null) {
			activityList = new ArrayList<Activity>();
		}
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				activityList.add(activity);
				List<Activity> nos = new ArrayList<Activity>();
				int size = activityList.size();
				for (int i = 0; i < size; i++) {
					Activity act = activityList.get(i);
					if (act != null && act.isFinishing()) {
						nos.add(act);
					}
				}
				activityList.removeAll(nos);
				nos = null;
			}
		});
	}

	public static void exit() {
		if (activityList == null)
			return;
		int size = activityList.size();
		for (int i = 0; i < size; i++) {
			Activity act = activityList.get(i);
			finish(act);
			act = null;
		}
		activityList.clear();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * Finish.
	 * 
	 * @param activity
	 *            the activity
	 */
	private static void finish(Activity activity) {
		if (activity == null)
			return;
		try {
			activity.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
