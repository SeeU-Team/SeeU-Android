package com.seeu.team.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.common.subviews.GenderIndex;
import com.seeu.common.subviews.Mark;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.nightcenter.MemberRecyclerAdapter;
import com.seeu.team.Asset;
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.team.like.Like;
import com.seeu.team.like.LikeService;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.seeu.teamwall.TeamWallFragment.TEAMWALL_STARTED_NAME;

/**
 * Created by thomasfouan on 07/05/2018.
 *
 * Activity that displays the team's profile.
 */
public class TeamProfileActivity extends Activity implements CustomResponseListener<Like> {

	public static final String TEAM_UP_RESULT_NAME = "result";
	public static final String TEAM_PROFILE_FORCE_RELOAD = "reload";

	private boolean startedFromTeamwall;

	private ConstraintLayout picture;
	private DownloadImageAndSetBackgroundTask asyncTask;
	private TextView place;
	private TextView name;
	private TextView tags;
	private GenderIndex genderIndex;
	private MemberRecyclerAdapter memberRecyclerAdapter;
	private AssetRecyclerAdapter assetRecyclerAdapter;
	private Mark mark;
	private TextView textDescription;

	private Team team;
	private List<Member> members;
	private List<Asset> assets;
	private LikeService likeService;

	public TeamProfileActivity() {
		super();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamprofile_activity);

		likeService = new LikeService(this);
		members = new ArrayList<>();
		assets = new ArrayList<>();

		picture = findViewById(R.id.teamPicture);
		place = findViewById(R.id.teamPlace);
		name = findViewById(R.id.teamName);
		tags = findViewById(R.id.teamTags);
		textDescription = findViewById(R.id.teamTextDescription);

		TextView markView = findViewById(R.id.teamMark);
		mark = new Mark(markView);

		View genderIndexView = findViewById(R.id.genderIndex);
		genderIndex = new GenderIndex(genderIndexView);

		Button teamUpBtn = findViewById(R.id.teamUpBtn);
		startedFromTeamwall = getIntent().getBooleanExtra(TEAMWALL_STARTED_NAME, false);
		if (!startedFromTeamwall) {
			teamUpBtn.setVisibility(View.GONE);
		}

		setupMemberRecycler();
		setupTeamDescriptionReycler();

		setTeamFromCaller();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (null != asyncTask) {
			asyncTask.cancelDownload();
		}
	}

	/**
	 * Method that set up the recycler view for the members.
	 */
	private void setupMemberRecycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, members, startedFromTeamwall);

		RecyclerView memberRecycler = findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	/**
	 * Method that set up the recycler view for the team assets.
	 */
	private void setupTeamDescriptionReycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		assetRecyclerAdapter = new AssetRecyclerAdapter(this, assets);

		RecyclerView teamDescriptionRecycler = findViewById(R.id.assetRecycler);
		teamDescriptionRecycler.setAdapter(assetRecyclerAdapter);
	}

	/**
	 * Set the team from the intent provided by the caller.
	 * If the caller does not provide a team, throws an exception.
	 */
	private void setTeamFromCaller() {
		boolean needReload = getIntent().getBooleanExtra(TEAM_PROFILE_FORCE_RELOAD, false);
		Serializable ser = getIntent().getSerializableExtra(Team.STORAGE_KEY);

		if (null == ser) {
			throw new IllegalStateException("No team has been provided for team profile");
		}

		team = (Team) ser;

		if (needReload) {
			loadTeam(team.getId());
		} else {
			updateLists();

			updateUI();
		}
	}

	private void loadTeam(Long teamId) {
		TeamService teamService = new TeamService(this);
		teamService.getTeam(teamId, new CustomResponseListener<Team>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(TeamProfileActivity.this, "An error occurred while trying to retrieve the team", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(Team response) {
				team = response;
				updateLists();

				updateUI();
			}
		});
	}

	private void updateLists() {
		if (null != team.getMembers()) {
			members.addAll(team.getMembers());
		}

		if (null != team.getAssets()) {
			assets.addAll(team.getAssets());
		}

		if (null != memberRecyclerAdapter) {
			memberRecyclerAdapter.notifyDataSetChanged();
		}

		if (null != assetRecyclerAdapter) {
			assetRecyclerAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Update the UI with info from the team entity.
	 */
	private void updateUI() {
		setPicture();

		place.setText(team.getPlace());
		name.setText(team.getName());
		tags.setText(team.getTagsAsString());
		genderIndex.setMaleProportion(team.getMaleProportion());
		textDescription.setText(team.getDescription());
		mark.setMark(team.getMark());

		memberRecyclerAdapter.notifyDataSetChanged();
		assetRecyclerAdapter.notifyDataSetChanged();
	}

	private void setPicture() {
		ImageUtils.runJustBeforeBeingDrawn(picture, () -> {
			asyncTask = new DownloadImageAndSetBackgroundTask(picture, 0);
			asyncTask.execute(team.getProfilePhotoUrl());
		});
	}

	/**
	 * Handle clicks on the team up button.
	 * Notify the other team's leader that this team liked his.
	 * @param view the view clicked
	 */
	public void teamUpActionBtn(View view) {
		MemberHasTeam memberHasTeam = SharedPreferencesManager.getObject(this, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);
		likeService.likeTeam(memberHasTeam.getTeam(), team, this);
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(this, "An error occurred while trying to team up", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(Like response) {
		// End this activity when the team up has been successfully made.
		// Notify the caller that this team was team up (for the TeamWall to remove it from the list)
		Intent intent = new Intent();
		intent.putExtra(TEAM_UP_RESULT_NAME, team);
		setResult(RESULT_OK, intent);
		finish();
	}
}
