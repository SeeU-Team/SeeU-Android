package com.seeu.teamwall;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.common.subviews.GenderIndex;
import com.seeu.common.subviews.TeamMemberPictures;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.team.Asset;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 19/03/2018.
 *
 * Holder for the team.
 */
public class TeamViewHolder extends ViewHolder {

	private static final int MAX_DESCRIPTION_PICTURES = 4;

	private TextView name;
	private TextView tags;
	private ConstraintLayout layoutPicture;
	private TeamMemberPictures teamMemberPictures;
	private GenderIndex genderIndex;
	private ImageView[] assetsViews;

	private ItemClickListener itemClickListener;
	private ItemClickListener teamUpBtnClickListener;

	private List<DownloadImageAndSetBackgroundTask> asyncTasks;

	public TeamViewHolder(View itemView, ItemClickListener itemClickListener, ItemClickListener teamUpBtnClickListener) {
		super(itemView);

		this.asyncTasks = new ArrayList<>();

		this.itemClickListener = itemClickListener;
		this.teamUpBtnClickListener = teamUpBtnClickListener;
		teamMemberPictures = new TeamMemberPictures(itemView);
		assetsViews = new ImageView[MAX_DESCRIPTION_PICTURES];

		name 					= itemView.findViewById(R.id.teamName);
		tags 					= itemView.findViewById(R.id.teamTags);
		layoutPicture 			= itemView.findViewById(R.id.teamPicture);
		assetsViews[0] 	= itemView.findViewById(R.id.asset1);
		assetsViews[1] 	= itemView.findViewById(R.id.asset2);
		assetsViews[2] 	= itemView.findViewById(R.id.asset3);
		assetsViews[3] 	= itemView.findViewById(R.id.asset4);

		View genderIndexView = itemView.findViewById(R.id.genderIndex);
		genderIndex = new GenderIndex(genderIndexView);

		Button button = itemView.findViewById(R.id.teamUpBtn);
		button.setOnClickListener(this::onTeamUpBtnClick);

		layoutPicture.setOnClickListener(this::onClick);
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
		ImageUtils.runJustBeforeBeingDrawn(layoutPicture, () -> {
			DownloadImageAndSetBackgroundTask asyncTask = new DownloadImageAndSetBackgroundTask(layoutPicture, 20);
			asyncTask.execute(pictureUrl);

			asyncTasks.add(asyncTask);
		});
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
	 * Set the team's assetsViews in the UI.
	 * @param assets the team's assetsViews
	 */
	private void setTeamDescriptions(List<Asset> assets) {
		for (int i = 0; i < assets.size(); i++) {
			Asset asset = assets.get(i);
			ImageView imageView = assetsViews[i];

			ImageUtils.runJustBeforeBeingDrawn(imageView, () -> {
				DownloadImageAndSetBackgroundTask asyncTask = new DownloadImageAndSetBackgroundTask(imageView, 0);
				asyncTask.execute(asset.getImageLight());

				asyncTasks.add(asyncTask);
			});
		}
	}

	/**
	 * Set the team's info in the view.
	 * @param team the team to display
	 */
	public void setData(Team team) {
		cancelPreviousTasks();

		List<String> memberPictures = new ArrayList<>();
		for (Member member : team.getMembers()) {
			memberPictures.add(member.getProfilePhotoUrl());
		}

		setName(team.getName());
		setTags(team.getTagsAsString());
		setPicture(team.getProfilePhotoUrl());
		setMemberPictures(memberPictures);
		setGenderIndex(team.getMaleProportion());
		setTeamDescriptions(team.getAssets());
	}

	public void onTeamUpBtnClick(View v) {
		teamUpBtnClickListener.onItemClick(v, getAdapterPosition());
	}

	public void onClick(View v) {
		itemClickListener.onItemClick(v, getAdapterPosition());
	}

	private void cancelPreviousTasks() {
		for (DownloadImageAndSetBackgroundTask asyncTask : asyncTasks) {
			asyncTask.cancelDownload();
		}
	}
}
