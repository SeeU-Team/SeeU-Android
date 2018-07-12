package com.seeu.notification;

/**
 * Created by thomasfouan on 11/07/2018.
 */
enum NotificationType {
	MESSAGE(0),
	LIKE(1),
	MERGE(2);

	private int value;

	NotificationType(int value) {
		this.value = value;
	}
}
