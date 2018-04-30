package com.seeu.messages;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.common.Team;
import com.seeu.common.Member;
import com.seeu.common.subviews.Mark;
import com.seeu.common.subviews.TeamMemberPictures;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class TeamCard implements OnClickListener {

	private ImageView picture;
	private TextView name;
	private TextView nbNotReadMessages;

	private Mark mark;
	private TeamMemberPictures memberPictures;

	public TeamCard(View itemView) {

		picture = itemView.findViewById(R.id.teamPicture);
		name = itemView.findViewById(R.id.teamName);
		nbNotReadMessages	= itemView.findViewById(R.id.nbNotReadMessages);

		TextView markView = itemView.findViewById(R.id.teamMark);
		mark = new Mark(markView);
		memberPictures = new TeamMemberPictures(itemView);

		CardView rootElement = itemView.findViewById(R.id.teamCard);
		rootElement.setOnClickListener(this);
	}

	public void setPicture(String url) {
		new DownloadImageAndSetBackgroundTask(picture, 2, 100, 80).execute(url);
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setMark(int mark) {
		this.mark.setMark(mark);
	}

	public void setNbNotReadMessages(int nbMessages) {
		String text = nbNotReadMessages.getResources().getString(R.string.not_read_messages_placeholder, nbMessages);
		nbNotReadMessages.setText(text);
	}

	public void setMemberPictures(List<String> urls) {
		memberPictures.setMemberPictures(urls);
	}

	public void setData(Team team) {
		if (null == team) {
			return;
		}

		setPicture(team.getPictureUrl());
		setName(team.getName());
		setMark(team.getMark());

		// TODO : get nb not read messages
		setNbNotReadMessages(21);

		List<String> urls = new ArrayList<>();
		for (Member member : team.getMembers()) {
			urls.add(member.getPictureUrl());
		}
		setMemberPictures(urls);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			urls = team.getMembers()
//					.stream()
//					.map(Member::getPictureUrl)
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
