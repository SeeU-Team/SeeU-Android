package com.seeu.notification;

/**
 * Created by thomasfouan on 11/07/2018.
 */
enum NotificationType {
	MESSAGE(0),
	TEAMUP(1),
	RECIPROCALTEAMUP(2),
	MERGE(3);

	private int value;

	NotificationType(int value) {
		this.value = value;
	}
}
