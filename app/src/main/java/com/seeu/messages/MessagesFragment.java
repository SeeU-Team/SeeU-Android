package com.seeu.messages;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.Team;
import com.seeu.TeamMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class MessagesFragment extends Fragment {

	private TeamCard teamCard;
	private Team team;

	public MessagesFragment() {
		teamCard = null;
		team = null;
		loadTeam();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_messages, container, false);

		teamCard = new TeamCard(view);
		teamCard.setData(team);

		return view;
	}

	private void loadTeam() {
		// TODO: make http request to load data
		team = new Team();
		team.setId(1L);
		team.setName("Ma Team <3");
		team.setMark(3);
		team.setPictureUrl(Team.DEBUG_PICTURE_URL);

		List<TeamMember> members = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			TeamMember member = new TeamMember();
			member.setId(i);
			member.setPictureUrl(TeamMember.DEBUG_PICTURE_URL);

			members.add(member);
		}
		team.setMembers(members);

		// if loading the data was slower than create the view, set data here
		if (null != teamCard) {
			teamCard.setData(team);
		}
	}
}
