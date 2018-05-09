package com.seeu.teamprofile;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.TeamDescription;

/**
 * Created by thomasfouan on 07/05/2018.
 */

class TeamDescriptionViewHolder extends ViewHolder {

	private ImageView icon;
	private TextView name;

	public TeamDescriptionViewHolder(View itemView) {
		super(itemView);

		icon	= itemView.findViewById(R.id.teamDescriptionImage);
		name	= itemView.findViewById(R.id.teamDescriptionName);
	}

	private void setIcon(int id) {
		icon.setImageResource(id);
	}

	private void setName(String name) {
		this.name.setText(name);
	}

	public void setData(TeamDescription teamDescription) {
		setIcon(teamDescription.getIcon());
		setName(teamDescription.getName());
	}
}
