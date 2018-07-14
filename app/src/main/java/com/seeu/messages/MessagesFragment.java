package com.seeu.messages;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.member.MemberService;
import com.seeu.member.MemberStatus;
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.team.like.LikeService;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Fragment for the Messages tab.
 * Give access to chat with the member's team, older member's, and other team leader if the current member is leader too.
 */
public class MessagesFragment extends Fragment {

	private Member currentUser;
	private MemberHasTeam memberHasTeam;
	private TeamService teamService;
	private LikeService likeService;

	private boolean isTeamCardFragmentAlreadySetup;

	private MemberRecyclerAdapter memberRecyclerAdapter;
	private List<Member> members;
	private MemberService memberService;

	private TeamRecyclerAdapter teamRecyclerAdapter;
	private List<Team> teams;

	public MessagesFragment() {
		memberHasTeam = null;
		isTeamCardFragmentAlreadySetup = false;
		members = new ArrayList<>();
		teams = new ArrayList<>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		teamService = new TeamService(getActivity());
		memberService = new MemberService(getActivity());
		likeService = new LikeService(getActivity());
		currentUser = SharedPreferencesManager.getEntity(getActivity(), Member.STORAGE_KEY, Member.class);
		memberHasTeam = SharedPreferencesManager.getObject(getActivity(), MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);

//		loadTeam();
		loadMembers();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.messages_fragment, container, false);

		setupTeamCard();

		setupMemberRecycler(view);
		setupTeamsParts(view);

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

//		FragmentManager fragmentManager = getChildFragmentManager();
//		List<Fragment> fragments = fragmentManager.getFragments();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//		for (Fragment fragment : fragments) {
//			fragmentTransaction.remove(fragment);
//		}
//
//		fragmentTransaction.commit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
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
	 * Setup the recycler view to display the team list.
	 *
	 * @param view the root view
	 */
	private void setupTeamRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		teamRecyclerAdapter = new TeamRecyclerAdapter(getActivity(), teams);

		RecyclerView teamRecycler = view.findViewById(R.id.teamRecycler);
		teamRecycler.setAdapter(teamRecyclerAdapter);
		teamRecycler.setVisibility(View.VISIBLE);
	}

	/**
	 * Setup the teams parts (title and recycler view).
	 * If the user is not leader, do nothing because all is hidden by default.
	 * Otherwise, display the "Teams" title and setup the recycler view
	 *
	 * @param view the root view
	 */
	private void setupTeamsParts(View view) {
		// Display all teams parts if the view is initialized, the data is loaded, and the member is the leader
		if (null == view
				|| null == memberHasTeam
				|| !MemberStatus.LEADER.equals(memberHasTeam.getStatus())) {
			return;
		}

		TextView teamsTitle = view.findViewById(R.id.teams);
		teamsTitle.setVisibility(View.VISIBLE);

		setupTeamRecycler(view);

		loadTeams();
	}

	/**
	 * Display the team card if the current user has a team.
	 */
	private void setupTeamCard() {
		if (!isTeamCardFragmentAlreadySetup
				&& null != memberHasTeam
				&& !MemberStatus.ALONE.equals(memberHasTeam.getStatus())) {
			isTeamCardFragmentAlreadySetup = true;

			Bundle bundle = new Bundle();
			bundle.putSerializable(MemberHasTeam.STORAGE_KEY, memberHasTeam);

			TeamCardFragment teamCardFragment = new TeamCardFragment();
			teamCardFragment.setArguments(bundle);

			FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.teamcard_frame_layout, teamCardFragment);
			fragmentTransaction.commit();
		}
	}

	/**
	 * Load info of the team of the member.
	 */
	private void loadTeam() {
		teamService.getTeam(currentUser, new CustomResponseListener<MemberHasTeam>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				if (HttpURLConnection.HTTP_NOT_FOUND == error.networkResponse.statusCode) {
					memberHasTeam = new MemberHasTeam();
					memberHasTeam.setMemberId(currentUser.getId());
					memberHasTeam.setStatus(MemberStatus.ALONE);
				} else {
					error.printStackTrace();
					Toast.makeText(getActivity(), "An error occurred while trying to retrieve my team", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onResponse(MemberHasTeam response) {
				memberHasTeam = response;

				// if loading the data was slower than create the view, set data here
				setupTeamCard();

				if (MemberStatus.LEADER.equals(memberHasTeam.getStatus())) {
					setupTeamsParts(getView());
					loadTeams();
				}
			}
		});
	}

	/**
	 * Load all members the current member has talked with before.
	 */
	private void loadMembers() {
		memberService.getFriends(currentUser, new CustomResponseListener<Member[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(getActivity(),"An error occurred while trying to retrieve members", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(Member[] response) {
				Collections.addAll(members, response);

				if (null != memberRecyclerAdapter) {
					memberRecyclerAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	/**
	 * Load all the teams mutually liked with the team of the member.
	 * The current member MUST be leader of his team to see the list.
	 */
	private void loadTeams() {

		likeService.getLikedTeams(memberHasTeam.getTeam(), new CustomResponseListener<Team[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(getActivity(),"An error occurred while trying to retrieve teams", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(Team[] response) {
				Collections.addAll(teams, response);

				if (null != teamRecyclerAdapter) {
					teamRecyclerAdapter.notifyDataSetChanged();
				}
			}
		});
	}
}
