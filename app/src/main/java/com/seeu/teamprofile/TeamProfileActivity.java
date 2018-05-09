package com.seeu.teamprofile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.common.Member;
import com.seeu.common.Team;
import com.seeu.common.subviews.GenderIndex;
import com.seeu.common.subviews.Mark;
import com.seeu.nightcenter.MemberRecyclerAdapter;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 07/05/2018.
 */

public class TeamProfileActivity extends Activity implements ViewTreeObserver.OnPreDrawListener {

	private ConstraintLayout picture;
	private TextView place;
	private TextView name;
	private TextView tags;
	private GenderIndex genderIndex;
	private MemberRecyclerAdapter memberRecyclerAdapter;
	private TeamDescriptionRecyclerAdapter descriptionRecyclerAdapter;
	private Mark mark;
	private TextView textDescription;

	private Team team;
	private boolean isPictureLayoutDrawn;

	private List<Member> members;
	private List<TeamDescription> descriptions;

	public TeamProfileActivity() {
		super();
		members = new ArrayList<>();
		descriptions = new ArrayList<>();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamprofile_activity);

		picture = findViewById(R.id.teamPicture);
		place = findViewById(R.id.teamPlace);
		name = findViewById(R.id.teamName);
		tags = findViewById(R.id.teamTags);
		textDescription = findViewById(R.id.teamTextDescription);

		TextView markView = findViewById(R.id.teamMark);
		mark = new Mark(markView);

		View genderIndexView = findViewById(R.id.genderIndex);
		genderIndex = new GenderIndex(genderIndexView);

		isPictureLayoutDrawn = false;
		picture.getViewTreeObserver().addOnPreDrawListener(this);

		setupMemberRecycler();
		setupTeamDescriptionReycler();

		final long teamId = getTeamIdFromCaller();
		loadTeam(teamId);
	}

	private void setupMemberRecycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, members);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		RecyclerView memberRecycler = findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);
		memberRecycler.setLayoutManager(layoutManager);
	}

	private void setupTeamDescriptionReycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		descriptionRecyclerAdapter = new TeamDescriptionRecyclerAdapter(this, descriptions);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		RecyclerView teamDescriptionRecycler	= findViewById(R.id.teamDescriptionRecycler);
		teamDescriptionRecycler.setAdapter(descriptionRecyclerAdapter);
		teamDescriptionRecycler.setLayoutManager(layoutManager);
	}

	private long getTeamIdFromCaller() {
		Bundle bundle = getIntent().getExtras();
		long teamId = -1;

		if (null != bundle) {
			teamId = bundle.getLong("TeamId", -1);
		}

		if (-1 == teamId) {
			throw new IllegalStateException("TeamId has not been retrieved from caller");
		}

		return teamId;
	}

	private void loadTeam(long id) {
		// TODO: make http request to load data
		team = Team.getDebugTeam((int) id);

		members.addAll(team.getMembers());
		team.setMembers(members);

		descriptions.addAll(team.getDescriptions());
		team.setDescriptions(descriptions);

		updateUI();

		memberRecyclerAdapter.notifyDataSetChanged();
		descriptionRecyclerAdapter.notifyDataSetChanged();
	}

	private void updateUI() {
		if (isPictureLayoutDrawn) {
			new DownloadImageAndSetBackgroundTask(picture, 0).execute(team.getPictureUrl());
		}

		float maleProportion = (team.getId() % 10) / (float) 10.0;

		place.setText(team.getPlace());
		name.setText(team.getName());
		tags.setText(team.getTags());
		genderIndex.setMaleProportion(maleProportion);
		textDescription.setText(team.getDescription());
		mark.setMark(team.getMark());
	}

	public void teamUpActionBtn(View view) {
		Toast.makeText(view.getContext(), "You clicked on team up button", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onPreDraw() {
		isPictureLayoutDrawn = true;
		picture.getViewTreeObserver().removeOnPreDrawListener(this);

		if (null != team) {
			new DownloadImageAndSetBackgroundTask(picture, 0).execute(team.getPictureUrl());
		}

		return true;
	}
}
