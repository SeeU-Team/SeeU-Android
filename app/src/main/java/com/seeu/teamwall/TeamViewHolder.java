package com.seeu.teamwall;

import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.common.ItemClickListener;
import com.seeu.R;
import com.seeu.common.subviews.GenderIndex;
import com.seeu.common.subviews.TeamMemberPictures;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

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

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setTags(String tags) {
		this.tags.setText(tags);
	}

	public void setPicture(String pictureUrl) {
		new DownloadImageAndSetBackgroundTask(layoutPicture, 20, 250, 250).execute(pictureUrl);
	}

	public void setMemberPictures(List<String> urls) {
		teamMemberPictures.setMemberPictures(urls);
	}

	public void setGenderIndex(float maleProportion) {
		genderIndex.setMaleProportion(maleProportion);
	}

	public void setDescriptionPictures() {
		descriptionPictures[0].setImageResource(R.drawable.icon_beer);
		descriptionPictures[1].setImageResource(R.drawable.icon_food);
		descriptionPictures[2].setImageResource(R.drawable.icon_soccer_ball);
		descriptionPictures[3].setImageResource(R.drawable.icon_swimming_pool);
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
