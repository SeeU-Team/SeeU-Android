package com.seeu.team.profile;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.team.TeamDescription;

/**
 * Created by thomasfouan on 07/05/2018.
 *
 * Holder for a team description.
 */
class TeamDescriptionViewHolder extends ViewHolder {

	private ImageView icon;
	private TextView name;

	public TeamDescriptionViewHolder(View itemView) {
		super(itemView);

		icon	= itemView.findViewById(R.id.teamDescriptionImage);
		name	= itemView.findViewById(R.id.teamDescriptionName);
	}

	/**
	 * Set the description's icon in the view.
	 * @param id the id of the icon in the resources to display.
	 */
	private void setIcon(int id) {
		icon.setImageResource(id);
	}

	/**
	 * Set the description's name in the view.
	 * @param name the description's name
	 */
	private void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Set the team description's info in the view.
	 * @param teamDescription the team description to display
	 */
	public void setData(TeamDescription teamDescription) {
		setIcon(teamDescription.getIcon());
		setName(teamDescription.getName());
	}
}
