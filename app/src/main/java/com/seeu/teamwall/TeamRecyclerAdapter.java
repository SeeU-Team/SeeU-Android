package com.seeu.teamwall;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seeu.common.ItemClickListener;
import com.seeu.R;
import com.seeu.common.Member;
import com.seeu.common.Team;
import com.seeu.teamprofile.TeamProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 19/03/2018.
 */

public class TeamRecyclerAdapter extends Adapter<TeamViewHolder> implements ItemClickListener {

	private LayoutInflater inflater;
	private List<Team> teams;

	public TeamRecyclerAdapter(Context context, @NonNull List<Team> teams) {
		this.inflater = LayoutInflater.from(context);
		this.teams = teams;
	}

	@Override
	public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.teamwall_layout_team_item, parent, false);
		return new TeamViewHolder(view, this);
	}

	@Override
	public void onBindViewHolder(TeamViewHolder holder, int position) {
		Team team = getItem(position);

		List<String> memberPictures = new ArrayList<>(position);
		for (int i = 0; i < position; i++) {
			memberPictures.add(team.getMembers().get(i).getPictureUrl());
		}
		float maleProportion = (position % 10) / (float) 10.0;

		holder.setName(team.getName());
		holder.setTags("#uno#dos#tres");
		holder.setPicture(team.getPictureUrl());
		holder.setMemberPictures(memberPictures);
		holder.setGenderIndex(maleProportion);
		holder.setDescriptionPictures();
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	public Team getItem(int position) {
		return teams.get(position);
	}

	@Override
	public void onItemClick(View view, int position) {
		Context context = view.getContext();
		Intent intent = new Intent(context, TeamProfileActivity.class);
		intent.putExtra("TeamId", getItem(position).getId());

		context.startActivity(intent);
		// Toast.makeText(context, "You clicked " + getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
	}
}
