package com.seeu;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by thomasfouan on 16/03/2018.
 */

class TypeTeamViewHolder extends ViewHolder implements OnClickListener {

	private CardView rootLayout;
	private TextView name;

	private ClickListener listener;

	public TypeTeamViewHolder(View itemView, ClickListener listener) {
		super(itemView);

		this.listener = listener;
		this.rootLayout = itemView.findViewById(R.id.rootLayoutTypeTeamItem);
		this.name = itemView.findViewById(R.id.typeTeamName);

		itemView.setOnClickListener(this);
	}

	public void setDefaultBackground() {
		this.rootLayout.setBackgroundResource(R.drawable.not_selected_type_team_background);
	}

	public void setSelectedBackground() {
		this.rootLayout.setBackgroundResource(R.drawable.selected_type_team_background);
	}

	public void setName(final String name) {
		this.name.setText(name);
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
