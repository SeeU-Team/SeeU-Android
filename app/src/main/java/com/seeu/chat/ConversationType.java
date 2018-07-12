package com.seeu.chat;

/**
 * Created by thomasfouan on 11/07/2018.
 */
public enum ConversationType {
	USER_TO_USER((short) 0),
	USER_TO_TEAM((short) 1),
	USER_TO_NIGHTCENTER((short) 2),
	TEAM_TO_BEFORE((short) 3);

	private short value;

	ConversationType(short value) {
		this.value = value;
	}
}
