package com.seeu.nightcenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Fragment for the night center.
 * The night center appears for members of 2 teams that merged.
 * Display information about the teams and their members.
 */
public class NightCenterFragment extends Fragment {

	private Member currentUser;

	private CardView myTeamCardView;
	private Team myTeam;
	private boolean myTeamPictureSet;

	private CardView mergedTeamCardView;
	private Team mergedTeam;
	private boolean mergedTeamPictureSet;

	private TeamService teamService;

	private MemberRecyclerAdapter memberRecyclerAdapter;
	private List<Member> members;

	public NightCenterFragment() {
		myTeam = null;
		myTeamPictureSet = false;

		mergedTeam = null;
		mergedTeamPictureSet = false;

		members = new ArrayList<>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.teamService = new TeamService(getActivity());
		this.currentUser = SharedPreferencesManager.getEntity(getActivity(), Member.class);

		loadMyTeam();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nightcenter_fragment, container, false);

		myTeamCardView = view.findViewById(R.id.firstTeamCardView);
		mergedTeamCardView = view.findViewById(R.id.secondTeamCardView);

		setupMemberRecycler(view);
		applyPicture();

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
	 * Load teams info from the database.
	 */
	private void loadMyTeam() {
		teamService.getTeam(currentUser, new CustomResponseListener<Team>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(getActivity(), "An error occurred while trying to retrieve my team", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(Team response) {
				myTeam = response;
				applyPicture();

				loadMergedTeam();

				members.addAll(myTeam.getMembers());
				if (null != memberRecyclerAdapter) {
					memberRecyclerAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void loadMergedTeam() {
		teamService.getMergedTeam(myTeam, new CustomResponseListener<Team>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(getActivity(), "An error occurred while trying to retrieve merged team", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(Team response) {
				mergedTeam = response;
				applyPicture();

				members.addAll(mergedTeam.getMembers());
				if (null != memberRecyclerAdapter) {
					memberRecyclerAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	/**
	 * Set the picture of each team in UI.
	 * This method is called 2 times, one when the view is loaded, and one when the request to the API is done.
	 * The reason is that we do not know which event is the longer, depending on the phone and the network connection.
	 */
	private void applyPicture() {
		if (!myTeamPictureSet
				&& null != myTeamCardView
				&& null != myTeam) {
			myTeamPictureSet = true;
			new DownloadImageAndSetBackgroundTask(myTeamCardView, 150, 150, 150).execute(myTeam.getPictureUrl());
		}

		if (!mergedTeamPictureSet
				&& null != mergedTeamCardView
				&& null != mergedTeam) {
			mergedTeamPictureSet = true;
			new DownloadImageAndSetBackgroundTask(mergedTeamCardView, 150, 150, 150).execute(mergedTeam.getPictureUrl());
		}
	}
}
