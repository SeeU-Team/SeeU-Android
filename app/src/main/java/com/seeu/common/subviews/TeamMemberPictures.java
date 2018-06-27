package com.seeu.common.subviews;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.team.Team;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.List;


/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Important : there is 2 classes that do the same thing.
 * This one is used inside a View Holder in a list, and cannot be a fragment.
 *
 * Class that manages the displaying of the small Member pictures for a Team.
 * It can display at most 5 members.
 * If there is more members, group them in a sixth bubble and show the number of extras.
 */
public class TeamMemberPictures {

	private ImageView[] memberPictures;
	private TextView extraMembers;

	public TeamMemberPictures(View itemView) {

		memberPictures = new ImageView[5];

		memberPictures[0]	= itemView.findViewById(R.id.teamMemberPicture1);
		memberPictures[1]	= itemView.findViewById(R.id.teamMemberPicture2);
		memberPictures[2]	= itemView.findViewById(R.id.teamMemberPicture3);
		memberPictures[3]	= itemView.findViewById(R.id.teamMemberPicture4);
		memberPictures[4]	= itemView.findViewById(R.id.teamMemberPicture5);

		extraMembers		= itemView.findViewById(R.id.extraMembers);
	}

	/**
	 * Set the urls of the member pictures that will be displayed.
	 * If there are less than 5 members, hide useless image views.
	 * If there are more than 5 members, increment a counter that will be displayed in the extra members bubble.
	 */
	public void setMemberPictures(List<String> urls) {
		int nbMembers = urls.size();

		for (int i = 0; i < nbMembers && i < memberPictures.length; i++) {
			memberPictures[i].setVisibility(View.VISIBLE);

			String url = urls.get(i);
			if (null != url) {
				new DownloadImageAndSetBackgroundTask(memberPictures[i], 16, 32, 32).execute(url);
			}
		}

		// Hide member pictures that are not used
		for (int i = nbMembers; i < memberPictures.length; i++) {
			memberPictures[i].setVisibility(View.GONE);
		}

		// Put all extra members in a bubble with a number
		if (nbMembers > memberPictures.length) {
			int nbExtraMembers = nbMembers - memberPictures.length;
			String text = extraMembers.getResources().getString(R.string.extra_member_placeholder, nbExtraMembers);

			extraMembers.setText(text);
			extraMembers.setVisibility(View.VISIBLE);
		} else {
			extraMembers.setVisibility(View.GONE);
		}
	}
}
