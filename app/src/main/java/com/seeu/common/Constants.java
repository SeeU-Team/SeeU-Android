package com.seeu.common;

public class Constants {

	/**
	 * Name used for the shared preferences file.
	 */
	public static final String SHARED_PREFERENCES_NAME = "SEEU";

	/**
	 * Name used to store the token in the Shared Preferences.
	 */
	public static final String SP_TOKEN_KEY = "token";

	private static final String SEEU_DOMAIN_URL = "192.168.2.200:8001";

	public static final String SEEU_API_URL = "http://" + SEEU_DOMAIN_URL;

	public static final String SEEU_WEB_SOCKET_SERVER_URL = "ws://" + SEEU_DOMAIN_URL + "/app/test";
}
