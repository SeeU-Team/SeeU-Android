package com.seeu.messages;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.common.Member;
import com.seeu.common.Team;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class MessagesFragment extends Fragment {

	private TeamCard teamCard;
	private Team team;

	private RecyclerView memberRecycler;
	private MemberRecyclerAdapter memberRecyclerAdapter;
	private List<Member> members;

	public MessagesFragment() {
		teamCard = null;
		team = null;
		members = new ArrayList<>();

		loadTeam();
		loadMembers();
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

		setupMemberRecycler(view);

		return view;
	}

	private void setupMemberRecycler(View view) {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(getActivity(), members);

		memberRecycler = view.findViewById(R.id.memberRecycler);
		memberRecycler.setLayoutManager(layoutManager);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	private void loadTeam() {
		// TODO: make http request to load data
		team = new Team();
		team.setId(1L);
		team.setName("Ma Team <3");
		team.setMark(3);
		team.setPictureUrl(Team.DEBUG_PICTURE_URL);

		List<Member> members = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			Member member = new Member();
			member.setId(i);
			member.setPictureUrl(Member.DEBUG_PICTURE_URL);

			members.add(member);
		}
		team.setMembers(members);

		// if loading the data was slower than create the view, set data here
		if (null != teamCard) {
			teamCard.setData(team);
		}
	}

	private void loadMembers() {
		// TODO: make http request to load data

		for (int i = 0; i < 10; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -i);

			Member member = new Member();
			member.setId(i);
			member.setPictureUrl(Member.DEBUG_PICTURE_URL);
			member.setName("Member " + i);
			member.setMark(i%6);
			member.setConnected(i%2 == 0);
			member.setLastConnection(calendar.getTime());

			members.add(member);
		}

		if (null != memberRecyclerAdapter) {
			memberRecyclerAdapter.notifyDataSetChanged();
		}
	}
}
