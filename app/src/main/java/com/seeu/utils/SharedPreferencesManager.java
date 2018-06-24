package com.seeu.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.seeu.common.Entity;

import java.util.Set;

/**
 * Created by thomasfouan on 10/06/2018.
 *
 * Class that manages the application's Shared Preferences.
 */
public class SharedPreferencesManager {

	/**
	 * Name used for the shared preferences file.
	 */
	private static final String SHARED_PREFERENCES_NAME = "SEEU";

	/**
	 * Name used to store the token in the shared preferences.
	 */
	private static final String SEEU_TOKEN_KEY = "seeutoken";

	/**
	 * Name used to store the Facebook access token in the shared preferences.
	 */
	private static final String FACEBOOK_ACCESS_TOKEN_KEY = "facebookaccesstoken";

	private static final Gson gson = new Gson();

	private SharedPreferencesManager() {
	}

	/**
	 * Put the token in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param token the token to store
	 */
	public static void putToken(Context context, String token) {
		putString(context, SEEU_TOKEN_KEY, token);
	}

	/**
	 * Get the token from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @return the token
	 */
	public static String getToken(Context context) {
		return getString(context, SEEU_TOKEN_KEY, null);
	}

	/**
	 * Put the Facebook access token in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param token the Facebook access token to store
	 */
	public static void putFacebookToken(Context context, String token) {
		putString(context, FACEBOOK_ACCESS_TOKEN_KEY, token);
	}

	/**
	 * Get the Facebook access token from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @return the Facebook access token
	 */
	public static String getFacebookToken(Context context) {
		return getString(context, FACEBOOK_ACCESS_TOKEN_KEY, null);
	}

	/**
	 * Put an {@link Entity} in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param value the entity to store
	 * @param <T> the type of the entity to store
	 */
	public static <T extends Entity> void putEntity(Context context, T value) {
		putObject(context, T.STORAGE_KEY, value);
	}

	/**
	 * Get the {@link Entity} from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param clazz the class of the entity to retrieve
	 * @param <T> the type of the entity to retrieve
	 * @return the entity
	 */
	public static <T extends Entity> T getEntity(Context context, Class<T> clazz) {
		return getObject(context, T.STORAGE_KEY, clazz);
	}

	/**
	 * Put the object in the shared preferences.
	 * Use {@link Gson} library to convert the object in JSON before store it as a String.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param value the value to store
	 */
	public static void putObject(Context context, String key, Object value) {
		String jsonValue = gson.toJson(value);
		putString(context, key, jsonValue);
	}

	/**
	 * Get the object from the shared preferences.
	 * Use {@link Gson} library to convert the JSON String to an object of the type passed in parameter.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param tClass the class of the value
	 * @param <T> the type of the value
	 * @return the value stored
	 */
	public static <T> T getObject(Context context, String key, Class<T> tClass) {
		String jsonValue = getString(context, key, null);
		return gson.fromJson(jsonValue, tClass);
	}

	/**
	 * Put a String in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param value the value to store
	 */
	public static void putString(Context context, String key, String value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putString(key, value)
				.apply();
	}

	/**
	 * Get a String from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param defValue the default value if there is no value assigned to the key
	 * @return the value if it exists, otherwise return defValue
	 */
	public static String getString(Context context, String key, @Nullable String defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getString(key, defValue);
	}

	/**
	 * Put a boolean in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param value the value to store
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putBoolean(key, value)
				.apply();
	}

	/**
	 * Get a boolean from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param defValue the default value if there is no value assigned to the key
	 * @return the value if it exists, otherwise return defValue
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getBoolean(key, defValue);
	}

	/**
	 * Put a float in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param value the value to store
	 */
	public static void putFloat(Context context, String key, float value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putFloat(key, value)
				.apply();
	}

	/**
	 * Get a float from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param defValue the default value if there is no value assigned to the key
	 * @return the value if it exists, otherwise return defValue
	 */
	public static float getFloat(Context context, String key, float defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getFloat(key, defValue);
	}

	/**
	 * Put an int in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param value the value to store
	 */
	public static void putInt(Context context, String key, int value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putInt(key, value)
				.apply();
	}

	/**
	 * Get an int from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param defValue the default value if there is no value assigned to the key
	 * @return the value if it exists, otherwise return defValue
	 */
	public static int getInt(Context context, String key, int defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getInt(key, defValue);
	}

	/**
	 * Put a long in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param value the value to store
	 */
	public static void putLong(Context context, String key, Long value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putLong(key, value)
				.apply();
	}

	/**
	 * Get a long from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param defValue the default value if there is no value assigned to the key
	 * @return the value if it exists, otherwise return defValue
	 */
	public static long getLong(Context context, String key, long defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getLong(key, defValue);
	}

	/**
	 * Put a String Set in the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param value the value to store
	 */
	public static void putStringSet(Context context, String key, Set<String> value) {
		context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.edit()
				.putStringSet(key, value)
				.apply();
	}

	/**
	 * Get a String Set from the shared preferences.
	 * @param context the context needed to get the shared preferences
	 * @param key the key assigned to the value
	 * @param defValue the default value if there is no value assigned to the key
	 * @return the value if it exists, otherwise return defValue
	 */
	public static Set<String> getStringSet(Context context, String key, @Nullable Set<String> defValue) {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
				.getStringSet(key, defValue);
	}

}
