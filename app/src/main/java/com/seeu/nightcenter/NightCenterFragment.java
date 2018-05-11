package com.seeu.nightcenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class NightCenterFragment extends Fragment {

	private CardView firstTeamCardView;
	private Team firstTeam;

	private CardView secondTeamCardView;
	private Team secondTeam;

	private MemberRecyclerAdapter memberRecyclerAdapter;
	private List<Member> members;

	public NightCenterFragment() {
		firstTeam = null;
		secondTeam = null;
		members = new ArrayList<>();

		loadTeams();
		loadMembers();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nightcenter_fragment, container, false);

		firstTeamCardView = view.findViewById(R.id.firstTeamCardView);
		secondTeamCardView = view.findViewById(R.id.secondTeamCardView);

		setupMemberRecycler(view);
		applyPicture();

		return view;
	}

	private void setupMemberRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(getActivity(), members);

		RecyclerView memberRecycler = view.findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	private void loadTeams() {
		// TODO: make http request to load data

		firstTeam = Team.getDebugTeam(0);
		secondTeam = Team.getDebugTeam(1);

		applyPicture();
	}

	private void loadMembers() {
		// TODO: make http request to load data

		for (int i = 0; i < 10; i++) {
			members.add(Member.getDebugMember(i));
		}

		if (null != memberRecyclerAdapter) {
			memberRecyclerAdapter.notifyDataSetChanged();
		}
	}

	private void applyPicture() {
		if (null != firstTeamCardView && null != firstTeam) {
			new DownloadImageAndSetBackgroundTask(firstTeamCardView, 150, 150, 150).execute(firstTeam.getPictureUrl());
		}

		if (null != secondTeamCardView && null != secondTeam) {
			new DownloadImageAndSetBackgroundTask(secondTeamCardView, 150, 150, 150).execute(secondTeam.getPictureUrl());
		}
	}
}
