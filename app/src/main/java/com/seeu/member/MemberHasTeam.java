package com.seeu.member;

import com.seeu.team.Team;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 25/06/2018.
 */
@Getter
@Setter
@Builder
public class MemberHasTeam {

	private Long memberId;
	private Team team;
	private MemberStatus status;
}
