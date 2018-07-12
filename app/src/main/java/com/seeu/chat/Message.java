package com.seeu.chat;

import android.app.Service;
import android.content.Context;

import com.seeu.common.Entity;
import com.seeu.member.Member;
import com.seeu.utils.SharedPreferencesManager;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 06/05/2018.
 *
 * Class that represents a message send between 2 entities (Team or Member).
 */
@AllArgsConstructor
@Getter
@Setter
public abstract class Message<T extends Entity> implements Serializable {

	public static final String STORAGE_KEY = "message";

	protected long id;
	protected T owner;
	protected String content;

	/**
	 * Check of this message belongs to the current user.
	 * Use the shared preferences to get the current user
	 * @param context the context to get the shared preferences from
	 * @return true if this message has been sent by the current user
	 */
	public abstract boolean belongsToCurrentUser(Context context);
}
