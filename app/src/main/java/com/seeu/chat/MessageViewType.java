package com.seeu.chat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomasfouan on 06/05/2018.
 */

public enum MessageViewType {
	MY_MESSAGE(0),
	OTHERS_MESSAGE(1);

	private int value;
	private static Map<Integer, MessageViewType> mapper = new HashMap<>();

	static {
		for (MessageViewType type : MessageViewType.values()) {
			mapper.put(type.getValue(), type);
		}
	}

	MessageViewType(int i) {
		value = i;
	}

	public int getValue() {
		return value;
	}

	public static MessageViewType fromValue(int value) {
		return mapper.get(value);
	}
}
