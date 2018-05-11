package com.seeu.member;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class Member implements Serializable {

	public static final String INTENT_EXTRA_KEY = "member";

	private long id;
	private String pictureUrl;
	private String name;
	private String catchPhrase;
	private String pictureDescriptionUrl;
	private String pictureDescriptionLabel;
	private int mark;
	private String description;
	private boolean isConnected;
	private Date lastConnection;

	public Member() {
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

	public String getCatchPhrase() {
		return catchPhrase;
	}

	public void setCatchPhrase(String catchPhrase) {
		this.catchPhrase = catchPhrase;
	}

	public String getPictureDescriptionUrl() {
		return pictureDescriptionUrl;
	}

	public void setPictureDescriptionUrl(String pictureDescriptionUrl) {
		this.pictureDescriptionUrl = pictureDescriptionUrl;
	}

	public String getPictureDescriptionLabel() {
		return pictureDescriptionLabel;
	}

	public void setPictureDescriptionLabel(String pictureDescriptionLabel) {
		this.pictureDescriptionLabel = pictureDescriptionLabel;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean connected) {
		isConnected = connected;
	}

	public Date getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(Date lastConnection) {
		this.lastConnection = lastConnection;
	}

	@Override
	public String toString() {
		return "member " + id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Member member = (Member) o;
		return id == member.id;
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}

	// TODO: Remove the debug url
	public static final String DEBUG_PICTURE_URL = "https://scontent.xx.fbcdn.net/v/t1.0-1/p200x200/22365631_1924906760859051_4812781837110089872_n.jpg?oh=a27a7d0053306572e7e485b647db99d4&oe=5B3A50DA";

	public static Member getDebugMember(int index) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -index);

		Member member = new Member();
		member.setId(index);
		member.setPictureUrl(Member.DEBUG_PICTURE_URL);
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
