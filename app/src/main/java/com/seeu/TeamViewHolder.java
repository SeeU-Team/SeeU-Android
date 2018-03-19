package com.seeu;

import android.net.Uri;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by thomasfouan on 19/03/2018.
 */

public class TeamViewHolder extends ViewHolder implements OnClickListener {

	private TextView name;
	private TextView tags;
	private ImageView picture;

	private ClickListener listener;

	public TeamViewHolder(View itemView, ClickListener listener) {
		super(itemView);

		this.listener = listener;

		name = itemView.findViewById(R.id.teamName);
		tags = itemView.findViewById(R.id.teamTags);
		picture = itemView.findViewById(R.id.teamPicture);

		picture.setOnClickListener(this);
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setTags(String tags) {
		this.tags.setText(tags);
	}

	public void setPicture(String pictureUrl) {
		this.picture.setImageURI(Uri.parse(pictureUrl));
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
