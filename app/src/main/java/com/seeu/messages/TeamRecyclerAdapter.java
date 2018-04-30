package com.seeu.messages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.common.Team;

import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class TeamRecyclerAdapter extends Adapter<TeamViewHolder> implements ItemClickListener {

	private LayoutInflater inflater;
	private List<Team> teams;

	public TeamRecyclerAdapter(Context context, @NonNull List<Team> teams) {
		this.inflater = LayoutInflater.from(context);
		this.teams = teams;
	}

	public Team getItem(int position) {
		return teams.get(position);
	}

	@Override
	public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.messages_layout_team_item, parent, false);
		return new TeamViewHolder(view, this);
	}

	@Override
	public void onBindViewHolder(TeamViewHolder holder, int position) {
		Team team = getItem(position);

		holder.setPicture(team.getPictureUrl());
		holder.setName(team.getName());
		holder.setTags(team.getTags());
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	@Override
	public void onItemClick(View view, int position) {
		Toast.makeText(view.getContext(), "You clicked " + getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
	}
}
