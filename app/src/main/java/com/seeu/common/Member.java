package com.seeu.common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class Member implements Serializable {

	private long id;
	private String pictureUrl;
	private String name;
	private int mark;
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

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
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

	// TODO: Remove the debug url
	public static final String DEBUG_PICTURE_URL = "https://scontent.xx.fbcdn.net/v/t1.0-1/p200x200/22365631_1924906760859051_4812781837110089872_n.jpg?oh=a27a7d0053306572e7e485b647db99d4&oe=5B3A50DA";

	public static Member getDebugMember(int index) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -index);

		Member member = new Member();
		member.setId(index);
		member.setPictureUrl(Member.DEBUG_PICTURE_URL);
		member.setName("Member " + index);
		member.setMark(index%6);
		member.setConnected(index%2 == 0);
		member.setLastConnection(calendar.getTime());

		return member;
	}
}
