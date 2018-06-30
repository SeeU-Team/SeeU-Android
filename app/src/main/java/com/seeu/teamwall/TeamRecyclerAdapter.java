package com.seeu.teamwall;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.common.ItemClickListener;
import com.seeu.R;
import com.seeu.member.MemberHasTeam;
import com.seeu.team.Team;
import com.seeu.team.TeamService;
import com.seeu.team.like.Like;
import com.seeu.team.like.LikeService;
import com.seeu.team.profile.TeamProfileActivity;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by thomasfouan on 19/03/2018.
 *
 * Adapter for the list of team in the teamwall.
 */
public class TeamRecyclerAdapter extends Adapter<TeamViewHolder> implements CustomResponseListener<Like> {

	private LayoutInflater inflater;
	private List<Team> teams;
	private LikeService likeService;
	private WeakReference<Context> contextRef;
	private Team lastTeamUp;
	private MemberHasTeam memberHasTeam;
	private ItemClickListener itemClickListener;

	public TeamRecyclerAdapter(Context context, @NonNull List<Team> teams, ItemClickListener itemClickListener) {
		this.inflater = LayoutInflater.from(context);
		this.teams = teams;
		this.likeService = new LikeService(context);
		this.contextRef = new WeakReference<>(context);
		this.lastTeamUp = null;
		this.itemClickListener = itemClickListener;

		this.memberHasTeam = SharedPreferencesManager.getObject(context, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);
	}

	@Override
	public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.teamwall_layout_team_item, parent, false);
		return new TeamViewHolder(view, itemClickListener, this::onTeamUpBtnClick);
	}

	@Override
	public void onBindViewHolder(TeamViewHolder holder, int position) {
		Team team = getItem(position);
		holder.setData(team);
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	/**
	 * Return the team at the given position.
	 * @param position the position of the team in the list.
	 * @return the team
	 */
	public Team getItem(int position) {
		return teams.get(position);
	}

	public void onTeamUpBtnClick(View view, int position) {
		lastTeamUp = getItem(position);
		likeService.likeTeam(memberHasTeam.getTeam(), lastTeamUp, this);
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Context context = contextRef.get();
		if (null != context) {
			Toast.makeText(context, "An error occurred while trying to team up", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onResponse(Like response) {
		teams.remove(lastTeamUp);
		notifyDataSetChanged();
	}
}
