package com.seeu.chat;

import android.content.Context;

import com.seeu.member.Member;
import com.seeu.utils.SharedPreferencesManager;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 30/06/2018.
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class MemberMessage extends Message<Member> {

	@Builder
	public MemberMessage(long id, Member owner, String content) {
		super(id, owner, content);
	}

	@Override
	public boolean belongsToCurrentUser(Context context) {
		final Member currentUser = SharedPreferencesManager.getEntity(context, Member.STORAGE_KEY, Member.class);
		return currentUser.getId().equals(owner.getId());
	}
}
