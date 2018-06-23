package com.seeu.messages;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.team.edit.EditTeamProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Fragment for the Messages tab.
 * Give access to chat with the member's team, older member's, and other team leader if the current member is leader too.
 */
public class MessagesFragment extends Fragment implements OnClickListener {

	private TeamCard teamCard;
	private Team team;

	private MemberRecyclerAdapter memberRecyclerAdapter;
	private List<Member> members;

	private TeamRecyclerAdapter teamRecyclerAdapter;
	private List<Team> teams;

	public MessagesFragment() {
		teamCard = null;
		team = null;
		members = new ArrayList<>();
		teams = new ArrayList<>();

		loadTeam();
		loadMembers();

		// TODO: check if the current member is leader or not
		loadTeams();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.messages_fragment, container, false);

		FloatingActionButton editTeamBtn = view.findViewById(R.id.editTeamBtn);
		editTeamBtn.setOnClickListener(this);

		teamCard = new TeamCard(view);
		teamCard.setData(team);

		setupMemberRecycler(view);
		setupTeamRecycler(view);

		return view;
	}

	/**
	 * Setup the recycler view to display the member list.
	 *
	 * @param view the root view
	 */
	private void setupMemberRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(getActivity(), members);

		RecyclerView memberRecycler = view.findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	/**
	 * Setup the recycler view to display the team list
	 *
	 * @param view the root view
	 */
	private void setupTeamRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		teamRecyclerAdapter = new TeamRecyclerAdapter(getActivity(), teams);

		RecyclerView teamRecycler = view.findViewById(R.id.teamRecycler);
		teamRecycler.setAdapter(teamRecyclerAdapter);
	}

	/**
	 * Load info of the team of the member.
	 */
	private void loadTeam() {
		// TODO: make http request to load data
		team = Team.getDebugTeam(1);

		// if loading the data was slower than create the view, set data here
		if (null != teamCard) {
			teamCard.setData(team);
		}
	}

	/**
	 * Load all members the current member has talked with before.
	 */
	private void loadMembers() {
		// TODO: make http request to load data

		for (int i = 0; i < 10; i++) {
			members.add(Member.getDebugMember(i));
		}

		if (null != memberRecyclerAdapter) {
			memberRecyclerAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Load all the teams mutually liked with the team of the member.
	 * The current member MUST be leader of his team to see the list.
	 */
	private void loadTeams() {
		// TODO: make http request to load data

		for (int i = 0; i < 10; i++) {
			teams.add(Team.getDebugTeam(i));
		}

		if (null != teamRecyclerAdapter) {
			teamRecyclerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		Context context = v.getContext();
		Intent intent = new Intent(context, EditTeamProfileActivity.class);
		intent.putExtra(Team.STORAGE_KEY, team);

		context.startActivity(intent);
	}
}
