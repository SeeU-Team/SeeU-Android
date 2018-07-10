package com.seeu.teamwall;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.team.profile.TeamProfileActivity;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Team wall fragment to search teams by category and location.
 */
public class TeamWallFragment extends Fragment {

	public static final int INTENT_TEAM_PROFILE = 4;

	private Member currentUser;
	private MemberHasTeam memberHasTeam;

	private CategoryRecyclerAdapter categoryRecyclerAdapter;
	private List<Category> categories;
	private CategoryService categoryService;

	private TeamRecyclerAdapter teamRecyclerAdapter;
	private List<Team> teams;
	private TeamService teamService;

	private boolean alreadyRefreshingTeams;

	/**
	 * Constructor
	 */
	public TeamWallFragment() {
		categories = new ArrayList<>();
		teams = new ArrayList<>();
		alreadyRefreshingTeams = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.teamService = new TeamService(this.getActivity());
		this.categoryService = new CategoryService(this.getActivity());

		currentUser = SharedPreferencesManager.getEntity(getActivity(), Member.STORAGE_KEY, Member.class);
		memberHasTeam = SharedPreferencesManager.getObject(getActivity(), MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);

		loadCategories();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teamwall_fragment, container, false);

		// TODO: get saved state from last instance of fragment
		setupCategoryRecycler(view);
		setupTeamRecycler(view);

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		// TODO: save the state of the fragment before it is destroy
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (INTENT_TEAM_PROFILE == requestCode && RESULT_OK == resultCode) {
			Team team = (Team) data.getSerializableExtra(TeamProfileActivity.TEAM_UP_RESULT_NAME);
			teams.remove(team);
			teamRecyclerAdapter.notifyDataSetChanged();
		}
	}

	public void onCategoryClick(View view, int position) {
		categoryRecyclerAdapter.setSelected(position);
		refreshTeams(categories.get(position));
	}

	public void onTeamClick(View view, int position) {
		Team team = teams.get(position);
		Intent intent = new Intent(getActivity(), TeamProfileActivity.class);
		intent.putExtra(Team.STORAGE_KEY, team);
		intent.putExtra(TeamProfileActivity.TEAM_UP_DISPLAY_BTN_NAME, true);

		startActivityForResult(intent, INTENT_TEAM_PROFILE);
	}

	/**
	 * Method that set up the recycler view for the team categories.
	 * @param view the view that the recycler view belongs to
	 */
	private void setupCategoryRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		categoryRecyclerAdapter = new CategoryRecyclerAdapter(getActivity(), categories, this::onCategoryClick);

		// set up the RecyclerView for the categories of team
		RecyclerView categoryRecycler = view.findViewById(R.id.categoryRecycler);
		categoryRecycler.setAdapter(categoryRecyclerAdapter);
	}

	/**
	 * Method that set up the recycler view for the teams.
	 * @param view the view that the recycler view belongs to
	 */
	private void setupTeamRecycler(View view) {
		teamRecyclerAdapter = new TeamRecyclerAdapter(getActivity(), teams, this::onTeamClick);

		RecyclerView teamRecycler = view.findViewById(R.id.teamRecycler);
		teamRecycler.setAdapter(teamRecyclerAdapter);
	}

	/**
	 * Load all the categories of teams. Refresh teams when received.
	 */
	private void loadCategories() {
		categories.clear();

		categoryService.getAllCategories(new CustomResponseListener<Category[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TeamWallFragment", "Error while loading categories", error);
				Toast.makeText(TeamWallFragment.this.getActivity(), "Error while loading categories " + error.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResponse(Category[] response) {
				Collections.addAll(categories, response);

				if (null != categoryRecyclerAdapter) {
					categoryRecyclerAdapter.setSelected(0);
					categoryRecyclerAdapter.notifyDataSetChanged();
				}

				if (response.length > 0) {
					refreshTeams(categories.get(0));
				}
			}
		});
	}

	/**
	 * Refresh the teams accordingly with the new selected category.
	 * @param selectedType the category of teams
	 */
	private void refreshTeams(Category selectedType) {
		if (alreadyRefreshingTeams) {
			return;
		}

		alreadyRefreshingTeams = true;

		teamService.getTeams(selectedType, memberHasTeam.getTeam(), new CustomResponseListener<Team[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				teams.clear();
				alreadyRefreshingTeams = false;
				Log.e("TeamWallFragment", "Error while loading Teams", error);
				Toast.makeText(TeamWallFragment.this.getActivity(), "Error while loading Teams " + error.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResponse(Team[] response) {
				teams.clear();
				alreadyRefreshingTeams = false;
				Collections.addAll(teams, response);

				if (null != teamRecyclerAdapter) {
					teamRecyclerAdapter.notifyDataSetChanged();
				}
			}
		});
	}
}
