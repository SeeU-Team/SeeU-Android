package com.seeu.team;

import com.seeu.common.Entity;
import com.seeu.member.Gender;
import com.seeu.member.Member;
import com.seeu.teamwall.Category;

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

	/**
	 * Key used when the entity is passed in an intent or store in the sharedPreferences.
	 */
	public static final String STORAGE_KEY = "team";

	private String description;
	private String place;
	private int mark;

	// Use ArrayList because it is Serializable (List interface is not)
	private ArrayList<Category> categories;
	private ArrayList<Tag> tags;
	private ArrayList<Member> members;
	private ArrayList<TeamDescription> descriptions;

	private boolean merged;

	public Team() {
		categories = new ArrayList<>();
		tags = new ArrayList<>();
		members = new ArrayList<>();
		descriptions = new ArrayList<>();
	}

	public void setTagsFromString(String tags) {
		String[] tagsArray = tags.split("#");
		this.tags.clear();

		for (String s : tagsArray) {
			if (!s.trim().isEmpty()) {
				Tag tag = Tag.builder()
						.name(s)
						.build();

				this.tags.add(tag);
			}
		}
	}

	public String getTagsAsString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Tag tag : tags) {
			stringBuilder.append("#");
			stringBuilder.append(tag.getName());
		}

		return stringBuilder.toString();
	}

	public float getMaleProportion() {
		float maleProportion = 0;

		for (Member member : members) {
			if (Gender.MALE.equals(member.getGender())) {
				maleProportion++;
			}
		}

		return maleProportion / members.size();
	}
}
