package com.seeu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

	private TypeTeamRecyclerAdapter typeTeamRecyclerAdapter;

	private List<Integer> colors;
	private List<String> names;

	public TeamWallFragment() {
		colors = new ArrayList<>();
		colors.add(Color.BLUE);
		colors.add(Color.YELLOW);
		colors.add(Color.MAGENTA);
		colors.add(Color.RED);
		colors.add(Color.BLACK);

		names = new ArrayList<>();
		names.add("Popular");
		names.add("Barbecue");
		names.add("Dancing");
		names.add("Hangover");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_teamwall, container, false);

		// set up the RecyclerView
		RecyclerView recyclerView = view.findViewById(R.id.typeTeamRecycler);
		LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
		recyclerView.setLayoutManager(horizontalLayoutManagaer);

		typeTeamRecyclerAdapter = new TypeTeamRecyclerAdapter(getActivity(), colors, names, this);
		recyclerView.setAdapter(typeTeamRecyclerAdapter);

		return view;
	}

	@Override
	public void onItemClick(View view, int position) {
		Toast.makeText(getActivity(), "You clicked " + typeTeamRecyclerAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
	}
}
