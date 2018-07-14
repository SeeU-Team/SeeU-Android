package com.seeu.common.subviews;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Important : there is 2 classes that do the same thing.
 * This one is a fragment and is used inside the TeamCard fragment.
 *
 * Class that manages the displaying of the small Member pictures for a Team.
 * It can display at most 5 members.
 * If there is more members, group them in a sixth bubble and show the number of extras.
 */
public class TeamMemberPicturesFragment extends Fragment {

	private Team team;

	private ImageView[] memberPictures;
	private TextView extraMembers;
	private List<DownloadImageAndSetBackgroundTask> asyncTasks;

	public TeamMemberPicturesFragment() {
		team = null;
		asyncTasks = new ArrayList<>(5);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.team_members_pictures_fragment, container, false);

		memberPictures = new ImageView[5];

		memberPictures[0]	= view.findViewById(R.id.teamMemberPicture1);
		memberPictures[1]	= view.findViewById(R.id.teamMemberPicture2);
		memberPictures[2]	= view.findViewById(R.id.teamMemberPicture3);
		memberPictures[3]	= view.findViewById(R.id.teamMemberPicture4);
		memberPictures[4]	= view.findViewById(R.id.teamMemberPicture5);

		extraMembers		= view.findViewById(R.id.extraMembers);

		Serializable ser = getArguments().getSerializable(Team.STORAGE_KEY);
		if (null != ser && ser instanceof Team) {
			team = (Team) ser;
			setMemberPictures();
		}

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		cancelTasks();
	}

	/**
	 * Set the urls of the member pictures that will be displayed.
	 * If there are less than 5 members, hide useless image views.
	 * If there are more than 5 members, increment a counter that will be displayed in the extra members bubble.
	 */
	public void setMemberPictures() {
		int nbMembers = (team.getMembers() != null) ? team.getMembers().size() : 0;

		for (int i = 0; i < nbMembers && i < memberPictures.length; i++) {
			ImageView currentPicture = memberPictures[i];
			currentPicture.setVisibility(View.VISIBLE);

			String url = team.getMembers().get(i).getProfilePhotoUrl();

			ImageUtils.runJustBeforeBeingDrawn(currentPicture, () -> {
				DownloadImageAndSetBackgroundTask asyncTask = new DownloadImageAndSetBackgroundTask(currentPicture, 16);
				asyncTask.execute(url);
				asyncTasks.add(asyncTask);
			});
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

	private void cancelTasks() {
		for (DownloadImageAndSetBackgroundTask asyncTask : asyncTasks) {
			asyncTask.cancelDownload();
		}
		asyncTasks.clear();
	}
}
