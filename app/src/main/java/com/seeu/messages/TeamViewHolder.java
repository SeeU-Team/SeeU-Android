package com.seeu.messages;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.team.Team;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * View holder for a team in the list.
 */
class TeamViewHolder extends ViewHolder implements OnClickListener {

	private ConstraintLayout layoutPicture;
	private TextView name;
	private TextView tags;

	private ItemClickListener listener;

	public TeamViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);

		layoutPicture	= itemView.findViewById(R.id.teamCardView);
		name			= itemView.findViewById(R.id.teamName);
		tags			= itemView.findViewById(R.id.teamTags);

		itemView.setOnClickListener(this);
		this.listener = listener;
	}

	/**
	 * Set the team's name in the UI.
	 * @param name the team's name
	 */
	private void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Set the team's tags in the UI.
	 * @param tags the team's tags
	 */
	private void setTags(String tags) {
		this.tags.setText(tags);
	}

	/**
	 * Set the team's picture in the UI.
	 * @param pictureUrl the picture's url
	 */
	private void setPicture(String pictureUrl) {
		new DownloadImageAndSetBackgroundTask(layoutPicture, 0, 250, 250).execute(pictureUrl);
	}

	/**
	 * Update the UI with info of the team.
	 * @param team the team to display
	 */
	public void setData(Team team) {
		setPicture(team.getPictureUrl());
		setName(team.getName());
		setTags(team.getTags());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
