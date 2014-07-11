package vn.game.speedorder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

	public static int determine(Context context) {
		if (getInt(context, Utils.KEY_BEST_SCORE, 0) > 0)
			return 1;
		else
			return 0;
	}

	public static void putInt(Context context, String key, int value) {
		SharedPreferences pref = context.getSharedPreferences("speedoder", 0);
		SharedPreferences.Editor edit = pref.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences pref = context.getSharedPreferences("speedoder", 0);
		return pref.getInt(key, defaultValue);
	}

	public static void putString(Context context, String key, String value) {
		SharedPreferences pref = context.getSharedPreferences("speedoder", 0);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getString(Context context, String key,
			String defaultValue) {
		SharedPreferences pref = context.getSharedPreferences("speedoder", 0);
		return pref.getString(key, defaultValue);

	}

	public static void removeValue(Context ctx, String key) {
		SharedPreferences settings = ctx.getSharedPreferences("speedoder", 0);
		SharedPreferences.Editor edit = settings.edit();
		edit.remove(key).commit();
	}

}
