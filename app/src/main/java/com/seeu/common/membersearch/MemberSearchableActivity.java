package com.seeu.common.membersearch;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.common.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberSearchableActivity extends Activity implements ItemClickListener {

	private List<Member> members;
	private List<Member> matchingMembers;

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

		memberRecyclerAdapter = new MemberRecyclerAdapter(this, matchingMembers, this);

		RecyclerView memberRecycler = findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			requestData(query);
		}
	}

	private void requestData(String query) {
		matchingMembers.clear();

		for (Member member : members) {
			if (member.getName().contains(query)) {
				matchingMembers.add(member);
			}
		}

		memberRecyclerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(View view, int position) {
		Intent intent = new Intent();
		intent.putExtra("member", members.get(position));

		setResult(RESULT_OK, intent);
		finish();
	}
}
