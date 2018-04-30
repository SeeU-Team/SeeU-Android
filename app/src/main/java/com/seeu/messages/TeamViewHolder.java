package com.seeu.messages;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 30/04/2018.
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

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setTags(String tags) {
		this.tags.setText(tags);
	}

	public void setPicture(String pictureUrl) {
		new DownloadImageAndSetBackgroundTask(layoutPicture, 0, 250, 250).execute(pictureUrl);
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
