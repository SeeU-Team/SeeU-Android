package com.seeu.teamwall;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.team.TeamDescription;
import com.seeu.common.subviews.GenderIndex;
import com.seeu.common.subviews.TeamMemberPictures;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 19/03/2018.
 *
 * Holder for the team.
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

	/**
	 * Set the team's name in the UI.
	 * @param name the team's name
	 */
	private void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Set the team's tags in the UI.
	 * @param tags the team's tags
	 */
	private void setTags(String tags) {
		this.tags.setText(tags);
	}

	/**
	 * Set the team's picture in the UI.
	 * @param pictureUrl the team picture's url
	 */
	private void setPicture(String pictureUrl) {
		new DownloadImageAndSetBackgroundTask(layoutPicture, 20, 250, 250).execute(pictureUrl);
	}

	/**
	 * Set the member pictures in the UI.
	 * @param urls the member picture's urls
	 */
	private void setMemberPictures(List<String> urls) {
		teamMemberPictures.setMemberPictures(urls);
	}

	/**
	 * Set the proportion of male of the team in the UI.
	 * @param maleProportion the proportion of male, between 0 and 1
	 */
	private void setGenderIndex(float maleProportion) {
		genderIndex.setMaleProportion(maleProportion);
	}

	/**
	 * Set the team's descriptions in the UI.
	 * @param teamDescriptions the team's descriptions
	 */
	private void setTeamDescriptions(List<TeamDescription> teamDescriptions) {
		for (int i = 0; i < teamDescriptions.size() && i < 4; i++) {
			descriptionPictures[i].setImageResource(teamDescriptions.get(i).getIcon());
		}
	}

	/**
	 * Set the team's info in the view.
	 * @param team the team to display
	 */
	public void setData(Team team) {
		List<String> memberPictures = new ArrayList<>();
		for (Member member : team.getMembers()) {
			memberPictures.add(member.getProfilePhotoUrl());
		}

		float maleProportion = (team.getId() % 10) / (float) 10.0;

		setName(team.getName());
		setTags(team.getTagsAsString());
		setPicture(team.getPictureUrl());
		setMemberPictures(memberPictures);
		setGenderIndex(maleProportion);
		setTeamDescriptions(team.getDescriptions());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
