package com.seeu.teamwall;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.common.ItemClickListener;
import com.seeu.R;
import com.seeu.team.Team;
import com.seeu.team.profile.TeamProfileActivity;

import java.util.List;

/**
 * Created by thomasfouan on 19/03/2018.
 *
 * Adapter for the list of team in the teamwall.
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
		holder.setData(team);
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	/**
	 * Return the team at the given position.
	 * @param position the position of the team in the list.
	 * @return the team
	 */
	public Team getItem(int position) {
		return teams.get(position);
	}

	@Override
	public void onItemClick(View view, int position) {
		Team team = getItem(position);
		Context context = view.getContext();
		Intent intent = new Intent(context, TeamProfileActivity.class);
		intent.putExtra(Team.STORAGE_KEY, team);

		context.startActivity(intent);
	}
}
