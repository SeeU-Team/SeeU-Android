package com.seeu.teamwall;

import android.app.Fragment;
import android.content.Context;
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
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Team wall fragment to search teams by types and location.
 */
public class TeamWallFragment extends Fragment implements ItemClickListener {

	private Member currentUser;
	private MemberHasTeam memberHasTeam;

	private TeamTypeRecyclerAdapter teamTypeRecyclerAdapter;
	private List<TeamType> types;
	private TeamTypeService teamTypeService;

	private TeamRecyclerAdapter teamRecyclerAdapter;
	private List<Team> teams;
	private TeamService teamService;

	/**
	 * Constructor
	 */
	public TeamWallFragment() {
		types = new ArrayList<>();
		teams = new ArrayList<>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.teamService = new TeamService(this.getActivity());
		this.teamTypeService = new TeamTypeService(this.getActivity());

		currentUser = SharedPreferencesManager.getEntity(getActivity(), Member.STORAGE_KEY, Member.class);
		memberHasTeam = SharedPreferencesManager.getEntity(getActivity(), Member.STORAGE_KEY, MemberHasTeam.class);

		loadTypes();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teamwall_fragment, container, false);

		// TODO: get saved state from last instance of fragment
		setupTeamTypeRecycler(view);
		setupTeamRecycler(view);

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		// TODO: save the state of the fragment before it is destroy

	}

	@Override
	public void onItemClick(View view, int position) {
		teamTypeRecyclerAdapter.setSelected(position);
		refreshTeams(types.get(position));
	}

	/**
	 * Method that set up the recycler view for the team types.
	 * @param view the view that the recycler view belongs to
	 */
	private void setupTeamTypeRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		teamTypeRecyclerAdapter = new TeamTypeRecyclerAdapter(getActivity(), types, this);

		// set up the RecyclerView for the types of team
		RecyclerView teamTypeRecycler = view.findViewById(R.id.teamTypeRecycler);
		teamTypeRecycler.setAdapter(teamTypeRecyclerAdapter);
	}

	/**
	 * Method that set up the recycler view for the teams.
	 * @param view the view that the recycler view belongs to
	 */
	private void setupTeamRecycler(View view) {
		teamRecyclerAdapter = new TeamRecyclerAdapter(getActivity(), teams);

		RecyclerView teamRecycler = view.findViewById(R.id.teamRecycler);
		teamRecycler.setAdapter(teamRecyclerAdapter);
	}

	/**
	 * Load all the types of teams. Refresh teams when received.
	 */
	private void loadTypes() {
		final Context context = this.getActivity();
		types.clear();

		teamTypeService.getAllTeamTypes(new CustomResponseListener<TeamType[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TeamWallFragment", "Error while loading TeamTypes", error);
				Toast.makeText(context, "Error while loading TeamTypes " + error.getMessage(), Toast.LENGTH_LONG).show();


//				TeamType teamType = new TeamType();
//				teamType.setId(0);
//				teamType.setName("Type Bar");
//				types.clear();
//				types.add(teamType);
//
//				if (null != teamTypeRecyclerAdapter) {
//					teamTypeRecyclerAdapter.setSelected(0);
//					teamTypeRecyclerAdapter.notifyDataSetChanged();
//				}
//				refreshTeams(types.get(0));
			}

			@Override
			public void onResponse(TeamType[] response) {
				Collections.addAll(types, response);

				if (null != teamTypeRecyclerAdapter) {
					teamTypeRecyclerAdapter.setSelected(0);
					teamTypeRecyclerAdapter.notifyDataSetChanged();
				}

				if (response.length > 0) {
					refreshTeams(types.get(0));
				}
			}
		});
	}

	/**
	 * Refresh the teams accordingly with the new selected type.
	 * @param selectedType the type of teams
	 */
	private void refreshTeams(TeamType selectedType) {
		final Context context = this.getActivity();
		teams.clear();

		teamService.getTeams(selectedType, new CustomResponseListener<Team[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TeamWallFragment", "Error while loading Teams", error);
				Toast.makeText(context, "Error while loading Teams " + error.getMessage(), Toast.LENGTH_LONG).show();

//				teams.clear();
//				teams.add(Team.getDebugTeam(1));
//
//				if (null != teamRecyclerAdapter) {
//					teamRecyclerAdapter.notifyDataSetChanged();
//				}
			}

			@Override
			public void onResponse(Team[] response) {
				Collections.addAll(teams, response);

				if (null != teamRecyclerAdapter) {
					teamRecyclerAdapter.notifyDataSetChanged();
				}
			}
		});
	}
}
