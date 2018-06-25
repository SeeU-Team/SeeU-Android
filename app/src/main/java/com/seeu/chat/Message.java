package com.seeu.chat;

import android.content.Context;

import com.seeu.member.Member;
import com.seeu.utils.SharedPreferencesManager;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 06/05/2018.
 *
 * Class that represents a message send between 2 entities (Team or Member).
 */
@Getter
@Setter
public class Message {

	private long id;
	private Member owner;
	private String content;

	public Message() {
	}

	/**
	 * Check of this message belongs to the current user.
	 * Use the shared preferences to get the current user
	 * @param context the context to get the shared preferences from
	 * @return true if this message has been sent by the current user
	 */
	public boolean belongsToCurrentUser(Context context) {
		final Member currentUser = SharedPreferencesManager.getEntity(context, Member.class);
		return currentUser.getId() == owner.getId();
	}

	public static Message getDebugMessage(int index) {
		Member owner = Member.getDebugMember(index);
		Message message = new Message();
		message.setId(index);
		message.setContent("Ceci est du texte long " + index);
		message.setOwner(owner);

		return message;
	}
}
