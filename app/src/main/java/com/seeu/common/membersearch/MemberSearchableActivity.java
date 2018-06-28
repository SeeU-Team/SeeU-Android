package com.seeu.common.membersearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.member.MemberService;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by thomasfouan on 10/05/2018.
 *
 * Activity that manages the search of members to add in a team.
 */
public class MemberSearchableActivity extends Activity implements ItemClickListener, TextWatcher, CustomResponseListener<Member[]> {

	private MemberService memberService;
	private List<Member> members;
	private List<Member> matchingMembers;
	private List<Member> alreadyAddedMembers;

	private TextView emptyListLabel;
	private RecyclerView memberRecycler;
	private MemberRecyclerAdapter memberRecyclerAdapter;

	public MemberSearchableActivity() {

		matchingMembers = new ArrayList<>();
		members = new ArrayList<>();
//		for (int i = 0; i < 20; i++) {
//			members.add(Member.getDebugMember(i));
//		}

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_searchable_activity);

		EditText searchText		= findViewById(R.id.searchText);
		emptyListLabel			= findViewById(R.id.emptyListLabel);
		memberRecycler			= findViewById(R.id.memberRecycler);

		searchText.addTextChangedListener(this);

		memberRecyclerAdapter = new MemberRecyclerAdapter(this, matchingMembers, this);
		memberRecycler.setAdapter(memberRecyclerAdapter);

		// Get all Facebook friends of the current user that uses the application
		memberService = new MemberService(this);
		getFacebookFriends();

		// Get the list of membersId from the intent
		Intent intent = getIntent();
		alreadyAddedMembers = (ArrayList<Member>) intent.getSerializableExtra("members");
	}

	private void getFacebookFriends() {
		String accessToken = SharedPreferencesManager.getFacebookToken(this);
		memberService.getFacebookFriends(accessToken, this);
	}

	/**
	 * Update the list of matching members accordingly to the query made by the user.
	 * The matching is made on the member's name.
	 * @param query the query made by the user
	 */
	private void requestData(String query) {
		matchingMembers.clear();

		for (Member member : members) {
			if (member.getName().toLowerCase().contains(query.toLowerCase())
					&& !alreadyAddedMembers.contains(member)) {
				matchingMembers.add(member);
			}
		}

		if (matchingMembers.isEmpty()) {
			emptyListLabel.setVisibility(View.VISIBLE);
			memberRecycler.setVisibility(View.INVISIBLE);
		} else {
			emptyListLabel.setVisibility(View.INVISIBLE);
			memberRecycler.setVisibility(View.VISIBLE);
		}

		memberRecyclerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(View view, int position) {
		Member member = matchingMembers.get(position);
		Intent intent = new Intent();
		intent.putExtra(Member.STORAGE_KEY, member);

		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		requestData(s.toString());
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		error.printStackTrace();
		Toast.makeText(this, "An error occurred while retrieving Facebook friends", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(Member[] response) {
		Collections.addAll(members, response);
	}
}
