package com.seeu.teamprofile;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.common.TeamDescription;

import java.util.List;

/**
 * Created by thomasfouan on 07/05/2018.
 */

class TeamDescriptionRecyclerAdapter extends Adapter<TeamDescriptionViewHolder> {

	private LayoutInflater inflater;
	private List<TeamDescription> teamDescriptions;

	public TeamDescriptionRecyclerAdapter(Context context, List<TeamDescription> teamDescriptions) {
		this.inflater = LayoutInflater.from(context);
		this.teamDescriptions = teamDescriptions;
	}

	private TeamDescription getItem(int position) {
		return teamDescriptions.get(position);
	}

	@Override
	public TeamDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.teamprofile_layout_team_description_item, parent, false);
		return new TeamDescriptionViewHolder(view);
	}

	@Override
	public void onBindViewHolder(TeamDescriptionViewHolder holder, int position) {
		TeamDescription teamDescription = getItem(position);
		holder.setData(teamDescription);
	}

	@Override
	public int getItemCount() {
		return teamDescriptions.size();
	}
}
