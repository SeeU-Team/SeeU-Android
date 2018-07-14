package com.seeu.nightcenter;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.team.like.LikeService;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;
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

	private MemberHasTeam memberHasTeam;

	private CardView myTeamCardView;
	private DownloadImageAndSetBackgroundTask myTeamAsyncTask;
	private TextView myTeamName;

	private CardView mergedTeamCardView;
	private DownloadImageAndSetBackgroundTask mergedTeamAsyncTask;
	private TextView mergedTeamName;
	private Team mergedTeam;

	private LikeService likeService;

	private MemberRecyclerAdapter memberRecyclerAdapter;
	private List<Member> members;

	public NightCenterFragment() {
		memberHasTeam = null;
		mergedTeam = null;
		members = new ArrayList<>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.likeService = new LikeService(getActivity());
		this.memberHasTeam = SharedPreferencesManager.getObject(getActivity(), MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);

		loadMergedTeam();

		members.addAll(memberHasTeam.getTeam().getMembers());
		if (null != memberRecyclerAdapter) {
			memberRecyclerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nightcenter_fragment, container, false);

		myTeamCardView = view.findViewById(R.id.firstTeamCardView);
		myTeamName = view.findViewById(R.id.firstTeamName);
		mergedTeamCardView = view.findViewById(R.id.secondTeamCardView);
		mergedTeamName = view.findViewById(R.id.secondTeamName);

		myTeamName.setText(memberHasTeam.getTeam().getName());

		setupMemberRecycler(view);
		setMyTeamPicture();

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (null != myTeamAsyncTask) {
			myTeamAsyncTask.cancelDownload();
		}

		if (null != mergedTeamAsyncTask) {
			mergedTeamAsyncTask.cancelDownload();
		}
	}

	/**
	 * Setup the recycler view to display the member list.
	 *
	 * @param view the root view
	 */
	private void setupMemberRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(getActivity(), members, false);

		RecyclerView memberRecycler = view.findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	private void loadMergedTeam() {
		likeService.getMergedTeam(memberHasTeam.getTeam(), new CustomResponseListener<Team>() {
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

				mergedTeamName.setText(response.getName());
				setMergedTeamPicture();

				members.addAll(mergedTeam.getMembers());
				if (null != memberRecyclerAdapter) {
					memberRecyclerAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void setMyTeamPicture() {
		ImageUtils.runJustBeforeBeingDrawn(myTeamCardView, () -> {
			myTeamAsyncTask = new DownloadImageAndSetBackgroundTask(myTeamCardView, 150);
			myTeamAsyncTask.execute(memberHasTeam.getTeam().getProfilePhotoUrl());
		});
	}

	private void setMergedTeamPicture() {
		ImageUtils.runJustBeforeBeingDrawn(mergedTeamCardView, () -> {
			mergedTeamAsyncTask = new DownloadImageAndSetBackgroundTask(mergedTeamCardView, 150);
			mergedTeamAsyncTask.execute(mergedTeam.getProfilePhotoUrl());
		});
	}
}
