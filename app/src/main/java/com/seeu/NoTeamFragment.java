package com.seeu;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.seeu.team.edit.EditTeamProfileActivity;

/**
 * Created by thomasfouan on 25/06/2018.
 *
 * Fragment that displays a message for users that does not belong to a team.
 * Invites them to create one.
 */
public class NoTeamFragment extends Fragment implements OnClickListener {

	public static final int TEAM_CREATION_REQUEST_CODE = 1;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.no_team_activity, container, false);

		Button createBtn = view.findViewById(R.id.createTeamBtn);
		createBtn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		Context context = v.getContext();
		Intent intent = new Intent(context, EditTeamProfileActivity.class);

		getActivity().startActivityForResult(intent, TEAM_CREATION_REQUEST_CODE);
	}
}
