package com.seeu.teamwall;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.common.Member;
import com.seeu.common.Team;
import com.seeu.common.TeamDescription;
import com.seeu.common.subviews.GenderIndex;
import com.seeu.common.subviews.TeamMemberPictures;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 19/03/2018.
 */

public class TeamViewHolder extends ViewHolder implements OnClickListener {

	private static final int MAX_DESCRIPTION_PICTURES = 4;

	private TextView name;
	private TextView tags;
	private ConstraintLayout layoutPicture;
	private TeamMemberPictures teamMemberPictures;
	private GenderIndex genderIndex;
	private ImageView[] descriptionPictures;

	private ItemClickListener listener;

	public TeamViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);

		this.listener = listener;
		teamMemberPictures = new TeamMemberPictures(itemView);
		descriptionPictures = new ImageView[MAX_DESCRIPTION_PICTURES];

		name 					= itemView.findViewById(R.id.teamName);
		tags 					= itemView.findViewById(R.id.teamTags);
		layoutPicture 			= itemView.findViewById(R.id.teamPicture);
		descriptionPictures[0] 	= itemView.findViewById(R.id.teamDescription1);
		descriptionPictures[1] 	= itemView.findViewById(R.id.teamDescription2);
		descriptionPictures[2] 	= itemView.findViewById(R.id.teamDescription3);
		descriptionPictures[3] 	= itemView.findViewById(R.id.teamDescription4);

		View genderIndexView = itemView.findViewById(R.id.genderIndex);
		genderIndex = new GenderIndex(genderIndexView);

		layoutPicture.setOnClickListener(this);
	}

	private void setName(String name) {
		this.name.setText(name);
	}

	private void setTags(String tags) {
		this.tags.setText(tags);
	}

	private void setPicture(String pictureUrl) {
		new DownloadImageAndSetBackgroundTask(layoutPicture, 20, 250, 250).execute(pictureUrl);
	}

	private void setMemberPictures(List<String> urls) {
		teamMemberPictures.setMemberPictures(urls);
	}

	private void setGenderIndex(float maleProportion) {
		genderIndex.setMaleProportion(maleProportion);
	}

	private void setDescriptionPictures(List<TeamDescription> descriptions) {
		for (int i = 0; i < descriptions.size() && i < 4; i++) {
			descriptionPictures[i].setImageResource(descriptions.get(i).getIcon());
		}
	}

	public void setData(Team team) {
		List<String> memberPictures = new ArrayList<>();
		for (Member member : team.getMembers()) {
			memberPictures.add(member.getPictureUrl());
		}

		float maleProportion = (team.getId() % 10) / (float) 10.0;

		setName(team.getName());
		setTags(team.getTags());
		setPicture(team.getPictureUrl());
		setMemberPictures(memberPictures);
		setGenderIndex(maleProportion);
		setDescriptionPictures(team.getDescriptions());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
