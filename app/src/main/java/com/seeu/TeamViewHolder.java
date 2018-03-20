package com.seeu;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 19/03/2018.
 */

public class TeamViewHolder extends ViewHolder implements OnClickListener {

	private static final int MAX_MEMBER_PICTURES = 5;

	private TextView name;
	private TextView tags;
	private ConstraintLayout layoutPicture;
	private ImageView[] teamMemberPictures;
	private TextView extraMembers;

	private ClickListener listener;

	public TeamViewHolder(View itemView, ClickListener listener) {
		super(itemView);

		this.listener = listener;
		teamMemberPictures = new ImageView[MAX_MEMBER_PICTURES];

		name 					= itemView.findViewById(R.id.teamName);
		tags 					= itemView.findViewById(R.id.teamTags);
		layoutPicture 			= itemView.findViewById(R.id.teamPicture);
		teamMemberPictures[0] 	= itemView.findViewById(R.id.teamMemberPicture1);
		teamMemberPictures[1] 	= itemView.findViewById(R.id.teamMemberPicture2);
		teamMemberPictures[2] 	= itemView.findViewById(R.id.teamMemberPicture3);
		teamMemberPictures[3] 	= itemView.findViewById(R.id.teamMemberPicture4);
		teamMemberPictures[4] 	= itemView.findViewById(R.id.teamMemberPicture5);
		extraMembers 			= itemView.findViewById(R.id.extraMembers);

		layoutPicture.setOnClickListener(this);
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setTags(String tags) {
		this.tags.setText(tags);
	}

	public void setPicture(String pictureUrl) {
		new DownloadImageAndSetBackgroundTask(layoutPicture, 20, 250, 250).execute(pictureUrl);
	}

	public void setTeamMemberPictures(String[] urls) {
		for (int i = 0; i < urls.length && i < MAX_MEMBER_PICTURES; i++) {
			teamMemberPictures[i].setVisibility(View.VISIBLE);
			new DownloadImageAndSetBackgroundTask(teamMemberPictures[i], 16, 32, 32).execute(urls[i]);
		}

		// Hide member pictures that are not used
		for (int i = urls.length; i < MAX_MEMBER_PICTURES; i++) {
			teamMemberPictures[i].setVisibility(View.GONE);
		}

		// Put all extra members in a circle with a number
		if (urls.length > MAX_MEMBER_PICTURES) {
			int nbExtraMembers = urls.length - MAX_MEMBER_PICTURES;
			String text = layoutPicture.getResources().getString(R.string.extra_member_placeholder, nbExtraMembers);
			extraMembers.setText(text);
			extraMembers.setVisibility(View.VISIBLE);
		} else {
			extraMembers.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
