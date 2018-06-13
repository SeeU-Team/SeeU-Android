package com.seeu.messages;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.ItemClickListener;
import com.seeu.team.Team;

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
		holder.setData(team);
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	@Override
	public void onItemClick(View view, int position) {
		Team team = getItem(position);
		Context context = view.getContext();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(Team.STORAGE_KEY, team);

		context.startActivity(intent);
	}
}
