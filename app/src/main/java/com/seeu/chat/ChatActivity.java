package com.seeu.chat;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.seeu.R;
import com.seeu.common.Constants;
import com.seeu.common.Entity;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.member.profile.MemberProfileActivity;
import com.seeu.team.Team;
import com.seeu.team.like.LikeService;
import com.seeu.team.like.Merge;
import com.seeu.team.profile.TeamProfileActivity;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
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
public class ChatActivity extends ListActivity implements CustomResponseListener<MemberMessage[]>, ViewTreeObserver.OnPreDrawListener {

	public static final String INTENT_IS_BEFORE_CONV = "beforeConversation";

	private Member currentUser;
	private MemberHasTeam memberHasTeam;
	private Entity receiver;
	private boolean isBeforeConv;

	private EditText newMessage;
	private ImageButton profilePicture;
	private boolean isProfilePictureLayoutDrawn;
	private Button mergeBtn;

	private MessageListAdapter messageListAdapter;
	private List<Message> messages;
	private MessageService messageService;

	private StompClient stompClient;
	private Gson gson = new Gson();
	private String sendingPath;

	public ChatActivity() {
		super();
		isProfilePictureLayoutDrawn = false;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);

		this.messageService = new MessageService(this);
		this.currentUser = SharedPreferencesManager.getEntity(this, Member.STORAGE_KEY, Member.class);
		this.memberHasTeam = SharedPreferencesManager.getObject(this, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);

		newMessage = findViewById(R.id.newMessage);
		profilePicture = findViewById(R.id.profilePicture);
		profilePicture.getViewTreeObserver().addOnPreDrawListener(this);
		mergeBtn = findViewById(R.id.mergeBtn);
		// NOTE: the class ListActivity find itself the listView if its id is 'list'

		messages = new ArrayList<>();
		messageListAdapter = new MessageListAdapter(this, messages);
		setListAdapter(messageListAdapter);

		getInfoFromCaller();
		updateUI();
		loadMessages();
		startWebSocketConfig();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (null != stompClient) {
			stompClient.disconnect();
		}
	}

	/**
	 * Get the receiver of the messages send by the current user.
	 * The receiver could be a Team or a Member.
	 */
	private void getInfoFromCaller() {
		isBeforeConv = getIntent().getBooleanExtra(INTENT_IS_BEFORE_CONV, false);
		receiver = (Entity) getIntent().getSerializableExtra(Member.STORAGE_KEY);

		if (null == receiver) {
			receiver = (Entity) getIntent().getSerializableExtra(Team.STORAGE_KEY);
		}

		if (null == receiver) {
			throw new IllegalStateException("No team nor member provided to chat activity");
		}
	}

	private void updateUI() {
		mergeBtn.setVisibility(isBeforeConv ? View.VISIBLE : View.GONE);
		setProfilePicture();
	}

	private void setProfilePicture() {
		if (isProfilePictureLayoutDrawn
				&& null != receiver) {
			new DownloadImageAndSetBackgroundTask(profilePicture, 40).execute(receiver.getProfilePhotoUrl());
		}
	}

	/**
	 * Load messages from API.
	 */
	private void loadMessages() {
		if (receiver instanceof Member) {
			messageService.getMessages(currentUser, (Member) receiver, this);
		} else if (!isBeforeConv) {
			messageService.getMessages((Team) receiver, this);
		} else {
			Context context = this;
			messageService.getMessages(memberHasTeam.getTeam(), (Team) receiver, new CustomResponseListener<TeamMessage[]>() {
				@Override
				public void onHeadersResponse(Map<String, String> headers) {
				}

				@Override
				public void onErrorResponse(VolleyError error) {
					error.printStackTrace();
					Toast.makeText(context, "An error occurred while trying to retrieve all messages", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onResponse(TeamMessage[] response) {
					onReceivedMessages(response);
				}
			});
		}
	}

	/**
	 * Start the web socket communication between the application and the server.
	 */
	private void startWebSocketConfig() {
		stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.SEEU_WEB_SOCKET_SERVER_URL);
		stompClient.connect();

		String destinationPath = "/topic";
		if (receiver instanceof Member) {
			destinationPath += "/user/" + currentUser.getId();
			sendingPath = "/toUser/" + receiver.getId();
		} else if (!isBeforeConv) {
			destinationPath += "/team/" + receiver.getId();
			sendingPath = "/toTeam/" + receiver.getId();
		} else {
			destinationPath += "/leader/" + memberHasTeam.getTeam().getId();
			sendingPath = "/toBefore/" + receiver.getId();
		}

		stompClient.topic(destinationPath).subscribe(topicMessage -> {
			Message message = gson.fromJson(topicMessage.getPayload(), isBeforeConv ? TeamMessage.class : MemberMessage.class);

			// Display only messages sent by me or the receiver of this conversation, or my team
			if (receiver instanceof Team
					|| receiver.equals(message.getOwner())
					|| currentUser.equals(message.getOwner())) {

				runOnUiThread(() -> addMessage(message));
			}
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
			Message message;
			if (isBeforeConv) {
				message = TeamMessage.builder()
						.content(text)
						.owner(memberHasTeam.getTeam())
						.build();
			} else {
				message = MemberMessage.builder()
						.content(text)
						.owner(currentUser)
						.build();
			}

			stompClient.send("/app" + sendingPath, gson.toJson(message)).subscribe();
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

	private void onReceivedMessages(Message[] response) {
		messages.clear();
		Collections.addAll(messages, response);

		if (null != messageListAdapter) {
			messageListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		error.printStackTrace();
		Toast.makeText(this, "An error occurred while trying to retrieve all messages", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(MemberMessage[] response) {
		onReceivedMessages(response);
	}

	public void onProfileClick(View v) {
		Intent intent;
		if (receiver instanceof Member) {
			intent = new Intent(this, MemberProfileActivity.class);
			intent.putExtra(Member.STORAGE_KEY, receiver);
		} else {
			intent = new Intent(this, TeamProfileActivity.class);
			intent.putExtra(Team.STORAGE_KEY, receiver);
		}

		startActivity(intent);
	}

	public void onMergeClick(View v) {
		LikeService likeService = new LikeService(this);
		MemberHasTeam memberHasTeam = SharedPreferencesManager.getObject(this, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);

		likeService.mergeTeam(memberHasTeam.getTeam(), (Team) receiver, new CustomResponseListener<Merge>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(ChatActivity.this, "An error occurred during the merge", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(Merge response) {
				mergeBtn.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public boolean onPreDraw() {
		if (!isProfilePictureLayoutDrawn) {
			isProfilePictureLayoutDrawn = true;
			profilePicture.getViewTreeObserver().removeOnPreDrawListener(this);

			setProfilePicture();
		}

		return true;
	}
}
