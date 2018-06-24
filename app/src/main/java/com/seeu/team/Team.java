package com.seeu.team;

import com.seeu.common.Entity;
import com.seeu.member.Member;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Entity that represents a Team in the application.
 * A team is the primary element of the app, with its members.
 */
@Getter
@Setter
public class Team extends Entity {

	private String pictureUrl;
	private String name;
	private String place;
	private String tags;
	private String description;
	private int mark;
	private ArrayList<Member> members; // Use ArrayList because it is Serializable (List interface is not)
	private ArrayList<TeamDescription> descriptions; // Use ArrayList because it is Serializable (List interface is not)

	public Team() {
		super("team");
		members = new ArrayList<>();
		descriptions = new ArrayList<>();
	}

	// TODO: Remove debug elements
	public static final String DEBUG_PICTURE_URL = "https://scontent.xx.fbcdn.net/v/t1.0-9/s720x720/28796203_2100578413291884_3128132353430932217_n.jpg?oh=cf890e20f692253214d595fd266fd73b&oe=5B3BCC27";

	public static Team getDebugTeam(int index) {
		ArrayList<Member> members = new ArrayList<>();
		for (int i = 0; i < index; i++) {
			members.add(Member.getDebugMember(i));
		}

		ArrayList<TeamDescription> descriptions = new ArrayList<>();
		for (int i = 0; i < index; i++) {
			descriptions.add(TeamDescription.getDebugTeamDescription(i));
		}

		String description = "Description " +
				index +
				". Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
				"Aenean ut imperdiet neque. " +
				"Phasellus sed blandit odio, nec elementum nunc. " +
				"Ut magna orci, lacinia iaculis risus nec, eleifend imperdiet mauris. " +
				"Vestibulum erat mi, accumsan ut erat at, placerat consequat turpis. " +
				"Donec eleifend enim ut accumsan tincidunt. " +
				"Donec vel massa sed sapien.";


		Team team = new Team();
		team.setId(index);
		team.setPictureUrl(Team.DEBUG_PICTURE_URL);
		team.setName("Team " + index);
		team.setPlace("A LA MAISON");
		team.setTags("#uno#dos#tres");
		team.setDescription(description);
		team.setMark(index%6);
		team.setMembers(members);
		team.setDescriptions(descriptions);

		return team;
	}
}
