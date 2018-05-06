package com.seeu.chat;

import com.seeu.common.Member;

/**
 * Created by thomasfouan on 06/05/2018.
 */

public class Message {

	private long id;
	private Member owner;
	private String content;

	public Message() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Member getOwner() {
		return owner;
	}

	public void setOwner(Member owner) {
		this.owner = owner;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean belongsToCurrentUser() {
		// TODO: Replace 0 by getting current id user from localdata
		return 0 == owner.getId();
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
