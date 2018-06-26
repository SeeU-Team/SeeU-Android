package com.seeu.team.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.common.subviews.GenderIndex;
import com.seeu.common.subviews.Mark;
import com.seeu.member.Member;
import com.seeu.nightcenter.MemberRecyclerAdapter;
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by thomasfouan on 07/05/2018.
 *
 * Activity that displays the team's profile.
 */
public class TeamProfileActivity extends Activity implements OnPreDrawListener, CustomResponseListener<Void> {

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
	private TeamService teamService;
	private boolean isPictureLayoutDrawn;

	public TeamProfileActivity() {
		super();
		isPictureLayoutDrawn = false;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamprofile_activity);

		this.teamService = new TeamService(this);

		picture = findViewById(R.id.teamPicture);
		place = findViewById(R.id.teamPlace);
		name = findViewById(R.id.teamName);
		tags = findViewById(R.id.teamTags);
		textDescription = findViewById(R.id.teamTextDescription);

		TextView markView = findViewById(R.id.teamMark);
		mark = new Mark(markView);

		View genderIndexView = findViewById(R.id.genderIndex);
		genderIndex = new GenderIndex(genderIndexView);

		picture.getViewTreeObserver().addOnPreDrawListener(this);

		setTeamFromCaller();

		setupMemberRecycler();
		setupTeamDescriptionReycler();

		updateUI();
	}

	/**
	 * Method that set up the recycler view for the members.
	 */
	private void setupMemberRecycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, team.getMembers());

		RecyclerView memberRecycler = findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	/**
	 * Method that set up the recycler view for the team descriptions.
	 */
	private void setupTeamDescriptionReycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		descriptionRecyclerAdapter = new TeamDescriptionRecyclerAdapter(this, team.getDescriptions());

		RecyclerView teamDescriptionRecycler = findViewById(R.id.teamDescriptionRecycler);
		teamDescriptionRecycler.setAdapter(descriptionRecyclerAdapter);
	}

	/**
	 * Set the team from the intent provided by the caller.
	 * If the caller does not provide a team, throws an exception.
	 */
	private void setTeamFromCaller() {
		Serializable ser = getIntent().getSerializableExtra(Team.STORAGE_KEY);

		if (null == ser) {
			throw new IllegalStateException("No team has been provided for team profile");
		}

		team = (Team) ser;

		if (null == team.getMembers()) {
			team.setMembers(new ArrayList<>());
		}

		if (null == team.getDescriptions()) {
			team.setDescriptions(new ArrayList<>());
		}
	}

	/**
	 * Update the UI with info from the team entity.
	 */
	private void updateUI() {
		if (isPictureLayoutDrawn) {
			new DownloadImageAndSetBackgroundTask(picture, 0).execute(team.getPictureUrl());
		}

		float maleProportion = (team.getId() % 10) / (float) 10.0;

		place.setText(team.getPlace());
		name.setText(team.getName());
		tags.setText(team.getTagsAsString());
		genderIndex.setMaleProportion(maleProportion);
		textDescription.setText(team.getDescription());
		mark.setMark(team.getMark());

		memberRecyclerAdapter.notifyDataSetChanged();
		descriptionRecyclerAdapter.notifyDataSetChanged();
	}

	/**
	 * Handle clicks on the team up button.
	 * Notify the other team's leader that this team liked his.
	 * @param view the view clicked
	 */
	public void teamUpActionBtn(View view) {
		// TODO: if the member is leader, like the team. Otherwise, send notification to the leader
		teamService.likeTeam(team, this);
//		Toast.makeText(view.getContext(), "You clicked on team up button", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(this, "An error occurred while trying to team up", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(Void response) {
		// End this activity when the team up has been successfully made
		finish();
	}
}
