package com.seeu.common;

import com.seeu.teamprofile.TeamDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class Team {

	private long id;
	private String pictureUrl;
	private String name;
	private String place;
	private String tags;
	private String description;
	private int mark;
	private List<Member> members;
	private List<TeamDescription> descriptions;

	public Team() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public List<TeamDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<TeamDescription> descriptions) {
		this.descriptions = descriptions;
	}

	@Override
	public String toString() {
		return "team " + id;
	}

	// TODO: Remove debug elements
	public static final String DEBUG_PICTURE_URL = "https://scontent.xx.fbcdn.net/v/t1.0-9/s720x720/28796203_2100578413291884_3128132353430932217_n.jpg?oh=cf890e20f692253214d595fd266fd73b&oe=5B3BCC27";

	public static Team getDebugTeam(int index) {
		List<Member> members = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			members.add(Member.getDebugMember(i));
		}

		List<TeamDescription> descriptions = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
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
