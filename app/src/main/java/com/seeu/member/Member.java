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
	private String name;
	private String catchPhrase;
	private String description;
	private String profilePhotoUrl;
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

	// TODO: Remove the debug url
	public static final String DEBUG_PICTURE_URL = "https://scontent.xx.fbcdn.net/v/t1.0-1/p200x200/22365631_1924906760859051_4812781837110089872_n.jpg?oh=a27a7d0053306572e7e485b647db99d4&oe=5B3A50DA";

	public static Member getDebugMember(int index) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -index);

		Member member = new Member();
		member.setId((long) index);
		member.setProfilePhotoUrl(Member.DEBUG_PICTURE_URL);
		member.setName("Member " + index);
		member.setCatchPhrase("Catch phrase " + index);
		member.setPictureDescriptionUrl(Member.DEBUG_PICTURE_URL);
		member.setPictureDescriptionLabel("Description label " + index);
		member.setMark(index%6);
		member.setDescription("Description " + index);
		member.setConnected(index%2 == 0);
		member.setLastConnection(calendar.getTime());

		return member;
	}
}
