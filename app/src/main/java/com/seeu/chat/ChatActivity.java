package com.seeu.chat;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomasfouan on 06/05/2018.
 */

public class ChatActivity extends ListActivity {

	private EditText newMessage;

	private MessageListAdapter messageListAdapter;
	private List<Message> messages;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);

		newMessage = findViewById(R.id.newMessage);
		// NOTE: the class ListActivity find itself the listView if its id is 'list'

		messages = new ArrayList<>();
		messageListAdapter = new MessageListAdapter(this, messages);
		setListAdapter(messageListAdapter);

		getInfoFromCaller();
		loadMessages();
	}

	private void getInfoFromCaller() {
		Bundle bundle = getIntent().getExtras();
		long teamId = -1;

		if (null != bundle) {
			teamId = bundle.getLong("TeamId");
		}
		System.out.println("TeamId received : " + teamId);
	}

	private void loadMessages() {
		// TODO: make http request to load data

		messages.clear();
		for (int i = 0; i < 10; i++) {
			messages.add(Message.getDebugMessage(i));
		}

		messageListAdapter.notifyDataSetChanged();
	}

	public void onNewMessageClick(View v) {
		String text = newMessage.getText().toString();

		if (!text.isEmpty()) {
			Message message = new Message();
			message.setId(messages.size());
			message.setContent(text);
			message.setOwner(Member.getDebugMember(0));

			messages.add(message);
			messageListAdapter.notifyDataSetChanged();
		}
		newMessage.getText().clear();
	}
}
