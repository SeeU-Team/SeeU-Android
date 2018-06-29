package com.seeu.member.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.subviews.Mark;
import com.seeu.member.Member;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.io.Serializable;

/**
 * Created by thomasfouan on 26/03/2018.
 *
 * Activity that display member's profile.
 */
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

	/**
	 * Set the member entity from the intent.
	 * Throw an exception if the member is not provided by caller.
	 */
	private void setMember() {
		Serializable ser = getIntent().getSerializableExtra(Member.STORAGE_KEY);

		if (null == ser) {
			throw new IllegalStateException("No member provided");
		}

		member = (Member) ser;
	}

	/**
	 * Update the UI with member's info.
	 */
	private void updateUI() {

		setPictureBlurred();
		setPicture();
		setPictureDescription();

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

	/**
	 * Update the blurred image view with the member's picture.
	 */
	private void setPictureBlurred() {
		if (isPictureBlurredLayoutDrawn
				&& null != member) {
			new DownloadImageAndSetBackgroundTask(pictureBlurred, 0, true).execute(member.getProfilePhotoUrl());
		}
	}

	/**
	 * Update the image view with member's picture.
	 */
	private void setPicture() {
		if (isPictureLayoutDrawn
				&& null != member) {
			new DownloadImageAndSetBackgroundTask(picture, 140).execute(member.getProfilePhotoUrl());
		}
	}

	/**
	 * Update the image view wit the member's description picture.
	 */
	private void setPictureDescription() {
		if (isPictureDescriptionLayoutDrawn
				&& null != member) {
			new DownloadImageAndSetBackgroundTask(pictureDescription, 0).execute(member.getProfilePhotoUrl());
		}
	}

	/**
	 * Method called just before the system will draw the picture blurred layout.
	 * This lets the activity know when the view is drawn and avoids manipulations before it has been drawn.
	 *
	 * @return true to proceed the drawing. False to cancel it
	 *
	 * For more information, see {@link ViewTreeObserver.OnPreDrawListener#onPreDraw()}
	 */
	public boolean onPreDrawPictureBlurred() {
		if (!isPictureBlurredLayoutDrawn) {
			isPictureBlurredLayoutDrawn = true;
			pictureBlurred.getViewTreeObserver().removeOnDrawListener(this::onPreDrawPictureBlurred);

			setPictureBlurred();
		}
		return true;
	}

	/**
	 * Method called just before the system will draw the picture layout.
	 * This lets the activity know when the view is drawn and avoids manipulations before it has been drawn.
	 *
	 * @return true to proceed the drawing. False to cancel it
	 *
	 * For more information, see {@link ViewTreeObserver.OnPreDrawListener#onPreDraw()}
	 */
	public boolean onPreDrawPicture() {
		if (!isPictureLayoutDrawn) {
			isPictureLayoutDrawn = true;
			picture.getViewTreeObserver().removeOnDrawListener(this::onPreDrawPicture);

			setPicture();
		}
		return true;
	}

	/**
	 * Method called just before the system will draw the description picture blurred layout.
	 * This lets the activity know when the view is drawn and avoids manipulations before it has been drawn.
	 *
	 * @return true to proceed the drawing. False to cancel it
	 *
	 * For more information, see {@link ViewTreeObserver.OnPreDrawListener#onPreDraw()}
	 */
	public boolean onPreDrawPictureDescription() {
		if (!isPictureDescriptionLayoutDrawn) {
			isPictureDescriptionLayoutDrawn = true;
			pictureDescription.getViewTreeObserver().removeOnDrawListener(this::onPreDrawPictureDescription);

			setPictureDescription();
		}
		return true;
	}

	/**
	 * Called when th user clicks on the message button.
	 * Starts the {@link ChatActivity}.
	 *
	 * @param v the button clicked
	 */
	public void onMessageActionClick(View v) {
		Context context = v.getContext();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(Member.STORAGE_KEY, member);

		context.startActivity(intent);
	}
}
