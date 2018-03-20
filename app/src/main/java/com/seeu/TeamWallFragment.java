package com.seeu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class TeamWallFragment extends Fragment implements ClickListener {

	private RecyclerView typeTeamRecycler;
	private TypeTeamRecyclerAdapter typeTeamRecyclerAdapter;

	private RecyclerView teamRecycler;
	private TeamRecyclerAdapter teamRecyclerAdapter;

	private List<String> typeNames;
	private List<String> teamNames;

	public TeamWallFragment() {
		typeNames = new ArrayList<>();
		teamNames = new ArrayList<>();

		loadTypes();
	}

	private void loadTypes() {
		// TODO: make http request to get types
		typeNames.clear();
		typeNames.add("Popular");
		typeNames.add("Barbecue");
		typeNames.add("Dancing");
		typeNames.add("Hangover");

		refreshTeams(0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_teamwall, container, false);

		// TODO: get saved state from last instance of fragment
		setupTypeTeamRecycler(view);
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
		typeTeamRecyclerAdapter.setSelected(position);

		// TODO: reload teams
		refreshTeams(position);
	}

	private void setupTypeTeamRecycler(View view) {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
		// Keep reference of the dataset (arraylist here) in the adapter
		typeTeamRecyclerAdapter = new TypeTeamRecyclerAdapter(getActivity(), typeNames, this);

		// set up the RecyclerView for the types of team
		typeTeamRecycler = view.findViewById(R.id.typeTeamRecycler);
		typeTeamRecycler.setLayoutManager(layoutManager);
		typeTeamRecycler.setAdapter(typeTeamRecyclerAdapter);
	}

	private void setupTeamRecycler(View view) {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
		teamRecyclerAdapter = new TeamRecyclerAdapter(getActivity(), teamNames, (view1, position) -> {
			Toast.makeText(getActivity(), "You clicked " + teamRecyclerAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
		});

		teamRecycler = view.findViewById(R.id.teamRecycler);
		teamRecycler.setLayoutManager(layoutManager);
		teamRecycler.setAdapter(teamRecyclerAdapter);
	}

	private void refreshTeams(int selectedType) {
		// TODO: make http request to get data
		teamNames.clear();
		for (int i = 0; i < 10; i++) {
			teamNames.add("Team " + typeNames.get(selectedType) + " - " + i);
		}

		if (null != teamRecyclerAdapter) {
			teamRecyclerAdapter.notifyDataSetChanged();
		}
	}
}
