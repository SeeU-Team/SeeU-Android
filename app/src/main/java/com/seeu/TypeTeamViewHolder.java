package com.seeu;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by thomasfouan on 16/03/2018.
 */

class TypeTeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

	public ConstraintLayout rootLayout;
	public TextView name;

	private ClickListener listener;
	private int position;

	public TypeTeamViewHolder(View itemView, ClickListener listener) {
		super(itemView);

		this.listener = listener;
		this.rootLayout = itemView.findViewById(R.id.colorView);
		this.name = itemView.findViewById(R.id.typeTeamName);
		this.position = -1;

		itemView.setOnClickListener(this);
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, position);
	}
}
