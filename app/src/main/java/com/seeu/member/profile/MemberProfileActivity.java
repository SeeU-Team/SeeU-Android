package com.seeu.member.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.subviews.Mark;
import com.seeu.member.Member;
import com.seeu.teamwall.TeamWallFragment;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by thomasfouan on 26/03/2018.
 *
 * Activity that display member's profile.
 */
public class MemberProfileActivity extends Activity {

	private Member member;
	private List<DownloadImageAndSetBackgroundTask> asyncTasks;

	private ConstraintLayout pictureBlurred;
	private CardView picture;
	private View connectedStatus;
	private TextView name;

	private ImageView pictureDescription;
	private TextView pictureDescriptionLabel;

	private Mark mark;

	private TextView textDescription;

	public MemberProfileActivity() {
		asyncTasks = new ArrayList<>(3);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberprofile_activity);

		pictureBlurred			= findViewById(R.id.memberPictureBlurred);
		picture					= findViewById(R.id.memberPicture);
		connectedStatus			= findViewById(R.id.connectedMemberIndicator);
		name					= findViewById(R.id.memberName);
		pictureDescription		= findViewById(R.id.memberPictureDescription);
		pictureDescriptionLabel = findViewById(R.id.memberPictureDescriptionLabel);
		TextView markView		= findViewById(R.id.memberMark);
		textDescription			= findViewById(R.id.memberTextDescription);

		mark = new Mark(markView);

		FloatingActionButton messageBtn = findViewById(R.id.messageAction);
		boolean startedFromTeamwall = getIntent().getBooleanExtra(TeamWallFragment.TEAMWALL_STARTED_NAME, false);
		if (startedFromTeamwall) {
			messageBtn.setVisibility(GONE);
		}

		setMember();
		updateUI();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		for (DownloadImageAndSetBackgroundTask asyncTask : asyncTasks) {
			asyncTask.cancelDownload();
		}
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
		pictureDescriptionLabel.setText(member.getPictureDescriptionLabel());
		mark.setMark(member.getMark());
		textDescription.setText(member.getDescription());
	}

	/**
	 * Update the blurred image view with the member's picture.
	 */
	private void setPictureBlurred() {
		ImageUtils.runJustBeforeBeingDrawn(pictureBlurred, () -> {
			DownloadImageAndSetBackgroundTask asyncTask = new DownloadImageAndSetBackgroundTask(pictureBlurred, 0, true);
			asyncTask.execute(member.getProfilePhotoUrl());
			asyncTasks.add(asyncTask);
		});
	}

	/**
	 * Update the image view with member's picture.
	 */
	private void setPicture() {
		ImageUtils.runJustBeforeBeingDrawn(picture, () -> {
			DownloadImageAndSetBackgroundTask asyncTask = new DownloadImageAndSetBackgroundTask(picture, 140);
			asyncTask.execute(member.getProfilePhotoUrl());
			asyncTasks.add(asyncTask);
		});
	}

	/**
	 * Update the image view wit the member's description picture.
	 */
	private void setPictureDescription() {
		ImageUtils.runJustBeforeBeingDrawn(pictureDescription, () -> {
			DownloadImageAndSetBackgroundTask asyncTask = new DownloadImageAndSetBackgroundTask(pictureDescription, 0);
			asyncTask.execute(member.getProfilePhotoUrl());
			asyncTasks.add(asyncTask);
		});
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
