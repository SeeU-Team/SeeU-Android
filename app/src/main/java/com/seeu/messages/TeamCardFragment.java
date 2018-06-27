package com.seeu.messages;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.subviews.Mark;
import com.seeu.common.subviews.TeamMemberPicturesFragment;
import com.seeu.member.MemberHasTeam;
import com.seeu.member.MemberStatus;
import com.seeu.team.Team;
import com.seeu.team.edit.EditTeamProfileActivity;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.io.Serializable;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Fragment for the Team Card view that contains info of the member's team.
 * Used in the Messages Fragment.
 */
public class TeamCardFragment extends Fragment implements OnClickListener {

	private FloatingActionButton editTeamBtn;

	private ImageView picture;
	private TextView name;
	private TextView nbNotReadMessages;

	private Mark mark;

	private MemberHasTeam memberHasTeam;

	public TeamCardFragment() {
		memberHasTeam = null;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teamcard_fragment, container, false);

		editTeamBtn = view.findViewById(R.id.editTeamBtn);
		editTeamBtn.setVisibility(View.GONE);
		editTeamBtn.setOnClickListener(this::onEditTeamClick);

		picture = view.findViewById(R.id.teamPicture);
		name = view.findViewById(R.id.teamName);
		nbNotReadMessages = view.findViewById(R.id.nbNotReadMessages);

		TextView markView = view.findViewById(R.id.teamMark);
		mark = new Mark(markView);

		CardView rootElement = view.findViewById(R.id.teamCard);
		rootElement.setOnClickListener(this);

		Serializable ser = getArguments().getSerializable(MemberHasTeam.STORAGE_KEY);
		if (null != ser && ser instanceof MemberHasTeam) {
			memberHasTeam = (MemberHasTeam) ser;
			updateUI();
		}

		return view;
	}

	private void setupTeamMemberPicturesFragment() {
		Bundle bundle = new Bundle();
		bundle.putSerializable(Team.STORAGE_KEY, memberHasTeam.getTeam());

		TeamMemberPicturesFragment fragment = new TeamMemberPicturesFragment();
		fragment.setArguments(bundle);

		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.team_members_pictures_frame_layout, fragment);
		fragmentTransaction.commit();
	}

	/**
	 * Update the visibility of the edit team button if the user is the leader.
	 * @param status the user's status (leader or member)
	 */
	private void updateEditBtn(MemberStatus status) {
		if (MemberStatus.LEADER.equals(status)) {
			editTeamBtn.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Set the picture of the team in the UI.
	 * @param url the picture url
	 */
	private void setPicture(String url) {
		if (null != url) {
			new DownloadImageAndSetBackgroundTask(picture, 6, 100, 80).execute(url);
		}
	}

	/**
	 * Set the team's name in the UI.
	 * @param name the team's name
	 */
	private void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Set the team's mark in the UI.
	 * @param mark the team's mark
	 */
	private void setMark(int mark) {
		this.mark.setMark(mark);
	}

	/**
	 * Set the number of unread messages by the current member of the team chat in the UI.
	 * @param nbMessages the number of unread messages
	 */
	private void setNbNotReadMessages(int nbMessages) {
		String text = nbNotReadMessages.getResources().getString(R.string.not_read_messages_placeholder, nbMessages);
		nbNotReadMessages.setText(text);
	}

	/**
	 * Update the UI from info of the team.
	 */
	private void updateUI() {
		if (null == memberHasTeam
				|| null == memberHasTeam.getTeam()) {
			return;
		}

		Team team = memberHasTeam.getTeam();

		updateEditBtn(memberHasTeam.getStatus());

		setPicture(team.getPictureUrl());
		setName(team.getName());
		setMark(team.getMark());

		// TODO : get nb not read messages
		setNbNotReadMessages(21);

		setupTeamMemberPicturesFragment();
	}

	public void onEditTeamClick(View v) {
		Context context = v.getContext();
		Intent intent = new Intent(context, EditTeamProfileActivity.class);
		intent.putExtra(Team.STORAGE_KEY, memberHasTeam.getTeam());

		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		Context context = v.getContext();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(Team.STORAGE_KEY, memberHasTeam.getTeam());

		context.startActivity(intent);
	}
}
