package com.seeu.messages;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.Team;
import com.seeu.TeamMember;
import com.seeu.TeamMemberPictures;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class TeamCard implements OnClickListener {

	private ImageView teamPicture;
	private TextView teamName;
	private TextView teamMark;
	private TextView nbNotReadMessages;

	private TeamMemberPictures teamMemberPictures;

	public TeamCard(View itemView) {

		teamPicture			= itemView.findViewById(R.id.teamPicture);
		teamName			= itemView.findViewById(R.id.teamName);
		teamMark			= itemView.findViewById(R.id.teamMark);
		nbNotReadMessages	= itemView.findViewById(R.id.nbNotReadMessages);

		teamMemberPictures = new TeamMemberPictures(itemView);

		CardView rootElement = itemView.findViewById(R.id.teamCard);
		rootElement.setOnClickListener(this);
	}

	public void setTeamPicture(String url) {
		new DownloadImageAndSetBackgroundTask(teamPicture, 2, 100, 80).execute(url);
	}

	public void setTeamName(String name) {
		teamName.setText(name);
	}

	public void setTeamMark(int mark) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < 5; i++) {
			if (i < mark) {
				builder.append("★");
			} else {
				builder.append("☆");
			}
		}

		teamMark.setText(builder.toString());
	}

	public void setNbNotReadMessages(int nbMessages) {
		nbNotReadMessages.setText(String.valueOf(nbMessages));
	}

	public void setTeamMemberPictures(List<String> urls) {
		teamMemberPictures.setMemberPictures(urls);
	}

	public void setData(Team team) {
		if (null == team) {
			return;
		}

		setTeamPicture(team.getPictureUrl());
		setTeamName(team.getName());
		setTeamMark(team.getMark());

		// TODO : get nb not read messages
		setNbNotReadMessages(21);

		List<String> urls = new ArrayList<>();
		for (TeamMember teamMember : team.getMembers()) {
			urls.add(teamMember.getPictureUrl());
		}
		setTeamMemberPictures(urls);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			urls = team.getMembers()
//					.stream()
//					.map(TeamMember::getPictureUrl)
//					.collect(Collectors.toList());
//		}
	}

	@Override
	public void onClick(View v) {
		// TODO: start new Activity for team messages screen
		Context context = v.getContext();
//		Intent intent = new Intent(context, ToChange.class);
//		context.startActivity(intent);
		Toast.makeText(context, "Team messages starts...", Toast.LENGTH_SHORT).show();
	}
}
