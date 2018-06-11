package com.seeu.teamwall;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.seeu.common.Constants;
import com.seeu.common.ItemClickListener;
import com.seeu.R;
import com.seeu.team.Team;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class TeamWallFragment extends Fragment implements ItemClickListener {

	private TeamTypeRecyclerAdapter teamTypeRecyclerAdapter;
	private List<TeamType> types;

	private TeamRecyclerAdapter teamRecyclerAdapter;
	private List<Team> teams;

	private RequestQueue requestQueue;

	public TeamWallFragment() {
		types = new ArrayList<>();
		teams = new ArrayList<>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestQueue = Volley.newRequestQueue(this.getActivity());

		loadTypes();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teamwall_fragment, container, false);

		// TODO: get saved state from last instance of fragment
		setupTeamTypeRecycler(view);
		setupTeamRecycler(view);

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		// TODO: save the state of the fragment before it is destroy

	}

	@Override
	public void onItemClick(View view, int position) {
		teamTypeRecyclerAdapter.setSelected(position);
		refreshTeams(types.get(position));
	}

	private void setupTeamTypeRecycler(View view) {
		// Keep reference of the dataset (arraylist here) in the adapter
		teamTypeRecyclerAdapter = new TeamTypeRecyclerAdapter(getActivity(), types, this);

		// set up the RecyclerView for the types of team
		RecyclerView teamTypeRecycler = view.findViewById(R.id.teamTypeRecycler);
		teamTypeRecycler.setAdapter(teamTypeRecyclerAdapter);
	}

	private void setupTeamRecycler(View view) {
		teamRecyclerAdapter = new TeamRecyclerAdapter(getActivity(), teams);

		RecyclerView teamRecycler = view.findViewById(R.id.teamRecycler);
		teamRecycler.setAdapter(teamRecyclerAdapter);
	}

	private void loadTypes() {
		final Context context = this.getActivity();
		types.clear();

		// TODO: make http request to get types
		String url = Constants.SEEU_API_URL + "/teamTypes";
		String token = this.getActivity()
				.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
				.getString(Constants.SP_TOKEN_KEY, null);

		GsonRequest<TeamType[]> request = new GsonRequest<>(
				url,
				Request.Method.GET,
				TeamType[].class,
				token,
				null,
				new CustomResponseListener<TeamType[]>() {
					@Override
					public void onHeadersResponse(Map<String, String> headers) {
					}

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TeamWallFragment", "Error while loading TeamTypes", error);
						Toast.makeText(context, "Error while loading TeamTypes " + error.getMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponse(TeamType[] response) {
						Collections.addAll(types, response);

						if (null != teamTypeRecyclerAdapter) {
							teamTypeRecyclerAdapter.notifyDataSetChanged();
						}

						if (response.length > 0) {
							refreshTeams(types.get(0));
						}
					}
				});

		requestQueue.add(request);
	}

	private void refreshTeams(TeamType selectedType) {
		final Context context = this.getActivity();
		teams.clear();

		// TODO: make http request to get data
		String url = Constants.SEEU_API_URL + "/teams?selectedTypeId=" + selectedType.getId();
		String token = this.getActivity()
				.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
				.getString(Constants.SP_TOKEN_KEY, null);

		GsonRequest<Team[]> request = new GsonRequest<>(
				url,
				Request.Method.GET,
				Team[].class,
				token,
				null,
				new CustomResponseListener<Team[]>() {
					@Override
					public void onHeadersResponse(Map<String, String> headers) {
					}

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TeamWallFragment", "Error while loading Teams", error);
						Toast.makeText(context, "Error while loading Teams " + error.getMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponse(Team[] response) {
						Collections.addAll(teams, response);

						if (null != teamRecyclerAdapter) {
							teamRecyclerAdapter.notifyDataSetChanged();
						}
					}
				});

		requestQueue.add(request);
	}
}
