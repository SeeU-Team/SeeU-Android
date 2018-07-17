package com.seeu.member;

import com.seeu.common.Entity;

import java.util.Calendar;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Member entity. Represents a user in the application, already in a team or not.
 */
@Getter
@Setter
public class Member extends Entity {

	/**
	 * Key used when the entity is passed in an intent or store in the sharedPreferences.
	 */
	public static final String STORAGE_KEY = "member";

	private Long facebookId;
	private Gender gender;
	private String description;
	private String pictureDescriptionUrl;
	private String pictureDescriptionLabel;
	private int mark;
	private boolean isConnected;
	private Date lastConnection;

	public Member() {
	}

	@Override
	public String toString() {
		return "member " + id;
	}
}
