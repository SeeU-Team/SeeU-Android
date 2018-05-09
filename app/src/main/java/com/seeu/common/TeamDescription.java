package com.seeu.common;

import com.seeu.R;

import java.io.Serializable;

/**
 * Created by thomasfouan on 07/05/2018.
 */

public class TeamDescription implements Serializable {

	private String name;
	private int icon;

	public TeamDescription() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public static TeamDescription getDebugTeamDescription(int index) {
		TeamDescription teamDescription = new TeamDescription();
		int modulo = index%4;

		switch (modulo) {
			case 0:
				teamDescription.setName("Beer");
				teamDescription.setIcon(R.drawable.icon_beer);
				break;
			case 1:
				teamDescription.setName("Food");
				teamDescription.setIcon(R.drawable.icon_food);
				break;
			case 2:
				teamDescription.setName("Sports");
				teamDescription.setIcon(R.drawable.icon_soccer_ball);
				break;
			case 3:
				teamDescription.setName("Swim");
				teamDescription.setIcon(R.drawable.icon_swimming_pool);
		}

		return teamDescription;
	}
}
