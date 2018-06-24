package com.seeu.team;

import com.seeu.R;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 07/05/2018.
 *
 * Entity that represents a team's description.
 * A description can be something that the team is or has (food, swimming pool, sports, etc.).
 */
@Getter
@Setter
public class TeamDescription implements Serializable {

	private String name;
	private int icon;

	public TeamDescription() {
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
