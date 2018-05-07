package com.seeu.teamprofile;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;

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

	public void setIcon(int id) {
		icon.setImageResource(id);
	}

	public void setName(String name) {
		this.name.setText(name);
	}
}
