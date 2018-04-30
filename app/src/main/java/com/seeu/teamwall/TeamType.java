package com.seeu.teamwall;

/**
 * Created by thomasfouan on 30/04/2018.
 */

class TeamType {

	private long id;
	private String name;

	public TeamType() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Type " + name;
	}
}
