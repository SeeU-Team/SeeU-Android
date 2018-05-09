package com.seeu.teamwall;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.seeu.common.ItemClickListener;
import com.seeu.R;

/**
 * Created by thomasfouan on 16/03/2018.
 */

class TeamTypeViewHolder extends ViewHolder implements OnClickListener {

	private CardView rootLayout;
	private TextView name;

	private ItemClickListener listener;

	public TeamTypeViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);

		this.listener = listener;
		this.rootLayout = itemView.findViewById(R.id.rootLayoutTeamTypeItem);
		this.name = itemView.findViewById(R.id.teamTypeName);

		itemView.setOnClickListener(this);
	}

	private void setDefaultBackground() {
		this.rootLayout.setBackgroundResource(R.drawable.not_selected_type_team_background);
	}

	private void setSelectedBackground() {
		this.rootLayout.setBackgroundResource(R.drawable.selected_type_team_background);
	}

	private void setName(final String name) {
		this.name.setText(name);
	}

	public void setData(TeamType teamType, boolean selected) {
		if (selected) {
			setSelectedBackground();
		} else {
			setDefaultBackground();
		}

		setName(teamType.getName());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
