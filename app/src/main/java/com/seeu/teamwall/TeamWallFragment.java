package com.seeu.teamwall;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.common.ItemClickListener;
import com.seeu.R;
import com.seeu.common.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class TeamWallFragment extends Fragment implements ItemClickListener {

	private TeamTypeRecyclerAdapter teamTypeRecyclerAdapter;
	private List<TeamType> types;

	private TeamRecyclerAdapter teamRecyclerAdapter;
	private List<Team> teams;

	public TeamWallFragment() {
		types = new ArrayList<>();
		teams = new ArrayList<>();

		loadTypes();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		// TODO: reload teams
		refreshTeams(position);
	}

	private void setupTeamTypeRecycler(View view) {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
		// Keep reference of the dataset (arraylist here) in the adapter
		teamTypeRecyclerAdapter = new TeamTypeRecyclerAdapter(getActivity(), types, this);

		// set up the RecyclerView for the types of team
		RecyclerView teamTypeRecycler = view.findViewById(R.id.teamTypeRecycler);
		teamTypeRecycler.setLayoutManager(layoutManager);
		teamTypeRecycler.setAdapter(teamTypeRecyclerAdapter);
	}

	private void setupTeamRecycler(View view) {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
		teamRecyclerAdapter = new TeamRecyclerAdapter(getActivity(), teams);

		RecyclerView teamRecycler = view.findViewById(R.id.teamRecycler);
		teamRecycler.setLayoutManager(layoutManager);
		teamRecycler.setAdapter(teamRecyclerAdapter);
	}

	private void loadTypes() {
		// TODO: make http request to get types
		String[] typeNames = {"Popular", "Barbecue", "Dancing", "Hangover"};
		types.clear();

		for (int i = 0; i < typeNames.length; i++) {
			TeamType teamType = new TeamType();
			teamType.setId(i);
			teamType.setName(typeNames[i]);

			types.add(teamType);
		}

		refreshTeams(0);
	}

	private void refreshTeams(int selectedType) {
		// TODO: make http request to get data
		teams.clear();
		for (int i = 0; i < 10; i++) {
			Team team = Team.getDebugTeam(i);
			team.setName("Team " + i + " - " + types.get(selectedType).getName());

			teams.add(team);
		}

		if (null != teamRecyclerAdapter) {
			teamRecyclerAdapter.notifyDataSetChanged();
		}
	}
}
