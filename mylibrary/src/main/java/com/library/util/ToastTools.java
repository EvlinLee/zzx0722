package com.library.util;

import android.content.Context;
import android.widget.Toast;

public class ToastTools {

	public static void showShort(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void showShort(Context context, int strRes) {
		Toast.makeText(context, strRes, Toast.LENGTH_SHORT).show();
	}

	public static void showLong(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	public static void showLong(Context context, int strRes) {
		Toast.makeText(context, strRes, Toast.LENGTH_LONG).show();
	}

}
