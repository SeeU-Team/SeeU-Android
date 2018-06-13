package com.seeu.member.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.subviews.Mark;
import com.seeu.member.Member;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.io.Serializable;

public class MemberProfileActivity extends Activity {

	private Member member;
	private boolean isPictureBlurredLayoutDrawn;
	private boolean isPictureLayoutDrawn;
	private boolean isPictureDescriptionLayoutDrawn;

	private ConstraintLayout pictureBlurred;
	private CardView picture;
	private View connectedStatus;
	private TextView name;
	private TextView catchPhrase;

	private ImageView pictureDescription;
	private TextView pictureDescriptionLabel;

	private Mark mark;

	private TextView textDescription;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberprofile_activity);
		isPictureBlurredLayoutDrawn = false;
		isPictureLayoutDrawn = false;
		isPictureDescriptionLayoutDrawn = false;

		pictureBlurred			= findViewById(R.id.memberPictureBlurred);
		picture					= findViewById(R.id.memberPicture);
		connectedStatus			= findViewById(R.id.connectedMemberIndicator);
		name					= findViewById(R.id.memberName);
		catchPhrase				= findViewById(R.id.memberCatchPhrase);
		pictureDescription		= findViewById(R.id.memberPictureDescription);
		pictureDescriptionLabel = findViewById(R.id.memberPictureDescriptionLabel);
		TextView markView		= findViewById(R.id.memberMark);
		textDescription			= findViewById(R.id.memberTextDescription);

		mark = new Mark(markView);

		pictureBlurred.getViewTreeObserver().addOnPreDrawListener(this::onPreDrawPictureBlurred);
		picture.getViewTreeObserver().addOnPreDrawListener(this::onPreDrawPicture);
		pictureDescription.getViewTreeObserver().addOnPreDrawListener(this::onPreDrawPictureDescription);

		setMember();
		updateUI();
	}

	private void setMember() {
		Serializable ser = getIntent().getSerializableExtra(Member.STORAGE_KEY);

		if (null == ser) {
			throw new IllegalStateException("No member provided");
		}

		member = (Member) ser;
	}

	private void updateUI() {

		if (isPictureBlurredLayoutDrawn) {
			setPictureBlurred();
		}
		if (isPictureLayoutDrawn) {
			setPicture();
		}
		if (isPictureDescriptionLayoutDrawn) {
			setPictureDescription();
		}

		if (member.isConnected()) {
			connectedStatus.setVisibility(View.VISIBLE);
		} else {
			connectedStatus.setVisibility(View.INVISIBLE);
		}

		name.setText(member.getName());
		catchPhrase.setText(member.getCatchPhrase());
		pictureDescriptionLabel.setText(member.getPictureDescriptionLabel());
		mark.setMark(member.getMark());
		textDescription.setText(member.getDescription());
	}

	private void setPictureBlurred() {
		if (isPictureBlurredLayoutDrawn
				&& null != member) {
			new DownloadImageAndSetBackgroundTask(pictureBlurred, 0, true).execute(member.getProfilePhotoUrl());
		}
	}

	private void setPicture() {
		if (isPictureLayoutDrawn
				&& null != member) {
			new DownloadImageAndSetBackgroundTask(picture, 140).execute(member.getProfilePhotoUrl());
		}
	}

	private void setPictureDescription() {
		if (isPictureDescriptionLayoutDrawn
				&& null != member) {
			new DownloadImageAndSetBackgroundTask(pictureDescription, 0).execute(member.getProfilePhotoUrl());
		}
	}

	public boolean onPreDrawPictureBlurred() {
		isPictureBlurredLayoutDrawn = true;
		pictureBlurred.getViewTreeObserver().removeOnDrawListener(this::onPreDrawPictureBlurred);

		setPictureBlurred();
		return true;
	}

	public boolean onPreDrawPicture() {
		isPictureLayoutDrawn = true;
		picture.getViewTreeObserver().removeOnDrawListener(this::onPreDrawPicture);

		setPicture();
		return true;
	}

	public boolean onPreDrawPictureDescription() {
		isPictureDescriptionLayoutDrawn = true;
		pictureDescription.getViewTreeObserver().removeOnDrawListener(this::onPreDrawPictureDescription);

		setPictureDescription();
		return true;
	}

	public void onMessageActionClick(View v) {
		Context context = v.getContext();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(Member.STORAGE_KEY, member);

		context.startActivity(intent);
	}
}
