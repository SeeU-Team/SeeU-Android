package com.seeu.teamwall;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seeu.ClickListener;
import com.seeu.R;
import com.seeu.TeamMember;
import com.seeu.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 19/03/2018.
 */

public class TeamRecyclerAdapter extends Adapter<TeamViewHolder> implements ClickListener {

	private LayoutInflater inflater;
	private List<String> names;

	public TeamRecyclerAdapter(Context context, List<String> names) {
		this.inflater = LayoutInflater.from(context);
		this.names = names;
	}

	@Override
	public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.layout_team_item, parent, false);
		return new TeamViewHolder(view, this);
	}

	@Override
	public void onBindViewHolder(TeamViewHolder holder, int position) {
		List<String> teamMemberPictures = new ArrayList<>(position);
		for (int i = 0; i < position; i++) {
			teamMemberPictures.add(TeamMember.DEBUG_PICTURE_URL);
		}
		float maleProportion = (position % 10) / (float) 10.0;

		holder.setName(names.get(position));
		holder.setTags("#uno#dos#tres");
		holder.setPicture(Team.DEBUG_PICTURE_URL);
		holder.setMemberPictures(teamMemberPictures);
		holder.setGenderIndex(maleProportion);
		holder.setDescriptionPictures();
	}

	@Override
	public int getItemCount() {
		return names.size();
	}

	public String getItem(int position) {
		return names.get(position);
	}

	@Override
	public void onItemClick(View view, int position) {
		Toast.makeText(view.getContext(), "You clicked " + getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
	}
}
