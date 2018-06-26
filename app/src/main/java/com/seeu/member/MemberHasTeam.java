package com.seeu.member;

import com.seeu.common.Entity;
import com.seeu.team.Team;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 25/06/2018.
 */
@Getter
@Setter
public class MemberHasTeam extends Entity {

	/**
	 * Key used when the entity is passed in an intent or store in the sharedPreferences.
	 */
	public static final String STORAGE_KEY = "memberHasTeam";

	private Long memberId;
	private Team team;
	private MemberStatus status;

	public MemberHasTeam() {
	}
}
