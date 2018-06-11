package com.seeu.chat;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.seeu.R;
import com.seeu.common.Constants;
import com.seeu.member.Member;
import com.seeu.team.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by thomasfouan on 06/05/2018.
 */

public class ChatActivity extends ListActivity {

	private Serializable receiver;

	private EditText newMessage;

	private MessageListAdapter messageListAdapter;
	private List<Message> messages;

	private StompClient stompClient;
	private Gson gson = new Gson();

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
		startWebSocketConfig();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		stompClient.disconnect();
	}

	private void getInfoFromCaller() {
		receiver = getIntent().getSerializableExtra(Team.INTENT_EXTRA_KEY);

		if (null == receiver) {
			receiver = getIntent().getSerializableExtra(Member.INTENT_EXTRA_KEY);
		}

		if (null == receiver) {
			throw new IllegalStateException("No team nor member provided to chat activity");
		}
	}

	private void loadMessages() {
		// TODO: make http request to load data

		messages.clear();
		for (int i = 0; i < 10; i++) {
			messages.add(Message.getDebugMessage(i));
		}

		messageListAdapter.notifyDataSetChanged();
	}

	private void startWebSocketConfig() {
		stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.SEEU_WEB_SOCKET_SERVER_URL);
		stompClient.connect();

		stompClient.topic("/topic/greetings").subscribe(topicMessage -> {
			Message message = gson.fromJson(topicMessage.getPayload(), Message.class);
			message.setOwner(Member.getDebugMember(0));
			runOnUiThread(() -> addMessage(message));
		});
	}

	public void onNewMessageClick(View v) {
		String text = newMessage.getText().toString();
		newMessage.getText().clear();

		if (!text.isEmpty()) {
			Message message = new Message();
//			message.setId(messages.size());
			message.setContent(text);
			// TODO: get current user
			message.setOwner(Member.getDebugMember(0));

			stompClient.send("/app/hello", gson.toJson(message)).subscribe();
		}
	}

	private void addMessage(final Message message) {
		messages.add(message);
		messageListAdapter.notifyDataSetChanged();
	}
}
