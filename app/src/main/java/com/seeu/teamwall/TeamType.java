package com.seeu.teamwall;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Entity that represents the team's type.
 * It can be Barbecue, Indoor, Outdoor, Gaming, Dancing, etc.
 */
@Getter
@Setter
public class TeamType {

	private long id;
	private String name;

	public TeamType() {
	}

	@Override
	public String toString() {
		return "Type " + name;
	}
}
