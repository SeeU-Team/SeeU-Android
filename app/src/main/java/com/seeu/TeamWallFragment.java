package com.seeu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class TeamWallFragment extends Fragment implements ClickListener {

	private RecyclerView recyclerView;
	private TypeTeamRecyclerAdapter typeTeamRecyclerAdapter;

	private List<String> names;
	private int selectedType;

	public TeamWallFragment() {
		names = new ArrayList<>();
		names.add("Popular");
		names.add("Barbecue");
		names.add("Dancing");
		names.add("Hangover");

		selectedType = 0;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_teamwall, container, false);

		// set up the RecyclerView
		recyclerView = view.findViewById(R.id.typeTeamRecycler);
		LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
		recyclerView.setLayoutManager(horizontalLayoutManager);

		typeTeamRecyclerAdapter = new TypeTeamRecyclerAdapter(getActivity(), names, this);
		recyclerView.setAdapter(typeTeamRecyclerAdapter);

		return view;
	}

	@Override
	public void onItemClick(View view, int position) {
		TypeTeamViewHolder currentSelectedView = (TypeTeamViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedType);
		currentSelectedView.setDefaultBackground();

		selectedType = position;
		TypeTeamViewHolder newSelectedView = (TypeTeamViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedType);
		newSelectedView.setSelectedBackground();

		Toast.makeText(getActivity(), "You clicked " + typeTeamRecyclerAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
	}
}
