package com.seeu.chat;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.seeu.R;
import com.seeu.common.Constants;
import com.seeu.common.Entity;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by thomasfouan on 06/05/2018.
 *
 * Activity that manages the chat.
 * Use web socket for real time message exchanges.
 */
public class ChatActivity extends ListActivity implements CustomResponseListener<Message[]> {

	private Member currentUser;
	private Entity receiver;

	private EditText newMessage;

	private MessageListAdapter messageListAdapter;
	private List<Message> messages;
	private MessageService messageService;

	private StompClient stompClient;
	private Gson gson = new Gson();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);

		this.messageService = new MessageService(this);

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

	/**
	 * Get the receiver of the messages send by the current user.
	 * The receiver could be a Team or a Member.
	 */
	private void getInfoFromCaller() {
		receiver = (Entity) getIntent().getSerializableExtra(Team.STORAGE_KEY);

		if (null == receiver) {
			receiver = (Entity) getIntent().getSerializableExtra(Member.STORAGE_KEY);
		}

		if (null == receiver) {
			throw new IllegalStateException("No team nor member provided to chat activity");
		}

		this.currentUser = SharedPreferencesManager.getEntity(this, Member.class);
	}

	/**
	 * Load messages from API.
	 */
	private void loadMessages() {
		// TODO: make http request to load data

//		messages.clear();
//		for (int i = 0; i < 10; i++) {
//			messages.add(Message.getDebugMessage(i));
//		}

		if (receiver instanceof Team) {
			messageService.getMessages(currentUser, (Team) receiver, this);
		} else {
			messageService.getMessages(currentUser, (Member) receiver, this);
		}
	}

	/**
	 * Start the web socket communication between the application and the server.
	 */
	private void startWebSocketConfig() {
		stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.SEEU_WEB_SOCKET_SERVER_URL);
		stompClient.connect();

		stompClient.topic("/topic/greetings").subscribe(topicMessage -> {
			Message message = gson.fromJson(topicMessage.getPayload(), Message.class);
			message.setOwner(Member.getDebugMember(0));
			runOnUiThread(() -> addMessage(message));
		});
	}

	/**
	 * Send a new message to the receiver throw the web socket.
	 * @param v the view that represents the button clicked by the user
	 */
	public void onNewMessageClick(View v) {
		String text = newMessage.getText().toString();
		newMessage.getText().clear();

		if (!text.isEmpty()) {
			Message message = new Message();
//			message.setId(messages.size());
			message.setContent(text);
			// TODO: get current user
			message.setOwner(currentUser);

			stompClient.send("/app/hello", gson.toJson(message)).subscribe();
		}
	}

	/**
	 * Add a message in the listview.
	 * @param message the message to add
	 */
	private void addMessage(final Message message) {
		messages.add(message);
		messageListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {

	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}

	@Override
	public void onResponse(Message[] response) {
		messages.clear();
		Collections.addAll(messages, response);

		if (null != messageListAdapter) {
			messageListAdapter.notifyDataSetChanged();
		}
	}
}
