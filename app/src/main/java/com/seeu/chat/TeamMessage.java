package com.seeu.chat;

import android.content.Context;

import com.seeu.member.MemberHasTeam;
import com.seeu.team.Team;
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
public class TeamMessage extends Message<Team> {

	@Builder
	public TeamMessage(long id, Team owner, String content) {
		super(id, owner, content);
	}

	@Override
	public boolean belongsToCurrentUser(Context context) {
		final MemberHasTeam memberHasTeam = SharedPreferencesManager.getObject(context, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);
		return memberHasTeam.getTeam().getId().equals(owner.getId());
	}
}
