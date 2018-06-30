package com.seeu.member;

import com.seeu.team.Team;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by thomasfouan on 25/06/2018.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberHasTeam implements Serializable {

	/**
	 * Key used when the entity is passed in an intent or store in the sharedPreferences.
	 */
	public static final String STORAGE_KEY = "memberHasTeam";

	private Long id;
	private Long memberId;
	private Team team;
	private MemberStatus status;
}
