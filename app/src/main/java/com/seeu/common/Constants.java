package com.seeu.common;

/**
 * Global constants used in the application.
 */
public class Constants {

	/**
	 * The domain address of the SEEU API.
	 */
	private static final String SEEU_DOMAIN_URL = "192.168.2.200:8001";

	/**
	 * The URL of the SEEU API.
	 */
	public static final String SEEU_API_URL = "http://" + SEEU_DOMAIN_URL;

	/**
	 * The URL of the Web Socket Server of SEEU.
	 */
	public static final String SEEU_WEB_SOCKET_SERVER_URL = "ws://" + SEEU_DOMAIN_URL + "/app/test";
}
