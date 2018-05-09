package com.seeu.common.membersearch;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.common.Member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemberSearchableActivity extends Activity implements ItemClickListener, TextWatcher {

	private List<Member> members;
	private List<Member> matchingMembers;
	private List<Long> alreadyAddedMembers;

	private EditText searchText;
	private TextView emptyListLabel;
	private MemberRecyclerAdapter memberRecyclerAdapter;

	public MemberSearchableActivity() {
		members = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			members.add(Member.getDebugMember(i));
		}

		matchingMembers = new ArrayList<>();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_searchable_activity);

		searchText					= findViewById(R.id.searchText);
		emptyListLabel				= findViewById(R.id.emptyListLabel);
		RecyclerView memberRecycler	= findViewById(R.id.memberRecycler);

		searchText.addTextChangedListener(this);

		memberRecyclerAdapter = new MemberRecyclerAdapter(this, matchingMembers, this);
		memberRecycler.setAdapter(memberRecyclerAdapter);

		// Get the list of membersId from the intent
		Intent intent = getIntent();
		long[] membersId = intent.getLongArrayExtra("membersId");

		alreadyAddedMembers = new ArrayList<>();
		for (long id : membersId) {
			alreadyAddedMembers.add(id);
		}
	}

	private void requestData(String query) {
		matchingMembers.clear();

		for (Member member : members) {
			if (member.getName().toLowerCase().contains(query.toLowerCase())
					&& !alreadyAddedMembers.contains(member.getId())) {
				matchingMembers.add(member);
			}
		}

		memberRecyclerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(View view, int position) {
		Intent intent = new Intent();
		intent.putExtra("member", matchingMembers.get(position));

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
}
