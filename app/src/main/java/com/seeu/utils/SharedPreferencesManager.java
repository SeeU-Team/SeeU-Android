package com.seeu.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.seeu.common.Entity;

import java.util.Set;

public class SharedPreferencesManager {

	/**
	 * Name used for the shared preferences file.
	 */
	private static final String SHARED_PREFERENCES_NAME = "SEEU";

	/**
	 * Name used to store the token in the Shared Preferences.
	 */
	private static final String TOKEN_KEY = "token";

	private static final Gson gson = new Gson();

	private SharedPreferencesManager() {

	}

	public static void putToken(Context context, String token) {
		putString(context, TOKEN_KEY, token);
	}

	public static String getToken(Context context) {
		return getString(context, TOKEN_KEY, null);
	}

	public static <T extends Entity> void putEntity(Context context, T value) {
		putObject(context, T.STORAGE_KEY, value);
	}

	public static <T extends Entity> T getEntity(Context context, Class<T> clazz) {
		return getObject(context, T.STORAGE_KEY, clazz);
	}

	public static void putObject(Context context, String key, Object value) {
		String jsonValue = gson.toJson(value);
		putString(context, key, jsonValue);
	}

	public static <T> T getObject(Context context, String key, Class<T> tClass) {
		String jsonValue = getString(context, key, null);
		return gson.fromJson(jsonValue, tClass);
	}

	public static void putString(Context context, String key, String value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putString(key, value)
				.apply();
	}

	public static String getString(Context context, String key, @Nullable String defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getString(key, defValue);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putBoolean(key, value)
				.apply();
	}

	public static boolean getBoolean(Context context, String key, boolean defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getBoolean(key, defValue);
	}

	public static void putFloat(Context context, String key, float value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putFloat(key, value)
				.apply();
	}

	public static float getFloat(Context context, String key, float defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getFloat(key, defValue);
	}

	public static void putInt(Context context, String key, int value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putInt(key, value)
				.apply();
	}

	public static int getInt(Context context, String key, int defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getInt(key, defValue);
	}

	public static void putLong(Context context, String key, Long value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putLong(key, value)
				.apply();
	}

	public static long getLong(Context context, String key, long defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getLong(key, defValue);
	}

	public static void putStringSet(Context context, String key, Set<String> value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putStringSet(key, value)
				.apply();
	}

	public static Set<String> getStringSet(Context context, String key, @Nullable Set<String> defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getStringSet(key, defValue);
	}

}
